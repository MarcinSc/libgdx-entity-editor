package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.system;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface TextureLoader {
    TextureRegion loadTexture(String atlas, String texture);
}
