package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface EntityEditorPreviewHandler {
    void initialize(EntityEditorPreview preview);

    void destroy(EntityEditorPreview preview);

    void render(Batch batch, float x, float y, float width, float height);
}
