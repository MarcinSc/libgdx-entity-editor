package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gempukku.libgdx.entity.editor.EntityEditorScreen;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;
import com.gempukku.libgdx.entity.editor.project.PreviewRenderer;

public class EntityEditorPreview<T, U extends EntityDefinition> extends Widget {
    private PreviewRenderer previewRenderer;
    private EntityEditorProject<T, U> project;
    private EntityEditorPreviewToolbar toolbar;
    private OrthographicCamera camera;
    private EntityEditorPreviewHandler defaultPreviewHandler;
    private EntityEditorPreviewHandler previewHandler;

    public EntityEditorPreview(EntityEditorProject<T, U> project, EntityEditorPreviewToolbar toolbar, OrthographicCamera camera) {
        this.project = project;
        this.toolbar = toolbar;
        this.camera = camera;

        toolbar.addZoomListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        applyZoom();
                    }
                });
    }

    public void setDefaultPreviewHandler(EntityEditorScreen screen, EntityEditorPreviewHandler defaultPreviewHandler) {
        if (previewHandler == null || previewHandler == this.defaultPreviewHandler)
            setPreviewHandler(screen, defaultPreviewHandler);
        this.defaultPreviewHandler = defaultPreviewHandler;
    }

    public void setPreviewHandler(EntityEditorScreen screen, EntityEditorPreviewHandler previewHandler) {
        if (previewHandler == null)
            previewHandler = defaultPreviewHandler;

        if (this.previewHandler != null)
            this.previewHandler.destroy(screen);
        this.previewHandler = previewHandler;
        this.previewHandler.initialize(screen);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        camera.viewportWidth = getWidth();
        camera.viewportHeight = getHeight();
        camera.update();

        previewRenderer.prepare(MathUtils.round(getWidth()), MathUtils.round(getHeight()));
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
            if (clipBegin()) {
                previewHandler.render(batch, getX(), getY(), getWidth(), getHeight());
                batch.flush();
                clipEnd();
            }
        }
    }

    private void applyZoom() {
        camera.zoom = 1f / toolbar.getZoom().getValue();
        camera.update();
    }

    public void setEditedEntity(U editedEntity, boolean entity) {
        previewHandler.setEditedEntity(editedEntity, project, entity);
    }

    public void panBy(float x, float y) {
        camera.position.set(camera.position.x + x * camera.zoom, camera.position.y + y * camera.zoom, camera.position.z);
        camera.update();
    }
}
