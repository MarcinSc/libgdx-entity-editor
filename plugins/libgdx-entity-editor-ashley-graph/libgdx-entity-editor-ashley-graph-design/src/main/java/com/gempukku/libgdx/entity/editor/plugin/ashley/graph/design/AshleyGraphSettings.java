package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;

public class AshleyGraphSettings extends VisTable {
    private final VisTextField rendererPipelineTextField;
    private final VisTextField templatesFolderTextField;
    private final VisTextField entityGroupsFolderTextField;
    private final VisTextField assetsFolderTextField;

    public AshleyGraphSettings(
            String rendererPipeline, String templatesFolder,
            String entityGroupsFolder, String assetsFolder) {
        align(Align.topLeft);

        add("Project settings").colspan(2).row();

        add("Renderer pipeline: ").left();
        rendererPipelineTextField = new VisTextField(rendererPipeline);
        add(rendererPipelineTextField).growX().row();

        add("Templates folder: ").left();
        templatesFolderTextField = new VisTextField(templatesFolder);
        add(templatesFolderTextField).growX().row();

        add("Entities folder: ").left();
        entityGroupsFolderTextField = new VisTextField(entityGroupsFolder);
        add(entityGroupsFolderTextField).growX().row();

        add("Assets folder: ").left();
        assetsFolderTextField = new VisTextField(assetsFolder);
        add(assetsFolderTextField).growX().row();
    }

    public String getRendererPipeline() {
        return rendererPipelineTextField.getText();
    }

    public String getTemplatesFolder() {
        return templatesFolderTextField.getText();
    }

    public String getEntityGroupsFolder() {
        return entityGroupsFolderTextField.getText();
    }

    public String getAssetsFolder() {
        return assetsFolderTextField.getText();
    }
}
