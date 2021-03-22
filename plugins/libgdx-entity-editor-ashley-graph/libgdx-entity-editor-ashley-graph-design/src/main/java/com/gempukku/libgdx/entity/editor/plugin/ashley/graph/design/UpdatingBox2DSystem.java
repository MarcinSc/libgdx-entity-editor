package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.Box2DBodyComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.PositionComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.system.Box2DSystem;

public class UpdatingBox2DSystem extends Box2DSystem {
    private ImmutableArray<Entity> bodyEntities;

    public UpdatingBox2DSystem(int priority, Vector2 gravity, boolean doSleep, float pixelsToMeters) {
        super(priority, gravity, doSleep, pixelsToMeters);
    }

    public UpdatingBox2DSystem(int priority, World world, float pixelsToMeters) {
        super(priority, world, pixelsToMeters);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        Family bodyAndPosition = Family.all(Box2DBodyComponent.class, PositionComponent.class).get();
        bodyEntities = engine.getEntitiesFor(bodyAndPosition);
    }

    @Override
    public void update(float deltaTime) {
        for (Entity bodyEntity : bodyEntities) {
            AshleyEntityComponent ashleyEntityComponent = bodyEntity.getComponent(AshleyEntityComponent.class);
            if (ashleyEntityComponent.isDirty())
                updateBody(bodyEntity);
        }

        super.update(deltaTime);
    }
}
