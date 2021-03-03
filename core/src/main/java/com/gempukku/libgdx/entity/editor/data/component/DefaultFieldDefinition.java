package com.gempukku.libgdx.entity.editor.data.component;

public class DefaultFieldDefinition implements FieldDefinition {
    private String name;
    private Type type;
    private String typeId;

    public DefaultFieldDefinition(String name, Type type, String typeId) {
        this.name = name;
        this.type = type;
        this.typeId = typeId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getFieldTypeId() {
        return typeId;
    }
}
