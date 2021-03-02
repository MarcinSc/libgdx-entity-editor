package com.gempukku.libgdx.entity.editor.ui.editor.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.function.Consumer;

public class EnumEditorWidget<T extends Enum<T>> extends VisTable {
    private Class<T> clazz;

    public EnumEditorWidget(
            boolean editable,
            Class<T> clazz, boolean allowsNull,
            T value, Consumer<T> callback) {
        this.clazz = clazz;

        Array<String> values = new Array<>();
        if (allowsNull)
            values.add(getDisplayed(null));
        for (Enum<T> enumConstant : clazz.getEnumConstants()) {
            values.add(getDisplayed(enumConstant));
        }

        VisSelectBox<String> selectBox = new VisSelectBox<>();
        selectBox.setItems(values);
        selectBox.setSelected(getDisplayed(value));
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.accept(getValue(selectBox.getSelected()));
            }
        };
        selectBox.addListener(changeListener);
        selectBox.setDisabled(!editable);

        add(selectBox).growX().row();
    }

    private String getDisplayed(Enum<T> value) {
        if (value == null)
            return "---";
        return value.name();
    }

    private T getValue(String displayName) {
        for (T enumConstant : clazz.getEnumConstants()) {
            if (getDisplayed(enumConstant).equals(displayName))
                return enumConstant;
        }
        return null;
    }
}
