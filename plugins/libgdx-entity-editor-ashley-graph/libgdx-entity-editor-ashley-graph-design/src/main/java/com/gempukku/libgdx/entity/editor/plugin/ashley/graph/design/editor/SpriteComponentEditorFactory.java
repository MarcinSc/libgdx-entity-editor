package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.FloatEditorWidget;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.GraphShaderPropertiesEditorWidget;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.PairOfFloatsEditorWidget;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.StringArrayEditorWidget;
import com.kotcrab.vis.ui.widget.VisTable;

public class SpriteComponentEditorFactory implements ComponentEditorFactory<SpriteComponent> {
    @Override
    public ComponentEditor createComponentEditor(SpriteComponent component, boolean editable) {
        return new SpriteComponentEditor(component, editable);
    }

    private class SpriteComponentEditor implements ComponentEditor<SpriteComponent> {
        private Table actor;
        private SpriteComponent component;

        public SpriteComponentEditor(SpriteComponent component, boolean editable) {
            this.component = component;

            PairOfFloatsEditorWidget size = new PairOfFloatsEditorWidget(EditorConfig.LABEL_WIDTH, editable,
                    "Width", component.getWidth(), "Height", component.getHeight(),
                    new PairOfFloatsEditorWidget.Callback() {
                        @Override
                        public void update(float value1, float value2) {
                            component.setWidth(value1);
                            component.setHeight(value2);
                        }
                    });
            StringArrayEditorWidget tags = new StringArrayEditorWidget("Tags", editable, component.getTags(),
                    new StringArrayEditorWidget.Callback() {
                        @Override
                        public void setValue(Array<String> value) {
                            component.setTags(value);
                        }
                    });

            FloatEditorWidget layer = new FloatEditorWidget(EditorConfig.LABEL_WIDTH, editable, "Layer", component.getLayer(),
                    new FloatEditorWidget.Callback() {
                        @Override
                        public void update(float value) {
                            component.setLayer(value);
                        }
                    });

            GraphShaderPropertiesEditorWidget properties = new GraphShaderPropertiesEditorWidget("Shader properties", editable, component.getProperties(),
                    new GraphShaderPropertiesEditorWidget.Callback() {
                        @Override
                        public void setValue(ObjectMap<String, Object> value) {
                            component.setProperties(value);
                        }
                    });

            VisTable tbl = new VisTable();
            tbl.add(size).growX().pad(3).row();
            tbl.add(layer).growX().pad(3).row();
            tbl.add(tags).growX().pad(3).row();
            tbl.add(properties).growX().pad(3).row();

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
        public SpriteComponent getComponent() {
            return component;
        }
    }
}
