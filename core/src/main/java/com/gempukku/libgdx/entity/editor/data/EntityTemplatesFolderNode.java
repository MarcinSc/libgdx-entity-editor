package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

public class EntityTemplatesFolderNode extends Tree.Node<Tree.Node, EntityTemplatesFolder, Label> {
    public EntityTemplatesFolderNode(Skin skin, EntityTemplatesFolder folder) {
        String name = folder.getName();
        Label label = new Label(name, skin);
        setActor(label);
        setValue(folder);
    }
}
