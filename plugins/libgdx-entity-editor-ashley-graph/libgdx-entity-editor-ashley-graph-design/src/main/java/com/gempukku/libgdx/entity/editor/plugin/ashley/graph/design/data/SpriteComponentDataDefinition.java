package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.gempukku.libgdx.entity.editor.data.component.FieldDefinition;
import com.gempukku.libgdx.entity.editor.data.component.type.FloatComponentFieldType;
import com.gempukku.libgdx.entity.editor.data.component.type.StringComponentFieldType;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteComponent;

public class SpriteComponentDataDefinition extends ComponentDataDefinition<SpriteComponent, SpriteComponentDataStorage> {
    public SpriteComponentDataDefinition() {
        super("SpriteComponent", true, "SpriteComponent", SpriteComponent.class.getName());
        addFieldType("width", FloatComponentFieldType.ID);
        addFieldType("height", FloatComponentFieldType.ID);
        addFieldType("layer", FloatComponentFieldType.ID);
        addFieldType("tags", FieldDefinition.Type.Array, StringComponentFieldType.ID);
        addFieldType("properties", GraphSpritesPropertiesFieldType.ID);
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
