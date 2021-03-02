package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FaceDirection;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FacingComponent;

public class FacingComponentDataStorage extends ComponentDataStorage<FacingComponent> {
    public FacingComponentDataStorage(FacingComponent component) {
        super(component);
    }

    @Override
    public ComponentDataStorage<FacingComponent> createDataStorage(FacingComponent component) {
        return new FacingComponentDataStorage(component);
    }

    @Override
    public Object getValue(String fieldName) {
        if (fieldName.equals("faceDirection"))
            return getComponent().getFaceDirection();
        else
            throw new IllegalArgumentException();
    }

    @Override
    public void setValue(String fieldName, Object value) {
        if (fieldName.equals("faceDirection"))
            getComponent().setFaceDirection((FaceDirection) value);
        else
            throw new IllegalArgumentException();
    }
}
