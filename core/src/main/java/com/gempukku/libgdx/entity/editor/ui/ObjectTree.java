package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.EntityDefinitionNode;
import com.gempukku.libgdx.entity.editor.data.EntityGroup;
import com.gempukku.libgdx.entity.editor.data.EntityGroupFolder;
import com.gempukku.libgdx.entity.editor.data.EntityGroupFolderNode;
import com.gempukku.libgdx.entity.editor.data.EntityGroupNode;
import com.gempukku.libgdx.entity.editor.data.EntityGroupsNode;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.plugin.ObjectTreeFeedback;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisTree;

import java.util.Comparator;

public class ObjectTree extends Table implements ObjectTreeData {
    private VisTree<Tree.Node, Object> tree;
    private ObjectTreeFeedback objectTreeFeedback;

    private EntityGroupsNode entityGroupsNode;
    private EntityEditorNode templatesNode;

    private Comparator<Tree.Node> comparator = new ObjectTreeNodeComparator();

    public ObjectTree(Skin skin) {
        super(skin);

        initialize();
    }

    private void initialize() {
        clearChildren();

        tree = new VisTree<>();
        tree.setIndentSpacing(20);

        tree.addListener(
                new ClickListener(Input.Buttons.RIGHT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Tree.Node clickedNode = tree.getNodeAt(y);
                        if (clickedNode instanceof EntityGroupsNode) {
                            entityGroupsClicked(clickedNode, x, y);
                        } else if (clickedNode instanceof EntityGroupNode) {
                            entityGroupClicked(clickedNode, ((EntityGroupNode) clickedNode).getValue(), x, y);
                        } else if (clickedNode instanceof EntityGroupFolderNode) {
                            entityGroupFolderClicked(clickedNode, ((EntityGroupFolderNode) clickedNode).getValue(), x, y);
                        }
                    }
                });
        tree.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Tree.Node selectedNode = tree.getSelectedNode();
                        if (selectedNode instanceof EntityDefinitionNode) {
                            fire(new EntitySelected(((EntityDefinitionNode) selectedNode).getValue()));
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

    private void entityGroupsClicked(Tree.Node node, float x, float y) {
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
                                        node.setExpanded(true);
                                    }

                                    @Override
                                    public void canceled() {

                                    }
                                });
                    }
                });
        popupMenu.addItem(createGroup);
        popupMenu.showMenu(getStage(), x + getX(), y + getY());
    }

    private void entityGroupClicked(Tree.Node treeNode, EntityGroupFolder entityGroupFolder, float x, float y) {
        MenuItem createFolder = new MenuItem("Create folder");
        createFolder.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Dialogs.showInputDialog(getStage(), "Create folder", "Folder name",
                                new InputDialogListener() {
                                    @Override
                                    public void finished(String input) {
                                        EntityGroupFolder entityFolder = objectTreeFeedback.createEntityFolder(entityGroupFolder, input);
                                        EntityGroupFolderNode node = new EntityGroupFolderNode(getSkin(), entityFolder);
                                        mergeInNode(treeNode, node);
                                        treeNode.setExpanded(true);
                                    }

                                    @Override
                                    public void canceled() {

                                    }
                                });
                    }
                });

        MenuItem createEntity = new MenuItem("Create entity");
        createEntity.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Dialogs.showInputDialog(getStage(), "Create entity", "Entity name",
                                new InputDialogListener() {
                                    @Override
                                    public void finished(String input) {
                                        EntityDefinition entity = objectTreeFeedback.createEntity(entityGroupFolder, input);
                                        EntityDefinitionNode node = new EntityDefinitionNode(getSkin(), entity);
                                        mergeInNode(treeNode, node);
                                        treeNode.setExpanded(true);
                                    }

                                    @Override
                                    public void canceled() {

                                    }
                                });
                    }
                });

        PopupMenu popupMenu = new PopupMenu();
        popupMenu.addItem(createFolder);
        popupMenu.addItem(createEntity);
        popupMenu.showMenu(getStage(), x + getX(), y + getY());
    }

    private void mergeInNode(Tree.Node parent, Tree.Node child) {
        int newIndex = findNewIndex(parent, child);
        parent.insert(newIndex, child);
    }

    private int findNewIndex(Tree.Node<? extends Tree.Node, ? extends Object, ? extends Actor> parent, Tree.Node<? extends Tree.Node, ? extends Object, ? extends Actor> child) {
        int index = 0;
        for (Tree.Node parentChild : parent.getChildren()) {
            if (comparator.compare(parentChild, child) > 0)
                return index;
            index++;
        }
        return index;
    }


    private void entityGroupFolderClicked(Tree.Node treeNode, EntityGroupFolder folder, float x, float y) {
        entityGroupClicked(treeNode, folder, x, y);
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

    private static class ObjectTreeNodeComparator implements Comparator<Tree.Node> {
        @Override
        public int compare(Tree.Node o1, Tree.Node o2) {
            int type1 = getTypeValue(o1);
            int type2 = getTypeValue(o2);
            if (type1 != type2)
                return type1 - type2;

            return getNameForNode(o1).compareTo(getNameForNode(o2));
        }

        private int getTypeValue(Tree.Node node) {
            if (node instanceof EntityGroupFolderNode)
                return 0;
            if (node instanceof EntityDefinitionNode)
                return 1;
            throw new IllegalArgumentException("Unknown type of node");
        }

        private String getNameForNode(Tree.Node node) {
            if (node instanceof EntityGroupFolderNode)
                return ((EntityGroupFolderNode) node).getValue().getName();
            if (node instanceof EntityDefinitionNode)
                return ((EntityDefinitionNode) node).getValue().getName();
            throw new IllegalArgumentException("Unknown type of node");
        }
    }
}
