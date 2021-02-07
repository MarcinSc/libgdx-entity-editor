package com.gempukku.libgdx.entity.editor.project;

import com.badlogic.gdx.utils.Array;

public class ProjectReaderRegistry {
    private static final Array<EntityEditorProjectInitializer> initializers = new Array<>();

    public static void register(EntityEditorProjectInitializer initializer) {
        initializers.add(initializer);
    }

    public static Iterable<EntityEditorProjectInitializer> getInitializers() {
        return initializers;
    }
}
