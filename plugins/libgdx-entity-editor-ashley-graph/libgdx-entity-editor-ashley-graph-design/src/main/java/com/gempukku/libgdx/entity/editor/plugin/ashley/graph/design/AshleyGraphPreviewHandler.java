package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.EntityEditorScreen;
import com.gempukku.libgdx.entity.editor.TextureSource;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.PositionComponent;
import com.gempukku.libgdx.entity.editor.ui.EntityEditorPreviewHandler;
import com.gempukku.libgdx.graph.util.WhitePixel;

public class AshleyGraphPreviewHandler extends InputListener implements EntityEditorPreviewHandler {
    private final ImmutableArray<Entity> positionEntities;

    private Camera camera;
    private TextureSource textureSource;
    private Vector3 tmpVector = new Vector3();
    private WhitePixel whitePixel;
    private long lastScrolled;
    private EntityEditorScreen screen;
    private ObjectMap<Shape2D, Entity> entityCenters = new ObjectMap<>();

    public AshleyGraphPreviewHandler(Engine engine, Camera camera, TextureSource textureSource) {
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
    public void destroy(EntityEditorScreen screen) {
        screen.getEntityEditorPreview().removeListener(this);
        whitePixel.dispose();
    }

    @Override
    public void render(Batch batch, float x, float y, float width, float height) {
        entityCenters.clear();
        TextureRegion crosshair = textureSource.getTexture("images/crosshair.png");
        for (Entity positionEntity : positionEntities) {
            PositionComponent position = positionEntity.getComponent(PositionComponent.class);
            tmpVector.set(position.getX(), position.getY(), 0);
            Vector3 location = camera.project(tmpVector, x, y, width, height);
            batch.draw(crosshair, location.x - 11, (location.y - 11), 23, 23);
            entityCenters.put(new Ellipse(location.x, location.y, 23, 23), positionEntity);
        }
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        screen.getStage().setScrollFocus(screen.getEntityEditorPreview());

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
