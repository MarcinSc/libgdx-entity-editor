package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.kotcrab.vis.ui.widget.VisLabel;

public class EntityGroupFolderNode extends Tree.Node<Tree.Node, EntityGroupFolder, Label> {
    public EntityGroupFolderNode(EntityGroupFolder folder) {
        String name = folder.getName();
        VisLabel label = new VisLabel(name);
        setActor(label);
        setValue(folder);
    }
}
