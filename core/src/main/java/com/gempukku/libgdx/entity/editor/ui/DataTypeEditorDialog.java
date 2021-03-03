package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.data.component.CustomDataDefinition;
import com.gempukku.libgdx.entity.editor.data.component.CustomFieldTypeRegistry;
import com.gempukku.libgdx.entity.editor.data.component.FieldDefinition;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class DataTypeEditorDialog extends VisDialog {
    private CustomDataDefinition dataDefinition;

    public DataTypeEditorDialog(CustomDataDefinition dataDefinition) {
        super("Data Type Editor");
        this.dataDefinition = dataDefinition;

        VisValidatableTextField nameField = new VisValidatableTextField(dataDefinition.getName());
        nameField.addValidator(
                new InputValidator() {
                    @Override
                    public boolean validateInput(String input) {
                        return input.trim().length() > 0;
                    }
                });
        nameField.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        dataDefinition.setName(nameField.getText());
                    }
                });

        VisValidatableTextField classNameField = new VisValidatableTextField(dataDefinition.getClassName());
        classNameField.addValidator(
                new InputValidator() {
                    @Override
                    public boolean validateInput(String input) {
                        return input.trim().length() > 0;
                    }
                });
        classNameField.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        dataDefinition.setClassName(classNameField.getText());
                    }
                });

        VisCheckBox componentCheckBox = new VisCheckBox("Is component", dataDefinition.isComponent());
        componentCheckBox.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        dataDefinition.setComponent(componentCheckBox.isChecked());
                    }
                });

        VerticalGroup fieldsGroup = new VerticalGroup();
        fieldsGroup.grow();
        fieldsGroup.top().left();

        for (FieldDefinition fieldDefinition : dataDefinition.getFieldTypes()) {
            fieldsGroup.addActor(new FieldEntry(fieldDefinition.getName(), CustomFieldTypeRegistry.getComponentFieldTypeById(fieldDefinition.getFieldTypeId()).getName()));
        }

        VisScrollPane fieldsScrollPane = new VisScrollPane(fieldsGroup) {
            @Override
            public float getPrefHeight() {
                return 150;
            }

            @Override
            public float getPrefWidth() {
                return 400;
            }
        };

        VisTextButton removeFields = new VisTextButton("Remove");
        removeFields.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        for (Actor child : fieldsGroup.getChildren()) {
                            FieldEntry fieldEntry = (FieldEntry) child;
                            if (fieldEntry.isSelected()) {
                                String name = fieldEntry.getName();
                                dataDefinition.removeField(name);
                                fieldsGroup.removeActor(fieldEntry);
                            }
                        }
                    }
                });
        VisTextButton addField = new VisTextButton("Add");
        addField.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        PopupMenu popupMenu = new PopupMenu();
                        for (ComponentFieldType<?> fieldType : CustomFieldTypeRegistry.getFieldTypes()) {
                            MenuItem menuItem = new MenuItem(fieldType.getName());
                            menuItem.addListener(
                                    new ChangeListener() {
                                        @Override
                                        public void changed(ChangeEvent event, Actor actor) {
                                            Dialogs.showInputDialog(getStage(), "Add field", "Field name",
                                                    new InputValidator() {
                                                        @Override
                                                        public boolean validateInput(String input) {
                                                            return !input.isEmpty() && !dataDefinition.hasField(input);
                                                        }
                                                    },
                                                    new InputDialogListener() {
                                                        @Override
                                                        public void finished(String input) {
                                                            dataDefinition.addFieldType(input, FieldDefinition.Type.Object, fieldType.getId());
                                                            fieldsGroup.addActor(new FieldEntry(input, fieldType.getName()));
                                                        }

                                                        @Override
                                                        public void canceled() {

                                                        }
                                                    });
                                        }
                                    });
                            popupMenu.addItem(menuItem);
                        }

                        popupMenu.showMenu(getStage(), addField);
                    }
                });

        VisTable fieldsButtons = new VisTable();
        fieldsButtons.add(removeFields);
        fieldsButtons.add(addField);

        Table contentTable = getContentTable();
        contentTable.add("Name: ");
        contentTable.add(nameField).growX();
        contentTable.row();
        contentTable.add("Class: ");
        contentTable.add(classNameField).growX();
        contentTable.row();
        contentTable.add(componentCheckBox).colspan(2).growX();
        contentTable.row();
        contentTable.add("Fields:").colspan(2).growX();
        contentTable.row();
        contentTable.add(fieldsScrollPane).colspan(2).growX();
        contentTable.row();
        contentTable.add(fieldsButtons).colspan(2).growX();
        contentTable.row();

        setModal(true);
        setResizable(true);
        pack();
        centerWindow();
    }

    private static class FieldEntry extends VisTable {
        private VisCheckBox checkBox;
        private String name;

        public FieldEntry(String name, String type) {
            this.name = name;
            checkBox = new VisCheckBox(name);
            checkBox.align(Align.left);
            add(checkBox).width(150);
            add(type).growX();
            row();
        }

        public String getName() {
            return name;
        }

        public boolean isSelected() {
            return checkBox.isChecked();
        }
    }
}
