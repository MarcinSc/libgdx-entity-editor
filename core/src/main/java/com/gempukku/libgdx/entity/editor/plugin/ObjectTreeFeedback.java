package com.gempukku.libgdx.entity.editor.plugin;

import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.EntityGroupFolder;

public interface ObjectTreeFeedback {
    void createEntityGroup(String entityGroupName);

    EntityGroupFolder createEntityFolder(EntityGroupFolder folder, String name);

    EntityDefinition createEntity(EntityGroupFolder folder, String name);
}
