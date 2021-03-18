package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.gempukku.libgdx.entity.editor.data.component.FieldDefinition;
import com.gempukku.libgdx.entity.editor.data.component.type.StringComponentFieldType;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteStateComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.AshleyGraphProject;

public class SpriteStateComponentDataDefinition extends ComponentDataDefinition<SpriteStateComponent, SpriteStateComponentDataStorage> {
    public SpriteStateComponentDataDefinition(AshleyGraphProject ashleyGraphProject) {
        super(ashleyGraphProject, "SpriteStateComponent", true, "SpriteStateComponent", SpriteStateComponent.class.getName());
        addFieldType("state", StringComponentFieldType.ID);
        addFieldType("states", FieldDefinition.Type.Map, "SpriteStateDataDef");
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
