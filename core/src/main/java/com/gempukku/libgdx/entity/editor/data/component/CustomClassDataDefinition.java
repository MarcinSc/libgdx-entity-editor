package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class CustomClassDataDefinition<T> extends DefaultDataDefinition<CustomDataStorage, T> {
    private boolean storedWithProject = false;
    private boolean readOnly = true;
    private Class<T> clazz;

    public CustomClassDataDefinition(String id, boolean component, String name, Class<T> clazz) {
        super(id, component, name, clazz.getName());
        this.clazz = clazz;
    }

    @Override
    public T createDefaultValue() {
        try {
            return ClassReflection.newInstance(clazz);
        } catch (ReflectionException e) {
            throw new GdxRuntimeException(e);
        }
    }

    @Override
    public T unpackFromDataStorage(CustomDataStorage dataStorage) {
        JsonValue jsonValue = serializeDataStorage(dataStorage);
        Json json = new Json();
        json.setUsePrototypes(false);
        return json.readValue(clazz, jsonValue);
    }

    @Override
    public CustomDataStorage wrapDataStorage(T value) {
        Json json = new Json();
        json.setUsePrototypes(false);
        JsonReader jsonReader = new JsonReader();
        JsonValue data = jsonReader.parse(json.toJson(value, clazz));
        return loadDataStorage(json, data);
    }

    @Override
    public CustomDataStorage loadDataStorage(Json json, JsonValue data) {
        return new CustomDataStorage(data);
    }

    @Override
    public JsonValue serializeDataStorage(CustomDataStorage dataStorage) {
        JsonValue result = new JsonValue(JsonValue.ValueType.object);
        for (FieldDefinition fieldType : getFieldTypes()) {
            String name = fieldType.getName();
            FieldDefinition.Type type = fieldType.getType();

            if (type == FieldDefinition.Type.Object) {
                result.addChild(name, dataStorage.getValue(name));
            } else if (type == FieldDefinition.Type.Array) {
                result.addChild(name, dataStorage.getValue(name));
            } else if (type == FieldDefinition.Type.Map) {
                result.addChild(name, dataStorage.getValue(name));
            }
        }
        return result;
    }

    @Override
    public JsonValue exportComponent(CustomDataStorage dataStorage) {
        return serializeDataStorage(dataStorage);
    }

    @Override
    public boolean isStoredWithProject() {
        return storedWithProject;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }
}
