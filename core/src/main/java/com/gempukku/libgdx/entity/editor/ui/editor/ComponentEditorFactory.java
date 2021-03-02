package com.gempukku.libgdx.entity.editor.ui.editor;

import com.gempukku.libgdx.entity.editor.data.component.DataStorage;

public interface ComponentEditorFactory<T extends DataStorage> {
    ComponentEditor createComponentEditor(T dataStorage, Runnable callback, boolean editable);
}
