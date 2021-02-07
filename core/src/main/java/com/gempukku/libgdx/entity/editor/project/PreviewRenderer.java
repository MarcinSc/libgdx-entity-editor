package com.gempukku.libgdx.entity.editor.project;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;

public interface PreviewRenderer {
    void render(Camera camera, Batch batch);
}
