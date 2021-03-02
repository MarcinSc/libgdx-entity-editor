package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.gempukku.libgdx.entity.editor.data.component.type.FloatComponentFieldType;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.PositionComponent;

public class PositionComponentDataDefinition extends ComponentDataDefinition<PositionComponent, PositionComponentDataStorage> {
    public PositionComponentDataDefinition() {
        super("PositionComponent", true, "PositionComponent", PositionComponent.class.getName());
        addFieldType("x", FloatComponentFieldType.ID);
        addFieldType("y", FloatComponentFieldType.ID);
    }

    @Override
    protected PositionComponentDataStorage createComponentDataStorage(PositionComponent component) {
        return new PositionComponentDataStorage(component);
    }

    @Override
    protected Class<PositionComponent> getComponentClass() {
        return PositionComponent.class;
    }
}
