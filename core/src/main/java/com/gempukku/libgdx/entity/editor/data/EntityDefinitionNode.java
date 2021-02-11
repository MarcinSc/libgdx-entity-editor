package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.kotcrab.vis.ui.widget.VisLabel;

public class EntityDefinitionNode extends Tree.Node<Tree.Node, EntityDefinition, Label> {
    public EntityDefinitionNode(EntityDefinition entity) {
        String name = entity.getName();
        VisLabel label = new VisLabel(name);
        setActor(label);
        setValue(entity);
    }
}
