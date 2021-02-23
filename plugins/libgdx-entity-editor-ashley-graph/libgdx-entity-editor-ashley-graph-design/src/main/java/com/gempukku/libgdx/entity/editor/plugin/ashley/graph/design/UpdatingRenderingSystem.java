package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.PositionComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.system.RenderingSystem;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.system.TextureLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class UpdatingRenderingSystem extends RenderingSystem {
    private ImmutableArray<Entity> spriteEntities;

    public UpdatingRenderingSystem(int priority, TimeProvider timeProvider, PipelineRenderer pipelineRenderer, TextureLoader textureLoader) {
        super(priority, timeProvider, pipelineRenderer, textureLoader);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        Family spriteAndPosition = Family.all(SpriteComponent.class, PositionComponent.class).get();
        spriteEntities = engine.getEntitiesFor(spriteAndPosition);
    }

    @Override
    public void update(float deltaTime) {
        for (Entity spriteEntity : spriteEntities) {
            AshleyEntityComponent ashleyEntityComponent = spriteEntity.getComponent(AshleyEntityComponent.class);
            if (ashleyEntityComponent.isDirty())
                updateSprite(spriteEntity);
        }

        super.update(deltaTime);
    }
}
