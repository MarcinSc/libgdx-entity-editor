package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.libgdx.entity.editor.project.PreviewRenderer;

public class EntityEditorPreview extends Actor {
    private PreviewRenderer previewRenderer;
    private OrthographicCamera camera;

    public EntityEditorPreview() {
        this.camera = new OrthographicCamera();
    }

    public void setPreviewRenderer(PreviewRenderer previewRenderer) {
        this.previewRenderer = previewRenderer;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (previewRenderer != null) {
            previewRenderer.render(batch, getX(), getY(), getWidth(), getHeight());
        }
    }
}
