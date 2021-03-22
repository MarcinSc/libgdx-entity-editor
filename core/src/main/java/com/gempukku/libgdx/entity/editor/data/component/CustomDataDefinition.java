package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.ui.editor.DataDefinitionEditableType;

public class CustomDataDefinition extends DefaultDataDefinition<CustomDataStorage, ObjectMap<String, JsonValue>> {
    private boolean storedWithProject = true;
    private boolean readOnly = false;
    private ObjectTreeData objectTreeData;

    public CustomDataDefinition(ObjectTreeData objectTreeData, String id, boolean component, String name, String className) {
        super(id, component, name, className);
        this.objectTreeData = objectTreeData;
    }

    @Override
    public ObjectMap<String, JsonValue> createDefaultValue() {
        ObjectMap<String, JsonValue> result = new ObjectMap<>();
        for (FieldDefinition fieldDefinition : getFieldTypes()) {
            final String name = fieldDefinition.getName();
            FieldDefinition.Type type = fieldDefinition.getType();
            String typeId = fieldDefinition.getFieldTypeId();
            EditableType fieldType = getEditableType(objectTreeData, typeId);

            if (type == FieldDefinition.Type.Object) {
                JsonValue value = fieldType.convertToJson(fieldType.getDefaultValue());
                result.put(name, value);
            } else if (type == FieldDefinition.Type.Array) {
                JsonValue array = new JsonValue(JsonValue.ValueType.array);
                result.put(name, array);
            } else if (type == FieldDefinition.Type.Map) {
                JsonValue map = new JsonValue(JsonValue.ValueType.object);
                result.put(name, map);
            }
        }

        return result;
    }

    private EditableType getEditableType(ObjectTreeData<?> objectTreeData, String typeId) {
        EditableType fieldType = CustomFieldTypeRegistry.getComponentFieldTypeById(typeId);
        if (fieldType == null) {
            fieldType = new DataDefinitionEditableType(objectTreeData, objectTreeData.getDataDefinitionById(typeId));
        }
        return fieldType;
    }

    @Override
    public ObjectMap<String, JsonValue> unpackFromDataStorage(CustomDataStorage dataStorage) {
        return dataStorage.getData();
    }

    @Override
    public CustomDataStorage wrapDataStorage(ObjectMap<String, JsonValue> value) {
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
            result.addChild(name, dataStorage.getValue(name));
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
