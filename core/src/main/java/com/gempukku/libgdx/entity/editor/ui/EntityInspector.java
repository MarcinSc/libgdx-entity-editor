package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.data.component.EntityComponentRegistry;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.CollapsibleWidget;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class EntityInspector<T, U extends EntityDefinition<T>> extends VisTable {
    private final VerticalGroup entityDetails;
    private final VerticalGroup entityComponents;

    private ObjectTreeData<U> objectTreeData;
    private U editedEntity;
    private EntityEditorProject<T, U> project;
    private boolean entity;
    private Runnable changeCallback;

    public EntityInspector() {
        entityDetails = new VerticalGroup();
        entityDetails.top();
        entityDetails.grow();

        entityComponents = new VerticalGroup();
        entityComponents.top();
        entityComponents.grow();

        add("Entity inspector").growX().row();
        addSeparator().growX().row();
        add(entityDetails).growX().row();

        VisScrollPane scrollPane = new VisScrollPane(entityComponents);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);
        add(scrollPane).grow().row();

        addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (!entity && editedEntity != null) {
                            fire(new TemplateChanged());
                        }
                        event.stop();
                    }
                });

        changeCallback = new Runnable() {
            @Override
            public void run() {
                project.entityChanged(editedEntity);
            }
        };
    }

    public void setObjectTreeData(ObjectTreeData objectTreeData) {
        this.objectTreeData = objectTreeData;
    }

    public void setEditedEntity(U editedEntity, EntityEditorProject<T, U> project, boolean entity) {
        this.editedEntity = editedEntity;
        this.project = project;
        this.entity = entity;

        rebuildUi();
    }

    private void rebuildUi() {
        entityDetails.clearChildren();
        entityComponents.clearChildren();

        if (editedEntity != null) {
            HorizontalGroup templatesGroup = new HorizontalGroup().wrap();

            VisTextButton addComponent = new VisTextButton("Add component");
            addComponent.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            PopupMenu menu = new PopupMenu();

                            for (Class coreComponent : EntityComponentRegistry.getCoreComponents()) {
                                if (project.supportsComponent(coreComponent) && !editedEntity.hasCoreComponent(coreComponent)) {
                                    Class<? extends T> coreComponentClass = (Class<? extends T>) coreComponent;
                                    MenuItem menuItem = new MenuItem(coreComponent.getSimpleName());
                                    menuItem.addListener(
                                            new ChangeListener() {
                                                @Override
                                                public void changed(ChangeEvent event, Actor actor) {
                                                    T component = project.createCoreComponent(coreComponentClass);
                                                    editedEntity.addCoreComponent(component);
                                                    project.entityChanged(editedEntity);
                                                    rebuildUi();
                                                }
                                            });
                                    menu.addItem(menuItem);
                                }
                            }

                            menu.showMenu(getStage(), addComponent);
                        }
                    });

            VisTextButton removeComponent = new VisTextButton("Remove component");
            removeComponent.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            PopupMenu menu = new PopupMenu();

                            for (Class<? extends T> coreComponent : editedEntity.getCoreComponents()) {
                                MenuItem menuItem = new MenuItem(coreComponent.getSimpleName());
                                menuItem.addListener(
                                        new ChangeListener() {
                                            @Override
                                            public void changed(ChangeEvent event, Actor actor) {
                                                editedEntity.removeCoreComponent(coreComponent);
                                                project.entityChanged(editedEntity);
                                                rebuildUi();
                                            }
                                        });
                                menu.addItem(menuItem);
                            }

                            menu.showMenu(getStage(), removeComponent);
                        }
                    });

            VisTextButton addTemplate = new VisTextButton("Add template");
            addTemplate.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            PopupMenu popupMenu = createAddTemplatePopup();
                            popupMenu.showMenu(getStage(), addTemplate);
                        }
                    });

            VisTextButton removeTemplates = new VisTextButton("Remove templates");
            removeTemplates.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            boolean changed = false;
                            for (Actor child : templatesGroup.getChildren()) {
                                TemplateContainer container = (TemplateContainer) child;
                                if (container.isChecked()) {
                                    editedEntity.removeTemplate(container.getId());
                                    changed = true;
                                }
                            }
                            if (changed) {
                                project.entityChanged(editedEntity);
                                rebuildUi();
                            }
                        }
                    });

            entityDetails.addActor(new VisLabel("Name: " + editedEntity.getName()));
            VisTable componentsButtonTable = new VisTable();
            componentsButtonTable.add(addComponent);
            componentsButtonTable.add(removeComponent).row();
            entityDetails.addActor(componentsButtonTable);
            entityDetails.addActor(new Separator());
            entityDetails.addActor(new VisLabel("Inherited templates"));

            for (String template : editedEntity.getTemplates()) {
                String path = objectTreeData.getTemplateById(template).getPath();
                templatesGroup.addActor(new TemplateContainer(template, path));
            }
            entityDetails.addActor(templatesGroup);
            VisTable templatesButtonTable = new VisTable();
            templatesButtonTable.add(addTemplate);
            templatesButtonTable.add(removeTemplates);
            if (entity) {
                VisTextButton convertToTemplate = new VisTextButton("Convert to template");
                convertToTemplate.addListener(
                        new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                Dialogs.showInputDialog(getStage(), "Convert to template", "Template name",
                                        new InputValidator() {
                                            @Override
                                            public boolean validateInput(String input) {
                                                return objectTreeData.canCreateTemplate(null, input);
                                            }
                                        },
                                        new InputDialogListener() {
                                            @Override
                                            public void finished(String input) {
                                                objectTreeData.convertToTemplate(input, editedEntity);
                                                rebuildUi();
                                            }

                                            @Override
                                            public void canceled() {

                                            }
                                        });
                            }
                        });
                templatesButtonTable.add(convertToTemplate);
            }
            templatesButtonTable.row();
            entityDetails.addActor(templatesButtonTable);

            for (Class<? extends T> inheritedCoreComponentClass : editedEntity.getInheritedCoreComponents()) {
                if (!editedEntity.hasCoreComponent(inheritedCoreComponentClass)) {
                    T inheritedCoreComponent = editedEntity.getInheritedCoreComponent(inheritedCoreComponentClass);
                    addCoreComponentEditor(inheritedCoreComponent, false, true);
                }
            }

            for (Class<? extends T> coreComponentClass : editedEntity.getCoreComponents()) {
                T coreComponent = editedEntity.getCoreComponent(coreComponentClass);
                addCoreComponentEditor(coreComponent, true, false);
            }
        }
    }

    private PopupMenu createAddTemplatePopup() {
        PopupMenu popupMenu = new PopupMenu();
        for (ObjectTreeData.LocatedEntityDefinition template : objectTreeData.getTemplates()) {
            String templateId = template.getEntityDefinition().getId();
            if (!editedEntity.hasTemplate(templateId)) {
                String menuLocation = template.getPath();
                PopupMenu targetMenu;
                if (menuLocation != null) {
                    String[] locationSplit = menuLocation.split("/");
                    targetMenu = findOrCreatePopupMenu(popupMenu, locationSplit, 0);
                } else {
                    targetMenu = popupMenu;
                }
                final String title = template.getEntityDefinition().getName();
                MenuItem valueMenuItem = new MenuItem(title);
                valueMenuItem.addListener(
                        new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                editedEntity.addTemplate(templateId);
                                project.entityChanged(editedEntity);
                                rebuildUi();
                            }
                        });
                targetMenu.addItem(valueMenuItem);
            }
        }
        return popupMenu;
    }

    private PopupMenu findOrCreatePopupMenu(PopupMenu popupMenu, String[] menuSplit, int startIndex) {
        for (Actor child : popupMenu.getChildren()) {
            if (child instanceof MenuItem) {
                MenuItem childMenuItem = (MenuItem) child;
                if (childMenuItem.getLabel().getText().toString().equals(menuSplit[startIndex]) && childMenuItem.getSubMenu() != null) {
                    if (startIndex + 1 < menuSplit.length) {
                        return findOrCreatePopupMenu(childMenuItem.getSubMenu(), menuSplit, startIndex + 1);
                    } else {
                        return childMenuItem.getSubMenu();
                    }
                }
            }
        }

        PopupMenu createdPopup = new PopupMenu();
        MenuItem createdMenuItem = new MenuItem(menuSplit[startIndex]);
        createdMenuItem.setSubMenu(createdPopup);
        popupMenu.addItem(createdMenuItem);
        if (startIndex + 1 < menuSplit.length) {
            return findOrCreatePopupMenu(createdPopup, menuSplit, startIndex + 1);
        } else {
            return createdPopup;
        }
    }

    private void addCoreComponentEditor(T coreComponent, boolean editable, boolean inherited) {
        Class<? extends T> clazz = (Class<? extends T>) coreComponent.getClass();
        ComponentEditorFactory<T> componentEditorFactory = (ComponentEditorFactory<T>) EntityComponentRegistry.getComponentEditorFactory(clazz);
        ComponentContainer container = new ComponentContainer(clazz, componentEditorFactory.createComponentEditor(coreComponent, changeCallback, editable), inherited);
        entityComponents.addActor(container);
    }

    private void removeCoreComponentEditor(Class<? extends T> clazz) {
        ComponentContainer componentContainer = findComponentContainer(clazz);
        entityComponents.removeActor(componentContainer);
    }

    private ComponentContainer findComponentContainer(Class<? extends T> clazz) {
        for (Actor child : entityComponents.getChildren()) {
            ComponentContainer container = (ComponentContainer) child;
            if (container.getComponentClass() == clazz)
                return container;
        }
        return null;
    }

    private class TemplateContainer extends VisCheckBox {
        private String id;

        public TemplateContainer(String id, String path) {
            super(path);
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    private class ComponentContainer extends VisTable {
        private Class<? extends T> componentClass;

        public ComponentContainer(Class<? extends T> componentClass, ComponentEditor<T> componentEditor, boolean inherited) {
            this.componentClass = componentClass;

            Table actor = componentEditor.getActor();
            actor.setFillParent(true);
            CollapsibleWidget collapsibleWidget = new CollapsibleWidget(actor, inherited);

            VisTextButton collapseButton = new VisTextButton(inherited ? "+" : "-");
            collapseButton.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if (collapsibleWidget.isCollapsed()) {
                                collapsibleWidget.setCollapsed(false);
                                collapseButton.setText("-");
                            } else {
                                collapsibleWidget.setCollapsed(true);
                                collapseButton.setText("+");
                            }
                        }
                    });

            VisLabel componentNameLabel = new VisLabel(componentClass.getSimpleName() + (inherited ? " (inherited)" : ""));
            if (inherited)
                componentNameLabel.setColor(Color.LIGHT_GRAY);
            componentNameLabel.setEllipsis(true);

            addSeparator().colspan(2).growX().row();
            add(collapseButton).width(20);
            add(componentNameLabel).growX().row();

            add(collapsibleWidget).colspan(2).growX().row();
        }

        public Class<? extends T> getComponentClass() {
            return componentClass;
        }
    }
}
