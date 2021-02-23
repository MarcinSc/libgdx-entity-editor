package com.gempukku.libgdx.entity.editor.ui.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public interface ComponentEditor<T> {
    Table getActor();

    void refresh();

    T getComponent();
}
