package com.gempukku.libgdx.entity.editor.data;

public interface ObjectTreeData {
    void addEntity(String entityGroup, String parentPath, String name, EntityDefinition entity);

    void addTemplate(String parentPath, String name, EntityDefinition template);

    LocatedEntityDefinition getTemplateById(String id);

    Iterable<String> getEntityGroups();

    Iterable<LocatedEntityDefinition> getEntities(String entityGroup);

    Iterable<LocatedEntityDefinition> getTemplates();

    boolean canCreateTemplate(String parentPath, String name);

    void convertToTemplate(String name, EntityDefinition entity);

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
