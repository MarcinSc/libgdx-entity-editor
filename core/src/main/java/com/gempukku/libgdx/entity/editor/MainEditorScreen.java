package com.gempukku.libgdx.entity.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntMap;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProjectInitializer;
import com.gempukku.libgdx.entity.editor.project.ProjectReaderRegistry;
import com.kotcrab.vis.ui.util.OsUtils;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

public class MainEditorScreen extends VisTable implements Disposable {
    private FileHandle editedProjectFolder;
    private EntityEditorProject entityEditorProject;
    private IntMap<Runnable> shortcuts = new IntMap<>();

    private MenuItem newMenuItem;
    private MenuItem open;
    private MenuItem save;
    private MenuItem close;
    private Container<EntityEditorScreen> entityEditorScreenContainer;
    private EntityEditorScreen editorScreen;

    public MainEditorScreen() {
        initialize();
    }

    private void initialize() {
        entityEditorScreenContainer = new Container<>();
        entityEditorScreenContainer.fill();

        MenuBar menuBar = createMenuBar();

        add(menuBar.getTable()).growX().row();
        add(entityEditorScreenContainer).grow().row();

        addListener(
                new InputListener() {
                    @Override
                    public boolean keyDown(InputEvent event, int keycode) {
                        boolean ctrlPressed = OsUtils.isMac() ?
                                Gdx.input.isKeyPressed(Input.Keys.SYM) :
                                Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
                        if (ctrlPressed) {
                            Runnable runnable = shortcuts.get(keycode);
                            if (runnable != null) {
                                runnable.run();
                                return true;
                            }
                        }
                        return false;
                    }
                });
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.addMenu(createFileMenu());

        return menuBar;
    }

    private Menu createFileMenu() {
        Menu fileMenu = new Menu("File");

        newMenuItem = new MenuItem("New");
        newMenuItem.setSubMenu(createNewMenu());
        fileMenu.addItem(newMenuItem);

        open = new MenuItem("Open project");
        open.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        open();
                    }
                });
        fileMenu.addItem(open);

        ChangeListener saveListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                save();
            }
        };
        save = new MenuItem("Save project");
        save.setDisabled(true);
        addControlShortcut(Input.Keys.S, save, saveListener);
        save.addListener(saveListener);
        fileMenu.addItem(save);

        fileMenu.addSeparator();

        close = new MenuItem("Close project");
        close.setDisabled(true);
        close.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        close();
                    }
                });
        fileMenu.addItem(close);

        fileMenu.addSeparator();

        MenuItem plugins = new MenuItem("Plugins...");
        plugins.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        PluginsDialog pluginsDialog = new PluginsDialog();
                        getStage().addActor(pluginsDialog);
                        pluginsDialog.centerWindow();
                    }
                });
        fileMenu.addItem(plugins);

        fileMenu.addSeparator();
        MenuItem exit = new MenuItem("Exit");
        exit.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        exit();
                    }
                });
        fileMenu.addItem(exit);

        return fileMenu;
    }

    private PopupMenu createNewMenu() {
        PopupMenu templateMenu = new PopupMenu();
        for (EntityEditorProjectInitializer initializer : ProjectReaderRegistry.getInitializers()) {
            String templateName = initializer.getTemplateName();
            MenuItem templateItem = new MenuItem(templateName);
            templateItem.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
                            fileChooser.setModal(true);
                            fileChooser.setSelectionMode(FileChooser.SelectionMode.DIRECTORIES);
                            fileChooser.setListener(new FileChooserAdapter() {
                                @Override
                                public void selected(Array<FileHandle> file) {
                                    FileHandle selectedFolder = file.get(0);
                                    try {
                                        entityEditorProject = initializer.createNewProject(selectedFolder);
                                        EntityEditorScreen entityEditorScreen = new EntityEditorScreen(entityEditorProject);
                                        entityEditorProject.initialize(entityEditorScreen);
                                        setEditedFile(entityEditorScreen, selectedFolder, true);
                                        entityEditorScreenContainer.setActor(entityEditorScreen);
                                    } catch (Exception exp) {
                                        Dialogs.showErrorDialog(getStage(), "Unable to create project", exp);
                                    }
                                }
                            });
                            getStage().addActor(fileChooser.fadeIn());
                        }
                    });
            templateMenu.addItem(templateItem);
        }
        return templateMenu;
    }

    private void open() {
        FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
        fileChooser.setModal(true);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.DIRECTORIES);
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                FileHandle selectedFile = file.get(0);
                try {
                    entityEditorProject = loadProjectFromFolder(selectedFile);
                    EntityEditorScreen entityEditorScreen = new EntityEditorScreen(entityEditorProject);
                    entityEditorProject.initialize(entityEditorScreen);
                    setEditedFile(entityEditorScreen, selectedFile, true);
                    entityEditorScreenContainer.setActor(entityEditorScreen);
                } catch (Exception exp) {
                    Dialogs.showErrorDialog(getStage(), "Unable to open project", exp);
                }
            }
        });
        getStage().addActor(fileChooser.fadeIn());
    }

    private void save() {
        entityEditorProject.save(editedProjectFolder, editorScreen.getObjectTreeData());
    }

    private void close() {
        entityEditorScreenContainer.setActor(null);
        entityEditorProject.dispose();
        setEditedFile(null, null, false);
    }

    private void exit() {
        Gdx.app.exit();
    }

    private EntityEditorProject loadProjectFromFolder(FileHandle folder) {
        for (EntityEditorProjectInitializer initializer : ProjectReaderRegistry.getInitializers()) {
            if (initializer.canReadProject(folder)) {
                return initializer.openProject(folder);
            }
        }
        throw new GdxRuntimeException("Unable to find plugin supporting the project file");
    }

    private void setEditedFile(EntityEditorScreen entityEditorScreen, FileHandle folder, boolean editing) {
        editorScreen = entityEditorScreen;
        editedProjectFolder = folder;
        if (editing) {
            newMenuItem.setDisabled(true);
            open.setDisabled(true);
            save.setDisabled(false);
            close.setDisabled(false);
        } else {
            newMenuItem.setDisabled(false);
            open.setDisabled(false);
            save.setDisabled(true);
            close.setDisabled(true);
        }
    }

    private void addControlShortcut(int key, final MenuItem menuItem, final ChangeListener listener) {
        menuItem.setShortcut(Input.Keys.CONTROL_LEFT, key);
        shortcuts.put(key, new Runnable() {
            @Override
            public void run() {
                if (!menuItem.isDisabled())
                    listener.changed(null, null);
            }
        });
    }

    public void update(float delta) {
        if (entityEditorProject != null)
            entityEditorProject.update(delta);
    }

    @Override
    public void dispose() {
        if (entityEditorProject != null) {
            entityEditorProject.dispose();
        }
    }
}
