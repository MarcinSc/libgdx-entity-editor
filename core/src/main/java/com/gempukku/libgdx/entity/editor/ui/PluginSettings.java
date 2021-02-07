package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class PluginSettings extends ScrollPane {
    public PluginSettings(Skin skin) {
        super(null, skin);
        setFadeScrollBars(true);
        setForceScroll(false, true);
    }
}
