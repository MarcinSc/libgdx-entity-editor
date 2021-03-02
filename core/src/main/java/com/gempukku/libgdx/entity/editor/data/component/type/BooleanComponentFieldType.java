package com.gempukku.libgdx.entity.editor.data.component.type;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.BooleanEditorWidget;
import com.github.javaparser.ast.type.Type;

import java.util.function.Consumer;

public class BooleanComponentFieldType implements ComponentFieldType<Boolean> {
    public static final String ID = "boolean";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "boolean";
    }

    @Override
    public Actor createEditor(boolean editable, Boolean fieldValue, Consumer<Boolean> consumer) {
        if (fieldValue == null)
            fieldValue = false;
        return new BooleanEditorWidget(editable, fieldValue, consumer);
    }

    @Override
    public boolean accepts(String componentClass, String fieldName, Type type, boolean exact) {
        if (exact)
            return false;
        return type.isPrimitiveType() && type.asString().equals("boolean");
    }
}
