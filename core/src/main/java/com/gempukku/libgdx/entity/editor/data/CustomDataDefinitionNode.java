package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.libgdx.entity.editor.data.component.DataDefinition;
import com.gempukku.libgdx.entity.editor.ui.ObjectTreeNode;
import com.kotcrab.vis.ui.widget.VisLabel;

public class CustomDataDefinitionNode extends ObjectTreeNode<ObjectTreeNode, DataDefinition<?, ?>> {
    public CustomDataDefinitionNode(DataDefinition<?, ?> dataDefinition, Drawable icon) {
        super(new VisLabel(dataDefinition.getName()));
        setValue(dataDefinition);
        setIcon(icon);
    }
}
