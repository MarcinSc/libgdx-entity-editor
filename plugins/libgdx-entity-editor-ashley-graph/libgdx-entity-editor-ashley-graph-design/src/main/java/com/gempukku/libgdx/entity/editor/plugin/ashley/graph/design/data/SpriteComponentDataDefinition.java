package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteComponent;

public class SpriteComponentDataDefinition extends ComponentDataDefinition<SpriteComponent, SpriteComponentDataStorage> {
    public SpriteComponentDataDefinition() {
        super("SpriteComponent", true, "SpriteComponent", SpriteComponent.class.getName());
    }

    @Override
    protected SpriteComponentDataStorage createComponentDataStorage(SpriteComponent component) {
        return new SpriteComponentDataStorage(component);
    }

    @Override
    protected Class<SpriteComponent> getComponentClass() {
        return SpriteComponent.class;
    }
}
