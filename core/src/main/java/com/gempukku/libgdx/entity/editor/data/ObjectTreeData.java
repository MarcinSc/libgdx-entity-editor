package com.gempukku.libgdx.entity.editor.data;

import com.gempukku.libgdx.entity.editor.data.component.CustomDataDefinition;

public interface ObjectTreeData<U extends EntityDefinition<?>> {
    void addEntity(String entityGroup, String parentPath, String name, U entity);

    void addTemplate(String parentPath, String name, U template);

    void addCustomDataType(CustomDataDefinition customDataDefinition);

    LocatedEntityDefinition<U> getTemplateById(String id);

    Iterable<String> getEntityGroups();

    Iterable<LocatedEntityDefinition<U>> getEntities(String entityGroup);

    Iterable<LocatedEntityDefinition<U>> getTemplates();

    Iterable<CustomDataDefinition> getCustomDataDefinitions();

    CustomDataDefinition getCustomDataDefinitionById(String id);

    boolean canCreateTemplate(String parentPath, String name);

    void convertToTemplate(String name, U entity);

    class LocatedEntityDefinition<U extends EntityDefinition<?>> {
        private final U entityDefinition;
        private final String path;

        public LocatedEntityDefinition(U entityDefinition, String path) {
            this.entityDefinition = entityDefinition;
            this.path = path;
        }

        public U getEntityDefinition() {
            return entityDefinition;
        }

        public String getPath() {
            return path;
        }
    }
}
