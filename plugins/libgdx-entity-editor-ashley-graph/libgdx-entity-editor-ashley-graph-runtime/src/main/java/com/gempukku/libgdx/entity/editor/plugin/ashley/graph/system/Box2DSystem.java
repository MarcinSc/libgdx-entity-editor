package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.Box2DBodyComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.Box2DBodyDataComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.PositionComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.ScaleComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.BoxFixtureShape;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.FixtureDefinition;

public class Box2DSystem extends EntitySystem implements Disposable {
    private World world;
    private float pixelsToMeters;

    private ObjectMap<String, SensorContactListener> sensorContactListeners = new ObjectMap<>();
    private ObjectMap<String, Short> categoryBits = new ObjectMap<>();
    private int nextCategoryBit;
    private boolean ownsWorld;

    private ImmutableArray<Entity> physicsEntities;
    private Array<EntityPositionUpdateListener> listeners = new Array<>();

    public Box2DSystem(int priority, World world, float pixelsToMeters) {
        super(priority);
        this.world = world;
        this.pixelsToMeters = pixelsToMeters;

        world.setContactListener(
                new ContactListener() {
                    @Override
                    public void beginContact(Contact contact) {
                        contactBegan(contact);
                    }

                    @Override
                    public void endContact(Contact contact) {
                        contactEnded(contact);
                    }

                    @Override
                    public void preSolve(Contact contact, Manifold oldManifold) {

                    }

                    @Override
                    public void postSolve(Contact contact, ContactImpulse impulse) {

                    }
                });
    }

    public Box2DSystem(int priority, Vector2 gravity, boolean doSleep, float pixelsToMeters) {
        this(priority, new World(gravity, doSleep), pixelsToMeters);
        this.ownsWorld = true;
    }

    public void addEntityPositionUpdateListener(EntityPositionUpdateListener listener) {
        listeners.add(listener);
    }

    public void removeEntityPositionUpdateListener(EntityPositionUpdateListener listener) {
        listeners.removeValue(listener, true);
    }

    public World getWorld() {
        return world;
    }

    public float getPixelsToMeters() {
        return pixelsToMeters;
    }

    @Override
    public void addedToEngine(Engine engine) {
        Family family = Family.all(Box2DBodyComponent.class, PositionComponent.class).get();
        engine.addEntityListener(family,
                new EntityListener() {
                    @Override
                    public void entityAdded(Entity entity) {
                        Box2DSystem.this.entityAdded(entity);
                    }

                    @Override
                    public void entityRemoved(Entity entity) {
                        Box2DSystem.this.entityRemoved(entity);
                    }
                });
        physicsEntities = engine.getEntitiesFor(family);
    }

    private void entityAdded(Entity entity) {
        Box2DBodyComponent physicsComponent = entity.getComponent(Box2DBodyComponent.class);
        Body body = createBody(entity, physicsComponent);

        Box2DBodyDataComponent physicsDataComponent = new Box2DBodyDataComponent();
        physicsDataComponent.setBody(body);
        entity.add(physicsDataComponent);

        Array<FixtureDefinition> fixtures = physicsComponent.getFixtures();
        if (fixtures != null) {
            for (FixtureDefinition fixture : fixtures) {
                if (fixture.isSensor()) {
                    SensorContactListener sensorContactListener = sensorContactListeners.get(fixture.getSensorType());
                    Object value = null;
                    if (sensorContactListener != null)
                        value = sensorContactListener.createNewSensorValue();
                    physicsDataComponent.addSensor(createSensor(entity, body, fixture, value));
                } else {
                    createFixture(entity, body, fixture);
                }
            }
        }
    }

    private void entityRemoved(Entity entity) {
        Box2DBodyDataComponent physicsComponent = entity.remove(Box2DBodyDataComponent.class);
        world.destroyBody(physicsComponent.getBody());
    }

    public void addSensorContactListener(String type, SensorContactListener sensorContactListener) {
        sensorContactListeners.put(type, sensorContactListener);
    }

    public void removeSensorContactListener(String type) {
        sensorContactListeners.remove(type);
    }

    private Body createBody(Entity entity, Box2DBodyComponent bodyComponent) {
        PositionComponent positionComponent = entity.getComponent(PositionComponent.class);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyComponent.getBodyType();
        bodyDef.fixedRotation = bodyComponent.isFixedRotation();
        bodyDef.bullet = bodyComponent.isBullet();
        bodyDef.linearDamping = bodyComponent.getLinearDamping();
        bodyDef.angularDamping = bodyComponent.getAngularDamping();
        bodyDef.allowSleep = bodyComponent.isAllowSleep();
        bodyDef.gravityScale = bodyComponent.getGravityScale();

        bodyDef.position.set(positionComponent.getX(), positionComponent.getY()).scl(1 / pixelsToMeters, 1 / pixelsToMeters);

        return world.createBody(bodyDef);
    }

