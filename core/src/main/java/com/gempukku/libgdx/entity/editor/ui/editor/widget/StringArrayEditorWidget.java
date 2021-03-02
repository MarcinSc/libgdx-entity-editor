package com.gempukku.libgdx.entity.editor.ui.editor.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import java.util.function.Consumer;

public class StringArrayEditorWidget extends VisTable {
    public StringArrayEditorWidget(
            boolean editable, Iterable<String> values, Consumer<Array<String>> callback) {
        final VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.grow();
        verticalGroup.align(Align.topLeft);

        for (String value : values) {
            addString(verticalGroup, value);
        }

        VisScrollPane scrollPane = new VisScrollPane(verticalGroup);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);

        VisTable buttonTable = new VisTable();

        VisTextButton addButton = new VisTextButton("Add value");
        addButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Dialogs.showInputDialog(getStage(), "Add value", "Enter the value",
                                new InputDialogListener() {
                                    @Override
                                    public void finished(String input) {
                                        addString(verticalGroup, input);
                                        updateValues(verticalGroup, callback);
                                    }

                                    @Override
                                    public void canceled() {

                                    }
                                });
                    }
                });
        addButton.setDisabled(!editable);

        VisTextButton removeButton = new VisTextButton("Remove selected");
        removeButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        for (Actor child : verticalGroup.getChildren()) {
                            VisCheckBox checkbox = (VisCheckBox) child;
                            if (checkbox.isChecked()) {
                                verticalGroup.removeActor(child);
                            }
                        }
                        updateValues(verticalGroup, callback);
                    }
                });
        removeButton.setDisabled(!editable);

        buttonTable.add(addButton).pad(3);
        buttonTable.add(removeButton).pad(3);

        add(scrollPane).height(100).growX().row();
        add(buttonTable).growX().row();
    }

    private void updateValues(VerticalGroup group, Consumer<Array<String>> callback) {
        Array<String> result = new Array<>();
        for (Actor child : group.getChildren()) {
            result.add(((VisCheckBox) child).getText().toString());
        }
        callback.accept(result);
    }

    private void addString(VerticalGroup verticalGroup, String value) {
        VisCheckBox checkBox = new VisCheckBox(value);
        checkBox.align(Align.left);
        verticalGroup.addActor(checkBox);
    }
}
