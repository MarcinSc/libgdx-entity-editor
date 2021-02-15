package com.gempukku.libgdx.entity.editor.data;

import com.gempukku.libgdx.entity.editor.plugin.ObjectTreeFeedback;

public interface ObjectTreeData {
    void setObjectTreeFeedback(ObjectTreeFeedback objectTreeFeedback);

    void addEntity(String entityGroup, String parentPath, String name, EntityDefinition entity);

    void addTemplate(String parentPath, String name, EntityDefinition template);

    LocatedEntityDefinition getTemplateById(String id);

    Iterable<String> getEntityGroups();

    Iterable<LocatedEntityDefinition> getEntities(String entityGroup);

    Iterable<LocatedEntityDefinition> getTemplates();

    class LocatedEntityDefinition {
        private EntityDefinition entityDefinition;
        private String path;

        public LocatedEntityDefinition(EntityDefinition entityDefinition, String path) {
            this.entityDefinition = entityDefinition;
            this.path = path;
        }

        public EntityDefinition getEntityDefinition() {
            return entityDefinition;
        }

        public String getPath() {
            return path;
        }
    }
}
