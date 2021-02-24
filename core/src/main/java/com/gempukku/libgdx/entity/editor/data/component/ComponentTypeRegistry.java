package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Predicate;
import com.github.javaparser.ast.type.Type;

public class ComponentTypeRegistry {
    private static ObjectMap<Predicate<Type>, ComponentFieldType> fieldTypes = new ObjectMap<>();

    public static void registerComponentFieldType(Predicate<Type> typePredicate, ComponentFieldType componentFieldType) {
        fieldTypes.put(typePredicate, componentFieldType);
    }

    public static ComponentFieldType getComponentFieldType(Type type) {
        for (ObjectMap.Entry<Predicate<Type>, ComponentFieldType> fieldTypeEntry : fieldTypes) {
            if (fieldTypeEntry.key.evaluate(type))
                return fieldTypeEntry.value;
        }
        return null;
    }
}
