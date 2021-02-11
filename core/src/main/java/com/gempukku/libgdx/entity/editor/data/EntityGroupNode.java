package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.kotcrab.vis.ui.widget.VisLabel;

public class EntityGroupNode extends Tree.Node<Tree.Node, EntityGroup, Label> {
    public EntityGroupNode(EntityGroup entityGroup) {
        String name = entityGroup.getName();
        VisLabel label = new VisLabel(name);
        setActor(label);
        setValue(entityGroup);
    }
}
