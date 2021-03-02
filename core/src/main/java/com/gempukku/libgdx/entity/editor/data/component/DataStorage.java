package com.gempukku.libgdx.entity.editor.data.component;

public interface DataStorage {
    Object getValue(String fieldName);

    void setValue(String fieldName, Object value);
}
