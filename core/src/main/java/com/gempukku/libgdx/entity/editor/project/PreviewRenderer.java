package com.gempukku.libgdx.entity.editor.project;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface PreviewRenderer {
    void prepare(int width, int height);

    void render(Batch batch, float x, float y, float width, float height);
}
