package com.gempukku.libgdx.entity.editor.data;

import com.gempukku.libgdx.entity.editor.plugin.ObjectTreeFeedback;

public interface ObjectTreeData {
    void setObjectTreeFeedback(ObjectTreeFeedback objectTreeFeedback);

    void addEntityGroup(EntityGroup entityGroup);

    void addEntityGroupFolder(String entityGroup, String parentPath, String name, EntityGroupFolder folder);

    void addEntity(String entityGroup, String parentPath, String name, EntityDefinition entity);

    EntityGroup getEntityGroup(String name);

    Iterable<EntityGroup> getEntityGroups();
}
