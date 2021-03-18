package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;

public class CustomDataStorage implements DataStorage {
    private ObjectMap<String, JsonValue> data;

    public CustomDataStorage(ObjectMap<String, JsonValue> data) {
        this.data = new ObjectMap<>();
        for (ObjectMap.Entry<String, JsonValue> datum : data) {
            this.data.put(datum.key, clone(datum.value));
        }
    }

    public CustomDataStorage(JsonValue jsonValue) {
        data = new ObjectMap<>();
        for (JsonValue value : jsonValue) {
            data.put(value.name(), clone(value));
        }
    }

    @Override
    public JsonValue getValue(String fieldName) {
        JsonValue value = data.get(fieldName);
        return clone(value);
    }

    @Override
    public void setValue(String fieldName, JsonValue value) {
        data.put(fieldName, clone(value));
    }

    public ObjectMap<String, JsonValue> getData() {
        return data;
    }

    private JsonValue clone(JsonValue value) {
        if (value.isString())
            return new JsonValue(value.asString());
        JsonReader reader = new JsonReader();
        return reader.parse(value.toJson(JsonWriter.OutputType.json));
    }
}
