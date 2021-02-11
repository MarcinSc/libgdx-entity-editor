package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.data.component.EntityComponentRegistry;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.Separator;

public class EntityInspector<T> extends Table {
    private final Table entityDetails;
    private final ObjectMap<String, ComponentEditor<T>> componentEditors;

    private EntityDefinition<T> editedEntity;

    public EntityInspector(Skin skin) {
        super(skin);

        entityDetails = new Table(skin);
        entityDetails.top();

        componentEditors = new ObjectMap<>();

        add("Entity inspector").growX().row();
        add(new Separator()).growX().row();
        add(entityDetails).grow().row();
    }

    public void setEditedEntity(EntityDefinition<T> editedEntity, EntityEditorProject<T> project) {
        this.editedEntity = editedEntity;

        entityDetails.clearChildren();
        componentEditors.clear();

        if (editedEntity != null) {
            Skin skin = getSkin();

            Table tbl = new Table();
            tbl.top();

            TextButton addComponent = new TextButton("Add component", skin);
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
                                                    addCoreComponentEditor(skin, tbl, component);
                                                }
                                            });
                                    menu.addItem(menuItem);
                                }
                            }

                            menu.showMenu(getStage(), addComponent);
                        }
                    });

            TextButton removeComponent = new TextButton("Remove component", skin);
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
                                                removeCoreComponentEditor(tbl, coreComponent);
                                            }
                                        });
                                menu.addItem(menuItem);
                            }

                            menu.showMenu(getStage(), removeComponent);
                        }
                    });

            entityDetails.add("Name: " + editedEntity.getName()).colspan(2).row();
            entityDetails.add(addComponent);
            entityDetails.add(removeComponent).row();

            for (Class<? extends T> coreComponentClass : editedEntity.getCoreComponents()) {
                T coreComponent = editedEntity.getCoreComponent(coreComponentClass);
                addCoreComponentEditor(skin, tbl, coreComponent);
            }

            ScrollPane scrollPane = new ScrollPane(tbl, skin);
            scrollPane.setFadeScrollBars(false);
            scrollPane.setForceScroll(false, true);

            entityDetails.add(scrollPane).colspan(2).grow().row();
        }
    }

    private void addCoreComponentEditor(Skin skin, Table table, T coreComponent) {
        Class<?> clazz = coreComponent.getClass();
        ComponentEditorFactory<T> componentEditorFactory = (ComponentEditorFactory<T>) EntityComponentRegistry.getComponentEditorFactory(clazz);
        ComponentEditor<T> componentEditor = componentEditorFactory.createComponentEditor(skin, coreComponent);
        table.add(componentEditor.getActor()).growX().row();
        componentEditors.put(clazz.getSimpleName(), componentEditor);
    }

    private void removeCoreComponentEditor(Table table, Class<? extends T> clazz) {
        ComponentEditor<T> componentEditor = componentEditors.remove(clazz.getSimpleName());
        table.removeActor(componentEditor.getActor());
    }
}
