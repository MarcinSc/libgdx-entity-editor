package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.JsonValue;

public interface DataStorage {
    JsonValue getValue(String fieldName);

    void setValue(String fieldName, JsonValue value);
}
