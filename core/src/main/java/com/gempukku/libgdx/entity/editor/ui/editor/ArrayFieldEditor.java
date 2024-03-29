package com.gempukku.libgdx.entity.editor.ui.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.data.component.EditableType;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;

import java.util.function.Consumer;

public class ArrayFieldEditor<T> extends VisTable {
    private final VerticalGroup arrayEntries;
    private final Array<T> result;
    private final Array<ArrayEntry<T>> arrayEntryActors = new Array<>();

    private final boolean editable;
    private EditableType<T> fieldType;
    private final Consumer<Array<T>> consumer;

    public ArrayFieldEditor(boolean editable, Iterable<JsonValue> value, EditableType<T> fieldType, Consumer<Array<T>> consumer) {
        this.result = new Array<>();
        for (JsonValue t : value) {
            result.add(fieldType.convertToValue(t));
        }
        this.editable = editable;
        this.fieldType = fieldType;
        this.consumer = consumer;

        arrayEntries = new VerticalGroup();
        arrayEntries.grow().left().top();
        arrayEntries.align(Align.topLeft);

        IntSpinnerModel spinnerModel = new IntSpinnerModel(result.size, 0, 1000, 1);
        Spinner spinner = new Spinner(null, spinnerModel);
        spinner.setDisabled(!editable);
        spinner.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        int newSize = spinnerModel.getValue();
                        if (newSize > arrayEntries.getChildren().size) {
                            expandTo(newSize);
                        } else if (newSize < arrayEntries.getChildren().size) {
                            shrinkTo(newSize);
                        }
                    }
                });
        for (int i = 0; i < result.size; i++) {
            int finalI = i;
            ArrayEntry<T> arrayEntry = new ArrayEntry<>(i, editable, result.get(i), fieldType,
                    new Consumer<T>() {
                        @Override
                        public void accept(T t) {
                            result.set(finalI, t);
                            consumer.accept(result);
                        }
                    });
            arrayEntryActors.add(arrayEntry);
            arrayEntries.addActor(arrayEntry);
        }

        add("Size:").width(120).left();
        add(spinner).growX().row();
        add(arrayEntries).colspan(2).top().grow().row();
    }

    private void shrinkTo(int size) {
        while (arrayEntryActors.size > size) {
            int removedIndex = arrayEntryActors.size - 1;
            ArrayEntry<T> removed = arrayEntryActors.removeIndex(removedIndex);
            arrayEntries.removeActor(removed);
            result.removeIndex(removedIndex);
        }
        consumer.accept(result);
    }

    private void expandTo(int size) {
        while (arrayEntryActors.size < size) {
            int addedIndex = arrayEntryActors.size;
            T value = fieldType.getDefaultValue();
            result.add(value);
            ArrayEntry<T> arrayEntry = new ArrayEntry<>(addedIndex, editable, value, fieldType,
                    new Consumer<T>() {
                        @Override
                        public void accept(T t) {
                            result.set(addedIndex, t);
                            consumer.accept(result);
                        }
                    });
            arrayEntryActors.add(arrayEntry);
            arrayEntries.addActor(arrayEntry);
        }
        consumer.accept(result);
    }

    private static class ArrayEntry<T> extends VisTable {
        public ArrayEntry(int index, boolean editable, T value, EditableType<T> fieldType, Consumer<T> consumer) {
            Actor editor = fieldType.createEditor(editable, value, consumer);

            add("[" + index + "]").left().width(120);
            add(editor).growX().row();
        }
    }
}
