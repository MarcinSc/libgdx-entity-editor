package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public interface ComponentEditorFactory<T> {
    ComponentEditor<T> createComponentEditor(Skin skin, T component);
}
