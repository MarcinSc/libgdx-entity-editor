package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.data.component.CustomFieldTypeRegistry;
import com.gempukku.libgdx.entity.editor.data.component.DefaultDataDefinition;
import com.gempukku.libgdx.entity.editor.data.component.FieldDefinition;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class DataTypeEditorDialog extends VisDialog {
    private DefaultDataDefinition<?> dataDefinition;

    public DataTypeEditorDialog(DefaultDataDefinition<?> dataDefinition) {
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
            fieldsGroup.addActor(new FieldEntry(fieldDefinition.getName(), fieldDefinition.getType().name(), CustomFieldTypeRegistry.getComponentFieldTypeById(fieldDefinition.getFieldTypeId()).getName()));
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
        VisSelectBox<FieldDefinition.Type> typesSelect = new VisSelectBox<>();
        typesSelect.setItems(FieldDefinition.Type.values());
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
                                                            FieldDefinition.Type type = typesSelect.getSelected();
                                                            dataDefinition.addFieldType(input, type, fieldType.getId());
                                                            fieldsGroup.addActor(new FieldEntry(input, type.name(), fieldType.getName()));
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
        fieldsButtons.pad(3);
        fieldsButtons.add(removeFields);
        fieldsButtons.add(typesSelect);
        fieldsButtons.add(addField);

        VisTable labels = new VisTable();
        labels.add("Name").width(150);
        labels.add("Type").width(100);
        labels.add("Field type").growX().row();

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
        contentTable.add(labels).colspan(2).growX();
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

        public FieldEntry(String name, String type, String fieldType) {
            this.name = name;
            checkBox = new VisCheckBox(name);
            checkBox.align(Align.left);
            add(checkBox).width(150);
            add(type).width(100);
            add(fieldType).growX();
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
