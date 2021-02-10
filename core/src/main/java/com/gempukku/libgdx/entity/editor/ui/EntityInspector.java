package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.data.component.EntityComponentRegistry;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.Separator;

public class EntityInspector extends Table {
    private Table entityDetails;
    private EntityDefinition editedEntity;

    public EntityInspector(Skin skin) {
        super(skin);

        entityDetails = new Table(skin);
        entityDetails.top();

        add("Entity inspector").growX().row();
        add(new Separator()).growX().row();
        add(entityDetails).grow().row();
    }

    public void setEditedEntity(EntityDefinition editedEntity, EntityEditorProject project) {
        this.editedEntity = editedEntity;

        entityDetails.clearChildren();

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

                            for (Class<?> coreComponent : EntityComponentRegistry.getCoreComponents()) {
                                if (project.supportsComponent(coreComponent) && !editedEntity.hasCoreComponent(coreComponent)) {
                                    MenuItem menuItem = new MenuItem(coreComponent.getSimpleName());
                                    menuItem.addListener(
                                            new ChangeListener() {
                                                @Override
                                                public void changed(ChangeEvent event, Actor actor) {
                                                    Object component = project.createCoreComponent(coreComponent);
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

            entityDetails.add("Name: " + editedEntity.getName()).row();
            entityDetails.add(addComponent).row();

            for (Object coreComponent : editedEntity.getCoreComponents()) {
                addCoreComponentEditor(skin, tbl, coreComponent);
            }

            ScrollPane scrollPane = new ScrollPane(tbl, skin);
            scrollPane.setFadeScrollBars(false);
            scrollPane.setForceScroll(false, true);

            entityDetails.add(scrollPane).grow().row();
        }
    }

    private void addCoreComponentEditor(Skin skin, Table table, Object coreComponent) {
        ComponentEditorFactory componentEditorFactory = EntityComponentRegistry.getComponentEditorFactory(coreComponent.getClass());
        ComponentEditor componentEditor = componentEditorFactory.createComponentEditor(skin, coreComponent);
        table.add(componentEditor.getActor()).growX().row();
    }
}
