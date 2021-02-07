package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

public class EntityGroupsNode extends Tree.Node<EntityGroupNode, Object, Label> {
    public EntityGroupsNode(Skin skin, String text) {
        super(new Label(text, skin));
    }
}
