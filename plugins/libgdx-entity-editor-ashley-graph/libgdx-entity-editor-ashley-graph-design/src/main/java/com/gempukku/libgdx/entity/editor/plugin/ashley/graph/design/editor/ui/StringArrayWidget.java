package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;

public class StringArrayWidget extends Table {
    public StringArrayWidget(
            Skin skin, String label, Iterable<String> values, Callback callback) {
        super(skin);

        final VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.top();
        verticalGroup.align(Align.topLeft);

        for (String value : values) {
            addString(skin, verticalGroup, value);
        }

        ScrollPane scrollPane = new ScrollPane(verticalGroup);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);

        add(label + ":").growX().row();
        add(scrollPane).height(100).growX().row();
        Table buttonTable = new Table(skin);

        TextButton addButton = new TextButton("Add value", skin);
        addButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Dialogs.showInputDialog(getStage(), "Add value", "Enter the value",
                                new InputDialogListener() {
                                    @Override
                                    public void finished(String input) {
                                        addString(skin, verticalGroup, input);
                                        callback.addValue(input);
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
                            CheckBox checkbox = (CheckBox) child;
                            if (checkbox.isChecked()) {
                                callback.removeValue(checkbox.getText().toString());
                                verticalGroup.removeActor(child);
                            }
                        }

                    }
                });

        buttonTable.add(addButton).pad(3);
        buttonTable.add(removeButton).pad(3);
        add(buttonTable).growX().row();
    }

    private void addString(Skin skin, VerticalGroup verticalGroup, String value) {
        CheckBox checkBox = new CheckBox(value, skin);
        verticalGroup.addActor(checkBox);
    }

    public interface Callback {
        void removeValue(String value);

        void addValue(String value);
    }
}
