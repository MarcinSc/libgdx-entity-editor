package com.gempukku.libgdx.entity.editor.plugin;

import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.EntityGroup;
import com.gempukku.libgdx.entity.editor.data.EntityGroupFolder;
import com.gempukku.libgdx.entity.editor.data.EntityTemplatesFolder;

public interface ObjectTreeFeedback<T extends EntityDefinition> {
    EntityGroup createEntityGroup(String entityGroupName);

    EntityGroupFolder createEntityFolder(String name);

    T createEntity(String id, String name);

    EntityTemplatesFolder createTemplatesFolder(String name);

    T createTemplate(String id, String name);

    T convertToTemplate(String id, String name, T entity);
}
