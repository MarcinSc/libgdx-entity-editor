package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.Array;
import com.github.javaparser.ast.type.Type;

public class CustomFieldTypeRegistry {
    private static Array<ComponentFieldType<?>> fieldTypes = new Array<>();

    public static void registerComponentFieldType(ComponentFieldType<?> componentFieldType) {
        fieldTypes.add(componentFieldType);
    }

    public static ComponentFieldType getComponentFieldTypeById(String id) {
        for (ComponentFieldType<?> fieldType : fieldTypes) {
            if (fieldType.getId().equals(id))
                return fieldType;
        }
        return null;
    }

    public static Array<ComponentFieldType<?>> getFieldTypes() {
        return fieldTypes;
    }

    public static ComponentFieldType<?> getComponentFieldType(String componentClass, String fieldName, Type type) {
        for (ComponentFieldType<?> fieldType : fieldTypes) {
            if (fieldType.accepts(componentClass, fieldName, type, true))
                return fieldType;
        }
        for (ComponentFieldType<?> fieldType : fieldTypes) {
            if (fieldType.accepts(componentClass, fieldName, type, false))
                return fieldType;
        }
        return null;
    }
}
