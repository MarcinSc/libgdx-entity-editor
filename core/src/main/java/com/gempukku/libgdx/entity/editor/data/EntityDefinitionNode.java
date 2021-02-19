package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.libgdx.entity.editor.ui.ObjectTreeNode;
import com.kotcrab.vis.ui.widget.VisLabel;

public class EntityDefinitionNode extends ObjectTreeNode<ObjectTreeNode, EntityDefinition> {
    public EntityDefinitionNode(EntityDefinition entity, Drawable icon) {
        super(new VisLabel(entity.getName()));
        setValue(entity);
        setIcon(icon);
    }
}
