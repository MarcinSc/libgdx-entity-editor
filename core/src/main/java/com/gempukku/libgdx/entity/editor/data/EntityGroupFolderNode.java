package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

public class EntityGroupFolderNode extends Tree.Node<Tree.Node, EntityGroupFolder, Label> {
    public EntityGroupFolderNode(Skin skin, EntityGroupFolder folder) {
        String name = folder.getName();
        Label label = new Label(name, skin);
        setActor(label);
        setValue(folder);
    }
}
