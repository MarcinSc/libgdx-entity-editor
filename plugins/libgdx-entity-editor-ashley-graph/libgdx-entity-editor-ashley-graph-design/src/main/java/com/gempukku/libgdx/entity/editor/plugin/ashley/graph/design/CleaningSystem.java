package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;

public class CleaningSystem extends EntitySystem {
    private Engine engine;
    private Family entities;

    public CleaningSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
        entities = Family.all(AshleyEntityComponent.class).get();
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : engine.getEntitiesFor(entities)) {
            AshleyEntityComponent ashleyEntityComponent = entity.getComponent(AshleyEntityComponent.class);
            if (ashleyEntityComponent.isDirty())
                ashleyEntityComponent.setDirty(false);
        }
    }
}
