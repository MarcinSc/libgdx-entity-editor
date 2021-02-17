package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.widget.VisLabel;

public class EntityTemplatesFolderNode extends Tree.Node<Tree.Node, EntityTemplatesFolder, Label> {
    public EntityTemplatesFolderNode(EntityTemplatesFolder folder, Drawable icon) {
        String name = folder.getName();
        VisLabel label = new VisLabel(name);
        setActor(label);
        setValue(folder);
        setIcon(icon);
    }
}
