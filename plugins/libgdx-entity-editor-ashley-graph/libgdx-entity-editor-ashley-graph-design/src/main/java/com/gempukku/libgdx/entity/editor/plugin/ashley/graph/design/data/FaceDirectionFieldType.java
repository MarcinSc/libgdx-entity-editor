package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FaceDirection;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.EnumEditorWidget;
import com.github.javaparser.ast.type.Type;

import java.util.function.Consumer;

public class FaceDirectionFieldType implements ComponentFieldType<FaceDirection> {
    public static final String ID = "FaceDirection";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Face Direction";
    }

    @Override
    public boolean accepts(String componentClass, String fieldName, Type type, boolean exact) {
        String stringType = type.asString();
        return stringType.equals(FaceDirection.class.getName()) || stringType.equals(FaceDirection.class.getSimpleName());
    }

    @Override
    public Actor createEditor(boolean editable, FaceDirection fieldValue, Consumer<FaceDirection> consumer) {
        return new EnumEditorWidget<FaceDirection>(
                editable, FaceDirection.class, false,
                fieldValue, consumer);
    }
}
