package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.system.TextureLoader;

public class DirectTextureLoader implements TextureLoader, Disposable {
    private FileHandle root;
    private ObjectMap<String, TextureRegion> textures = new ObjectMap<>();

    public DirectTextureLoader(FileHandle root) {
        this.root = root;
    }

    @Override
    public TextureRegion loadTexture(String atlas, String texture) {
        if (texture == null || !root.child(texture).exists())
            return null;

        TextureRegion result = textures.get(texture);
        if (result == null) {
            try {
                result = new TextureRegion(new Texture(root.child(texture)));
                textures.put(texture, result);
            } catch (Exception exp) {
                return null;
            }
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
