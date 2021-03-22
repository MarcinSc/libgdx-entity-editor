package com.gempukku.libgdx.entity.editor.ui.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.data.component.EditableType;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

import java.util.function.Consumer;

public class MapFieldEditor<T> extends VisTable {
    private final VerticalGroup mapEntries;
    private final ObjectMap<String, T> result;

    private final boolean editable;
    private EditableType<T> fieldType;
    private Runnable changeCallback;

    public MapFieldEditor(boolean editable, Iterable<JsonValue> values, EditableType<T> fieldType,
                          Consumer<ObjectMap<String, T>> consumer) {
        result = new ObjectMap<>();
        for (JsonValue value : values) {
            result.put(value.name(), fieldType.convertToValue(value));
        }

        this.editable = editable;
        this.fieldType = fieldType;

        this.changeCallback = new Runnable() {
            @Override
            public void run() {
                result.clear();
                for (Actor child : mapEntries.getChildren()) {
                    MapEntry<T> mapEntry = (MapEntry<T>) child;
                    String key = mapEntry.getKey();
                    if (!result.containsKey(key))
                        result.put(key, mapEntry.getValue());
                }
                consumer.accept(result);
            }
        };

        mapEntries = new VerticalGroup();
        mapEntries.grow().left().top();
        mapEntries.align(Align.topLeft);

        VisTextButton addEntryButton = new VisTextButton("Add entry");
        addEntryButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        addMapEntry("", fieldType.getDefaultValue());
                        changeCallback.run();
                    }
                });
        VisTextButton removeEntriesButton = new VisTextButton("Remove entries");
        removeEntriesButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        for (Actor child : mapEntries.getChildren()) {
                            MapEntry<T> mapEntry = (MapEntry<T>) child;
                            if (mapEntry.isSelected()) {
                                mapEntries.removeActor(child);
                            }
                        }
                        changeCallback.run();
                    }
                });

        VisTable buttonTable = new VisTable();
        buttonTable.top().left().pad(3);
        buttonTable.add(addEntryButton);
        buttonTable.add(removeEntriesButton);

        for (ObjectMap.Entry<String, T> mapEntry : result) {
            addMapEntry(mapEntry.key, mapEntry.value);
        }

        add(buttonTable).growX().row();
        add(mapEntries).top().grow().row();
    }

    private void addMapEntry(String key, T value) {
        mapEntries.addActor(new MapEntry<T>(editable, key, value, fieldType, changeCallback));
    }

    private boolean isValidKey(Actor skipActor, String key) {
        for (Actor child : mapEntries.getChildren()) {
            MapEntry<T> mapEntry = (MapEntry<T>) child;
            if (skipActor != child && key.equals(mapEntry.getKey()))
                return false;
        }
        return true;
    }

    private class MapEntry<T> extends VisTable {
        private VisCheckBox checkBox;
        private VisValidatableTextField keyTextField;
        private T mapValue;

        public MapEntry(boolean editable, String key, T value, EditableType<T> fieldType, Runnable changeCallback) {
            this.mapValue = value;
            this.checkBox = new VisCheckBox(null);

            this.keyTextField = new VisValidatableTextField(key);
            this.keyTextField.addValidator(
                    new InputValidator() {
                        @Override
                        public boolean validateInput(String input) {
                            return isValidKey(MapEntry.this, input);
                        }
                    });
            this.keyTextField.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            changeCallback.run();
                        }
                    });
            if (fieldType.isEditorSmall()) {
                Actor editor = fieldType.createEditor(editable, value, new Consumer<T>() {
                    @Override
                    public void accept(T t) {
                        mapValue = t;
                        changeCallback.run();
                    }
                });

                add(checkBox);
                add(keyTextField).left().width(120);
                add(editor).growX().row();
            } else {
                VisTextButton editButton = new VisTextButton("Edit");
                editButton.addListener(
                        new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                VisDialog dialog = new VisDialog("Edit value");
                                dialog.setResizable(true);
                                Table contentTable = dialog.getContentTable();

                                Actor editor = fieldType.createEditor(editable, mapValue, new Consumer<T>() {
                                    @Override
                                    public void accept(T t) {
                                        mapValue = t;
                                        changeCallback.run();
                                    }
                                });

                                VisScrollPane scrollPane = new VisScrollPane(editor);
                                scrollPane.setFadeScrollBars(false);
                                scrollPane.setForceScroll(false, true);

                                contentTable.add(scrollPane).grow();
                                VisTextButton done = new VisTextButton("Done");
                                done.addListener(
                                        new ChangeListener() {
                                            @Override
                                            public void changed(ChangeEvent event, Actor actor) {
                                                dialog.hide();
                                            }
                                        });
                                dialog.getButtonsTable().add(done);
                                dialog.pack();
                                dialog.centerWindow();
                                dialog.show(getStage());
                            }
                        });

                add(checkBox);
                add(keyTextField).left().width(120);
                add(editButton).growX().row();
            }
        }

        public String getKey() {
            return keyTextField.getText();
        }

        public T getValue() {
            return mapValue;
        }

        public boolean isSelected() {
            return checkBox.isChecked();
        }
    }
}
