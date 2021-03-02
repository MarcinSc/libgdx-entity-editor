package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.ObjectMap;

public interface CustomDataDefinition {
    boolean isComponent();

    String getId();

    String getName();

    String getClassName();

    ObjectMap<String, String> getFieldTypes();
//
//    ObjectMap<String, ComponentFieldType> getArrayFieldTypes();
//
//    ObjectMap<String, CustomDataDefinition> getObjectArrayFieldTypes();
//
//    ObjectMap<String, CustomDataDefinition> getObjectMapFieldTypes();
}
