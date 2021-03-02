package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.AnchorComponent;

public class AnchorComponentDataStorage extends ComponentDataStorage<AnchorComponent> {
    public AnchorComponentDataStorage(AnchorComponent component) {
        super(component);
    }

    @Override
    public ComponentDataStorage<AnchorComponent> createDataStorage(AnchorComponent component) {
        return new AnchorComponentDataStorage(component);
    }

    @Override
    public Object getValue(String fieldName) {
        if (fieldName.equals("x"))
            return getComponent().getX();
        else if (fieldName.equals("y"))
            return getComponent().getY();
        else
            throw new IllegalArgumentException();
    }

    @Override
    public void setValue(String fieldName, Object value) {
        if (fieldName.equals("x"))
            getComponent().setX(((Number) value).floatValue());
        else if (fieldName.equals("y"))
            getComponent().setY(((Number) value).floatValue());
        else
            throw new IllegalArgumentException();
    }

}
