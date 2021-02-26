package com.gempukku.libgdx.entity.editor.ui.editor;

public interface ComponentEditorFactory<T> {
    ComponentEditor createComponentEditor(T component, Runnable callback, boolean editable);
}
