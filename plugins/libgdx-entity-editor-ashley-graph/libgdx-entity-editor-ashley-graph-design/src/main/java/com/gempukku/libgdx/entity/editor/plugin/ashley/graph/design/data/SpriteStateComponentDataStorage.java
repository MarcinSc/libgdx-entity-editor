package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteStateComponent;

public class SpriteStateComponentDataStorage extends ComponentDataStorage<SpriteStateComponent> {
    public SpriteStateComponentDataStorage(SpriteStateComponent component) {
        super(component);
    }

    @Override
    public ComponentDataStorage<SpriteStateComponent> createDataStorage(SpriteStateComponent component) {
        return new SpriteStateComponentDataStorage(component);
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
