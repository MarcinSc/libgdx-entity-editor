package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class EntityComponentRegistry {
    private static final Array<Class<?>> coreComponents = new Array<>();
    private static final ObjectMap<String, ComponentEditorFactory> componentEditorFactories = new ObjectMap<>();

    public static <T> void registerCoreComponent(Class<T> componentClass, ComponentEditorFactory<T> componentEditorFactory) {
        coreComponents.add(componentClass);
        componentEditorFactories.put(componentClass.getName(), componentEditorFactory);
    }

    public static Iterable<Class<?>> getCoreComponents() {
        return coreComponents;
    }

    public static <T> ComponentEditorFactory<T> getComponentEditorFactory(Class<T> componentClass) {
        return componentEditorFactories.get(componentClass.getName());
    }
}
