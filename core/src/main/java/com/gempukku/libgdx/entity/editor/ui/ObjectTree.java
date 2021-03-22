package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gempukku.libgdx.entity.editor.TextureSource;
import com.gempukku.libgdx.entity.editor.data.CustomDataDefinitionNode;
import com.gempukku.libgdx.entity.editor.data.CustomDataTypesNode;
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
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.data.component.CustomDataDefinition;
import com.gempukku.libgdx.entity.editor.data.component.CustomFieldTypeRegistry;
import com.gempukku.libgdx.entity.editor.data.component.DataDefinition;
import com.gempukku.libgdx.entity.editor.data.component.DefaultDataDefinition;
import com.gempukku.libgdx.entity.editor.data.component.FieldDefinition;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.util.dialog.OptionDialogListener;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTree;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

public class ObjectTree<T, U extends EntityDefinition> extends VisTable implements ObjectTreeData<U> {
    private static final Pattern namePattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-]{0,14}$");
    private VisTree<ObjectTreeNode, Object> tree;
    private EntityEditorProject<T, U> project;

    private EntityGroupsNode entityGroupsNode;
    private EntityTemplatesNode templatesNode;
    private CustomDataTypesNode customDataTypesNode;

    private Comparator<ObjectTreeNode> comparator = new ObjectTreeNodeComparator<T, U>();
    private TextureSource textureSource;

    public ObjectTree(EntityEditorProject<T, U> project, TextureSource textureSource) {
        this.project = project;
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
                        } else if (clickedNode instanceof CustomDataTypesNode) {
                            customDataTypesClicked(x, y);
                        } else if (clickedNode instanceof CustomDataDefinitionNode) {
                            customDataTypeClicked((CustomDataDefinitionNode) clickedNode, x, y);
                        }
                    }
                });
        tree.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        ObjectTreeNode selectedNode = tree.getSelectedNode();
                        if (selectedNode instanceof EntityDefinitionNode) {
                            fire(new EntitySelected(((EntityDefinitionNode<T, U>) selectedNode).getValue(), true));
                        } else if (selectedNode instanceof EntityTemplateNode) {
                            fire(new EntitySelected(((EntityTemplateNode<T, U>) selectedNode).getValue(), false));
                        } else {
                            fire(new EntitySelected(null, false));
                        }
                    }
                });

        entityGroupsNode = new EntityGroupsNode("Entity Groups");
        templatesNode = new EntityTemplatesNode("Templates");
        customDataTypesNode = new CustomDataTypesNode("Custom data types");
        tree.add(entityGroupsNode);
        tree.add(templatesNode);
        tree.add(customDataTypesNode);

        VisScrollPane scrollPane = new VisScrollPane(tree);
        scrollPane.setForceScroll(false, true);
        scrollPane.setFadeScrollBars(false);

        add(scrollPane).grow();
    }

    private void customDataTypeClicked(CustomDataDefinitionNode treeNode, float x, float y) {
        if (!treeNode.getValue().isReadOnly()) {
            MenuItem edit = new MenuItem("Edit");
            edit.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            DataTypeEditorDialog dialog = new DataTypeEditorDialog((DefaultDataDefinition) treeNode.getValue()) {
                                @Override
                                protected void result(Object object) {
                                }
                            };
                            dialog.button("Done", "Done");
                            dialog.show(getStage());
                        }
                    });

            MenuItem remove = new MenuItem("Remove");
            remove.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            Dialogs.showOptionDialog(getStage(), "Delete type", "Are you sure you want to delete this type?",
                                    Dialogs.OptionDialogType.YES_CANCEL,
                                    new OptionDialogListener() {
                                        @Override
                                        public void yes() {
                                            deleteCustomType(treeNode);
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
            popupMenu.addItem(edit);
            popupMenu.addItem(remove);
            popupMenu.showMenu(getStage(), x + getX(), y + getY());
        }
    }

    private void entityDefinitionClicked(EntityDefinitionNode<T, U> treeNode, float x, float y) {
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

    private void entityTemplateClicked(EntityTemplateNode<T, U> treeNode, float x, float y) {
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

    private void deleteEntity(EntityDefinitionNode<T, U> entity) {
        project.removeEntity(entity.getValue());
        entity.remove();
    }

    private void deleteCustomType(CustomDataDefinitionNode dataDefinitionNode) {
        String dataDefinitionId = dataDefinitionNode.getValue().getId();
        for (LocatedEntityDefinition<U> templateDefinition : getTemplates()) {
            templateDefinition.getEntityDefinition().removeComponent(dataDefinitionId);
        }
        for (String entityGroup : getEntityGroups()) {
            for (LocatedEntityDefinition<U> entity : getEntities(entityGroup)) {
                entity.getEntityDefinition().removeComponent(dataDefinitionId);
            }
        }

        dataDefinitionNode.remove();
        rebuildAllEntities();
    }

    private void deleteTemplate(EntityTemplateNode<T, U> template) {
        String templateId = template.getValue().getId();
        for (LocatedEntityDefinition<U> templateDefinition : getTemplates()) {
            templateDefinition.getEntityDefinition().removeTemplate(templateId);
        }
        for (String entityGroup : getEntityGroups()) {
            for (LocatedEntityDefinition<U> entity : getEntities(entityGroup)) {
                entity.getEntityDefinition().removeTemplate(templateId);
            }
        }

        project.removeTemplate(template.getValue());
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
                                        EntityTemplateNode<T, U> node = new EntityTemplateNode<>(project.createTemplate(createId(), input), new TextureRegionDrawable(textureSource.getTexture("images/template.png")));
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
        EntityGroupFolderNode node = new EntityGroupFolderNode(project.createEntityFolder(input), new TextureRegionDrawable(textureSource.getTexture("images/entity-folder.png")));
        mergeInNode(treeNode, node);
        return node;
    }

    private EntityTemplatesFolderNode createEntityTemplatesFolderNode(ObjectTreeNode treeNode, String input) {
        EntityTemplatesFolderNode node = new EntityTemplatesFolderNode(project.createTemplatesFolder(input), new TextureRegionDrawable(textureSource.getTexture("images/template-folder.png")));
        mergeInNode(treeNode, node);
        return node;
    }

    private void customDataTypesClicked(float x, float y) {
        PopupMenu popupMenu = new PopupMenu();

        MenuItem createNew = new MenuItem("Add new");
        createNew.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        String id = createId();
                        CustomDataDefinition customDataDefinition = new CustomDataDefinition(ObjectTree.this, id, false, "", "");
                        DataTypeEditorDialog dialog = new DataTypeEditorDialog(customDataDefinition) {
                            @Override
                            protected void result(Object object) {
                                if (object.equals("Add")) {
                                    addCustomDataType(customDataDefinition);
                                }
                            }
                        };
                        dialog.button("Cancel", "Cancel");
                        dialog.button("Add", "Add");
                        dialog.show(getStage());
                    }
                });

        MenuItem createFromFile = new MenuItem("Add from file...");
        createFromFile.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        FileHandle projectFolder = project.getProjectFolder();
                        FileChooser fileChooser = new FileChooser(projectFolder, FileChooser.Mode.OPEN);
                        fileChooser.setModal(true);
                        fileChooser.setListener(new FileChooserAdapter() {
                            @Override
                            public void selected(Array<FileHandle> file) {
                                FileHandle selectedFile = file.get(0);
                                if (selectedFile.path().startsWith(projectFolder.path())) {
                                    String id = createId();
                                    // The java file is in the project folder
                                    try {
                                        CustomDataDefinition customDataDefinition = parseCustomComponentDefinition(id, selectedFile);
                                        DataTypeEditorDialog dialog = new DataTypeEditorDialog(customDataDefinition) {
                                            @Override
                                            protected void result(Object object) {
                                                if (object.equals("Add")) {
                                                    addCustomDataType(customDataDefinition);
                                                }
                                            }
                                        };
                                        dialog.button("Cancel", "Cancel");
                                        dialog.button("Add", "Add");
                                        dialog.show(getStage());
                                    } catch (Exception exp) {
                                        Dialogs.showErrorDialog(getStage(), "Unable to parse the component", exp);
                                    }
                                } else {
                                    Dialogs.showErrorDialog(getStage(), "The file has to be in the project folder");
                                }
                            }
                        });
                        getStage().addActor(fileChooser.fadeIn());
                    }
                });

        popupMenu.addItem(createNew);
        popupMenu.addItem(createFromFile);
        popupMenu.showMenu(getStage(), x + getX(), y + getY());
    }

    private CustomDataDefinition parseCustomComponentDefinition(String id, FileHandle javaFile) throws IOException {
        CompilationUnit compilationUnit = StaticJavaParser.parse(javaFile.file());
        String name = compilationUnit.getPrimaryTypeName().get();
        ClassOrInterfaceDeclaration classDeclaration = compilationUnit.getClassByName(name).get();
        String className = classDeclaration.getFullyQualifiedName().get();

        CustomDataDefinition result = new CustomDataDefinition(this, id, false, name, className);
        classDeclaration.findAll(FieldDeclaration.class).stream()
                .filter(f -> !f.isStatic() && !f.isTransient())
                .forEach(f -> {
                            VariableDeclarator variable = f.getVariable(0);
                            String variableName = variable.getName().asString();
                            Type variableType = variable.getType();
                            Optional<ArrayType> arrayType = variableType.toArrayType();
                            Optional<ClassOrInterfaceType> classOrInterfaceType = variableType.toClassOrInterfaceType();

                            if (arrayType.isPresent()) {
                                variableType = arrayType.get().getComponentType();
                                ComponentFieldType<?> componentFieldType = CustomFieldTypeRegistry.getComponentFieldType(className, variableName, variableType);
                                if (componentFieldType != null) {
                                    result.addFieldType(variableName, FieldDefinition.Type.Array, componentFieldType.getId());
                                } else {
                                    throw new GdxRuntimeException("Unable to parse class");
                                }
                            } else if (isGdxArrayType(classOrInterfaceType)) {
                                Optional<NodeList<Type>> o = classOrInterfaceType.get().getTypeArguments();
                                if (o.isPresent()) {
                                    ComponentFieldType<?> componentFieldType = CustomFieldTypeRegistry.getComponentFieldType(className, variableName, o.get().get(0));
                                    if (componentFieldType != null) {
                                        result.addFieldType(variableName, FieldDefinition.Type.Array, componentFieldType.getId());
                                    } else {
                                        throw new GdxRuntimeException("Unable to parse class");
                                    }
                                } else {
                                    throw new GdxRuntimeException("Unable to parse class");
                                }
                            } else if (isMapType(classOrInterfaceType)) {
                                Optional<NodeList<Type>> o = classOrInterfaceType.get().getTypeArguments();
                                if (o.isPresent()) {
                                    NodeList<Type> typeParameters = o.get();
                                    String firstTypeParameter = typeParameters.get(0).asString();
                                    if (!firstTypeParameter.equals("String") && !firstTypeParameter.equals("java.lang.String"))
                                        throw new GdxRuntimeException("Unable to parse class");
                                    if (typeParameters.size() < 2)
                                        throw new GdxRuntimeException("Unable to parse class");

                                    ComponentFieldType<?> componentFieldType = CustomFieldTypeRegistry.getComponentFieldType(className, variableName, typeParameters.get(1));
                                    if (componentFieldType != null) {
                                        result.addFieldType(variableName, FieldDefinition.Type.Map, componentFieldType.getId());
                                    } else {
                                        throw new GdxRuntimeException("Unable to parse class");
                                    }
                                } else {
                                    throw new GdxRuntimeException("Unable to parse class");
                                }
                            } else {
                                ComponentFieldType<?> componentFieldType = CustomFieldTypeRegistry.getComponentFieldType(className, variableName, variableType);
                                if (componentFieldType != null) {
                                    result.addFieldType(variableName, FieldDefinition.Type.Object, componentFieldType.getId());
                                } else {
                                    throw new GdxRuntimeException("Unable to parse class");
                                }
                            }
                        }
                );
        return result;
    }

    private boolean isMapType(Optional<ClassOrInterfaceType> classOrInterfaceType) {
        return classOrInterfaceType.isPresent() &&
                (classOrInterfaceType.get().getName().asString().equals("ObjectMap") || classOrInterfaceType.get().getName().asString().equals("com.badlogic.gdx.utils.ObjectMap")
                        || classOrInterfaceType.get().getName().asString().equals("Map") || classOrInterfaceType.get().getName().asString().equals("java.util.Map"));
    }

    private boolean isGdxArrayType(Optional<ClassOrInterfaceType> classOrInterfaceType) {
        return classOrInterfaceType.isPresent() &&
                (classOrInterfaceType.get().getName().asString().equals("Array") || classOrInterfaceType.get().getName().asString().equals("com.badlogic.gdx.utils.Array"));
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
        EntityGroup entityGroup = project.createEntityGroup(input);
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
                                        U entity = project.createEntity(createId(), input);
                                        EntityDefinitionNode<T, U> node = new EntityDefinitionNode<>(entity, new TextureRegionDrawable(textureSource.getTexture("images/entity.png")));
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
    public void addEntity(String entityGroup, String parentPath, String name, U entity) {
        EntityGroupNode group = getEntityGroupNode(entityGroup, true);
        ObjectTreeNode entityGroupFolderNode = getEntityGroupFolderNode(group, parentPath, true);
        EntityDefinitionNode<T, U> node = new EntityDefinitionNode<>(entity, new TextureRegionDrawable(textureSource.getTexture("images/entity.png")));
        mergeInNode(entityGroupFolderNode, node);
    }

    @Override
    public void addTemplate(String parentPath, String name, U template) {
        ObjectTreeNode entityGroupFolderNode = getEntityTemplateFolderNode(parentPath, true);
        EntityTemplateNode<T, U> node = new EntityTemplateNode<>(template, new TextureRegionDrawable(textureSource.getTexture("images/template.png")));
        mergeInNode(entityGroupFolderNode, node);
    }

    @Override
    public void addCustomDataType(DataDefinition dataDefinition) {
        CustomDataDefinitionNode componentNode = new CustomDataDefinitionNode(dataDefinition, null);
        mergeInNode(customDataTypesNode, componentNode);
    }

    @Override
    public LocatedEntityDefinition<U> getTemplateById(String id) {
        return findTemplateById(templatesNode, id, null);
    }

    private LocatedEntityDefinition<U> findTemplateById(ObjectTreeNode node, String id, String parentPath) {
        for (Object child : node.getChildren()) {
            if (child instanceof EntityTemplatesFolderNode) {
                EntityTemplatesFolderNode folder = (EntityTemplatesFolderNode) child;
                String folderName = folder.getValue().getName();
                LocatedEntityDefinition<U> result = findTemplateById((ObjectTreeNode) child, id, getFullPath(parentPath, folderName));
                if (result != null)
                    return result;
            }
            if (child instanceof EntityTemplateNode) {
                EntityTemplateNode<T, U> template = (EntityTemplateNode<T, U>) child;
                U templateDefinition = template.getValue();
                if (templateDefinition.getId().equals(id)) {
                    return new LocatedEntityDefinition<>(templateDefinition, getFullPath(parentPath, templateDefinition.getName()));
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
    public Iterable<LocatedEntityDefinition<U>> getEntities(String entityGroup) {
        EntityGroupNode entityGroupNode = getEntityGroupNode(entityGroup, false);
        Array<LocatedEntityDefinition<U>> result = new Array<>();
        appendEntities(entityGroupNode, result, null);
        return result;
    }

    @Override
    public Iterable<LocatedEntityDefinition<U>> getTemplates() {
        Array<LocatedEntityDefinition<U>> result = new Array<>();
        appendTemplates(templatesNode, result, null);
        return result;
    }

    @Override
    public Iterable<DataDefinition<?, ?>> getDataDefinitions() {
        Array<DataDefinition<?, ?>> result = new Array<>();
        for (CustomDataDefinitionNode child : customDataTypesNode.getChildren()) {
            result.add(child.getValue());
        }
        return result;
    }

    @Override
    public DataDefinition<?, ?> getDataDefinitionById(String id) {
        for (CustomDataDefinitionNode child : customDataTypesNode.getChildren()) {
            DataDefinition<?, ?> dataDefinition = child.getValue();
            if (dataDefinition.getId().equals(id))
                return dataDefinition;
        }
        return null;
    }

    //
//    private boolean canCreateEntity(String entityGroup, String parentPath, String name) {
//        if (!validEntityGroup(entityGroup) || !validParentPath(parentPath) || !validName(name))
//            return false;
//
//        EntityGroupNode entityGroupNode = getEntityGroupNode(entityGroup, false);
//        if (entityGroupNode == null)
//            return true;
//        ObjectTreeNode entityGroupFolderNode = getEntityGroupFolderNode(entityGroupNode, parentPath, false);
//        if (entityGroupFolderNode == null)
//            return true;
//        for (Object child : entityGroupFolderNode.getChildren()) {
//            if (child instanceof EntityDefinitionNode) {
//                EntityDefinitionNode<T, U> entity = (EntityDefinitionNode<T, U>) child;
//                if (entity.getValue().getName().equals(name))
//                    return false;
//            }
//        }
//        return true;
//    }

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
                EntityDefinitionNode<T, U> entity = (EntityDefinitionNode<T, U>) child;
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
                EntityTemplateNode<T, U> template = (EntityTemplateNode<T, U>) child;
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
                EntityTemplateNode<T, U> template = (EntityTemplateNode<T, U>) child;
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
    public void convertToTemplate(String name, U entity) {
        String id = createId();
        U template = project.convertToTemplate(id, name, entity);
        EntityTemplateNode<T, U> node = new EntityTemplateNode<>(template, new TextureRegionDrawable(textureSource.getTexture("images/template.png")));
        mergeInNode(templatesNode, node);

        project.entityChanged(entity);
    }

    private void appendEntities(ObjectTreeNode<ObjectTreeNode, ? extends Object> entityGroupNode, Array<LocatedEntityDefinition<U>> result, String path) {
        for (ObjectTreeNode child : entityGroupNode.getChildren()) {
            if (child instanceof EntityGroupFolderNode) {
                EntityGroupFolder folder = ((EntityGroupFolderNode) child).getValue();
                appendEntities(child, result, (path == null) ? folder.getName() : path + "/" + folder.getName());
            } else if (child instanceof EntityDefinitionNode) {
                result.add(new LocatedEntityDefinition<>(((EntityDefinitionNode<T, U>) child).getValue(), path));
            }
        }
    }

    private void appendTemplates(ObjectTreeNode<ObjectTreeNode, ? extends Object> entityGroupNode, Array<LocatedEntityDefinition<U>> result, String path) {
        for (ObjectTreeNode child : entityGroupNode.getChildren()) {
            if (child instanceof EntityTemplatesFolderNode) {
                EntityTemplatesFolder folder = ((EntityTemplatesFolderNode) child).getValue();
                appendTemplates(child, result, (path == null) ? folder.getName() : path + "/" + folder.getName());
            } else if (child instanceof EntityTemplateNode) {
                result.add(new LocatedEntityDefinition<>(((EntityTemplateNode<T, U>) child).getValue(), path));
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
                EntityDefinitionNode<T, U> entityDefinitionNode = (EntityDefinitionNode<T, U>) child;
                project.entityChanged(entityDefinitionNode.getValue());
            }
        }
    }

    private static class ObjectTreeNodeComparator<T, U extends EntityDefinition> implements Comparator<ObjectTreeNode> {
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
            if (node instanceof CustomDataDefinitionNode)
                return 5;
            throw new IllegalArgumentException("Unknown type of node");
        }

        private String getNameForNode(ObjectTreeNode node) {
            if (node instanceof EntityGroupNode)
                return ((EntityGroupNode) node).getValue().getName();
            if (node instanceof EntityGroupFolderNode)
                return ((EntityGroupFolderNode) node).getValue().getName();
            if (node instanceof EntityDefinitionNode)
                return ((EntityDefinitionNode<T, U>) node).getValue().getName();
            if (node instanceof EntityTemplatesFolderNode)
                return ((EntityTemplatesFolderNode) node).getValue().getName();
            if (node instanceof EntityTemplateNode)
                return ((EntityTemplateNode<T, U>) node).getValue().getName();
            if (node instanceof CustomDataDefinitionNode)
                return ((CustomDataDefinitionNode) node).getValue().getName();
            throw new IllegalArgumentException("Unknown type of node");
        }
    }
}
