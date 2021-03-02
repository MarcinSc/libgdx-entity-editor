package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;

public interface DataDefinition<T extends DataStorage> {
    boolean isComponent();

    String getId();

    String getName();

    String getClassName();

    boolean isStoreWithProject();

    Iterable<FieldDefinition> getFieldTypes();

    T createDataStorage(EntityEditorProject project);

    T loadDataStorage(Json json, JsonValue data);

    JsonValue serializeDataStorage(Json json, T dataStorage);

    JsonValue exportComponent(Json json, T dataStorage);
//
//    ObjectMap<String, ComponentFieldType> getArrayFieldTypes();
//
//    ObjectMap<String, CustomDataDefinition> getObjectArrayFieldTypes();
//
//    ObjectMap<String, CustomDataDefinition> getObjectMapFieldTypes();
}
