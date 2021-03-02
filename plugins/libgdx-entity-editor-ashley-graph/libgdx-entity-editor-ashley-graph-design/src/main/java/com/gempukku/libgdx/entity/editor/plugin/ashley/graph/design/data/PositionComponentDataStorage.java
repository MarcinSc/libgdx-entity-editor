package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.PositionComponent;

public class PositionComponentDataStorage extends ComponentDataStorage<PositionComponent> {
    public PositionComponentDataStorage(PositionComponent component) {
        super(component);
    }

    @Override
    public ComponentDataStorage<PositionComponent> createDataStorage(PositionComponent component) {
        return new PositionComponentDataStorage(component);
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
