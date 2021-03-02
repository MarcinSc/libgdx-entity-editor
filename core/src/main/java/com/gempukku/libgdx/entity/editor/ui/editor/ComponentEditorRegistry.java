package com.gempukku.libgdx.entity.editor.ui.editor;

import com.badlogic.gdx.utils.ObjectMap;

public class ComponentEditorRegistry {
    private static final ObjectMap<String, ComponentEditorFactory<?>> componentEditorFactories = new ObjectMap<>();

    public static void registerComponentEditorFactory(String componentId, ComponentEditorFactory<?> componentEditorFactory) {
        componentEditorFactories.put(componentId, componentEditorFactory);
    }

    public static ComponentEditorFactory<?> getComponentEditorFactory(String componentId) {
        return componentEditorFactories.get(componentId);
    }
}
