package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteComponent;

public class SpriteComponentDataStorage extends ComponentDataStorage<SpriteComponent> {
    public SpriteComponentDataStorage(SpriteComponent component) {
        super(component);
    }

    @Override
    public ComponentDataStorage<SpriteComponent> createDataStorage(SpriteComponent component) {
        return new SpriteComponentDataStorage(component);
    }

    @Override
    public Object getValue(String fieldName) {
        throw new IllegalArgumentException();
    }

    @Override
    public void setValue(String fieldName, Object value) {
        throw new IllegalArgumentException();
    }
}
