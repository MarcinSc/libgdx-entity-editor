package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

public class EntityGroupNode extends Tree.Node<Tree.Node, EntityGroup, Label> {
    public EntityGroupNode(Skin skin, EntityGroup entityGroup) {
        String name = entityGroup.getName();
        Label label = new Label(name, skin);
        setActor(label);
        setValue(entityGroup);
    }
}
