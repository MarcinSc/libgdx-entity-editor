package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.EntityEditorScreen;
import com.gempukku.libgdx.entity.editor.TextureSource;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.PositionComponent;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;
import com.gempukku.libgdx.entity.editor.ui.EntityEditorPreview;
import com.gempukku.libgdx.entity.editor.ui.EntityEditorPreviewHandler;
import com.gempukku.libgdx.entity.editor.ui.EntityEditorPreviewToolbar;
import com.gempukku.libgdx.entity.editor.ui.EntitySelected;
import com.gempukku.libgdx.graph.util.WhitePixel;

public class AshleyGraphPreviewHandler extends InputListener implements EntityEditorPreviewHandler {
    private final ImmutableArray<Entity> positionEntities;

    private EntityEditorProject project;
    private Camera camera;
    private TextureSource textureSource;
    private Vector3 tmpVector = new Vector3();
    private WhitePixel whitePixel;
    private long lastScrolled;
    private EntityEditorScreen screen;
    private ObjectMap<Shape2D, Entity> entityCenters = new ObjectMap<>();
    private AshleyEntityDefinition editedEntity;

    private boolean panning;
    private float panX;
    private float panY;
    private float moveX;
    private float moveY;
    private float entityX;
    private float entityY;

    public AshleyGraphPreviewHandler(EntityEditorProject project, Engine engine, Camera camera, TextureSource textureSource) {
        this.project = project;
        this.camera = camera;
        this.textureSource = textureSource;
        Family position = Family.all(PositionComponent.class).get();
        positionEntities = engine.getEntitiesFor(position);
    }

    @Override
    public void initialize(EntityEditorScreen screen) {
        this.screen = screen;
        whitePixel = new WhitePixel();
        screen.getEntityEditorPreview().addListener(this);
    }

    @Override
    public <T, U extends EntityDefinition<T>> void setEditedEntity(U editedEntity, EntityEditorProject<T, U> project, boolean entity) {
        this.editedEntity = (AshleyEntityDefinition) editedEntity;
    }

    @Override
    public void destroy(EntityEditorScreen screen) {
        screen.getEntityEditorPreview().removeListener(this);
        whitePixel.dispose();
    }

    @Override
    public void render(Batch batch, float x, float y, float width, float height) {
        entityCenters.clear();
        TextureRegion crosshair = textureSource.getTexture("images/crosshair.png");
        TextureRegion selectedCrosshair = textureSource.getTexture("images/crosshair-selected.png");
        for (Entity positionEntity : positionEntities) {
            PositionComponent position = positionEntity.getComponent(PositionComponent.class);
            tmpVector.set(position.getX(), position.getY(), 0);
            Vector3 location = camera.project(tmpVector, x, y, width, height);
            if (editedEntity != null && editedEntity.getEntity() == positionEntity) {
                batch.draw(selectedCrosshair, location.x - 11, (location.y - 11), 23, 23);
            } else {
                batch.draw(crosshair, location.x - 11, (location.y - 11), 23, 23);
            }
            entityCenters.put(new Ellipse(location.x, location.y, 23, 23), positionEntity);
        }
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        if (panning) {
            EntityEditorPreview preview = screen.getEntityEditorPreview();
            preview.panBy(panX - x, panY - y);
            panX = x;
            panY = y;
        } else {
            if (editedEntity.hasCoreComponent(PositionComponent.class)) {
                EntityEditorPreviewToolbar toolbar = screen.getEntityEditorPreviewToolbar();

                PositionComponent position = editedEntity.getEntity().getComponent(PositionComponent.class);
                float zoom = toolbar.getZoom().getValue();
                float newX = entityX + (x - moveX) / zoom;
                float newY = entityY + (y - moveY) / zoom;

                float snap = toolbar.getSnap();
                if (snap > 0) {
                    newX = snap * MathUtils.round(newX / snap);
                    newY = snap * MathUtils.round(newY / snap);
                }
                position.setPosition(newX, newY);
                project.entityChanged(editedEntity);
            }
        }
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        screen.getStage().setScrollFocus(screen.getEntityEditorPreview());

        boolean handled = false;
        if (button == Input.Buttons.LEFT) {
            EntityEditorPreview preview = screen.getEntityEditorPreview();

            for (Entity positionEntity : positionEntities) {
                PositionComponent position = positionEntity.getComponent(PositionComponent.class);
                tmpVector.set(position.getX(), position.getY(), 0);
                Vector3 location = camera.project(tmpVector, preview.getX(), preview.getY(), preview.getWidth(), preview.getHeight());
                float dst = Vector2.dst(location.x, location.y, x, y);
                if (dst < 11) {
                    AshleyEntityComponent entityComponent = positionEntity.getComponent(AshleyEntityComponent.class);
                    preview.fire(new EntitySelected(entityComponent.getEntityDefinition(), true));
                    handled = true;
                    break;
                }
            }
        }

        if (!handled) {
            panning = true;
            panX = x;
            panY = y;
        } else {
            panning = false;
            PositionComponent position = editedEntity.getEntity().getComponent(PositionComponent.class);
            entityX = position.getX();
            entityY = position.getY();
            moveX = x;
            moveY = y;
        }

        return true;
    }

    @Override
    public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
        long currentTime = System.currentTimeMillis();
        if (lastScrolled + 30 < currentTime) {
            if (amountY > 0) {
                screen.getEntityEditorPreviewToolbar().zoomIn();
            } else if (amountY < 1) {
                screen.getEntityEditorPreviewToolbar().zoomOut();
            }
            lastScrolled = currentTime;
        }
        return true;
    }
}
