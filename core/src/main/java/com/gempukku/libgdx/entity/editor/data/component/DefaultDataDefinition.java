package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.Array;

public abstract class DefaultDataDefinition<T extends DataStorage> implements DataDefinition<T> {
    private String id;
    private boolean component;
    private String name;
    private String className;
    private Array<FieldDefinition> fieldTypes = new Array<>();

    public DefaultDataDefinition(String id, boolean component, String name, String className) {
        this.id = id;
        this.component = component;
        this.name = name;
        this.className = className;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isComponent() {
        return component;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public Iterable<FieldDefinition> getFieldTypes() {
        return fieldTypes;
    }

    public void setComponent(boolean component) {
        this.component = component;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void addFieldType(String fieldName, String typeId) {
        addFieldType(fieldName, FieldDefinition.Type.Object, typeId);
    }

    public void addFieldType(String fieldName, FieldDefinition.Type type, String typeId) {
        fieldTypes.add(new DefaultFieldDefinition(fieldName, type, typeId));
    }

    public void removeField(String name) {
        for (FieldDefinition fieldType : fieldTypes) {
            if (fieldType.getName().equals(name)) {
                fieldTypes.removeValue(fieldType, true);
                break;
            }
        }
    }

    public boolean hasField(String name) {
        for (FieldDefinition fieldType : fieldTypes) {
            if (fieldType.getName().equals(name))
                return true;
        }

        return false;
    }
}
