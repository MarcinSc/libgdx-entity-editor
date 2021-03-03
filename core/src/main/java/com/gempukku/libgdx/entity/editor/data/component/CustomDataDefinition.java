package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;

public class CustomDataDefinition extends DefaultDataDefinition<CustomDataStorage> {
    public CustomDataDefinition(String id, boolean component, String name, String className) {
        super(id, component, name, className);
    }

    @Override
    public CustomDataStorage createDataStorage(EntityEditorProject project) {
        return new CustomDataStorage();
    }

    @Override
    public CustomDataStorage loadDataStorage(Json json, JsonValue data) {
        return new CustomDataStorage(json.readValue(ObjectMap.class, data));
    }

    @Override
    public JsonValue serializeDataStorage(Json json, CustomDataStorage dataStorage) {
        JsonValue result = new JsonValue(JsonValue.ValueType.object);
        for (FieldDefinition fieldType : getFieldTypes()) {
            String name = fieldType.getName();
            FieldDefinition.Type type = fieldType.getType();
            String fieldTypeId = fieldType.getFieldTypeId();
            ComponentFieldType componentFieldType = CustomFieldTypeRegistry.getComponentFieldTypeById(fieldTypeId);
            if (type == FieldDefinition.Type.Object) {
                JsonValue value = componentFieldType.convertToJson(dataStorage.getValue(name));
                result.addChild(name, value);
            } else if (type == FieldDefinition.Type.Array) {
                JsonValue array = new JsonValue(JsonValue.ValueType.array);
                Array values = (Array) dataStorage.getValue(name);
                for (Object value : values) {
                    array.addChild(componentFieldType.convertToJson(value));
                }

                result.addChild(name, array);
            }
        }
        return result;
    }

    @Override
    public JsonValue exportComponent(Json json, CustomDataStorage dataStorage) {
        return serializeDataStorage(json, dataStorage);
    }

    @Override
    public boolean isStoreWithProject() {
        return true;
    }
}
