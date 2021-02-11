package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.SpriteStateDataDef;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.EditorConfig;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.VisDialog;

public class SpriteStateEditorWidget extends Table {
    public SpriteStateEditorWidget(
            Skin skin,
            String label, ObjectMap<String, SpriteStateDataDef> states, Callback callback) {
        super(skin);

        final VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.grow();
        verticalGroup.align(Align.topLeft);

        for (ObjectMap.Entry<String, SpriteStateDataDef> stateEntry : states.entries()) {
            addEntry(skin, verticalGroup, stateEntry.key, stateEntry.value, callback);
        }

        ScrollPane scrollPane = new ScrollPane(verticalGroup, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);

        Table buttonTable = new Table(skin);

        TextButton addButton = new TextButton("Add state", skin);
        addButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Dialogs.showInputDialog(getStage(), "Add state", "Enter state name",
                                new InputDialogListener() {
                                    @Override
                                    public void finished(String input) {
                                        addEntry(skin, verticalGroup, input, new SpriteStateDataDef(), callback);
                                        updateValues(verticalGroup, callback);
                                    }

                                    @Override
                                    public void canceled() {

                                    }
                                });
                    }
                });
        TextButton removeButton = new TextButton("Remove selected", skin);
        removeButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        for (Actor child : verticalGroup.getChildren()) {
                            StateWidget stateWidget = (StateWidget) child;
                            if (stateWidget.isSelected()) {
                                verticalGroup.removeActor(child);
                            }
                        }
                        updateValues(verticalGroup, callback);
                    }
                });

        buttonTable.add(addButton).pad(3);
        buttonTable.add(removeButton).pad(3);

        add(label + ":").growX().row();
        add(scrollPane).height(100).growX().row();
        add(buttonTable).growX().row();
    }

    private void updateValues(VerticalGroup group, Callback callback) {
        ObjectMap<String, SpriteStateDataDef> result = new ObjectMap<>();
        for (Actor child : group.getChildren()) {
            StateWidget stateWidget = (StateWidget) child;
            result.put(stateWidget.getName(), stateWidget.getDataDef());
        }
        callback.setValue(result);
    }

    private void addEntry(Skin skin, VerticalGroup verticalGroup, String name, SpriteStateDataDef spriteStateData, Callback callback) {
        StateWidget stateWidget = new StateWidget(skin, name, spriteStateData, new Runnable() {
            @Override
            public void run() {
                updateValues(verticalGroup, callback);
            }
        });
        verticalGroup.addActor(stateWidget);
    }

    public interface Callback {
        void setValue(ObjectMap<String, SpriteStateDataDef> value);
    }

    private class StateWidget extends Table {
        private CheckBox checkBox;
        private String name;
        private SpriteStateDataDef dataDef;

        private StateWidget(Skin skin, String name, SpriteStateDataDef dataDef, Runnable callback) {
            super(skin);
            this.name = name;
            this.dataDef = dataDef;
            checkBox = new CheckBox(name, skin);
            TextButton textButton = new TextButton("Edit data", skin);
            textButton.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            VisDialog dialog = new VisDialog("Edit state data");
                            dialog.setResizable(true);
                            dialog.getContentTable().add(
                                    new GraphShaderPropertiesEditorWidget(
                                            skin, "Sprite state data",
                                            dataDef.getProperties(), new GraphShaderPropertiesEditorWidget.Callback() {
                                        @Override
                                        public void setValue(ObjectMap<String, Object> value) {
                                            dataDef.setProperties(value);
                                            callback.run();
                                        }
                                    })).grow();
                            TextButton done = new TextButton("Done", skin);
                            done.addListener(
                                    new ChangeListener() {
                                        @Override
                                        public void changed(ChangeEvent event, Actor actor) {
                                            dialog.hide();
                                        }
                                    });
                            dialog.getButtonsTable().add(done);
                            dialog.show(getStage(), new Action() {
                                @Override
                                public boolean act(float delta) {
                                    dialog.setSize(600, 320);
                                    dialog.centerWindow();
                                    return true;
                                }
                            });
                        }
                    });

            add(checkBox).width(EditorConfig.LABEL_WIDTH);
            add(textButton).growX().row();
        }

        @Override
        public String getName() {
            return name;
        }

        public SpriteStateDataDef getDataDef() {
            return dataDef;
        }

        public boolean isSelected() {
            return checkBox.isChecked();
        }
    }
}
