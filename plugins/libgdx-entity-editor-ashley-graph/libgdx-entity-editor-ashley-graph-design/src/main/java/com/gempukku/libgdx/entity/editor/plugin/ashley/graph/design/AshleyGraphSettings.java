package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

public class AshleyGraphSettings extends VisTable {
    private final VisTextField rendererPipelineTextField;
    private final VisTextField assetsFolderTextField;

    private final VisTextField exportFolderTextField;
    private final VisTextField templatesSubfolderTextField;
    private final VisTextField entitiesSubfolderTextField;
    private Runnable exportRunnable;

    public AshleyGraphSettings(
            String rendererPipeline, String assetsFolder,
            String exportFolder,
            String templatesSubfolder, String entitiesSubfolder, Runnable exportRunnable) {
        this.exportRunnable = exportRunnable;
        align(Align.topLeft);

        rendererPipelineTextField = new VisTextField(rendererPipeline);
        assetsFolderTextField = new VisTextField(assetsFolder);
        exportFolderTextField = new VisTextField(exportFolder);
        templatesSubfolderTextField = new VisTextField(templatesSubfolder);
        entitiesSubfolderTextField = new VisTextField(entitiesSubfolder);

        VisDialog exportDialog = createExportDialog();
        VisTable exportButtonTable = new VisTable();
        VisTextButton exportButton = new VisTextButton("Export");
        exportButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        getStage().addActor(exportDialog.fadeIn());
                    }
                });

        add("Project settings").colspan(2).row();

        add("Renderer pipeline: ").left();
        add(rendererPipelineTextField).growX().row();

        add("Assets folder: ").left();
        add(assetsFolderTextField).growX().row();

        exportButtonTable.add(exportButton).expandX().right().row();
        add(exportButtonTable).colspan(2).row();
    }

    private VisDialog createExportDialog() {
        VisDialog exportDialog = new VisDialog("Export");
        exportDialog.setResizable(true);
        exportDialog.setModal(true);

        Table contentTable = exportDialog.getContentTable();
        contentTable.add("Export folder: ").left();
        contentTable.add(exportFolderTextField).growX().row();
        contentTable.add("Templates subfolder: ").left();
        contentTable.add(templatesSubfolderTextField).growX().row();
        contentTable.add("Entities subfolder: ").left();
        contentTable.add(entitiesSubfolderTextField).growX().row();
        VisLabel warningLabel = new VisLabel("Warning! Exporting will delete everything in templates and entities subfolders.");
        warningLabel.setColor(Color.RED);

        contentTable.add(warningLabel).colspan(2).row();

        Table buttonsTable = exportDialog.getButtonsTable();
        VisTextButton cancelButton = new VisTextButton("Cancel");
        cancelButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        exportDialog.fadeOut();
                    }
                });
        VisTextButton exportButton = new VisTextButton("Export");
        exportButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        exportRunnable.run();
                        exportDialog.fadeOut();
                    }
                });
        buttonsTable.add(cancelButton);
        buttonsTable.add(exportButton).row();

        exportDialog.pack();
        exportDialog.centerWindow();

        return exportDialog;
    }

    public String getRendererPipeline() {
        return rendererPipelineTextField.getText();
    }

    public String getAssetsFolder() {
        return assetsFolderTextField.getText();
    }

    public String getExportFolder() {
        return exportFolderTextField.getText();
    }

    public String getTemplatesSubfolder() {
        return templatesSubfolderTextField.getText();
    }

    public String getEntitiesSubfolder() {
        return entitiesSubfolderTextField.getText();
    }
}
