package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteStateComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.SpriteStateDataDef;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.SpriteStateEditorWidget;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.StringEditorWidget;
import com.kotcrab.vis.ui.widget.VisTable;

public class SpriteStateComponentEditorFactory implements ComponentEditorFactory<SpriteStateComponent> {
    @Override
    public ComponentEditor createComponentEditor(SpriteStateComponent component) {
        return new SpriteStateComponentEditor(component);
    }

    private class SpriteStateComponentEditor implements ComponentEditor<SpriteStateComponent> {
        private Table actor;
        private SpriteStateComponent component;

        public SpriteStateComponentEditor(SpriteStateComponent component) {
            this.component = component;

            StringEditorWidget state = new StringEditorWidget(
                    EditorConfig.LABEL_WIDTH,
                    "State", component.getState(),
                    new StringEditorWidget.Callback() {
                        @Override
                        public void update(String value) {
                            component.setState(value);
                        }
                    });

            SpriteStateEditorWidget spriteStateEditorWidget = new SpriteStateEditorWidget(
                    "Sprite states", component.getStates(),
                    new SpriteStateEditorWidget.Callback() {
                        @Override
                        public void setValue(ObjectMap<String, SpriteStateDataDef> value) {
                            component.setStates(value);
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

        @Override
        public SpriteStateComponent getComponent() {
            return component;
        }
    }
}