    public void updateBody(Entity entity) {
        entityRemoved(entity);
        entityAdded(entity);
    }

    private Box2DBodyDataComponent.SensorData createSensor(Entity entity, Body body, FixtureDefinition fixtureDefinition, Object sensorValue) {
        ScaleComponent scaleComponent = entity.getComponent(ScaleComponent.class);
        float scaleX = 1;
        float scaleY = 1;
        if (scaleComponent != null) {
            scaleX = scaleComponent.getX();
            scaleY = scaleComponent.getY();
        }

        BoxFixtureShape fixtureShape = (BoxFixtureShape) fixtureDefinition.getShape();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(scaleX * fixtureShape.getWidth() / 2 / pixelsToMeters, scaleY * fixtureShape.getHeight() / 2 / pixelsToMeters,
                new Vector2(scaleX * fixtureShape.getCenter().x / pixelsToMeters, scaleY * fixtureShape.getCenter().y / pixelsToMeters), fixtureShape.getAngle());

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = fixtureDefinition.getFriction();
        fixtureDef.restitution = fixtureDefinition.getRestitution();
        fixtureDef.density = fixtureDefinition.getDensity();
        fixtureDef.isSensor = fixtureDefinition.isSensor();
        fixtureDef.filter.categoryBits = getBits(fixtureDefinition.getCategory());
        fixtureDef.filter.maskBits = getBits(fixtureDefinition.getMask());
        fixtureDef.shape = shape;

        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();

        if (fixtureDefinition.isSensor()) {
            Box2DBodyDataComponent.SensorData sensorData = new Box2DBodyDataComponent.SensorData(fixtureDefinition.getSensorName(), fixtureDefinition.getSensorType(), sensorValue);
            fixture.setUserData(sensorData);
            return sensorData;
        } else {
            fixture.setUserData(entity);
            return null;
        }
    }

    private void createFixture(Entity entity, Body body, FixtureDefinition fixtureDefinition) {
        createSensor(entity, body, fixtureDefinition, null);
    }

    public short getBits(Array<String> categories) {
        short result = 0;
        if (categories != null) {
            for (String category : categories) {
                result |= getBitForCategory(category);
            }
        }
        return result;
    }

    public short getBitForCategory(String category) {
        Short result = categoryBits.get(category);
        if (result == null) {
            result = (short) (0x1 << nextCategoryBit);
            categoryBits.put(category, result);
            nextCategoryBit++;
        }
        return result;
    }

    @Override
    public void update(float delta) {
        if (delta > 0) {
            world.step(delta, 6, 2);

            for (Entity physicsEntity : physicsEntities) {
                Box2DBodyDataComponent physicsComponent = physicsEntity.getComponent(Box2DBodyDataComponent.class);
                Body body = physicsComponent.getBody();
                if (body.getType() == BodyDef.BodyType.DynamicBody) {
                    PositionComponent position = physicsEntity.getComponent(PositionComponent.class);
                    position.setX(body.getPosition().x * pixelsToMeters);
                    position.setY(body.getPosition().y * pixelsToMeters);

                    for (EntityPositionUpdateListener listener : listeners) {
                        listener.positionUpdated(physicsEntity);
                    }
                }
            }
        }
    }

    private void contactBegan(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        processContactBegun(fixtureB, fixtureA);
        processContactBegun(fixtureA, fixtureB);
    }

    private void processContactBegun(Fixture fixture1, Fixture fixture2) {
        if (fixture2.getUserData() != null && fixture2.isSensor()) {
            Box2DBodyDataComponent.SensorData sensorData = (Box2DBodyDataComponent.SensorData) fixture2.getUserData();
            String type = sensorData.getType();
            sensorContactListeners.get(type).contactBegun(sensorData.getData(), fixture1);
        }
    }

    private void contactEnded(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        processContactEnded(fixtureB, fixtureA);
        processContactEnded(fixtureA, fixtureB);
    }

    private void processContactEnded(Fixture fixture1, Fixture fixture2) {
        if (fixture2.getUserData() != null && fixture2.isSensor()) {
            Box2DBodyDataComponent.SensorData sensorData = (Box2DBodyDataComponent.SensorData) fixture2.getUserData();
            String type = sensorData.getType();
            sensorContactListeners.get(type).contactEnded(sensorData.getData(), fixture1);
        }
    }

    @Override
    public void dispose() {
        if (ownsWorld) {
            world.dispose();
        }
    }
}
