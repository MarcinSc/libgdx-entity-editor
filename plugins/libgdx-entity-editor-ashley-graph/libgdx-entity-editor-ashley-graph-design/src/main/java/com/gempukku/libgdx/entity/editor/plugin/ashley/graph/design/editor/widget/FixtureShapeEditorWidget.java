package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.widget;

import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.BoxFixtureShape;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.FixtureShape;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.FloatEditorWidget;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.PairOfFloatsEditorWidget;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.function.Consumer;

public class FixtureShapeEditorWidget extends VisTable {
    private BoxFixtureShape fixtureShape;
    private Consumer<FixtureShape> consumer;

    public FixtureShapeEditorWidget(
            boolean editable, FixtureShape shape, Consumer<FixtureShape> consumer) {
        // TODO Temporarily assume it's Box shape
        this.fixtureShape = (BoxFixtureShape) shape;
        this.consumer = consumer;

        FloatEditorWidget widthWidget = new FloatEditorWidget(editable, fixtureShape.getWidth(),
                new Consumer<Number>() {
                    @Override
                    public void accept(Number number) {
                        fixtureShape.setWidth(number.floatValue());
                        updateValue();
                    }
                });

        FloatEditorWidget heightWidget = new FloatEditorWidget(editable, fixtureShape.getHeight(),
                new Consumer<Number>() {
                    @Override
                    public void accept(Number number) {
                        fixtureShape.setHeight(number.floatValue());
                        updateValue();
                    }
                });

        PairOfFloatsEditorWidget centerWidget = new PairOfFloatsEditorWidget(20, editable,
                "x", fixtureShape.getCenter().x, "y", fixtureShape.getCenter().y,
                new PairOfFloatsEditorWidget.Callback() {
                    @Override
                    public void update(float value1, float value2) {
                        fixtureShape.getCenter().set(value1, value2);
                        updateValue();
                    }
                });

        FloatEditorWidget angleWidget = new FloatEditorWidget(editable, fixtureShape.getAngle(),
                new Consumer<Number>() {
                    @Override
                    public void accept(Number number) {
                        fixtureShape.setAngle(number.floatValue());
                        updateValue();
                    }
                });

        add("width: ");
        add(widthWidget).growX().row();
        add("height: ");
        add(heightWidget).growX().row();
        add("center: ");
        add(centerWidget).growX().row();
        add("angle: ");
        add(angleWidget).growX().row();
    }

    private void updateValue() {
        consumer.accept(fixtureShape);
    }
}
