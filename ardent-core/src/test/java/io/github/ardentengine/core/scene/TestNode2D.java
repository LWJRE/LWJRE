package io.github.ardentengine.core.scene;

import io.github.scalamath.vecmatlib.Mat2x3f;
import io.github.scalamath.vecmatlib.Vec2f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestNode2D {

    @Test
    public void testTranslate() {
        var node = new Node2D();
        node.position = new Vec2f(1.0f, 2.0f);
        node.translate(1.0f, 0.5f);
        Assertions.assertEquals(new Vec2f(2.0f, 2.5f), node.position);
        node.translate(new Vec2f(-0.5f, 2.0f));
        Assertions.assertEquals(new Vec2f(1.5f, 4.5f), node.position);
    }

    @Test
    public void testRotate() {
        var node = new Node2D();
        node.rotation = Math.PI;
        node.rotate(Math.PI * 0.5);
        Assertions.assertEquals(Math.PI * 1.5, node.rotation, 1e-6);
    }

    @Test
    public void testRotateDegrees() {
        var node = new Node2D();
        node.rotation = Math.PI * 0.5;
        node.rotateDegrees(90.0);
        Assertions.assertEquals(Math.PI, node.rotation, 1e-6);
    }

    @Test
    public void testRotationDegrees() {
        var node = new Node2D();
        node.rotation = Math.PI * 0.25;
        Assertions.assertEquals(45.0, node.rotationDegrees(), 1e-6);
    }

    @Test
    public void testSetRotationDegrees() {
        var node = new Node2D();
        node.setRotationDegrees(45.0);
        Assertions.assertEquals(Math.PI * 0.25, node.rotation, 1e-6);
    }

    @Test
    public void testApplyScale() {
        var node = new Node2D();
        node.scale = new Vec2f(1.5f, 1.0f);
        node.applyScale(2.0f, 1.5f);
        Assertions.assertEquals(new Vec2f(3.0f, 1.5f), node.scale);
        node.applyScale(new Vec2f(2.0f, 0.5f));
        Assertions.assertEquals(new Vec2f(6.0f, 0.75f), node.scale);
    }

    @Test
    public void testLocalTransform() {
        var node = new Node2D();
        node.position = new Vec2f(2.0f, 1.0f);
        node.rotation = Math.PI * 0.5;
        node.scale = new Vec2f(1.1f, 1.1f);
        var expected = Mat2x3f.translation(2.0f, 1.0f)
            .multiply(Mat2x3f.rotation(Math.PI * 0.5), 0.0f, 0.0f, 1.0f)
            .multiply(Mat2x3f.scaling(1.1f, 1.1f), 0.0f, 0.0f, 1.0f);
        Assertions.assertEquals(expected, node.localTransform());
    }

    @Test
    public void testGlobalTransform() {
        var parent = new Node2D();
        parent.position = new Vec2f(2.0f, 1.0f);
        parent.rotation = Math.PI * 0.5;
        parent.scale = new Vec2f(1.1f, 1.1f);
        var child = new Node2D();
        child.position = new Vec2f(0.0f, 1.0f);
        child.rotation = Math.PI * 0.25;
        child.scale = new Vec2f(1.0f, 2.0f);
        parent.addChild(child);
        var expected = Mat2x3f.translation(0.9f, 1.0f)
            .multiply(Mat2x3f.rotation(Math.PI * 0.75), 0.0f, 0.0f, 1.0f)
            .multiply(Mat2x3f.scaling(1.1f, 2.2f), 0.0f, 0.0f, 1.0f);
        Assertions.assertEquals(expected, child.globalTransform());
    }

    @Test
    public void testGlobalPosition() {
        var parent = new Node2D();
        parent.position = new Vec2f(1.0f, 0.0f);
        parent.rotation = Math.PI * 0.5;
        var child = new Node2D();
        child.position = new Vec2f(1.0f, 0.0f);
        parent.addChild(child);
        Assertions.assertEquals(new Vec2f(1.0f, 1.0f), child.globalPosition());
    }

    @Test
    public void testSetGlobalPosition() {
        var parent = new Node2D();
        parent.position = new Vec2f(1.0f, 0.0f);
        var child = new Node2D();
        parent.addChild(child);
        child.setGlobalPosition(1.0f, 1.0f);
        Assertions.assertEquals(new Vec2f(0.0f, 1.0f), child.position);
        child.setGlobalPosition(Vec2f.Zero());
        Assertions.assertEquals(new Vec2f(-1.0f, 0.0f), child.position);
    }

    @Test
    public void testGlobalRotation() {
        var parent = new Node2D();
        parent.rotation = Math.PI * 0.5;
        var child = new Node2D();
        child.position = new Vec2f(1.0f, 1.0f);
        child.rotation = Math.PI * 0.25;
        parent.addChild(child);
        Assertions.assertEquals(Math.PI * 0.75, child.globalRotation(), 1e-6);
    }

    @Test
    public void testGlobalRotationDegrees() {
        var parent = new Node2D();
        parent.rotation = Math.PI * 0.5;
        var child = new Node2D();
        child.position = new Vec2f(1.0f, 1.0f);
        child.rotation = Math.PI * 0.25;
        parent.addChild(child);
        Assertions.assertEquals(135.0, child.globalRotationDegrees(), 1e-6);
    }

    @Test
    public void testSetGlobalRotation() {
        var parent = new Node2D();
        parent.rotation = Math.PI * 0.5;
        var child = new Node2D();
        parent.addChild(child);
        child.setGlobalRotation(Math.PI * 0.75);
        Assertions.assertEquals(Math.PI * 0.25, child.rotation, 1e-6);
    }

    @Test
    public void testSetGlobalRotationDegrees() {
        var parent = new Node2D();
        parent.rotation = Math.PI * 0.5;
        var child = new Node2D();
        parent.addChild(child);
        child.setGlobalRotationDegrees(135.0);
        Assertions.assertEquals(Math.PI * 0.25, child.rotation, 1e-6);
    }

    @Test
    public void testGlobalScale() {
        var parent = new Node2D();
        parent.scale = new Vec2f(2.0f, 1.5f);
        var child = new Node2D();
        child.scale = new Vec2f(1.5f, 3.0f);
        parent.addChild(child);
        Assertions.assertEquals(new Vec2f(3.0f, 4.5f), child.globalScale());
    }

    @Test
    public void testSetGlobalScale() {
        var parent = new Node2D();
        parent.scale = new Vec2f(2.0f, 1.5f);
        var child = new Node2D();
        parent.addChild(child);
        child.setGlobalScale(3.0f, 4.5f);
        Assertions.assertEquals(new Vec2f(1.5f, 3.0f), child.scale);
    }

    @Test
    public void testSetLocalTransform() {
        var transform = Mat2x3f.translation(2.0f, 1.0f)
            .multiply(Mat2x3f.rotation(Math.PI * 0.25), 0.0f, 0.0f, 1.0f)
            .multiply(Mat2x3f.scaling(1.1f, 1.1f), 0.0f, 0.0f, 1.0f);
        var node = new Node2D();
        node.setLocalTransform(transform);
        Assertions.assertEquals(new Vec2f(2.0f, 1.0f), node.position);
        Assertions.assertEquals(Math.PI * 0.25, node.rotation, 1e-6);
        Assertions.assertEquals(new Vec2f(1.1f, 1.1f), node.scale);
    }

    @Test
    public void testSetGlobalTransform() {
        var transform = Mat2x3f.translation(2.0f, 1.0f)
            .multiply(Mat2x3f.rotation(Math.PI * 0.25), 0.0f, 0.0f, 1.0f)
            .multiply(Mat2x3f.scaling(1.1f, 1.1f), 0.0f, 0.0f, 1.0f);
        var parent = new Node2D();
        parent.position = new Vec2f(3.0f, -1.0f);
        parent.rotation = Math.PI * 0.5;
        parent.scale = new Vec2f(2.0f, 2.0f);
        var child = new Node2D();
        parent.addChild(child);
        child.setGlobalTransform(transform);
        Assertions.assertEquals(new Vec2f(2.0f, 1.0f), child.globalPosition());
        Assertions.assertEquals(Math.PI * 0.25, child.globalRotation(), 1e-6);
        Assertions.assertEquals(new Vec2f(1.1f, 1.1f), child.globalScale());
    }

    @Test
    public void testReparent() {
        var root = new Node2D();
        root.position = new Vec2f(2.0f, 1.0f);
        var p1 = new Node2D();
        p1.position = new Vec2f(0.1f, 0.1f);
        root.addChild(p1);
        var p2 = new Node2D();
        p2.position = new Vec2f(1.0f, 3.0f);
        root.addChild(p2);
        var node = new Node2D();
        p1.addChild(node);
        Assertions.assertEquals(p1, node.getParent());
        var position = node.globalPosition();
        node.reparent(p2);
        Assertions.assertEquals(p2, node.getParent());
        // TODO: This should be more accurate
        Assertions.assertTrue(node.globalPosition().equalsApprox(position));
    }

    @Test
    public void testToLocal() {
        var parent = new Node2D();
        parent.position = new Vec2f(2.0f, 1.0f);
        var child = new Node2D();
        child.position = new Vec2f(3.0f, -0.5f);
        parent.addChild(child);
        Assertions.assertEquals(new Vec2f(1.0f, 1.0f), child.toLocal(new Vec2f(6.0f, 1.5f)));
        Assertions.assertEquals(new Vec2f(1.0f, 1.0f), child.toLocal(6.0f, 1.5f));
    }

    @Test
    public void testToGlobal() {
        var parent = new Node2D();
        parent.position = new Vec2f(2.0f, 1.0f);
        var child = new Node2D();
        child.position = new Vec2f(3.0f, -0.5f);
        parent.addChild(child);
        Assertions.assertEquals(new Vec2f(6.0f, 1.5f), child.toGlobal(new Vec2f(1.0f, 1.0f)));
        Assertions.assertEquals(new Vec2f(6.0f, 1.5f), child.toGlobal(1.0f, 1.0f));
    }

    @Test
    public void testAngleTo() {
        var node = new Node2D();
        node.position = new Vec2f(2.0f, 1.0f);
        Assertions.assertEquals(Math.PI * 0.25, node.angleTo(new Vec2f(3.0f, 2.0f)), 1e-6);
        Assertions.assertEquals(Math.PI * 0.25, node.angleTo(3.0f, 2.0f), 1e-6);
    }

    @Test
    public void testLookAt() {
        var node = new Node2D();
        node.position = new Vec2f(2.0f, 1.0f);
        node.rotation = 0.1;
        node.lookAt(new Vec2f(3.0f, 2.0f));
        Assertions.assertEquals(Math.PI * 0.25, node.rotation, 1e-6);
        node.lookAt(2.0f, 5.0f);
        Assertions.assertEquals(Math.PI * 0.5, node.rotation, 1e-6);
    }
}
