package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FaceDirection;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FacingComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.PositionComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.ScaleComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteStateComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.SpriteStateDataDef;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.value.value.TextureValue;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprites;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteUpdater;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class RenderingSystem extends EntitySystem {
    private static Vector2 tmpPosition = new Vector2();

    private TimeProvider timeProvider;
    private PipelineRenderer pipelineRenderer;
    private TextureLoader textureLoader;
    private ImmutableArray<Entity> spriteEntities;

    private TextureRegion defaultTextureRegion;

    public RenderingSystem(int priority, TimeProvider timeProvider, PipelineRenderer pipelineRenderer, TextureLoader textureLoader) {
        super(priority);
        this.timeProvider = timeProvider;
        this.pipelineRenderer = pipelineRenderer;
        this.textureLoader = textureLoader;
    }

    public void setDefaultTextureRegion(TextureRegion defaultTextureRegion) {
        this.defaultTextureRegion = defaultTextureRegion;
    }

    @Override
    public void addedToEngine(Engine engine) {
        Family spriteAndPosition = Family.all(SpriteComponent.class, PositionComponent.class).get();
        spriteEntities = engine.getEntitiesFor(spriteAndPosition);
        engine.addEntityListener(spriteAndPosition,
                new EntityListener() {
                    @Override
                    public void entityAdded(Entity entity) {
                        SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);

                        GraphSprites graphSprites = pipelineRenderer.getPluginData(GraphSprites.class);
                        GraphSprite graphSprite = graphSprites.createSprite(spriteComponent.getLayer());
                        spriteComponent.setGraphSprite(graphSprite);

                        setSpriteProperties(entity, true);

                        for (String tag : spriteComponent.getTags()) {
                            graphSprites.addTag(graphSprite, tag);
                        }
                    }

                    @Override
                    public void entityRemoved(Entity entity) {
                        SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
                        GraphSprite graphSprite = spriteComponent.getGraphSprite();
                        pipelineRenderer.getPluginData(GraphSprites.class).destroySprite(graphSprite);
                    }
                });
    }

    @Override
    public void update(float delta) {
        for (Entity spriteEntity : spriteEntities) {
            SpriteComponent sprite = spriteEntity.getComponent(SpriteComponent.class);
            SpriteStateComponent spriteState = spriteEntity.getComponent(SpriteStateComponent.class);

            if (spriteState != null && spriteState.isDirty()) {
                SpriteStateDataDef stateData = spriteState.getStates().get(spriteState.getState());

                sprite.setTexture(stateData.getAtlas(), stateData.getTexture());
                sprite.setProperties(stateData.getProperties());
                String timePropertyName = spriteState.getTimePropertyName();
                if (timePropertyName != null)
                    sprite.getProperties().put(timePropertyName, timeProvider.getTime());
            }

            setSpriteProperties(spriteEntity, false);
        }
    }

    private void setSpriteProperties(Entity entity, boolean force) {
        GraphSprites graphSprites = pipelineRenderer.getPluginData(GraphSprites.class);
        final SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
        final PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
        final ScaleComponent scaleComponent = entity.getComponent(ScaleComponent.class);
        final FacingComponent facingComponent = entity.getComponent(FacingComponent.class);

        GraphSprite graphSprite = spriteComponent.getGraphSprite();

        if (force || spriteComponent.isDirty() || positionComponent.isDirty() || (scaleComponent != null && scaleComponent.isDirty())
                || (facingComponent != null && facingComponent.isDirty())) {
            graphSprites.updateSprite(graphSprite,
                    new SpriteUpdater() {
                        @Override
                        public void processUpdate(Vector3 position, Vector2 size, Vector2 anchor) {
                            tmpPosition.set(positionComponent.getX(), positionComponent.getY());
                            position.set(tmpPosition, spriteComponent.getLayer());

                            if (scaleComponent != null) {
                                size.set(scaleComponent.getX(), scaleComponent.getY());
                            } else {
                                size.set(1, 1);
                            }

                            if (facingComponent != null) {
                                FaceDirection faceDirection = facingComponent.getFaceDirection();
                                size.scl(faceDirection.getDirection(), 1);
                            }

                            anchor.set(spriteComponent.getAnchorX(), spriteComponent.getAnchorY());
                        }
                    });
        }

        if (force || spriteComponent.isDirty()) {
            TextureRegion textureRegion = textureLoader.loadTexture(spriteComponent.getAtlas(), spriteComponent.getTexture());
            if (textureRegion != null)
                graphSprites.setProperty(graphSprite, spriteComponent.getTexturePropertyName(), textureRegion);
            else if (defaultTextureRegion != null)
                graphSprites.setProperty(graphSprite, spriteComponent.getTexturePropertyName(), defaultTextureRegion);

            for (ObjectMap.Entry<String, Object> property : spriteComponent.getProperties()) {
                Object value = property.value;
                if (value instanceof Number) {
                    graphSprites.setProperty(graphSprite, property.key, ((Number) value).floatValue());
                } else if (value instanceof TextureValue) {
                    TextureValue textureValue = (TextureValue) value;
                    TextureRegion textureRegionValue = textureLoader.loadTexture(textureValue.getAtlas(), textureValue.getTexture());
                    graphSprites.setProperty(graphSprite, property.key, textureRegionValue);
                } else {
                    graphSprites.setProperty(graphSprite, property.key, value);
                }
            }

            for (String tag : graphSprite.getAllTags()) {
                if (!spriteComponent.hasTag(tag))
                    graphSprites.removeTag(graphSprite, tag);
            }

            for (String tag : spriteComponent.getTags()) {
                if (!graphSprite.hasTag(tag))
                    graphSprites.addTag(graphSprite, tag);
            }
        }
    }
}
