package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.SpriteStateDataDef;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.EditorConfig;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

public class SpriteStateEditorWidget extends VisTable {
    public SpriteStateEditorWidget(
            String label, ObjectMap<String, SpriteStateDataDef> states, Callback callback) {
        final VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.grow();
        verticalGroup.align(Align.topLeft);

        for (ObjectMap.Entry<String, SpriteStateDataDef> stateEntry : states.entries()) {
            addEntry(verticalGroup, stateEntry.key, stateEntry.value, callback);
        }

        VisScrollPane scrollPane = new VisScrollPane(verticalGroup);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);

        VisTable buttonTable = new VisTable();

        VisTextField addButton = new VisTextField("Add state");
        addButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Dialogs.showInputDialog(getStage(), "Add state", "Enter state name",
                                new InputDialogListener() {
                                    @Override
                                    public void finished(String input) {
                                        addEntry(verticalGroup, input, new SpriteStateDataDef(), callback);
                                        updateValues(verticalGroup, callback);
                                    }

                                    @Override
                                    public void canceled() {

                                    }
                                });
                    }
                });
        VisTextField removeButton = new VisTextField("Remove selected");
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

    private void addEntry(VerticalGroup verticalGroup, String name, SpriteStateDataDef spriteStateData, Callback callback) {
        StateWidget stateWidget = new StateWidget(name, spriteStateData, new Runnable() {
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

    private class StateWidget extends VisTable {
        private VisCheckBox checkBox;
        private String name;
        private SpriteStateDataDef dataDef;

        private StateWidget(String name, SpriteStateDataDef dataDef, Runnable callback) {
            this.name = name;
            this.dataDef = dataDef;
            checkBox = new VisCheckBox(name);
            VisTextButton textButton = new VisTextButton("Edit data");
            textButton.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            VisDialog dialog = new VisDialog("Edit state data");
                            dialog.setResizable(true);
                            dialog.getContentTable().add(
                                    new GraphShaderPropertiesEditorWidget(
                                            "Sprite state data",
                                            dataDef.getProperties(), new GraphShaderPropertiesEditorWidget.Callback() {
                                        @Override
                                        public void setValue(ObjectMap<String, Object> value) {
                                            dataDef.setProperties(value);
                                            callback.run();
                                        }
                                    })).grow();
                            VisTextButton done = new VisTextButton("Done");
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
