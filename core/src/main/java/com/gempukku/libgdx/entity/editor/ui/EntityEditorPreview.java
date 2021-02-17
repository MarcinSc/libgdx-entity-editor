package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.libgdx.entity.editor.project.PreviewRenderer;

public class EntityEditorPreview extends Actor {
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
}
