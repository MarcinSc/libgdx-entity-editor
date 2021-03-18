package com.gempukku.libgdx.entity.editor.data.component;

import com.github.javaparser.ast.type.Type;

public interface ComponentFieldType<T> extends EditableType<T> {
    String getId();

    String getName();

    boolean accepts(String componentClass, String fieldName, Type type, boolean exact);
}
