package com.gempukku.libgdx.entity.editor;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;
import com.gempukku.libgdx.entity.editor.project.PreviewRenderer;
import com.gempukku.libgdx.entity.editor.ui.EntityEditorPreview;
import com.gempukku.libgdx.entity.editor.ui.EntityInspector;
import com.gempukku.libgdx.entity.editor.ui.EntitySelected;
import com.gempukku.libgdx.entity.editor.ui.ObjectTree;
import com.gempukku.libgdx.entity.editor.ui.PluginSettings;
import com.gempukku.libgdx.entity.editor.ui.UtilityPanel;

public class EntityEditorScreen extends Table {
    private ObjectTree objectTree;
    private PluginSettings pluginSettings;
    private EntityEditorPreview entityEditorPreview;
    private UtilityPanel utilityPanel;
    private EntityInspector entityInspector;
    private OrthographicCamera camera;

    public EntityEditorScreen(Skin skin, EntityEditorProject project) {
        super(skin);

        camera = new OrthographicCamera();
        camera.position.set(0, 0, 0);
        camera.update();

        objectTree = new ObjectTree(skin);
        pluginSettings = new PluginSettings(skin);
        entityEditorPreview = new EntityEditorPreview(camera);
        utilityPanel = new UtilityPanel();
        entityInspector = new EntityInspector(skin);

        SplitPane leftSplitPane = new SplitPane(objectTree, pluginSettings, true, skin);

        SplitPane centerSplitPane = new SplitPane(entityEditorPreview, utilityPanel, true, skin);

        SplitPane leftCenterSplitPane = new SplitPane(leftSplitPane, centerSplitPane, false, skin);

        SplitPane mainSplitPane = new SplitPane(leftCenterSplitPane, entityInspector, false, skin);

        add(mainSplitPane).grow();

        addListener(
                new EventListener() {
                    @Override
                    public boolean handle(Event event) {
                        if (event instanceof EntitySelected) {
                            EntityDefinition entity = ((EntitySelected) event).getEntity();
                            entityInspector.setEditedEntity(entity, project);
                            return true;
                        }
                        return false;
                    }
                });
    }

    public ObjectTreeData getObjectTreeData() {
        return objectTree;
    }

    public void setPluginSettings(Actor actor) {
        pluginSettings.setActor(actor);
    }

    public void setPreviewRenderer(PreviewRenderer previewRenderer) {
        entityEditorPreview.setPreviewRenderer(previewRenderer);
    }

    public void setUtilityPanel(Actor actor) {
        utilityPanel.setActor(actor);
    }

    public int getPreviewWidth() {
        return MathUtils.round(entityEditorPreview.getWidth());
    }

    public int getPreviewHeight() {
        return MathUtils.round(entityEditorPreview.getHeight());
    }

    public Camera getCamera() {
        return camera;
    }
}
