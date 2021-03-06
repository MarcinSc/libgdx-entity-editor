package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.GraphSpriteProperties;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.value.CurrentTimeValue;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.value.TextureValue;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.EditorConfig;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.PairOfStringsEditorWidget;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.util.SimpleNumberFormatter;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;

import java.util.function.Consumer;

public class GraphSpritePropertiesEditorWidget extends VisTable {
    private final Runnable updateValue;
    private final VerticalGroup verticalGroup;
    private GraphSpriteProperties graphSpriteProperties;
    private Consumer<GraphSpriteProperties> consumer;

    public GraphSpritePropertiesEditorWidget(
            boolean editable, GraphSpriteProperties graphSpriteProperties, Consumer<GraphSpriteProperties> consumer) {
        this.graphSpriteProperties = graphSpriteProperties;
        this.consumer = consumer;

        verticalGroup = new VerticalGroup();
        verticalGroup.grow();
        verticalGroup.align(Align.topLeft);

        updateValue = new Runnable() {
            @Override
            public void run() {
                updateValue();
            }
        };

        for (ObjectMap.Entry<String, Object> entry : graphSpriteProperties) {
            addProperty(verticalGroup, entry.key, editable, entry.value);
        }

        VisScrollPane scrollPane = new VisScrollPane(verticalGroup);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);

        VisTable buttonTable = new VisTable();

        VisTextButton addButton = new VisTextButton("Add property...");
        addButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        PopupMenu popupMenu = new PopupMenu();

                        MenuItem floatMenuItem = new MenuItem("Float");
                        floatMenuItem.addListener(
                                new AddPropertyChangeListener(0f));
                        popupMenu.addItem(floatMenuItem);
                        MenuItem vector2MenuItem = new MenuItem("Vector2");
                        vector2MenuItem.addListener(
                                new AddPropertyChangeListener(new Vector2()));
                        popupMenu.addItem(vector2MenuItem);
                        MenuItem vector3MenuItem = new MenuItem("Vector3");
                        vector3MenuItem.addListener(
                                new AddPropertyChangeListener(new Vector3()));
                        popupMenu.addItem(vector3MenuItem);
                        MenuItem colorMenuItem = new MenuItem("Color");
                        colorMenuItem.addListener(
                                new AddPropertyChangeListener(new Color()));
                        popupMenu.addItem(colorMenuItem);
                        MenuItem textureRegionMenuItem = new MenuItem("TextureRegion");
                        textureRegionMenuItem.addListener(
                                new AddPropertyChangeListener(new TextureValue("", "")));
                        popupMenu.addItem(textureRegionMenuItem);
                        MenuItem currentTimeMenuItem = new MenuItem("Current time");
                        currentTimeMenuItem.addListener(
                                new AddPropertyChangeListener(new CurrentTimeValue()));
                        popupMenu.addItem(currentTimeMenuItem);

