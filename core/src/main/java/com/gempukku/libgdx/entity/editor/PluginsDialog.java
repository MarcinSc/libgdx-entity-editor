package com.gempukku.libgdx.entity.editor;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.entity.editor.plugin.PluginDefinition;
import com.gempukku.libgdx.entity.editor.plugin.PluginPreferences;
import com.gempukku.libgdx.entity.editor.plugin.PluginRegistry;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;

public class PluginsDialog extends VisWindow {
    private Array<String> pluginJars = new Array<>();
    private final VerticalGroup pluginList;

    public PluginsDialog(Skin skin) {
        super("Plugins");
        setModal(true);
        setResizable(true);
        addCloseButton();
        align(Align.topLeft);
        setSize(640, 480);

        pluginList = new VerticalGroup();
        pluginList.top();
        pluginList.grow();
        pluginList.pad(5);

        Table listHeader = new Table(skin);
        listHeader.pad(2);
        listHeader.add("JAR path").growX().left();
        listHeader.add("Plugin name").width(150).left();
        listHeader.add("Version").width(80).left();
        listHeader.add("Loaded").width(60);
        listHeader.add("Remove").width(60);
        listHeader.row();
        pluginList.addActor(listHeader);

        for (final PluginDefinition pluginDefinition : PluginRegistry.getPluginDefinitions()) {
            addPluginDefinition(skin, pluginDefinition);
        }

        ScrollPane scrollPane = new ScrollPane(pluginList, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);

        Table buttonTable = new Table(skin);
        buttonTable.pad(5);
        TextButton addPlugin = new TextButton("Install plugin", skin);
        addPlugin.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        addPlugin();
                    }
                });

        buttonTable.add(addPlugin);
        buttonTable.add("").growX();

        TextButton cancel = new TextButton("Cancel", skin);
        cancel.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        close();
                    }
                });
        buttonTable.add(cancel);
        buttonTable.add("").width(5);

        TextButton save = new TextButton("Save", skin);
        save.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        PluginPreferences.savePlugins(pluginJars);
                        close();
                    }
                });
        buttonTable.add(save);

        add(scrollPane).grow();
        row();
        add(new Label("Any changes made on this screen take effect after restarting the application", skin));
        row();
        add(new Separator());
        row();
        add(buttonTable).growX();
        row();
    }

    private void addPluginDefinition(Skin skin, final PluginDefinition pluginDefinition) {
        final Separator separator = new Separator();
        pluginList.addActor(separator);
        final Table pluginTable = new Table(skin);
        pluginTable.pad(2);
        pluginTable.add(createValueLabel(skin, pluginDefinition.jarPath)).growX().left();
        pluginTable.add(createValueLabel(skin, pluginDefinition.pluginName)).width(150).left();
        pluginTable.add(createValueLabel(skin, pluginDefinition.pluginVersion)).width(80).left();
        pluginTable.add(createValueLabel(skin, String.valueOf(pluginDefinition.loaded))).width(60);
        if (pluginDefinition.canBeRemoved) {
            pluginJars.add(pluginDefinition.jarPath);
            TextButton textButton = new TextButton("Remove", skin);
            textButton.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            pluginJars.removeValue(pluginDefinition.jarPath, false);
                            pluginList.removeActor(pluginTable);
                            pluginList.removeActor(separator);
                        }
                    });
            pluginTable.add(textButton).width(60);
        } else {
            pluginTable.add("").width(60);
        }
        pluginTable.row();
        pluginList.addActor(pluginTable);
    }

    private Label createValueLabel(Skin skin, String text) {
        Label label = new Label(text, skin);
        label.setWrap(true);
        label.setColor(Color.GRAY);
        return label;
    }

    private void addPlugin() {
        FileTypeFilter filter = new FileTypeFilter(true);
        filter.addRule("Java Jar File (*.jar)", "jar");

        FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
        fileChooser.setModal(true);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setFileTypeFilter(filter);
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                FileHandle selectedFile = file.get(0);
                processPlugin(selectedFile);
            }
        });
        getStage().addActor(fileChooser.fadeIn());
    }

    private void processPlugin(FileHandle selectedFile) {
        try {
            PluginDefinition pluginDefinition = PluginPreferences.getPluginDefinition(selectedFile);
            addPluginDefinition(getSkin(), pluginDefinition);
        } catch (Exception exp) {
            Dialogs.showErrorDialog(getStage(), "Unable to process the file as a plugin");
        }
    }
}
