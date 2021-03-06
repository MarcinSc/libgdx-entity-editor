package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.data.component.type.StringComponentFieldType;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data.SpriteComponentDataStorage;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.widget.GraphShaderPropertiesEditorWidget;
import com.gempukku.libgdx.entity.editor.ui.editor.ArrayFieldEditor;
import com.gempukku.libgdx.entity.editor.ui.editor.ComponentEditor;
import com.gempukku.libgdx.entity.editor.ui.editor.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.FloatEditorWidget;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.function.Consumer;

public class SpriteComponentEditorFactory implements ComponentEditorFactory<SpriteComponentDataStorage> {
    @Override
    public ComponentEditor createComponentEditor(SpriteComponentDataStorage dataStorage, Runnable callback, boolean editable) {
        return new SpriteComponentEditor(dataStorage.getComponent(), callback, editable);
    }

    private class SpriteComponentEditor implements ComponentEditor {
        private Table actor;

        public SpriteComponentEditor(SpriteComponent component, Runnable callback, boolean editable) {
            FloatEditorWidget width = new FloatEditorWidget(editable, component.getWidth(),
                    new Consumer<Number>() {
                        @Override
                        public void accept(Number number) {
                            component.setWidth(number.floatValue());
                            callback.run();
                        }
                    });
            FloatEditorWidget height = new FloatEditorWidget(editable, component.getHeight(),
                    new Consumer<Number>() {
                        @Override
                        public void accept(Number number) {
                            component.setHeight(number.floatValue());
                            callback.run();
                        }
                    });

            ArrayFieldEditor<String> tags = new ArrayFieldEditor<>(editable, component.getTags(), new StringComponentFieldType(),
                    new Consumer<Array<String>>() {
                        @Override
                        public void accept(Array<String> strings) {
                            component.setTags(strings);
                            callback.run();
                        }
                    });

            FloatEditorWidget layer = new FloatEditorWidget(editable, component.getLayer(),
                    new Consumer<Number>() {
                        @Override
                        public void accept(Number number) {
                            component.setLayer(number.floatValue());
                            callback.run();
                        }
                    });

            GraphShaderPropertiesEditorWidget properties = new GraphShaderPropertiesEditorWidget("Shader properties", editable, component.getProperties(),
                    new GraphShaderPropertiesEditorWidget.Callback() {
                        @Override
                        public void setValue(ObjectMap<String, Object> value) {
                            component.setProperties(value);
                            callback.run();
                        }
                    });

            VisTable tbl = new VisTable();
            tbl.add("Width: ").width(EditorConfig.LABEL_WIDTH).left().pad(3);
            tbl.add(width).growX().pad(3).row();
            tbl.add("Height: ").width(EditorConfig.LABEL_WIDTH).left().pad(3);
            tbl.add(height).growX().pad(3).row();
            tbl.add("Layer: ").width(EditorConfig.LABEL_WIDTH).left().pad(3);
            tbl.add(layer).growX().pad(3).row();
            tbl.add("Tags:").colspan(2).left().pad(3).row();
            tbl.add(tags).colspan(2).growX().pad(3).row();
            tbl.add(properties).colspan(2).growX().pad(3).row();

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
