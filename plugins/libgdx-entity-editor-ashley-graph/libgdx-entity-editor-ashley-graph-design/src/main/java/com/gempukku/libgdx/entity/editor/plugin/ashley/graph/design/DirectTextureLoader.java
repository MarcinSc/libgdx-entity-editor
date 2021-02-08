package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.system.TextureLoader;

public class DirectTextureLoader implements TextureLoader, Disposable {
    private ObjectMap<String, TextureRegion> textures = new ObjectMap<>();

    @Override
    public TextureRegion loadTexture(String atlas, String texture) {
        TextureRegion result = textures.get(texture);
        if (result == null) {
            result = new TextureRegion(new Texture(Gdx.files.internal(texture)));
            textures.put(texture, result);
        }
        return result;
    }

    @Override
    public void dispose() {
        for (TextureRegion texture : textures.values()) {
            texture.getTexture().dispose();
        }
        textures.clear();
    }
}
