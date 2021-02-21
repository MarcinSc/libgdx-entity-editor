package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.entity.editor.TextureSource;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.EntityDefinitionNode;
import com.gempukku.libgdx.entity.editor.data.EntityGroup;
import com.gempukku.libgdx.entity.editor.data.EntityGroupFolder;
import com.gempukku.libgdx.entity.editor.data.EntityGroupFolderNode;
import com.gempukku.libgdx.entity.editor.data.EntityGroupNode;
import com.gempukku.libgdx.entity.editor.data.EntityGroupsNode;
import com.gempukku.libgdx.entity.editor.data.EntityTemplateNode;
import com.gempukku.libgdx.entity.editor.data.EntityTemplatesFolder;
import com.gempukku.libgdx.entity.editor.data.EntityTemplatesFolderNode;
import com.gempukku.libgdx.entity.editor.data.EntityTemplatesNode;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.plugin.ObjectTreeFeedback;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.util.dialog.OptionDialogListener;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTree;

import java.util.Comparator;
import java.util.UUID;
import java.util.regex.Pattern;

public class ObjectTree extends VisTable implements ObjectTreeData {
    private static final Pattern namePattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-]*$");
    private VisTree<ObjectTreeNode, Object> tree;
    private ObjectTreeFeedback objectTreeFeedback;

    private EntityGroupsNode entityGroupsNode;
    private EntityTemplatesNode templatesNode;

    private Comparator<ObjectTreeNode> comparator = new ObjectTreeNodeComparator();
    private TextureSource textureSource;

    public ObjectTree(TextureSource textureSource) {
        this.textureSource = textureSource;
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
                        ObjectTreeNode clickedNode = tree.getNodeAt(y);
                        if (clickedNode instanceof EntityGroupsNode) {
                            entityGroupsClicked(x, y);
                        } else if (clickedNode instanceof EntityGroupNode
                                || clickedNode instanceof EntityGroupFolderNode) {
                            entityGroupClicked(clickedNode, x, y);
                        } else if (clickedNode instanceof EntityTemplatesNode
                                || clickedNode instanceof EntityTemplatesFolderNode) {
                            entityTemplatesClicked(clickedNode, x, y);
                        } else if (clickedNode instanceof EntityTemplateNode) {
                            entityTemplateClicked((EntityTemplateNode) clickedNode, x, y);
                        } else if (clickedNode instanceof EntityDefinitionNode) {
                            entityDefinitionClicked((EntityDefinitionNode) clickedNode, x, y);
                        }
                    }
                });
        tree.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        ObjectTreeNode selectedNode = tree.getSelectedNode();
                        if (selectedNode instanceof EntityDefinitionNode) {
                            fire(new EntitySelected(((EntityDefinitionNode) selectedNode).getValue(), true));
                        } else if (selectedNode instanceof EntityTemplateNode) {
                            fire(new EntitySelected(((EntityTemplateNode) selectedNode).getValue(), false));
                        } else {
                            fire(new EntitySelected(null, false));
                        }
                    }
                });

        entityGroupsNode = new EntityGroupsNode("Entity Groups");
        templatesNode = new EntityTemplatesNode("Templates");
        tree.add(entityGroupsNode);
        tree.add(templatesNode);

        VisScrollPane scrollPane = new VisScrollPane(tree);
        scrollPane.setForceScroll(false, true);
        scrollPane.setFadeScrollBars(false);

        add(scrollPane).grow();
    }

    private void entityDefinitionClicked(EntityDefinitionNode treeNode, float x, float y) {
        MenuItem renameEntity = new MenuItem("Rename");
        renameEntity.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Dialogs.showInputDialog(getStage(), "Rename entity", "Entity name",
                                new InputValidator() {
                                    @Override
                                    public boolean validateInput(String input) {
                                        return canCreateEntity(treeNode.getParent(), input);
                                    }
                                },
                                new InputDialogListener() {
                                    @Override
                                    public void finished(String input) {
                                        treeNode.setName(input);
                                    }

                                    @Override
                                    public void canceled() {

                                    }
                                });
                    }
                });
        MenuItem deleteEntity = new MenuItem("Delete entity");
        deleteEntity.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Dialogs.showOptionDialog(getStage(), "Delete entity", "Are you sure you want to delete the entity?",
                                Dialogs.OptionDialogType.YES_CANCEL,
                                new OptionDialogListener() {
                                    @Override
                                    public void yes() {
                                        deleteEntity(treeNode);
                                    }

                                    @Override
                                    public void no() {

                                    }

                                    @Override
                                    public void cancel() {

                                    }
                                });
                    }
                });

        PopupMenu popupMenu = new PopupMenu();
        popupMenu.addItem(renameEntity);
        popupMenu.addItem(deleteEntity);
        popupMenu.showMenu(getStage(), x + getX(), y + getY());
    }

    private void entityTemplateClicked(EntityTemplateNode treeNode, float x, float y) {
        MenuItem renameTemplate = new MenuItem("Rename");
        renameTemplate.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Dialogs.showInputDialog(getStage(), "Rename template", "Template name",
                                new InputValidator() {
                                    @Override
                                    public boolean validateInput(String input) {
                                        return canCreateTemplate(treeNode.getParent(), input);
                                    }
                                },
                                new InputDialogListener() {
                                    @Override
                                    public void finished(String input) {
                                        treeNode.setName(input);
                                    }

                                    @Override
                                    public void canceled() {

                                    }
                                });
                    }
                });
        MenuItem deleteTemplate = new MenuItem("Delete template");
        deleteTemplate.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Dialogs.showOptionDialog(getStage(), "Delete template", "Are you sure you want to delete the template?",
                                Dialogs.OptionDialogType.YES_CANCEL,
                                new OptionDialogListener() {
                                    @Override
                                    public void yes() {
                                        deleteTemplate(treeNode);
                                    }

                                    @Override
                                    public void no() {

                                    }

                                    @Override
                                    public void cancel() {

                                    }
                                });
                    }
                });

        PopupMenu popupMenu = new PopupMenu();
        popupMenu.addItem(renameTemplate);
        popupMenu.addItem(deleteTemplate);
        popupMenu.showMenu(getStage(), x + getX(), y + getY());
    }

    private void deleteEntity(EntityDefinitionNode entity) {
        objectTreeFeedback.removeEntity(entity.getValue());
        entity.remove();
    }

    private void deleteTemplate(EntityTemplateNode template) {
        String templateId = template.getValue().getId();
        for (LocatedEntityDefinition templateDefinition : getTemplates()) {
            templateDefinition.getEntityDefinition().removeTemplate(templateId);
        }
        for (String entityGroup : getEntityGroups()) {
            for (LocatedEntityDefinition entity : getEntities(entityGroup)) {
                entity.getEntityDefinition().removeTemplate(templateId);
            }
        }

        objectTreeFeedback.removeTemplate(template.getValue());
        template.remove();
        rebuildAllEntities();
    }

    private void entityTemplatesClicked(ObjectTreeNode treeNode, float x, float y) {
        MenuItem createFolder = new MenuItem("Create folder");
        createFolder.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Dialogs.showInputDialog(getStage(), "Create folder", "Folder name",
                                new InputValidator() {
                                    @Override
                                    public boolean validateInput(String input) {
                                        return canCreateTemplatesFolder(treeNode, input);
                                    }
                                },
                                new InputDialogListener() {
                                    @Override
                                    public void finished(String input) {
                                        createEntityTemplatesFolderNode(treeNode, input);
                                        treeNode.setExpanded(true);
                                    }

                                    @Override
                                    public void canceled() {

                                    }
                                });
                    }
                });

        MenuItem createEntity = new MenuItem("Create template");
        createEntity.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Dialogs.showInputDialog(getStage(), "Create entity", "Entity name",
                                new InputValidator() {
                                    @Override
                                    public boolean validateInput(String input) {
                                        return canCreateTemplate(treeNode, input);
                                    }
                                },
                                new InputDialogListener() {
                                    @Override
                                    public void finished(String input) {
                                        EntityTemplateNode node = new EntityTemplateNode(objectTreeFeedback.createTemplate(createId(), input), new TextureRegionDrawable(textureSource.getTexture("images/template.png")));
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

    private EntityGroupFolderNode createEntityGroupFolderNode(ObjectTreeNode treeNode, String input) {
        EntityGroupFolderNode node = new EntityGroupFolderNode(objectTreeFeedback.createEntityGroup(input), new TextureRegionDrawable(textureSource.getTexture("images/entity-folder.png")));
        mergeInNode(treeNode, node);
        return node;
    }

    private EntityTemplatesFolderNode createEntityTemplatesFolderNode(ObjectTreeNode treeNode, String input) {
        EntityTemplatesFolderNode node = new EntityTemplatesFolderNode(objectTreeFeedback.createTemplatesFolder(input), new TextureRegionDrawable(textureSource.getTexture("images/template-folder.png")));
        mergeInNode(treeNode, node);
        return node;
    }

    private void entityGroupsClicked(float x, float y) {
        PopupMenu popupMenu = new PopupMenu();
        MenuItem createGroup = new MenuItem("Create group");
        createGroup.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Dialogs.showInputDialog(getStage(), "Create entity group", "Group name",
                                new InputValidator() {
                                    @Override
                                    public boolean validateInput(String input) {
                                        return canCreateEntityGroup(input);
                                    }
                                },
                                new InputDialogListener() {
                                    @Override
                                    public void finished(String input) {
                                        createEntityGroupNode(input);
                                        entityGroupsNode.setExpanded(true);
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

    private EntityGroupNode createEntityGroupNode(String input) {
        EntityGroup entityGroup = objectTreeFeedback.createEntityGroup(input);
        EntityGroupNode node = new EntityGroupNode(entityGroup, new TextureRegionDrawable(textureSource.getTexture("images/entity-group.png")));
        mergeInNode(entityGroupsNode, node);
        return node;
    }

    private void entityGroupClicked(ObjectTreeNode treeNode, float x, float y) {
        MenuItem createFolder = new MenuItem("Create folder");
        createFolder.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Dialogs.showInputDialog(getStage(), "Create folder", "Folder name",
                                new InputValidator() {
                                    @Override
                                    public boolean validateInput(String input) {
                                        return canCreateEntityFolder(treeNode, input);
                                    }
                                },
                                new InputDialogListener() {
                                    @Override
                                    public void finished(String input) {
                                        createEntityGroupFolderNode(treeNode, input);
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
                                new InputValidator() {
                                    @Override
                                    public boolean validateInput(String input) {
                                        return canCreateEntity(treeNode, input);
                                    }
                                },
                                new InputDialogListener() {
                                    @Override
                                    public void finished(String input) {
                                        EntityDefinition entity = objectTreeFeedback.createEntity(createId(), input);
                                        EntityDefinitionNode node = new EntityDefinitionNode(entity, new TextureRegionDrawable(textureSource.getTexture("images/entity.png")));
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

    private String createId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private void mergeInNode(ObjectTreeNode parent, ObjectTreeNode child) {
        int newIndex = findNewIndex(parent, child);
        parent.insert(newIndex, child);
    }

    private int findNewIndex(ObjectTreeNode<? extends ObjectTreeNode, ? extends Object> parent, ObjectTreeNode<? extends ObjectTreeNode, ? extends Object> child) {
        int index = 0;
        for (ObjectTreeNode parentChild : parent.getChildren()) {
            if (comparator.compare(parentChild, child) > 0)
                return index;
            index++;
        }
        return index;
    }

    @Override
    public void setObjectTreeFeedback(ObjectTreeFeedback objectTreeFeedback) {
        this.objectTreeFeedback = objectTreeFeedback;
    }

    @Override
    public void addEntity(String entityGroup, String parentPath, String name, EntityDefinition entity) {
        EntityGroupNode group = getEntityGroupNode(entityGroup, true);
        ObjectTreeNode entityGroupFolderNode = getEntityGroupFolderNode(group, parentPath, true);
        EntityDefinitionNode node = new EntityDefinitionNode(entity, new TextureRegionDrawable(textureSource.getTexture("images/entity.png")));
        mergeInNode(entityGroupFolderNode, node);
    }

    @Override
    public void addTemplate(String parentPath, String name, EntityDefinition template) {
        ObjectTreeNode entityGroupFolderNode = getEntityTemplateFolderNode(parentPath, true);
        EntityTemplateNode node = new EntityTemplateNode(template, new TextureRegionDrawable(textureSource.getTexture("images/template.png")));
        mergeInNode(entityGroupFolderNode, node);
    }

    @Override
    public LocatedEntityDefinition getTemplateById(String id) {
        return findTemplateById(templatesNode, id, null);
    }

    private LocatedEntityDefinition findTemplateById(ObjectTreeNode node, String id, String parentPath) {
        for (Object child : node.getChildren()) {
            if (child instanceof EntityTemplatesFolderNode) {
                EntityTemplatesFolderNode folder = (EntityTemplatesFolderNode) child;
                String folderName = folder.getValue().getName();
                LocatedEntityDefinition result = findTemplateById((ObjectTreeNode) child, id, getFullPath(parentPath, folderName));
                if (result != null)
                    return result;
            }
            if (child instanceof EntityTemplateNode) {
                EntityTemplateNode template = (EntityTemplateNode) child;
                EntityDefinition templateDefinition = template.getValue();
                if (templateDefinition.getId().equals(id)) {
                    return new LocatedEntityDefinition(templateDefinition, getFullPath(parentPath, templateDefinition.getName()));
                }
            }
        }
        return null;
    }

    @Override
    public Iterable<String> getEntityGroups() {
        Array<String> result = new Array<>();
        for (EntityGroupNode child : entityGroupsNode.getChildren()) {
            result.add(child.getValue().getName());
        }

        return result;
    }

    @Override
    public Iterable<LocatedEntityDefinition> getEntities(String entityGroup) {
        EntityGroupNode entityGroupNode = getEntityGroupNode(entityGroup, false);
        Array<LocatedEntityDefinition> result = new Array<>();
        appendEntities(entityGroupNode, result, null);
        return result;
    }

    @Override
    public Iterable<LocatedEntityDefinition> getTemplates() {
        Array<LocatedEntityDefinition> result = new Array<>();
        appendTemplates(templatesNode, result, null);
        return result;
    }

    private boolean canCreateEntity(String entityGroup, String parentPath, String name) {
        if (!validEntityGroup(entityGroup) || !validParentPath(parentPath) || !validName(name))
            return false;

        EntityGroupNode entityGroupNode = getEntityGroupNode(entityGroup, false);
        if (entityGroupNode == null)
            return true;
        ObjectTreeNode entityGroupFolderNode = getEntityGroupFolderNode(entityGroupNode, parentPath, false);
        if (entityGroupFolderNode == null)
            return true;
        for (Object child : entityGroupFolderNode.getChildren()) {
            if (child instanceof EntityDefinitionNode) {
                EntityDefinitionNode entity = (EntityDefinitionNode) child;
                if (entity.getValue().getName().equals(name))
                    return false;
            }
        }
        return true;
    }

    private boolean canCreateEntityFolder(ObjectTreeNode parent, String name) {
        if (!validName(name))
            return false;

        for (Object child : parent.getChildren()) {
            if (child instanceof EntityGroupFolderNode) {
                EntityGroupFolderNode folder = (EntityGroupFolderNode) child;
                if (folder.getValue().getName().equals(name))
                    return false;
            }
        }
        return true;
    }

    private boolean canCreateEntityGroup(String name) {
        if (!validEntityGroup(name))
            return false;

        for (EntityGroupNode child : entityGroupsNode.getChildren()) {
            if (child.getValue().getName().equals(name))
                return false;
        }
        return true;
    }

    private boolean canCreateTemplatesFolder(ObjectTreeNode parent, String name) {
        if (!validName(name))
            return false;

        for (Object child : parent.getChildren()) {
            if (child instanceof EntityTemplatesFolderNode) {
                EntityTemplatesFolderNode folder = (EntityTemplatesFolderNode) child;
                if (folder.getValue().getName().equals(name))
                    return false;
            }
        }
        return true;
    }

    private boolean canCreateEntity(ObjectTreeNode parent, String name) {
        if (!validName(name))
            return false;

        for (Object child : parent.getChildren()) {
            if (child instanceof EntityDefinitionNode) {
                EntityDefinitionNode entity = (EntityDefinitionNode) child;
                if (entity.getValue().getName().equals(name))
                    return false;
            }
        }
        return true;
    }

    private boolean canCreateTemplate(ObjectTreeNode parent, String name) {
        if (!validName(name))
            return false;

        for (Object child : parent.getChildren()) {
            if (child instanceof EntityTemplateNode) {
                EntityTemplateNode template = (EntityTemplateNode) child;
                if (template.getValue().getName().equals(name))
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean canCreateTemplate(String parentPath, String name) {
        if (!validParentPath(parentPath) || !validName(name))
            return false;

        ObjectTreeNode entityTemplatesFolderNode = getEntityTemplateFolderNode(parentPath, false);
        if (entityTemplatesFolderNode == null)
            return true;
        for (Object child : entityTemplatesFolderNode.getChildren()) {
            if (child instanceof EntityTemplateNode) {
                EntityTemplateNode template = (EntityTemplateNode) child;
                if (template.getValue().getName().equals(name))
                    return false;
            }
        }
        return true;
    }

    private boolean validEntityGroup(String groupName) {
        return validName(groupName);
    }

    private boolean validParentPath(String parentPath) {
        if (parentPath == null)
            return true;
        String[] pathSplit = parentPath.split("/");
        for (String value : pathSplit) {
            if (!validName(value))
                return false;
        }
        return true;
    }

    private boolean validName(String value) {
        if (namePattern.matcher(value).matches())
            return true;
        return false;
    }

    @Override
    public void convertToTemplate(String name, EntityDefinition entity) {
        String id = createId();
        EntityDefinition template = objectTreeFeedback.convertToTemplate(id, name, entity);
        EntityTemplateNode node = new EntityTemplateNode(template, new TextureRegionDrawable(textureSource.getTexture("images/template.png")));
        mergeInNode(templatesNode, node);
        entity.rebuildEntity();
    }

    private void appendEntities(ObjectTreeNode<ObjectTreeNode, ? extends Object> entityGroupNode, Array<LocatedEntityDefinition> result, String path) {
        for (ObjectTreeNode child : entityGroupNode.getChildren()) {
            if (child instanceof EntityGroupFolderNode) {
                EntityGroupFolder folder = ((EntityGroupFolderNode) child).getValue();
                appendEntities(child, result, (path == null) ? folder.getName() : path + "/" + folder.getName());
            } else if (child instanceof EntityDefinitionNode) {
                result.add(new LocatedEntityDefinition(((EntityDefinitionNode) child).getValue(), path));
            }
        }
    }

    private void appendTemplates(ObjectTreeNode<ObjectTreeNode, ? extends Object> entityGroupNode, Array<LocatedEntityDefinition> result, String path) {
        for (ObjectTreeNode child : entityGroupNode.getChildren()) {
            if (child instanceof EntityTemplatesFolderNode) {
                EntityTemplatesFolder folder = ((EntityTemplatesFolderNode) child).getValue();
                appendTemplates(child, result, (path == null) ? folder.getName() : path + "/" + folder.getName());
            } else if (child instanceof EntityTemplateNode) {
                result.add(new LocatedEntityDefinition(((EntityTemplateNode) child).getValue(), path));
            }
        }
    }

    private EntityGroupNode getEntityGroupNode(String name, boolean create) {
        for (EntityGroupNode child : entityGroupsNode.getChildren()) {
            if (child.getValue().getName().equals(name))
                return child;
        }

        if (create)
            return createEntityGroupNode(name);
        else
            return null;
    }

    private ObjectTreeNode getEntityGroupFolderNode(EntityGroupNode groupNode, String path, boolean create) {
        if (path == null)
            return groupNode;

        String[] pathElements = path.split("/");

        return findEntityGroupFolderNode(groupNode, create, pathElements, 0);
    }

    private ObjectTreeNode findEntityGroupFolderNode(ObjectTreeNode<? extends ObjectTreeNode, ? extends Object> node, boolean create, String[] path, int index) {
        if (path.length == index)
            return node;

        String name = path[index];
        for (ObjectTreeNode child : node.getChildren()) {
            if (child instanceof EntityGroupFolderNode) {
                EntityGroupFolder folder = ((EntityGroupFolderNode) child).getValue();
                if (folder.getName().equals(name))
                    return findEntityGroupFolderNode(child, create, path, index + 1);
            }
        }

        if (create)
            return findEntityGroupFolderNode(createEntityGroupFolderNode(node, name), create, path, index + 1);
        else
            return null;
    }

    private ObjectTreeNode getEntityTemplateFolderNode(String path, boolean create) {
        if (path == null)
            return templatesNode;

        String[] pathElements = path.split("/");

        return findEntityTemplatesFolderNode(templatesNode, create, pathElements, 0);
    }

    private ObjectTreeNode findEntityTemplatesFolderNode(ObjectTreeNode<? extends ObjectTreeNode, ? extends Object> node, boolean create, String[] path, int index) {
        if (path.length == index)
            return node;

        String name = path[index];
        for (ObjectTreeNode child : node.getChildren()) {
            if (child instanceof EntityTemplatesFolderNode) {
                EntityTemplatesFolder folder = ((EntityTemplatesFolderNode) child).getValue();
                if (folder.getName().equals(name))
                    return findEntityTemplatesFolderNode(child, create, path, index + 1);
            }
        }

        if (create)
            return findEntityGroupFolderNode(createEntityTemplatesFolderNode(node, name), create, path, index + 1);
        else
            return null;
    }

    public static String getFullPath(String parentPath, String name) {
        if (parentPath == null)
            return name;
        else
            return parentPath + "/" + name;
    }

    public void rebuildAllEntities() {
        rebuildAllEntities(entityGroupsNode);
    }

    private void rebuildAllEntities(ObjectTreeNode node) {
        for (Object child : node.getChildren()) {
            if (child instanceof EntityGroupNode) {
                rebuildAllEntities((EntityGroupNode) child);
            } else if (child instanceof EntityGroupFolderNode) {
                rebuildAllEntities((EntityGroupFolderNode) child);
            } else if (child instanceof EntityDefinitionNode) {
                EntityDefinitionNode entityDefinitionNode = (EntityDefinitionNode) child;
                entityDefinitionNode.getValue().rebuildEntity();
            }
        }
    }

    private static class ObjectTreeNodeComparator implements Comparator<ObjectTreeNode> {
        @Override
        public int compare(ObjectTreeNode o1, ObjectTreeNode o2) {
            int type1 = getTypeValue(o1);
            int type2 = getTypeValue(o2);
            if (type1 != type2)
                return type1 - type2;

            return getNameForNode(o1).compareToIgnoreCase(getNameForNode(o2));
        }

        private int getTypeValue(ObjectTreeNode node) {
            if (node instanceof EntityGroupNode)
                return 0;
            if (node instanceof EntityGroupFolderNode)
                return 1;
            if (node instanceof EntityDefinitionNode)
                return 2;
            if (node instanceof EntityTemplatesFolderNode)
                return 3;
            if (node instanceof EntityTemplateNode)
                return 4;
            throw new IllegalArgumentException("Unknown type of node");
        }

        private String getNameForNode(ObjectTreeNode node) {
            if (node instanceof EntityGroupNode)
                return ((EntityGroupNode) node).getValue().getName();
            if (node instanceof EntityGroupFolderNode)
                return ((EntityGroupFolderNode) node).getValue().getName();
            if (node instanceof EntityDefinitionNode)
                return ((EntityDefinitionNode) node).getValue().getName();
            if (node instanceof EntityTemplatesFolderNode)
                return ((EntityTemplatesFolderNode) node).getValue().getName();
            if (node instanceof EntityTemplateNode)
                return ((EntityTemplateNode) node).getValue().getName();
            throw new IllegalArgumentException("Unknown type of node");
        }
    }
}
