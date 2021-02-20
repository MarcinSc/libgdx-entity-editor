package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.gempukku.libgdx.entity.editor.EntityEditorScreen;

public interface EntityEditorPreviewHandler {
    void initialize(EntityEditorScreen preview);

    void destroy(EntityEditorScreen preview);

    void render(Batch batch, float x, float y, float width, float height);
}
