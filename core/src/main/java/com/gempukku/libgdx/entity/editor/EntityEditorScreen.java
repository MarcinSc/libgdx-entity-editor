package com.gempukku.libgdx.entity.editor;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;
import com.gempukku.libgdx.entity.editor.project.PreviewRenderer;
import com.gempukku.libgdx.entity.editor.ui.EntityEditorPreview;
import com.gempukku.libgdx.entity.editor.ui.EntityEditorPreviewHandler;
import com.gempukku.libgdx.entity.editor.ui.EntityEditorPreviewToolbar;
import com.gempukku.libgdx.entity.editor.ui.EntityInspector;
import com.gempukku.libgdx.entity.editor.ui.EntitySelected;
import com.gempukku.libgdx.entity.editor.ui.ObjectTree;
import com.gempukku.libgdx.entity.editor.ui.PluginSettings;
import com.gempukku.libgdx.entity.editor.ui.TemplateChanged;
import com.gempukku.libgdx.entity.editor.ui.UtilityPanel;
import com.kotcrab.vis.ui.widget.VisSplitPane;
import com.kotcrab.vis.ui.widget.VisTable;

public class EntityEditorScreen<T, U extends EntityDefinition> extends VisTable {
    private final ObjectTree<T, U> objectTree;
    private final PluginSettings pluginSettings;
    private final EntityEditorPreviewToolbar entityEditorPreviewToolbar;
    private final EntityEditorPreview<T, U> entityEditorPreview;
    private final UtilityPanel utilityPanel;
    private final EntityInspector<T, U> entityInspector;
    private final OrthographicCamera camera;
    private final TextureSource textureSource;

    public EntityEditorScreen(EntityEditorProject<T, U> project, TextureSource textureSource) {
        this.textureSource = textureSource;
        camera = new OrthographicCamera();
        camera.position.set(0, 0, 0);
        camera.update();

        objectTree = new ObjectTree<>(project, textureSource);
        pluginSettings = new PluginSettings();
        entityEditorPreviewToolbar = new EntityEditorPreviewToolbar();
        entityEditorPreview = new EntityEditorPreview<>(project, entityEditorPreviewToolbar, camera);
        utilityPanel = new UtilityPanel();
        entityInspector = new EntityInspector<>(project, objectTree);

        VisSplitPane leftSplitPane = new VisSplitPane(objectTree, pluginSettings, true);

        VisTable previewTable = new VisTable();
        previewTable.add(entityEditorPreviewToolbar).growX().row();
        previewTable.add(entityEditorPreview).grow().row();

        VisSplitPane centerSplitPane = new VisSplitPane(previewTable, utilityPanel, true);

        VisSplitPane leftCenterSplitPane = new VisSplitPane(leftSplitPane, centerSplitPane, false);

        VisSplitPane mainSplitPane = new VisSplitPane(leftCenterSplitPane, entityInspector, false);

        add(mainSplitPane).grow();

        addListener(
                new EventListener() {
                    @Override
                    public boolean handle(Event event) {
                        if (event instanceof EntitySelected) {
                            EntitySelected<U> entityEvent = (EntitySelected<U>) event;
                            entityInspector.setEditedEntity(entityEvent.getEntity(), entityEvent.isEntity());
                            entityEditorPreview.setEditedEntity(entityEvent.getEntity(), entityEvent.isEntity());
                            return true;
                        }
                        return false;
                    }
                });
        addListener(
                new EventListener() {
                    @Override
                    public boolean handle(Event event) {
                        if (event instanceof TemplateChanged) {
                            objectTree.rebuildAllEntities();
                            return true;
                        }
                        return false;
                    }
                });
    }

    public ObjectTreeData<U> getObjectTreeData() {
        return objectTree;
    }

    public void setPluginSettings(Actor actor) {
        pluginSettings.setActor(actor);
    }

    public void setPreviewRenderer(PreviewRenderer previewRenderer) {
        entityEditorPreview.setPreviewRenderer(previewRenderer);
    }

    public void setDefaultPreviewHandler(EntityEditorPreviewHandler previewHandler) {
        entityEditorPreview.setDefaultPreviewHandler(this, previewHandler);
    }

    public EntityInspector<T, U> getEntityInspector() {
        return entityInspector;
    }

    public EntityEditorPreview<T, U> getEntityEditorPreview() {
        return entityEditorPreview;
    }

    public EntityEditorPreviewToolbar getEntityEditorPreviewToolbar() {
        return entityEditorPreviewToolbar;
    }

    public void setUtilityPanel(Actor actor) {
        utilityPanel.setActor(actor);
    }

    public Camera getCamera() {
        return camera;
    }

    public TextureSource getTextureSource() {
        return textureSource;
    }
}
