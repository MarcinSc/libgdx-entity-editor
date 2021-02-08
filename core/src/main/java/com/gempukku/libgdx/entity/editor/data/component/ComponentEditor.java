package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface ComponentEditor<T> {
    Actor getActor();

    void refresh();

    T getComponent();
}
