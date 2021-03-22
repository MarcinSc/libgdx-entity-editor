package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Transform;

public class PhysicsRenderingHelper {
    /**
     * vertices for polygon rendering
     **/
    private final static Vector2[] vertices = new Vector2[1000];
    private static Vector2 t = new Vector2();
    private static Vector2 axis = new Vector2();
    private static final Vector2 f = new Vector2();
    private static final Vector2 v = new Vector2();
    private static final Vector2 lv = new Vector2();

    public static void drawShape(ShapeRenderer renderer, Fixture fixture, Transform transform, Color color) {
        if (vertices[0] == null) {
            // Initialize vertices
            for (int i = 0; i < vertices.length; i++)
                vertices[i] = new Vector2();
        }
        if (fixture.getType() == Shape.Type.Circle) {
            CircleShape circle = (CircleShape) fixture.getShape();
            t.set(circle.getPosition());
            transform.mul(t);
            drawSolidCircle(renderer, t, circle.getRadius(), axis.set(transform.vals[Transform.COS], transform.vals[Transform.SIN]), color);
            return;
        }

        if (fixture.getType() == Shape.Type.Edge) {
            EdgeShape edge = (EdgeShape) fixture.getShape();
            edge.getVertex1(vertices[0]);
            edge.getVertex2(vertices[1]);
            transform.mul(vertices[0]);
            transform.mul(vertices[1]);
            drawSolidPolygon(renderer, vertices, 2, color, true);
            return;
        }

        if (fixture.getType() == Shape.Type.Polygon) {
            PolygonShape chain = (PolygonShape) fixture.getShape();
            if (chain != null) {
                int vertexCount = chain.getVertexCount();
                for (int i = 0; i < vertexCount; i++) {
                    chain.getVertex(i, vertices[i]);
                    transform.mul(vertices[i]);
                }
                drawSolidPolygon(renderer, vertices, vertexCount, color, true);
            }
            return;
        }

        if (fixture.getType() == Shape.Type.Chain) {
            ChainShape chain = (ChainShape) fixture.getShape();
            int vertexCount = chain.getVertexCount();
            for (int i = 0; i < vertexCount; i++) {
                chain.getVertex(i, vertices[i]);
                transform.mul(vertices[i]);
            }
            drawSolidPolygon(renderer, vertices, vertexCount, color, false);
        }
    }

    private static void drawSolidCircle(ShapeRenderer renderer, Vector2 center, float radius, Vector2 axis, Color color) {
        float angle = 0;
        float angleInc = 2 * (float) Math.PI / 20;
        renderer.setColor(color.r, color.g, color.b, color.a);
        for (int i = 0; i < 20; i++, angle += angleInc) {
            v.set((float) Math.cos(angle) * radius + center.x, (float) Math.sin(angle) * radius + center.y);
            if (i == 0) {
                lv.set(v);
                f.set(v);
                continue;
            }
            renderer.line(lv.x, lv.y, v.x, v.y);
            lv.set(v);
        }
        renderer.line(f.x, f.y, lv.x, lv.y);
        renderer.line(center.x, center.y, 0, center.x + axis.x * radius, center.y + axis.y * radius, 0);
    }

    private static void drawSolidPolygon(ShapeRenderer renderer, Vector2[] vertices, int vertexCount, Color color, boolean closed) {
        renderer.setColor(color.r, color.g, color.b, color.a);
        lv.set(vertices[0]);
        f.set(vertices[0]);
        for (int i = 1; i < vertexCount; i++) {
            Vector2 v = vertices[i];
            renderer.line(lv.x, lv.y, v.x, v.y);
            lv.set(v);
        }
        if (closed) renderer.line(f.x, f.y, lv.x, lv.y);
    }

    private static void drawSegment(ShapeRenderer renderer, Vector2 x1, Vector2 x2, Color color) {
        renderer.setColor(color);
        renderer.line(x1.x, x1.y, x2.x, x2.y);
    }
}
