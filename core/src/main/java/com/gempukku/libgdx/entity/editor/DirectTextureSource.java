package com.gempukku.libgdx.entity.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

public class DirectTextureSource implements TextureSource, Disposable {
    private ObjectMap<String, TextureRegion> textureMap = new ObjectMap<>();

    @Override
    public TextureRegion getTexture(String path) {
        TextureRegion textureRegion = textureMap.get(path);
        if (textureRegion == null) {
            textureRegion = new TextureRegion(new Texture(Gdx.files.internal(path)));
            textureMap.put(path, textureRegion);
        }

        return textureRegion;
    }

    @Override
    public void dispose() {
        for (TextureRegion value : textureMap.values()) {
            value.getTexture().dispose();
        }
        textureMap.clear();
    }
}
