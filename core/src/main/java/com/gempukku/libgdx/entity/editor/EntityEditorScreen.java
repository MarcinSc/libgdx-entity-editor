package com.gempukku.libgdx.entity.editor;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.project.PreviewRenderer;
import com.gempukku.libgdx.entity.editor.ui.EntityEditorPreview;
import com.gempukku.libgdx.entity.editor.ui.EntityInspector;
import com.gempukku.libgdx.entity.editor.ui.ObjectTree;
import com.gempukku.libgdx.entity.editor.ui.PluginSettings;
import com.gempukku.libgdx.entity.editor.ui.UtilityPanel;

public class EntityEditorScreen extends Table {
    private ObjectTree objectTree;
    private PluginSettings pluginSettings;
    private EntityEditorPreview entityEditorPreview;
    private UtilityPanel utilityPanel;
    private EntityInspector entityInspector;

    public EntityEditorScreen(Skin skin) {
        super(skin);

        objectTree = new ObjectTree(skin);
        pluginSettings = new PluginSettings(skin);
        entityEditorPreview = new EntityEditorPreview();
        utilityPanel = new UtilityPanel();
        entityInspector = new EntityInspector(skin);

        SplitPane leftSplitPane = new SplitPane(objectTree, pluginSettings, true, skin);

        SplitPane centerSplitPane = new SplitPane(entityEditorPreview, utilityPanel, true, skin);

        SplitPane leftCenterSplitPane = new SplitPane(leftSplitPane, centerSplitPane, false, skin);

        SplitPane mainSplitPane = new SplitPane(leftCenterSplitPane, entityInspector, false, skin);

        add(mainSplitPane).grow();
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
}
