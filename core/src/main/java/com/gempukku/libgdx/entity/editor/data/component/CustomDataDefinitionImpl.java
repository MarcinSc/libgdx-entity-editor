package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.ObjectMap;

public class CustomDataDefinitionImpl implements CustomDataDefinition {
    private String id;
    private boolean component;
    private String name;
    private String className;
    private ObjectMap<String, String> fieldTypes = new ObjectMap<>();

    public CustomDataDefinitionImpl(String id, boolean component, String name, String className) {
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

    public void setComponent(boolean component) {
        this.component = component;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void addFieldType(String name, String fieldType) {
        fieldTypes.put(name, fieldType);
    }

    @Override
    public ObjectMap<String, String> getFieldTypes() {
        return fieldTypes;
    }

    public void removeField(String name) {
        fieldTypes.remove(name);
    }

    public boolean hasField(String name) {
        return fieldTypes.containsKey(name);
    }
}
