package com.gempukku.libgdx.entity.editor.data.component;

public interface FieldDefinition {
    enum Type {
        Object, Array
    }

    String getName();

    Type getType();

    String getFieldTypeId();
}
