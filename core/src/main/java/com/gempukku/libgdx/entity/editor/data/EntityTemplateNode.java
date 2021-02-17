package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;

public class EntityTemplateNode extends Tree.Node<Tree.Node, EntityDefinition, Label> {
    public EntityTemplateNode(EntityDefinition entity, Drawable icon) {
        String name = entity.getName();
        VisLabel label = new VisLabel(name);
        label.setAlignment(Align.left);
        setActor(label);
        setValue(entity);
        setIcon(icon);
    }
}
