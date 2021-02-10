package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

public class EnumEditorWidget<T extends Enum<T>> extends Table {
    private Class<T> clazz;

    public EnumEditorWidget(
            Skin skin, float width,
            Class<T> clazz, boolean allowsNull,
            String label, T value, Callback<T> callback) {
        super(skin);
        this.clazz = clazz;

        Array<String> values = new Array<>();
        if (allowsNull)
            values.add(getDisplayed(null));
        for (Enum<T> enumConstant : clazz.getEnumConstants()) {
            values.add(getDisplayed(enumConstant));
        }

        SelectBox<String> selectBox = new SelectBox<>(skin);
        selectBox.setItems(values);
        selectBox.setSelected(getDisplayed(value));
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.update(getValue(selectBox.getSelected()));
            }
        };
        selectBox.addListener(changeListener);

        add(label + ": ").width(width);
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

    public interface Callback<T extends Enum<T>> {
        void update(T value);
    }
}
