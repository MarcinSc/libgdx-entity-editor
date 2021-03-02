package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.gempukku.libgdx.entity.editor.data.component.type.FloatComponentFieldType;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.AnchorComponent;

public class AnchorComponentDataDefinition extends ComponentDataDefinition<AnchorComponent, AnchorComponentDataStorage> {
    public AnchorComponentDataDefinition() {
        super("AnchorComponent", true, "AnchorComponent", AnchorComponent.class.getName());
        addFieldType("x", FloatComponentFieldType.ID);
        addFieldType("y", FloatComponentFieldType.ID);
    }

    @Override
    protected AnchorComponentDataStorage createComponentDataStorage(AnchorComponent component) {
        return new AnchorComponentDataStorage(component);
    }

    @Override
    protected Class<AnchorComponent> getComponentClass() {
        return AnchorComponent.class;
    }
}
