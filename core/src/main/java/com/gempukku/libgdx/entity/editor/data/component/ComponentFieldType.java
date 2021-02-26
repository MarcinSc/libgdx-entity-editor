package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.javaparser.ast.type.Type;

public interface ComponentFieldType<T> {
    boolean accepts(Type type);

    Actor createEditor(float labelWidth, boolean editable, String fieldName,
                       T fieldValue, ObjectMap<String, Object> componentData, Runnable callback);
}