                        popupMenu.showMenu(getStage(), addButton);
                    }
                });
        addButton.setDisabled(!editable);
        VisTextButton removeButton = new VisTextButton("Remove selected");
        removeButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        for (Actor child : verticalGroup.getChildren()) {
                            ShaderProperty shaderProperty = (ShaderProperty) child;
                            if (shaderProperty.isSelected()) {
                                verticalGroup.removeActor(child);
                            }
                        }
                        updateValue();
                    }
                });
        removeButton.setDisabled(!editable);

        buttonTable.add(addButton).pad(3);
        buttonTable.add(removeButton).pad(3);

        add(scrollPane).grow().row();
        add(buttonTable).growX().row();
    }

    @Override
    public float getPrefHeight() {
        return 150;
    }

    private void addProperty(VerticalGroup verticalGroup, String name, boolean editable, Object value) {
        ShaderProperty shaderProperty = new ShaderProperty(name, editable, value, updateValue);
        verticalGroup.addActor(shaderProperty);
    }

    private void updateValue() {
        graphSpriteProperties.clear();
        for (Actor child : verticalGroup.getChildren()) {
            ShaderProperty shaderProperty = (ShaderProperty) child;
            graphSpriteProperties.put(shaderProperty.getPropertyName(), shaderProperty.getValue());
        }
        consumer.accept(graphSpriteProperties);
    }

    private class AddPropertyChangeListener extends ChangeListener {
        private Object value;

        public AddPropertyChangeListener(Object value) {
            this.value = value;
        }

        @Override
        public void changed(ChangeEvent event, Actor actor) {
            Dialogs.showInputDialog(getStage(), "Add property", "Enter property name",
                    new InputDialogListener() {
                        @Override
                        public void finished(String input) {
                            addProperty(verticalGroup, input, true, value);
                            updateValue();
                        }

                        @Override
                        public void canceled() {

                        }
                    });
        }
    }

    private static class ShaderProperty extends VisTable {
        private String propertyName;
        private ShaderFieldType fieldType;
        private ShaderPropertyValue shaderPropertyValue;
        private VisCheckBox checkBox;

        public ShaderProperty(String propertyName, boolean editable, Object value, Runnable callback) {
            this.propertyName = propertyName;
            if (value instanceof Number) {
                fieldType = ShaderFieldType.Float;
                shaderPropertyValue = new FloatShaderPropertyValue(((Number) value).floatValue(), editable, callback);
            } else if (value instanceof Vector2) {
                fieldType = ShaderFieldType.Vector2;
                Vector2 v2 = (Vector2) value;
                shaderPropertyValue = new Vector2PropertyValue(v2.x, v2.y, editable, callback);
            } else if (value instanceof Vector3) {
                fieldType = ShaderFieldType.Vector3;
                Vector3 v3 = (Vector3) value;
                shaderPropertyValue = new Vector3PropertyValue(v3.x, v3.y, v3.z, editable, callback);
            } else if (value instanceof Color) {
                fieldType = ShaderFieldType.Vector4;
                Color color = (Color) value;
                shaderPropertyValue = new ColorPropertyValue(color, editable, callback);
            } else if (value instanceof TextureValue) {
                fieldType = ShaderFieldType.TextureRegion;
                TextureValue texture = (TextureValue) value;
                shaderPropertyValue = new TextureRegionPropertyValue(editable, texture, callback);
            } else if (value instanceof CurrentTimeValue) {
                fieldType = ShaderFieldType.Float;
                shaderPropertyValue = new CurrentTimePropertyValue();
            } else {
                throw new GdxRuntimeException("Unknown property value");
            }

            checkBox = new VisCheckBox(propertyName);
            checkBox.align(Align.topLeft);
            add(checkBox).width(EditorConfig.LABEL_WIDTH).fill();
            add((Actor) shaderPropertyValue).growX().row();
        }

        public ShaderFieldType getFieldType() {
            return fieldType;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public Object getValue() {
            return shaderPropertyValue.getValue();
        }

        public boolean isSelected() {
            return checkBox.isChecked();
        }
    }

    private static class CurrentTimePropertyValue extends VisTable implements ShaderPropertyValue {
        @Override
        public Object getValue() {
            return new CurrentTimeValue();
        }

        @Override
        public Actor getActor() {
            return this;
        }
    }

    private static class TextureRegionPropertyValue extends VisTable implements ShaderPropertyValue {
        private String atlas;
        private String texture;

        public TextureRegionPropertyValue(boolean editable, TextureValue textureValue, Runnable callback) {
            this.atlas = textureValue.getAtlas();
            this.texture = textureValue.getTexture();

            PairOfStringsEditorWidget widget = new PairOfStringsEditorWidget(
                    EditorConfig.LABEL_WIDTH, editable, "Atlas", textureValue.getAtlas(),
                    "Texture", textureValue.getTexture(), new PairOfStringsEditorWidget.Callback() {
                @Override
                public void update(String value1, String value2) {
                    atlas = value1;
                    texture = value2;
                    callback.run();
                }
            });

            add(widget).growX().row();
        }

        @Override
        public Actor getActor() {
            return this;
        }

        @Override
        public Object getValue() {
            return new TextureValue(atlas, texture);
        }
    }

    private static class ColorPropertyValue extends VisTable implements ShaderPropertyValue {
        private VisLabel colorLabel;

        public ColorPropertyValue(Color color, boolean editable, Runnable callback) {
            colorLabel = new VisLabel(color.toString());

            VisTextButton modifyColor = new VisTextButton("Change");
            modifyColor.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            final ColorPicker picker = new ColorPicker();
                            picker.setListener(new ColorPickerAdapter() {
                                @Override
                                public void finished(Color newColor) {
                                    colorLabel.setText(newColor.toString());
                                    callback.run();
                                    picker.dispose();
                                }
                            });
                            picker.setColor(Color.valueOf(colorLabel.getText().toString()));
                            getStage().addActor(picker.fadeIn());
                        }
                    });
            modifyColor.setDisabled(!editable);

            add(colorLabel).growX();
            add(modifyColor).row();
        }

        @Override
        public Actor getActor() {
            return this;
        }

        @Override
        public Object getValue() {
            return Color.valueOf(colorLabel.getText().toString());
        }
    }

    private static class Vector3PropertyValue extends VisTable implements ShaderPropertyValue {
        private VisValidatableTextField xField;
        private VisValidatableTextField yField;
        private VisValidatableTextField zField;

        public Vector3PropertyValue(float x, float y, float z, boolean editable, Runnable callback) {
            ChangeListener listener = new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    callback.run();
                }
            };

            xField = new VisValidatableTextField(Validators.FLOATS);
            xField.setText(SimpleNumberFormatter.format(x));
            xField.addListener(listener);
            xField.setDisabled(!editable);
            yField = new VisValidatableTextField(Validators.FLOATS);
            yField.setText(SimpleNumberFormatter.format(y));
            yField.addListener(listener);
            yField.setDisabled(!editable);
            zField = new VisValidatableTextField(Validators.FLOATS);
            zField.setText(SimpleNumberFormatter.format(z));
            zField.addListener(listener);
            zField.setDisabled(!editable);

            add("X: ");
            add(xField).growX().row();
            add("Y: ");
            add(yField).growX().row();
            add("Z: ");
            add(zField).growX().row();
        }

        @Override
        public Actor getActor() {
            return this;
        }

        @Override
        public Object getValue() {
            return new Vector3(
                    Float.parseFloat(xField.getText()),
                    Float.parseFloat(yField.getText()),
                    Float.parseFloat(zField.getText()));
        }
    }

    private static class Vector2PropertyValue extends VisTable implements ShaderPropertyValue {
        private VisValidatableTextField xField;
        private VisValidatableTextField yField;

        public Vector2PropertyValue(float x, float y, boolean editable, Runnable callback) {
            ChangeListener listener = new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    callback.run();
                }
            };

            xField = new VisValidatableTextField(Validators.FLOATS);
            xField.setText(SimpleNumberFormatter.format(x));
            xField.addListener(listener);
            xField.setDisabled(!editable);
            yField = new VisValidatableTextField(Validators.FLOATS);
            yField.setText(SimpleNumberFormatter.format(y));
            yField.addListener(listener);
            yField.setDisabled(!editable);

            add("X: ");
            add(xField).growX().row();
            add("Y: ");
            add(yField).growX().row();
        }

        @Override
        public Actor getActor() {
            return this;
        }

        @Override
        public Object getValue() {
            return new Vector2(
                    Float.parseFloat(xField.getText()),
                    Float.parseFloat(yField.getText()));
        }
    }

    private static class FloatShaderPropertyValue extends VisValidatableTextField implements ShaderPropertyValue {
        public FloatShaderPropertyValue(float value, boolean editable, Runnable callback) {
            super(Validators.FLOATS);
            setText(SimpleNumberFormatter.format(value));
            setDisabled(!editable);
            addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            callback.run();
                        }
                    });
        }

        @Override
        public Actor getActor() {
            return this;
        }

        @Override
        public Object getValue() {
            return Float.parseFloat(getText());
        }
    }

    private interface ShaderPropertyValue {
        Object getValue();

        Actor getActor();
    }
}
