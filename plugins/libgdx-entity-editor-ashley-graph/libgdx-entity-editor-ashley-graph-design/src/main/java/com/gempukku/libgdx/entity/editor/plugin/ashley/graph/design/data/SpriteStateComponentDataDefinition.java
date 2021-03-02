package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteStateComponent;

public class SpriteStateComponentDataDefinition extends ComponentDataDefinition<SpriteStateComponent, SpriteStateComponentDataStorage> {
    public SpriteStateComponentDataDefinition() {
        super("SpriteStateComponent", true, "SpriteStateComponent", SpriteStateComponent.class.getName());
    }

    @Override
    protected SpriteStateComponentDataStorage createComponentDataStorage(SpriteStateComponent component) {
        return new SpriteStateComponentDataStorage(component);
    }

    @Override
    protected Class<SpriteStateComponent> getComponentClass() {
        return SpriteStateComponent.class;
    }
}
