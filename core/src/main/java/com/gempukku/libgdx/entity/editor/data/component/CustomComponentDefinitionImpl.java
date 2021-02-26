package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.ObjectMap;

public class CustomComponentDefinitionImpl implements CustomComponentDefinition {
    private String id;
    private String path;
    private String name;
    private String className;
    private ObjectMap<String, ComponentFieldType> fieldTypes = new ObjectMap<>();

    public CustomComponentDefinitionImpl(String id, String path, String name, String className) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.className = className;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getClassName() {
        return className;
    }

    public void addFieldType(String name, ComponentFieldType fieldType) {
        fieldTypes.put(name, fieldType);
    }

    @Override
    public ObjectMap<String, ComponentFieldType> getFieldTypes() {
        return fieldTypes;
    }

    @Override
    public ObjectMap<String, ComponentFieldType> getArrayFieldTypes() {
        // TODO
        return null;
    }

    @Override
    public ObjectMap<String, CustomDataDefinition> getObjectArrayFieldTypes() {
        // TODO
        return null;
    }

    @Override
    public ObjectMap<String, CustomDataDefinition> getObjectMapFieldTypes() {
        // TODO
        return null;
    }
}
