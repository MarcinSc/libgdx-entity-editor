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
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;

public class StringArrayEditorWidget extends Table {
    public StringArrayEditorWidget(
            Skin skin,
            String label, Iterable<String> values, Callback callback) {
        super(skin);

        final VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.grow();
        verticalGroup.align(Align.topLeft);

        for (String value : values) {
            addString(skin, verticalGroup, value);
        }

        ScrollPane scrollPane = new ScrollPane(verticalGroup, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);

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
                            CheckBox checkbox = (CheckBox) child;
                            if (checkbox.isChecked()) {
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
        Array<String> result = new Array<>();
        for (Actor child : group.getChildren()) {
            result.add(((CheckBox) child).getText().toString());
        }
        callback.setValue(result);
    }

    private void addString(Skin skin, VerticalGroup verticalGroup, String value) {
        CheckBox checkBox = new CheckBox(value, skin);
        checkBox.align(Align.left);
        verticalGroup.addActor(checkBox);
    }

    public interface Callback {
        void setValue(Array<String> value);
    }
}
