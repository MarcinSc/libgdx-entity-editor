package com.gempukku.libgdx.entity.editor.ui;

public enum EntityEditorPreviewZoom {
    Z1(0.01f, "1%"),
    Z2(0.02f, "2%"),
    Z3(0.05f, "5%"),
    Z4(0.1f, "10%"),
    Z5(0.2f, "20%"),
    Z6(0.25f, "25%"),
    Z7(0.33f, "33%"),
    Z8(0.5f, "50%"),
    Z9(0.75f, "75%"),
    Z10(1f, "100%"),
    Z11(1.33f, "133%"),
    Z12(1.67f, "167%"),
    Z13(2f, "200%"),
    Z14(3f, "300%"),
    Z15(4f, "400%"),
    Z16(5, "500%"),
    Z17(6, "600%"),
    Z18(7, "700%"),
    Z19(8, "800%"),
    Z20(10, "1000%");

    private float value;
    private String display;

    EntityEditorPreviewZoom(float value, String display) {
        this.value = value;
        this.display = display;
    }

    public float getValue() {
        return value;
    }

    public String getDisplay() {
        return display;
    }

    public String toString() {
        return getDisplay();
    }
}
