package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public interface ComponentEditor<T> {
    Table getActor();

    void refresh();

    T getComponent();
}
