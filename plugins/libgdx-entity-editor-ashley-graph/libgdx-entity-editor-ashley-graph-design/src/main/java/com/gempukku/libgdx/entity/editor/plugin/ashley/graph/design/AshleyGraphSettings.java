package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;

public class AshleyGraphSettings extends Table {
    private final TextField rendererPipelineTextField;
    private final TextField templatesFolderTextField;
    private final TextField entityGroupsFolderTextField;
    private final TextField assetsFolderTextField;

    public AshleyGraphSettings(Skin skin,
                               String rendererPipeline, String templatesFolder,
                               String entityGroupsFolder, String assetsFolder) {
        super(skin);
        align(Align.topLeft);

        add("Project settings").colspan(2).row();

        add("Renderer pipeline: ").left();
        rendererPipelineTextField = new TextField(rendererPipeline, skin);
        add(rendererPipelineTextField).growX().row();

        add("Templates folder: ").left();
        templatesFolderTextField = new TextField(templatesFolder, skin);
        add(templatesFolderTextField).growX().row();

        add("Entities folder: ").left();
        entityGroupsFolderTextField = new TextField(entityGroupsFolder, skin);
        add(entityGroupsFolderTextField).growX().row();

        add("Assets folder: ").left();
        assetsFolderTextField = new TextField(assetsFolder, skin);
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
