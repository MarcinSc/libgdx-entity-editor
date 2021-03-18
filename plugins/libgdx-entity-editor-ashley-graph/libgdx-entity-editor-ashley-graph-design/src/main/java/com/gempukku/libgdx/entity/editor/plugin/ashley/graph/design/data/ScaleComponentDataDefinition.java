package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.gempukku.libgdx.entity.editor.data.component.type.FloatComponentFieldType;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.ScaleComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.AshleyGraphProject;

public class ScaleComponentDataDefinition extends ComponentDataDefinition<ScaleComponent, ScaleComponentDataStorage> {
    public ScaleComponentDataDefinition(AshleyGraphProject ashleyGraphProject) {
        super(ashleyGraphProject, "ScaleComponent", true, "ScaleComponent", ScaleComponent.class.getName());
        addFieldType("x", FloatComponentFieldType.ID);
        addFieldType("y", FloatComponentFieldType.ID);
    }

    @Override
    protected ScaleComponentDataStorage createComponentDataStorage(ScaleComponent component) {
        return new ScaleComponentDataStorage(component);
    }

    @Override
    protected Class<ScaleComponent> getComponentClass() {
        return ScaleComponent.class;
    }
}
