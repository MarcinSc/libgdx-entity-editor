package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

import com.badlogic.ashley.core.Component;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;

public class SpriteDataComponent implements Component {
    private transient GraphSprite graphSprite;

    public GraphSprite getGraphSprite() {
        return graphSprite;
    }

    public void setGraphSprite(GraphSprite graphSprite) {
        this.graphSprite = graphSprite;
    }
}
