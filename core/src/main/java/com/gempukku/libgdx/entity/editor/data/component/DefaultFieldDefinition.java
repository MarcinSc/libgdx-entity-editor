package com.gempukku.libgdx.entity.editor.data.component;

public class DefaultFieldDefinition implements FieldDefinition {
    private String name;
    private String typeId;

    public DefaultFieldDefinition(String name, String typeId) {
        this.name = name;
        this.typeId = typeId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTypeId() {
        return typeId;
    }
}
