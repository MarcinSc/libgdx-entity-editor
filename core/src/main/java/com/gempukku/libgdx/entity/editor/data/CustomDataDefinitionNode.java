package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.libgdx.entity.editor.data.component.CustomDataDefinition;
import com.gempukku.libgdx.entity.editor.ui.ObjectTreeNode;
import com.kotcrab.vis.ui.widget.VisLabel;

public class CustomDataDefinitionNode extends ObjectTreeNode<ObjectTreeNode, CustomDataDefinition> {
    public CustomDataDefinitionNode(CustomDataDefinition customDataDefinition, Drawable icon) {
        super(new VisLabel(customDataDefinition.getName()));
        setValue(customDataDefinition);
        setIcon(icon);
    }
}
