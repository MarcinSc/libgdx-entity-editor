package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.libgdx.entity.editor.ui.ObjectTreeNode;
import com.kotcrab.vis.ui.widget.VisLabel;

public class EntityTemplateNode<T, U extends EntityDefinition> extends ObjectTreeNode<ObjectTreeNode, U> {
    public EntityTemplateNode(U entity, Drawable icon) {
        super(new VisLabel(entity.getName()));
        setValue(entity);
        setIcon(icon);
    }

    public void setName(String name) {
        getActor().setText(name);
        getValue().setName(name);
    }
}
