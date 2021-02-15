package com.gempukku.libgdx.entity.editor.plugin;

import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.EntityGroup;
import com.gempukku.libgdx.entity.editor.data.EntityGroupFolder;
import com.gempukku.libgdx.entity.editor.data.EntityTemplatesFolder;

public interface ObjectTreeFeedback {
    EntityGroup createEntityGroup(String entityGroupName);

    EntityGroupFolder createEntityFolder(String name);

    EntityDefinition createEntity(String id, String name);

    EntityTemplatesFolder createTemplatesFolder(String name);

    EntityDefinition createTemplate(String id, String name);
}
