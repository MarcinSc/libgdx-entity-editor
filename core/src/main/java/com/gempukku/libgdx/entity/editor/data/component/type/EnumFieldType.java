package com.gempukku.libgdx.entity.editor.data.component.type;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.EnumEditorWidget;
import com.github.javaparser.ast.type.Type;

import java.util.function.Consumer;

public class EnumFieldType<T extends Enum<T>> implements ComponentFieldType<T> {
    private Class<T> clazz;
    private T defaultValue;

    public EnumFieldType(Class<T> clazz, T defaultValue) {
        this.clazz = clazz;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getId() {
        return clazz.getName();
    }

    @Override
    public String getName() {
        return clazz.getName();
    }

    @Override
    public boolean accepts(String componentClass, String fieldName, Type type, boolean exact) {
        String stringType = type.asString();
        return stringType.equals(clazz.getName()) || stringType.equals(clazz.getSimpleName());
    }

    @Override
    public Actor createEditor(boolean editable, T value, Consumer<T> consumer) {
        if (value == null)
            value = getDefaultValue();
        return new EnumEditorWidget<T>(
                editable, clazz, false,
                value, consumer);
    }

    @Override
    public JsonValue convertToJson(T value) {
        return new JsonValue(value.name());
    }

    @Override
    public T convertToValue(JsonValue json) {
        String value = json.asString();
        for (T enumConstant : clazz.getEnumConstants()) {
            if (enumConstant.name().equals(value))
                return enumConstant;
        }

        return null;
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public boolean isEditorSmall() {
        return true;
    }
}
