package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.utils.Array;

public class EntityComponentRegistry {
    private static final Array<Class<?>> coreComponents = new Array<>();

    public static void registerCoreComponent(Class<?> componentClass) {
        coreComponents.add(componentClass);
    }

    public static Iterable<Class<?>> getCoreComponents() {
        return coreComponents;
    }
}
