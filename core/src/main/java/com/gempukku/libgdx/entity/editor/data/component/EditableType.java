package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;

import java.util.function.Consumer;

public interface EditableType<T> {
    Actor createEditor(boolean editable, T value, Consumer<T> consumer);

    JsonValue convertToJson(T value);

    T convertToValue(JsonValue json);

    T getDefaultValue();

    boolean isEditorSmall();
}
