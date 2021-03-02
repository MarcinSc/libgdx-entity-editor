package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.javaparser.ast.type.Type;

public interface ComponentFieldType<T> {
    String getId();

    String getName();

    boolean accepts(String componentClass, String fieldName, Type type, boolean exact);

    Actor createEditor(float labelWidth, boolean editable, String fieldName,
                       T fieldValue, ObjectMap<String, Object> componentData, Runnable callback);
}
