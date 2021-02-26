package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.Array;
import com.github.javaparser.ast.type.Type;

public class CustomComponentRegistry {
    private static Array<ComponentFieldType<?>> fieldTypes = new Array<>();

    public static void registerComponentFieldType(ComponentFieldType<?> componentFieldType) {
        fieldTypes.add(componentFieldType);
    }

    public static ComponentFieldType<?> getComponentFieldType(Type type) {
        for (ComponentFieldType<?> fieldType : fieldTypes) {
            if (fieldType.accepts(type))
                return fieldType;
        }
        return null;
    }
}
