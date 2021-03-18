package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.Box2DBodyComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.FixtureDefinition;

public class Box2DBodyComponentDataStorage extends ComponentDataStorage<Box2DBodyComponent> {
    public Box2DBodyComponentDataStorage(Box2DBodyComponent component) {
        super(component);
    }

    @Override
    public ComponentDataStorage<Box2DBodyComponent> createDataStorage(Box2DBodyComponent component) {
        return new Box2DBodyComponentDataStorage(component);
    }

    @Override
    public JsonValue getValue(String fieldName) {
        if (fieldName.equals("bodyType"))
            return new JsonValue(getComponent().getBodyType().name());
        else if (fieldName.equals("fixtures")) {
            Json json = new Json();
            json.setUsePrototypes(false);
            JsonReader jsonReader = new JsonReader();

            JsonValue result = new JsonValue(JsonValue.ValueType.array);
            for (FixtureDefinition fixture : getComponent().getFixtures()) {
                result.addChild(jsonReader.parse(json.toJson(fixture, FixtureDefinition.class)));
            }
            return result;
        } else if (fieldName.equals("fixedRotation")) {
            return new JsonValue(getComponent().isFixedRotation());
        } else if (fieldName.equals("bullet")) {
            return new JsonValue(getComponent().isBullet());
        } else if (fieldName.equals("linearDamping")) {
            return new JsonValue(getComponent().getLinearDamping());
        } else if (fieldName.equals("angularDamping")) {
            return new JsonValue(getComponent().getAngularDamping());
        } else if (fieldName.equals("allowSleep")) {
            return new JsonValue(getComponent().isAllowSleep());
        } else if (fieldName.equals("gravityScale")) {
            return new JsonValue(getComponent().getGravityScale());
        } else
            throw new IllegalArgumentException();
    }

    @Override
    public void setValue(String fieldName, JsonValue value) {
        if (fieldName.equals("bodyType"))
            getComponent().setBodyType(BodyDef.BodyType.valueOf(value.asString()));
        else if (fieldName.equals("fixtures")) {
            Json json = new Json();
            json.setUsePrototypes(false);

            Array<FixtureDefinition> fixtures = new Array<>();
            for (JsonValue jsonValue : value) {
                fixtures.add(json.readValue(FixtureDefinition.class, jsonValue));
            }
            getComponent().setFixtures(fixtures);
        } else if (fieldName.equals("fixedRotation")) {
            getComponent().setFixedRotation(value.asBoolean());
        } else if (fieldName.equals("bullet")) {
            getComponent().setBullet(value.asBoolean());
        } else if (fieldName.equals("linearDamping")) {
            getComponent().setLinearDamping(value.asFloat());
        } else if (fieldName.equals("angularDamping")) {
            getComponent().setAngularDamping(value.asFloat());
        } else if (fieldName.equals("allowSleep")) {
            getComponent().setAllowSleep(value.asBoolean());
        } else if (fieldName.equals("gravityScale")) {
            getComponent().setGravityScale(value.asFloat());
        } else
            throw new IllegalArgumentException();
    }
}
