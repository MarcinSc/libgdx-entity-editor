package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.github.javaparser.ast.type.Type;

import java.util.function.Consumer;

public interface ComponentFieldType<T> {
    String getId();

    String getName();

    boolean accepts(String componentClass, String fieldName, Type type, boolean exact);

    Actor createEditor(boolean editable, T fieldValue, Consumer<T> consumer);

    JsonValue convertToJson(T value);

    T getDefaultValue();
}
