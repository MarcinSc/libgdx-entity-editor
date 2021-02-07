package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;

public class ObjectTree extends Table implements ObjectTreeData {
    private Tree<EntityEditorNode, String> tree;

    public ObjectTree(Skin skin) {
        super(skin);

        initialize();
    }

    private void initialize() {
        clearChildren();

        tree = new Tree<EntityEditorNode, String>(getSkin());
        tree.add(new EntityEditorNode(getSkin(), "Entity Groups"));
        tree.add(new EntityEditorNode(getSkin(), "Templates"));

        ScrollPane scrollPane = new ScrollPane(tree);
        scrollPane.setForceScroll(false, true);
        scrollPane.setFadeScrollBars(false);

        add(scrollPane).grow();
    }
}
