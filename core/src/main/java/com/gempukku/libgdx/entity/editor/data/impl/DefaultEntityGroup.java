package com.gempukku.libgdx.entity.editor.data.impl;

import com.gempukku.libgdx.entity.editor.data.EntityGroup;

public class DefaultEntityGroup extends DefaultEntityGroupFolder implements EntityGroup {
    private boolean enabled;

    public DefaultEntityGroup(String name, boolean enabled) {
        super(name);
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
