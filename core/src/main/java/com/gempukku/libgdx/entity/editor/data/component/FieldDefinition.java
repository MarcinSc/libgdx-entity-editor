package com.gempukku.libgdx.entity.editor.data.component;

public interface FieldDefinition {
    enum Type {
        Object, Array, Map
    }

    String getName();

    Type getType();

    String getFieldTypeId();
}
