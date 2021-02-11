package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.kotcrab.vis.ui.widget.VisLabel;

public class EntityGroupsNode extends Tree.Node<EntityGroupNode, Object, Label> {
    public EntityGroupsNode(String text) {
        super(new VisLabel(text));
    }
}
