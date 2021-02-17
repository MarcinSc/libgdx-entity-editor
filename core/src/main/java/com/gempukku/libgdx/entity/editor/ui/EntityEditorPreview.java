package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.libgdx.entity.editor.project.PreviewRenderer;

public class EntityEditorPreview extends Actor {
    private enum Zoom {
        Z1(0.01f, "1%"),
        Z2(0.02f, "2%"),
        Z3(0.05f, "5%"),
        Z4(0.1f, "10%"),
        Z5(0.2f, "20%"),
        Z6(0.25f, "25%"),
        Z7(0.33f, "33%"),
        Z8(0.5f, "50%"),
        Z9(0.75f, "75%"),
        Z10(1f, "100%"),
        Z11(1.33f, "133%"),
        Z12(1.67f, "167%"),
        Z13(2f, "200%"),
        Z14(3f, "300%"),
        Z15(4f, "400%"),
        Z16(5, "500%"),
        Z17(6, "600%"),
        Z18(7, "700%"),
        Z19(8, "800%"),
        Z20(10, "1000%");

        private float value;
        private String display;

        Zoom(float value, String display) {
            this.value = value;
            this.display = display;
        }

        public float getValue() {
            return value;
        }

        public String getDisplay() {
            return display;
        }
    }

    private int zoomFactor = 0;
    private int zoomMin = -9;
    private int zoomMax = 10;
    private PreviewRenderer previewRenderer;
    private OrthographicCamera camera;
    private EntityEditorPreviewHandler defaultPreviewHandler;
    private EntityEditorPreviewHandler previewHandler;

    public EntityEditorPreview(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void setDefaultPreviewHandler(EntityEditorPreviewHandler defaultPreviewHandler) {
        if (previewHandler == null || previewHandler == this.defaultPreviewHandler)
            setPreviewHandler(defaultPreviewHandler);
        this.defaultPreviewHandler = defaultPreviewHandler;
    }

    public void setPreviewHandler(EntityEditorPreviewHandler previewHandler) {
        if (previewHandler == null)
            previewHandler = defaultPreviewHandler;

        if (this.previewHandler != null)
            this.previewHandler.destroy(this);
        this.previewHandler = previewHandler;
        this.previewHandler.initialize(this);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        camera.viewportWidth = getWidth();
        camera.viewportHeight = getHeight();
        camera.update();
    }

    public void setPreviewRenderer(PreviewRenderer previewRenderer) {
        this.previewRenderer = previewRenderer;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (previewRenderer != null) {
            previewRenderer.render(batch, getX(), getY(), getWidth(), getHeight());
        }
        if (previewHandler != null) {
            previewHandler.render(batch, getX(), getY(), getWidth(), getHeight());
        }
    }

    public void zoomIn() {
        zoomFactor = Math.min(zoomMax, zoomFactor + 1);
        applyZoom();
    }

    public void zoomOut() {
        zoomFactor = Math.max(zoomMin, zoomFactor - 1);
        applyZoom();
    }

    private void applyZoom() {
        camera.zoom = getCurrentZoom().value;
        camera.update();
    }

    private Zoom getCurrentZoom() {
        return Zoom.values()[zoomFactor - zoomMin];
    }
}
