package com.gempukku.example.component;

import com.badlogic.ashley.core.Component;

public class ArrayFloatComponent implements Component {
    private Array<Float> value;

    public Array<Float> getValue() {
        return value;
    }

    public void setValue(Array<Float> value) {
        this.value = value;
    }
}
