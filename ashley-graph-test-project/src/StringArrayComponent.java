package com.gempukku.example.component;

import com.badlogic.ashley.core.Component;

public class StringArrayComponent implements Component {
    private Array<String> value;

    public Array<String> getValue() {
        return value;
    }

    public void setValue(Array<String> value) {
        this.value = value;
    }
}
