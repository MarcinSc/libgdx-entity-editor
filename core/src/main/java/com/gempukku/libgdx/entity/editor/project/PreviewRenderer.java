package com.gempukku.libgdx.entity.editor.project;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface PreviewRenderer {
    void render(Batch batch, float x, float y, float width, float height);
}
