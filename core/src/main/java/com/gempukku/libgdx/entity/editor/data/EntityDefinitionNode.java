package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

public class EntityDefinitionNode extends Tree.Node<Tree.Node, EntityDefinition, Label> {
    public EntityDefinitionNode(Skin skin, EntityDefinition entity) {
        String name = entity.getName();
        Label label = new Label(name, skin);
        setActor(label);
        setValue(entity);
    }
}
