package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

public class EntityTemplatesNode extends Tree.Node<Tree.Node, Object, Label> {
    public EntityTemplatesNode(Skin skin, String text) {
        super(new Label(text, skin));
    }

    public void addListener(EventListener eventListener) {
        getActor().addListener(eventListener);
    }
}
