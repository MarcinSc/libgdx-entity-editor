package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.system;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.DirtyComponent;

public class CleaningSystem extends EntitySystem {
    private Engine engine;

    public CleaningSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : engine.getEntities()) {
            for (Component component : entity.getComponents()) {
                if (component instanceof DirtyComponent) {
                    ((DirtyComponent) component).clean();
                }
            }
        }
    }
}
