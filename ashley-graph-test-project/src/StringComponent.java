package com.gempukku.example.component;

import com.badlogic.ashley.core.Component;

public class StringComponent implements Component {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
