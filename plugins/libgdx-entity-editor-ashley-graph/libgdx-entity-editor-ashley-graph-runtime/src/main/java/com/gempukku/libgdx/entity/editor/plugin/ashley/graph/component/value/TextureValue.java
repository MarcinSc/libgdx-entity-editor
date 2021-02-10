package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.value;

public class TextureValue {
    private String atlas;
    private String texture;

    public TextureValue() {
    }

    public TextureValue(String atlas, String texture) {
        this.atlas = atlas;
        this.texture = texture;
    }

    public String getAtlas() {
        return atlas;
    }

    public String getTexture() {
        return texture;
    }
}
