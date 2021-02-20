package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.gempukku.libgdx.entity.editor.EntityEditorScreen;
import com.gempukku.libgdx.entity.editor.TextureSource;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.AnchorComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.PositionComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.ScaleComponent;
import com.gempukku.libgdx.entity.editor.ui.EntityEditorPreviewHandler;
import com.gempukku.libgdx.graph.util.WhitePixel;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class AshleyGraphPreviewHandler extends InputListener implements EntityEditorPreviewHandler {
    private final Family position;
    private final Family positionAndScale;

    private Engine engine;
    private Camera camera;
    private TextureSource textureSource;
    private Vector3 tmpVector = new Vector3();
    private WhitePixel whitePixel;
    private long lastScrolled;
    private EntityEditorScreen screen;

    public AshleyGraphPreviewHandler(Engine engine, Camera camera, TextureSource textureSource) {
        this.engine = engine;
        this.camera = camera;
        this.textureSource = textureSource;
        position = Family.all(PositionComponent.class).get();
        positionAndScale = Family.all(PositionComponent.class, ScaleComponent.class).get();
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
        TextureRegion crosshair = textureSource.getTexture("images/crosshair.png");
        for (Entity positionEntity : engine.getEntitiesFor(position)) {
            PositionComponent position = positionEntity.getComponent(PositionComponent.class);
            tmpVector.set(position.getX(), position.getY(), 0);
            Vector3 location = camera.project(tmpVector, x, y, width, height);
            batch.draw(crosshair, location.x - 11, (location.y - 11), 23, 23);
        }

        ShapeDrawer shapeDrawer = new ShapeDrawer(batch, whitePixel.textureRegion);
        for (Entity positionAndScaleEntity : engine.getEntitiesFor(positionAndScale)) {
            PositionComponent position = positionAndScaleEntity.getComponent(PositionComponent.class);
            ScaleComponent scale = positionAndScaleEntity.getComponent(ScaleComponent.class);

            AnchorComponent anchor = positionAndScaleEntity.getComponent(AnchorComponent.class);
            float anchorX = anchor != null ? anchor.getX() : 0.5f;
            float anchorY = anchor != null ? anchor.getY() : 0.5f;

            tmpVector.set(position.getX() - scale.getX() * (1 - anchorX), position.getY() - scale.getY() * (1 - anchorY), 0);
            Vector3 firstCorner = camera.project(tmpVector, x, y, width, height);
            float cornerX = firstCorner.x;
            float cornerY = firstCorner.y;
            tmpVector.set(position.getX() + scale.getX() * anchorX, position.getY() + scale.getY() * anchorY, 0);
            Vector3 secondCorner = camera.project(tmpVector, x, y, width, height);
            shapeDrawer.rectangle(cornerX, cornerY, secondCorner.x - cornerX, secondCorner.y - cornerY);
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
