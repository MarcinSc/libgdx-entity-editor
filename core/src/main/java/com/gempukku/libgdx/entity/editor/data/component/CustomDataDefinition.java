package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

public class CustomDataDefinition extends DefaultDataDefinition<CustomDataStorage, ObjectMap> {
    private boolean storedWithProject = true;
    private boolean readOnly = false;

    public CustomDataDefinition(String id, boolean component, String name, String className) {
        super(id, component, name, className);
    }

    @Override
    public ObjectMap createDefaultValue() {
        return new ObjectMap<>();
    }

    @Override
    public ObjectMap unpackFromDataStorage(CustomDataStorage dataStorage) {
        return dataStorage.getData();
    }

    @Override
    public CustomDataStorage wrapDataStorage(ObjectMap value) {
        return new CustomDataStorage(value);
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
            String fieldTypeId = fieldType.getFieldTypeId();
            ComponentFieldType componentFieldType = CustomFieldTypeRegistry.getComponentFieldTypeById(fieldTypeId);

            if (type == FieldDefinition.Type.Object) {
                result.addChild(name, dataStorage.getValue(name));
            } else if (type == FieldDefinition.Type.Array) {
                JsonValue array = new JsonValue(JsonValue.ValueType.array);
                for (JsonValue value : dataStorage.getValue(name)) {
                    array.addChild(value);
                }

                result.addChild(name, array);
            } else if (type == FieldDefinition.Type.Map) {
                JsonValue map = new JsonValue(JsonValue.ValueType.object);
                for (JsonValue jsonValue : dataStorage.getValue(name)) {
                    map.addChild(jsonValue.name(), jsonValue);
                }

                result.addChild(name, map);
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
