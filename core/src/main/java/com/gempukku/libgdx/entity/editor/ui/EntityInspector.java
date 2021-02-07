package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.Separator;

public class EntityInspector extends Table {
    private Table entityDetails;

    public EntityInspector(Skin skin) {
        super(skin);

        entityDetails = new Table(skin);

        add("Entity inspector").growX().row();
        add(new Separator()).growX().row();
        add(entityDetails).grow().row();
    }
}
