package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.GraphSpriteProperties;

public class SpriteComponentDataStorage extends ComponentDataStorage<SpriteComponent> {
    public SpriteComponentDataStorage(SpriteComponent component) {
        super(component);
    }

    @Override
    public ComponentDataStorage<SpriteComponent> createDataStorage(SpriteComponent component) {
        return new SpriteComponentDataStorage(component);
    }

    @Override
    public Object getValue(String fieldName) {
        if (fieldName.equals("width"))
            return getComponent().getWidth();
        else if (fieldName.equals("height"))
            return getComponent().getHeight();
        else if (fieldName.equals("layer"))
            return getComponent().getLayer();
        else if (fieldName.equals("tags"))
            return getComponent().getTags();
        else if (fieldName.equals("properties"))
            return getComponent().getProperties();
        else
            throw new IllegalArgumentException();
    }

    @Override
    public void setValue(String fieldName, Object value) {
        if (fieldName.equals("width"))
            getComponent().setWidth(((Number) value).floatValue());
        else if (fieldName.equals("height"))
            getComponent().setHeight(((Number) value).floatValue());
        else if (fieldName.equals("layer"))
            getComponent().setLayer(((Number) value).floatValue());
        else if (fieldName.equals("tags"))
            getComponent().setTags((Array<String>) value);
        else if (fieldName.equals("properties"))
            getComponent().setProperties((GraphSpriteProperties) value);
        else
            throw new IllegalArgumentException();
    }
}
