package com.gempukku.libgdx.entity.editor.plugin;

import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.EntityGroupFolder;
import com.gempukku.libgdx.entity.editor.data.EntityTemplatesFolder;

public interface ObjectTreeFeedback {
    void createEntityGroup(String entityGroupName);

    EntityGroupFolder createEntityFolder(String name);

    EntityDefinition createEntity(String name);

    EntityTemplatesFolder createTemplatesFolder(String name);

    EntityDefinition createTemplate(String name);
}
