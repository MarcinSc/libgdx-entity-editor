package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.data.component.EntityComponentRegistry;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;
import com.kotcrab.vis.ui.widget.CollapsibleWidget;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class EntityInspector<T> extends VisTable {
    private final VerticalGroup entityDetails;
    private final VerticalGroup entityComponents;

    private ObjectTreeData objectTreeData;
    private EntityDefinition<T> editedEntity;

    public EntityInspector() {
        entityDetails = new VerticalGroup();
        entityDetails.top();
        entityDetails.fill();

        entityComponents = new VerticalGroup();
        entityComponents.top();
        entityComponents.grow();

        add("Entity inspector").growX().row();
        add(new Separator()).growX().row();
        add(entityDetails).growX().row();

        VisScrollPane scrollPane = new VisScrollPane(entityComponents);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);
        add(scrollPane).grow().row();
    }

    public void setObjectTreeData(ObjectTreeData objectTreeData) {
        this.objectTreeData = objectTreeData;
    }

    public void setEditedEntity(EntityDefinition<T> editedEntity, EntityEditorProject<T> project, boolean entity) {
        this.editedEntity = editedEntity;

        entityDetails.clearChildren();
        entityComponents.clearChildren();

        if (editedEntity != null) {
            VisTextButton addComponent = new VisTextButton("Add component");
            addComponent.addListener(
                    new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
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
                                                    addCoreComponentEditor(component, true);
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
                    new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            PopupMenu menu = new PopupMenu();

                            for (Class<? extends T> coreComponent : editedEntity.getCoreComponents()) {
                                MenuItem menuItem = new MenuItem(coreComponent.getSimpleName());
                                menuItem.addListener(
                                        new ChangeListener() {
                                            @Override
                                            public void changed(ChangeEvent event, Actor actor) {
                                                editedEntity.removeCoreComponent(coreComponent);
                                                removeCoreComponentEditor(coreComponent);
                                            }
                                        });
                                menu.addItem(menuItem);
                            }

                            menu.showMenu(getStage(), removeComponent);
                        }
                    });

            entityDetails.addActor(new VisLabel("Name: " + editedEntity.getName()));
            VisTable buttonTable = new VisTable();

            buttonTable.add(addComponent);
            buttonTable.add(removeComponent).row();
            entityDetails.addActor(buttonTable);

            for (Class<? extends T> inheritedCoreComponentClass : editedEntity.getInheritedCoreComponents(objectTreeData)) {
                if (!editedEntity.hasCoreComponent(inheritedCoreComponentClass)) {
                    T inheritedCoreComponent = editedEntity.getInheritedCoreComponent(objectTreeData, inheritedCoreComponentClass);
                    addCoreComponentEditor(inheritedCoreComponent, false);
                }
            }

            for (Class<? extends T> coreComponentClass : editedEntity.getCoreComponents()) {
                T coreComponent = editedEntity.getCoreComponent(coreComponentClass);
                addCoreComponentEditor(coreComponent, true);
            }
        }
    }

    private void addCoreComponentEditor(T coreComponent, boolean editable) {
        Class<? extends T> clazz = (Class<? extends T>) coreComponent.getClass();
        ComponentEditorFactory<T> componentEditorFactory = (ComponentEditorFactory<T>) EntityComponentRegistry.getComponentEditorFactory(clazz);
        ComponentContainer container = new ComponentContainer(clazz, componentEditorFactory.createComponentEditor(coreComponent, editable));
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

    private class ComponentContainer extends VisTable {
        private Class<? extends T> componentClass;

        public ComponentContainer(Class<? extends T> componentClass, ComponentEditor<T> componentEditor) {
            this.componentClass = componentClass;

            Table actor = componentEditor.getActor();
            actor.setFillParent(true);
            CollapsibleWidget collapsibleWidget = new CollapsibleWidget(actor);

            VisTextButton collapseButton = new VisTextButton("-");
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

            VisLabel componentNameLabel = new VisLabel(componentClass.getSimpleName());
            componentNameLabel.setEllipsis(true);

            add(new Separator()).colspan(2).growX().row();
            add(collapseButton).width(20);
            add(componentNameLabel).growX().row();

            add(collapsibleWidget).colspan(2).growX().row();
        }

        public Class<? extends T> getComponentClass() {
            return componentClass;
        }
    }
}
