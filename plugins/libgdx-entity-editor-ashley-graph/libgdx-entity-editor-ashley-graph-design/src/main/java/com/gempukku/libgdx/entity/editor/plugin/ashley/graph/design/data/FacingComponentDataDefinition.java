package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FaceDirection;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FacingComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.AshleyGraphProject;

public class FacingComponentDataDefinition extends ComponentDataDefinition<FacingComponent, FacingComponentDataStorage> {
    public FacingComponentDataDefinition(AshleyGraphProject ashleyGraphProject) {
        super(ashleyGraphProject, "FacingComponent", true, "FacingComponent", FacingComponent.class.getName());
        addFieldType("faceDirection", FaceDirection.class.getName());
    }

    @Override
    protected FacingComponentDataStorage createComponentDataStorage(FacingComponent component) {
        return new FacingComponentDataStorage(component);
    }

    @Override
    protected Class<FacingComponent> getComponentClass() {
        return FacingComponent.class;
    }
}
