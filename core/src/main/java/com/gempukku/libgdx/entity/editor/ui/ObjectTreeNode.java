package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.kotcrab.vis.ui.widget.VisLabel;

public abstract class ObjectTreeNode<T extends ObjectTreeNode, U> extends Tree.Node<T, U, VisLabel> {
    public ObjectTreeNode(VisLabel actor) {
        super(actor);
    }
}
