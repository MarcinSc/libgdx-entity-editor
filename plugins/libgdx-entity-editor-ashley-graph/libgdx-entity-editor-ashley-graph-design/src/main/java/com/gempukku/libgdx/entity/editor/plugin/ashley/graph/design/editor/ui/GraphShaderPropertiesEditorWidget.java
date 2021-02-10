package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.value.CurrentTimeValue;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.value.TextureValue;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.EditorConfig;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.util.SimpleNumberFormatter;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;

public class GraphShaderPropertiesEditorWidget extends Table {
    private final Runnable updateValue;
    private final VerticalGroup verticalGroup;
    private final Callback callback;

    public GraphShaderPropertiesEditorWidget(
            Skin skin,
            String label, ObjectMap<String, Object> pipelineProperties, Callback callback) {
        super(skin);
        this.callback = callback;

        verticalGroup = new VerticalGroup();
        verticalGroup.grow();
        verticalGroup.align(Align.topLeft);

        updateValue = new Runnable() {
            @Override
            public void run() {
                updateValue();
            }
        };

        for (ObjectMap.Entry<String, Object> entry : pipelineProperties.entries()) {
            addProperty(skin, verticalGroup, entry.key, entry.value);
        }

        ScrollPane scrollPane = new ScrollPane(verticalGroup, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);

        Table buttonTable = new Table(skin);

        TextButton addButton = new TextButton("Add property...", skin);
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
        TextButton removeButton = new TextButton("Remove selected", skin);
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

        buttonTable.add(addButton).pad(3);
        buttonTable.add(removeButton).pad(3);

        add(label + ":").growX().row();
        add(scrollPane).height(200).growX().row();
        add(buttonTable).growX().row();
    }

    private void addProperty(Skin skin, VerticalGroup verticalGroup, String name, Object value) {
        ShaderProperty shaderProperty = new ShaderProperty(skin, name, value, updateValue);
        verticalGroup.addActor(shaderProperty);
    }

    private void updateValue() {
        ObjectMap<String, Object> result = new ObjectMap<>();
        for (Actor child : verticalGroup.getChildren()) {
            ShaderProperty shaderProperty = (ShaderProperty) child;
            result.put(shaderProperty.getPropertyName(), shaderProperty.getValue());
        }
        callback.setValue(result);
    }

    public interface Callback {
        void setValue(ObjectMap<String, Object> value);
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
                            addProperty(getSkin(), verticalGroup, input, value);
                            updateValue();
                        }

                        @Override
                        public void canceled() {

                        }
                    });
        }
    }

    private static class ShaderProperty extends Table {
        private String propertyName;
        private ShaderFieldType fieldType;
        private ShaderPropertyValue shaderPropertyValue;
        private CheckBox checkBox;

        public ShaderProperty(Skin skin, String propertyName, Object value, Runnable callback) {
            super(skin);
            this.propertyName = propertyName;
            if (value instanceof Number) {
                fieldType = ShaderFieldType.Float;
                shaderPropertyValue = new FloatShaderPropertyValue(((Number) value).floatValue(), callback);
            } else if (value instanceof Vector2) {
                fieldType = ShaderFieldType.Vector2;
                Vector2 v2 = (Vector2) value;
                shaderPropertyValue = new Vector2PropertyValue(skin, v2.x, v2.y, callback);
            } else if (value instanceof Vector3) {
                fieldType = ShaderFieldType.Vector3;
                Vector3 v3 = (Vector3) value;
                shaderPropertyValue = new Vector3PropertyValue(skin, v3.x, v3.y, v3.z, callback);
            } else if (value instanceof Color) {
                fieldType = ShaderFieldType.Vector4;
                Color color = (Color) value;
                shaderPropertyValue = new ColorPropertyValue(skin, color, callback);
            } else if (value instanceof TextureValue) {
                fieldType = ShaderFieldType.TextureRegion;
                TextureValue texture = (TextureValue) value;
                shaderPropertyValue = new TextureRegionPropertyValue(skin, texture, callback);
            } else if (value instanceof CurrentTimeValue) {
                fieldType = ShaderFieldType.Float;
                shaderPropertyValue = new CurrentTimePropertyValue(skin, callback);
            } else {
                throw new GdxRuntimeException("Unknown property value");
            }

            checkBox = new CheckBox(propertyName, skin);
            add(checkBox).width(EditorConfig.LABEL_WIDTH);
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

    private static class CurrentTimePropertyValue extends Table implements ShaderPropertyValue {
        public CurrentTimePropertyValue(Skin skin, Runnable callback) {
            super(skin);
        }

        @Override
        public Object getValue() {
            return new CurrentTimeValue();
        }

        @Override
        public Actor getActor() {
            return this;
        }
    }

    private static class TextureRegionPropertyValue extends Table implements ShaderPropertyValue {
        private String atlas;
        private String texture;

        public TextureRegionPropertyValue(Skin skin, TextureValue textureValue, Runnable callback) {
            super(skin);
            this.atlas = textureValue.getAtlas();
            this.texture = textureValue.getTexture();

            PairOfStringsEditorWidget widget = new PairOfStringsEditorWidget(
                    skin, EditorConfig.LABEL_WIDTH, "Atlas", textureValue.getAtlas(),
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

    private static class ColorPropertyValue extends Table implements ShaderPropertyValue {
        private Label colorLabel;

        public ColorPropertyValue(Skin skin, Color color, Runnable callback) {
            super(skin);
            colorLabel = new Label(color.toString(), skin);

            TextButton modifyColor = new TextButton("Change", skin);
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

    private static class Vector3PropertyValue extends Table implements ShaderPropertyValue {
        private VisValidatableTextField xField;
        private VisValidatableTextField yField;
        private VisValidatableTextField zField;

        public Vector3PropertyValue(Skin skin, float x, float y, float z, Runnable callback) {
            super(skin);

            ChangeListener listener = new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    callback.run();
                }
            };

            xField = new VisValidatableTextField(Validators.FLOATS);
            xField.setText(SimpleNumberFormatter.format(x));
            xField.addListener(listener);
            yField = new VisValidatableTextField(Validators.FLOATS);
            yField.setText(SimpleNumberFormatter.format(y));
            yField.addListener(listener);
            zField = new VisValidatableTextField(Validators.FLOATS);
            zField.setText(SimpleNumberFormatter.format(z));
            zField.addListener(listener);

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

    private static class Vector2PropertyValue extends Table implements ShaderPropertyValue {
        private VisValidatableTextField xField;
        private VisValidatableTextField yField;

        public Vector2PropertyValue(Skin skin, float x, float y, Runnable callback) {
            super(skin);

            ChangeListener listener = new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    callback.run();
                }
            };

            xField = new VisValidatableTextField(Validators.FLOATS);
            xField.setText(SimpleNumberFormatter.format(x));
            xField.addListener(listener);
            yField = new VisValidatableTextField(Validators.FLOATS);
            yField.setText(SimpleNumberFormatter.format(y));
            yField.addListener(listener);

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
        public FloatShaderPropertyValue(float value, Runnable callback) {
            super(Validators.FLOATS);
            setText(SimpleNumberFormatter.format(value));
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
