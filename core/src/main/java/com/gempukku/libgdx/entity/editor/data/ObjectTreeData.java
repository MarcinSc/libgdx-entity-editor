package com.gempukku.libgdx.entity.editor.data;

import com.gempukku.libgdx.entity.editor.plugin.ObjectTreeFeedback;

public interface ObjectTreeData {
    void setObjectTreeFeedback(ObjectTreeFeedback objectTreeFeedback);

    void addEntityGroup(EntityGroup entityGroup);

    EntityGroup getEntityGroup(String name);

    Iterable<EntityGroup> getEntityGroups();
}
