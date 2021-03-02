package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteStateComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.SpriteStateDataDef;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data.SpriteStateComponentDataStorage;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.widget.SpriteStateEditorWidget;
import com.gempukku.libgdx.entity.editor.ui.editor.ComponentEditor;
import com.gempukku.libgdx.entity.editor.ui.editor.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.StringEditorWidget;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.function.Consumer;

public class SpriteStateComponentEditorFactory implements ComponentEditorFactory<SpriteStateComponentDataStorage> {
    @Override
    public ComponentEditor createComponentEditor(SpriteStateComponentDataStorage dataStorage, Runnable callback, boolean editable) {
        return new SpriteStateComponentEditor(dataStorage.getComponent(), callback, editable);
    }

    private class SpriteStateComponentEditor implements ComponentEditor {
        private Table actor;

        public SpriteStateComponentEditor(SpriteStateComponent component, Runnable callback, boolean editable) {
            StringEditorWidget state = new StringEditorWidget(
                    editable,
                    component.getState(),
                    new Consumer<String>() {
                        @Override
                        public void accept(String s) {
                            component.setState(s);
                            callback.run();
                        }
                    });

            SpriteStateEditorWidget spriteStateEditorWidget = new SpriteStateEditorWidget(
                    "Sprite states", editable, component.getStates(),
                    new SpriteStateEditorWidget.Callback() {
                        @Override
                        public void setValue(ObjectMap<String, SpriteStateDataDef> value) {
                            component.setStates(value);
                            callback.run();
                        }
                    });

            VisTable tbl = new VisTable();
            tbl.add(state).growX().pad(3).row();
            tbl.add(spriteStateEditorWidget).growX().pad(3).row();

            this.actor = tbl;
        }

        @Override
        public Table getActor() {
            return actor;
        }

        @Override
        public void refresh() {

        }
    }
}
