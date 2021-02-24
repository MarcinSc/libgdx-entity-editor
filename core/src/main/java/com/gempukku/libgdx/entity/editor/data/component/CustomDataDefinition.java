package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.ObjectMap;

public interface CustomDataDefinition {
    String getClassName();

    ObjectMap<String, ComponentFieldType> getFieldTypes();

    ObjectMap<String, ComponentFieldType> getArrayFieldTypes();

    ObjectMap<String, CustomDataDefinition> getObjectArrayFieldTypes();

    ObjectMap<String, CustomDataDefinition> getObjectMapFieldTypes();
}
