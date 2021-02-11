package com.gempukku.libgdx.entity.editor.data.component;

public interface ComponentEditorFactory<T> {
    ComponentEditor<T> createComponentEditor(T component);
}
