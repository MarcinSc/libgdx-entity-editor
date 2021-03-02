package com.gempukku.libgdx.entity.editor.data.component.type;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.LongEditorWidget;
import com.github.javaparser.ast.type.Type;

import java.util.function.Consumer;

public class LongComponentFieldType implements ComponentFieldType<Number> {
    public static final String ID = "long";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "long";
    }

    @Override
    public Actor createEditor(boolean editable, Number fieldValue, Consumer<Number> consumer) {
        if (fieldValue == null)
            fieldValue = 0;
        return new LongEditorWidget(editable, fieldValue.intValue(), consumer);
    }

    @Override
    public boolean accepts(String componentClass, String fieldName, Type type, boolean exact) {
        if (exact)
            return false;
        return type.isPrimitiveType() && type.asString().equals("long");
    }
}
