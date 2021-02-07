package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

public class EntityEditorNode extends Tree.Node<Tree.Node, Object, Label> {
    public EntityEditorNode(Skin skin, String text) {
        super(new Label(text, skin));
    }

    public void addListener(EventListener eventListener) {
        getActor().addListener(eventListener);
    }
}
