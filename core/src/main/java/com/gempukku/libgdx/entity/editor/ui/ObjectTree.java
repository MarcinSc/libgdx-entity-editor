package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.entity.editor.data.EntityGroup;
import com.gempukku.libgdx.entity.editor.data.EntityGroupNode;
import com.gempukku.libgdx.entity.editor.data.EntityGroupsNode;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.plugin.ObjectTreeFeedback;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisTree;

public class ObjectTree extends Table implements ObjectTreeData {
    private VisTree<Tree.Node, Object> tree;
    private ObjectTreeFeedback objectTreeFeedback;

    private EntityGroupsNode entityGroupsNode;
    private EntityEditorNode templatesNode;

    public ObjectTree(Skin skin) {
        super(skin);

        initialize();
    }

    private void initialize() {
        clearChildren();

        tree = new VisTree<>();
        tree.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        System.out.println("Clicked " + event.getButton() + " " + System.currentTimeMillis());
                        Tree.Node clickedNode = tree.getNodeAt(y);
                        if (clickedNode.getValue() instanceof EntityGroupsNode) {
                            entityGroupsClicked(x, y);
                        }
                    }
                });
        entityGroupsNode = new EntityGroupsNode(getSkin(), "Entity Groups");
        templatesNode = new EntityEditorNode(getSkin(), "Templates");
        tree.add(entityGroupsNode);
        tree.add(templatesNode);

        ScrollPane scrollPane = new ScrollPane(tree);
        scrollPane.setForceScroll(false, true);
        scrollPane.setFadeScrollBars(false);

        add(scrollPane).grow();
    }

    private void entityGroupsClicked(float x, float y) {
        PopupMenu popupMenu = new PopupMenu();
        MenuItem createGroup = new MenuItem("Create group");
        createGroup.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Dialogs.showInputDialog(getStage(), "Create entity group", "Group name",
                                new InputDialogListener() {
                                    @Override
                                    public void finished(String input) {
                                        objectTreeFeedback.createEntityGroup(input);
                                    }

                                    @Override
                                    public void canceled() {

                                    }
                                });
                    }
                });
        popupMenu.add(createGroup);
        popupMenu.showMenu(getStage(), x, y);
    }

    @Override
    public void setObjectTreeFeedback(ObjectTreeFeedback objectTreeFeedback) {
        this.objectTreeFeedback = objectTreeFeedback;
    }

    @Override
    public void addEntityGroup(EntityGroup entityGroup) {
        entityGroupsNode.add(
                new EntityGroupNode(getSkin(), entityGroup));
    }

    @Override
    public EntityGroup getEntityGroup(String name) {
        for (EntityGroupNode child : entityGroupsNode.getChildren()) {
            if (child.getValue().getName().equals(name))
                return child.getValue();
        }
        return null;
    }

    @Override
    public Iterable<EntityGroup> getEntityGroups() {
        Array<EntityGroup> result = new Array<>();
        for (EntityGroupNode child : entityGroupsNode.getChildren()) {
            result.add(child.getValue());
        }

        return result;
    }
}
