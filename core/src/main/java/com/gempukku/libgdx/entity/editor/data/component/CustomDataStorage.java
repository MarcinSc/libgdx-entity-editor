package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.ObjectMap;

public class CustomDataStorage implements DataStorage {
    private ObjectMap<String, Object> data;

    public CustomDataStorage() {
        this(new ObjectMap<>());
    }

    public CustomDataStorage(ObjectMap<String, Object> data) {
        this.data = data;
    }

    @Override
    public Object getValue(String fieldName) {
        return data.get(fieldName);
    }

    @Override
    public void setValue(String fieldName, Object value) {
        data.put(fieldName, value);
    }

    public ObjectMap<String, Object> getData() {
        return data;
    }
}
