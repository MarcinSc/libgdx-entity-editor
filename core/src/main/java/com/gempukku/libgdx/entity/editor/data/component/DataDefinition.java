package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public interface DataDefinition<T extends DataStorage, U> {
    boolean isComponent();

    String getId();

    String getName();

    String getClassName();

    boolean isStoredWithProject();

    boolean isReadOnly();

    Iterable<FieldDefinition> getFieldTypes();

    U createDefaultValue();

    U unpackFromDataStorage(T dataStorage);

    T wrapDataStorage(U value);

    T loadDataStorage(Json json, JsonValue data);

    JsonValue serializeDataStorage(T dataStorage);

    JsonValue exportComponent(T dataStorage);
//
//    ObjectMap<String, ComponentFieldType> getArrayFieldTypes();
//
//    ObjectMap<String, CustomDataDefinition> getObjectArrayFieldTypes();
//
//    ObjectMap<String, CustomDataDefinition> getObjectMapFieldTypes();
}
