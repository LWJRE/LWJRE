package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.math.MathUtils;
import io.github.ardentengine.core.math.Matrix2x3;
import io.github.ardentengine.core.math.Vector2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestNode2D {

    @Test
    public void testPosition() {
        var node = new Node2D();
        node.setPosition(2.0f, 3.0f);
        var position = new Vector2(2.0f, 3.0f);
        Assertions.assertEquals(position, node.position());
        position = new Vector2(4.0f, 5.0f);
        node.setPosition(position);
        Assertions.assertEquals(position, node.position());
        Assertions.assertThrows(NullPointerException.class, () -> node.setPosition(null));
    }

    @Test
    public void testTranslate() {
        var node = new Node2D();
        node.setPosition(1.0f, 2.0f);
        node.translate(1.0f, 0.5f);
        Assertions.assertEquals(new Vector2(2.0f, 2.5f), node.position());
        node.translate(new Vector2(-0.5f, 2.0f));
        Assertions.assertEquals(new Vector2(1.5f, 4.5f), node.position());
    }

    @Test
    public void testRotation() {
        var node = new Node2D();
        node.setRotation(Math.PI / 6.0);
        Assertions.assertEquals(Math.PI / 6.0, node.rotation(), MathUtils.EPSILON);
    }

    @Test
    public void testRotationDegrees() {
        var node = new Node2D();
        node.setRotationDegrees(45.0);
        Assertions.assertEquals(45.0, node.rotationDegrees(), MathUtils.EPSILON);
    }

    @Test
    public void testRotate() {
        var node = new Node2D();
        node.setRotation(Math.PI);
        node.rotate(Math.PI / 2.0);
        Assertions.assertEquals(Math.PI * 1.5, node.rotation(), MathUtils.EPSILON);
    }

    @Test
    public void testRotateDegrees() {
        var node = new Node2D();
        node.setRotation(Math.PI / 2.0);
        node.rotateDegrees(90.0f);
        Assertions.assertEquals(Math.PI, node.rotation(), MathUtils.EPSILON);
    }

    @Test
    public void testScale() {
        var node = new Node2D();
        node.setScale(2.0f, 3.0f);
        var scale = new Vector2(2.0f, 3.0f);
        Assertions.assertEquals(scale, node.scale());
        scale = new Vector2(4.0f, 5.0f);
        node.setScale(scale);
        Assertions.assertEquals(scale, node.scale());
        Assertions.assertThrows(NullPointerException.class, () -> node.setScale(null));
    }

    @Test
    public void testApplyScale() {
        var node = new Node2D();
        node.setScale(1.5f, 1.0f);
        node.applyScale(2.0f, 1.5f);
        Assertions.assertEquals(new Vector2(3.0f, 1.5f), node.scale());
        node.applyScale(new Vector2(2.0f, 0.5f));
        Assertions.assertEquals(new Vector2(6.0f, 0.75f), node.scale());
    }

    // TODO: Z index

    @Test
    public void testGetLocalTransform() {
        var node = new Node2D();
        node.setPosition(2.0f, 1.0f);
        node.setRotation(Math.PI / 2.0);
        node.setScale(1.1f, 1.1f);
        var expected = Matrix2x3.translation(2.0f, 1.0f)
            .multiply(Matrix2x3.rotation(Math.PI / 2.0), 0.0f, 0.0f, 1.0f)
            .multiply(Matrix2x3.scaling(1.1f, 1.1f), 0.0f, 0.0f, 1.0f);
        Assertions.assertEquals(expected, node.localTransform());
    }

    @Test
    public void testGetGlobalTransform() {
        var parent = new Node2D();
        parent.setPosition(2.0f, 1.0f);
        parent.setRotation(Math.PI / 2.0);
        parent.setScale(1.1f, 1.1f);
        var child = new Node2D();
        child.setPosition(0.0f, 1.0f);
        child.setRotation(Math.PI / 4.0);
        child.setScale(1.0f, 2.0f);
        parent.addChild(child);
        var expected = Matrix2x3.translation(0.9f, 1.0f)
            .multiply(Matrix2x3.rotation(Math.PI * 0.75), 0.0f, 0.0f, 1.0f)
            .multiply(Matrix2x3.scaling(1.1f, 2.2f), 0.0f, 0.0f, 1.0f);
        Assertions.assertEquals(expected, child.globalTransform());
    }

    @Test
    public void testGetGlobalPosition() {
        var parent = new Node2D();
        parent.setPosition(1.0f, 0.0f);
        parent.setRotation(Math.PI / 2.0);
        var child = new Node2D();
        child.setPosition(1.0f, 0.0f);
        parent.addChild(child);
        Assertions.assertEquals(new Vector2(1.0f, 1.0f), child.globalPosition());
    }

    @Test
    public void testSetGlobalPosition() {
        var parent = new Node2D();
        parent.setPosition(1.0f, 0.0f);
        var child = new Node2D();
        parent.addChild(child);
        child.setGlobalPosition(1.0f, 1.0f);
        Assertions.assertEquals(new Vector2(0.0f, 1.0f), child.position());
        child.setGlobalPosition(Vector2.ZERO);
        Assertions.assertEquals(new Vector2(-1.0f, 0.0f), child.position());
    }

    @Test
    public void testGetGlobalRotation() {
        var parent = new Node2D();
        parent.setRotation(Math.PI / 2.0);
        var child = new Node2D();
        child.setPosition(1.0f, 1.0f);
        child.setRotation(Math.PI / 4.0);
        parent.addChild(child);
        Assertions.assertEquals(3.0 * Math.PI / 4.0, child.globalRotation(), 1e-6);
    }

    @Test
    public void testGetGlobalRotationDegrees() {
        var parent = new Node2D();
        parent.setRotation(Math.PI / 2.0);
        var child = new Node2D();
        child.setPosition(1.0f, 1.0f);
        child.setRotation(Math.PI / 4.0);
        parent.addChild(child);
        Assertions.assertEquals(135.0f, child.globalRotationDegrees(), 1e-6f);
    }

    @Test
    public void testSetGlobalRotation() {
        var parent = new Node2D();
        parent.setRotation(Math.PI / 2.0);
        var child = new Node2D();
        parent.addChild(child);
        child.setGlobalRotation(3.0 * Math.PI / 4.0);
        Assertions.assertEquals(Math.PI / 4.0, child.rotation(), 1e-6);
    }

    @Test
    public void testSetGlobalRotationDegrees() {
        var parent = new Node2D();
        parent.setRotation(Math.PI / 2.0);
        var child = new Node2D();
        parent.addChild(child);
        child.setGlobalRotationDegrees(135.0f);
        Assertions.assertEquals(Math.PI / 4.0, child.rotation(), 1e-6);
    }

    @Test
    public void testGetGlobalScale() {
        var parent = new Node2D();
        parent.setScale(2.0f, 1.5f);
        var child = new Node2D();
        child.setScale(1.5f, 3.0f);
        parent.addChild(child);
        Assertions.assertEquals(new Vector2(3.0f, 4.5f), child.globalScale());
    }

    @Test
    public void testSetGlobalScale() {
        var parent = new Node2D();
        parent.setScale(2.0f, 1.5f);
        var child = new Node2D();
        parent.addChild(child);
        child.setGlobalScale(3.0f, 4.5f);
        Assertions.assertEquals(new Vector2(1.5f, 3.0f), child.scale());
        child.setGlobalScale(Vector2.ONE);
        Assertions.assertEquals(new Vector2(0.5f, 1.0f / 1.5f), child.scale());
    }

    @Test
    public void testSetLocalTransform() {
        var transform = Matrix2x3.translation(2.0f, 1.0f)
            .multiply(Matrix2x3.rotation(Math.PI / 4.0), 0.0f, 0.0f, 1.0f)
            .multiply(Matrix2x3.scaling(1.1f, 1.1f), 0.0f, 0.0f, 1.0f);
        var node = new Node2D();
        node.setLocalTransform(transform);
        Assertions.assertEquals(new Vector2(2.0f, 1.0f), node.position());
        Assertions.assertEquals(Math.PI / 4.0, node.rotation(), MathUtils.EPSILON);
        Assertions.assertEquals(new Vector2(1.1f, 1.1f), node.scale());
    }

    @Test
    public void testSetGlobalTransform() {
        var transform = Matrix2x3.translation(2.0f, 1.0f)
            .multiply(Matrix2x3.rotation(Math.PI / 4.0), 0.0f, 0.0f, 1.0f)
            .multiply(Matrix2x3.scaling(1.1f, 1.1f), 0.0f, 0.0f, 1.0f);
        var parent = new Node2D();
        parent.setPosition(3.0f, -1.0f);
        parent.setRotation(Math.PI / 2.0);
        parent.setScale(2.0f, 2.0f);
        var child = new Node2D();
        parent.addChild(child);
        child.setGlobalTransform(transform);
        Assertions.assertEquals(new Vector2(2.0f, 1.0f), child.globalPosition());
        Assertions.assertEquals(Math.PI / 4.0, child.globalRotation(), MathUtils.EPSILON);
        Assertions.assertEquals(new Vector2(1.1f, 1.1f), child.globalScale());
    }

    @Test
    public void testSetParentKeepTransform() {
        var root = new Node2D();
        root.setPosition(2.0f, 1.0f);
        var p1 = new Node2D();
        p1.setPosition(0.1f, 0.1f);
        root.addChild(p1);
        var p2 = new Node2D();
        p2.setPosition(1.0f, 3.0f);
        root.addChild(p2);
        var node = new Node2D();
        p1.addChild(node);
        Assertions.assertEquals(p1, node.parent());
        var transform = node.globalTransform();
        node.setParent(p2, true);
        Assertions.assertEquals(p2, node.parent());
        // FIXME: This should be more stable
        Assertions.assertEquals(transform, node.globalTransform());
//        Assertions.assertTrue(transform.equalsApprox(node.globalTransform()));
    }

    @Test
    public void testToLocal() {
        var parent = new Node2D();
        parent.setPosition(2.0f, 1.0f);
        var child = new Node2D();
        child.setPosition(3.0f, -0.5f);
        parent.addChild(child);
        Assertions.assertEquals(new Vector2(1.0f, 1.0f), child.toLocal(new Vector2(6.0f, 1.5f)));
        Assertions.assertEquals(new Vector2(1.0f, 1.0f), child.toLocal(6.0f, 1.5f));
    }

    @Test
    public void testToGlobal() {
        var parent = new Node2D();
        parent.setPosition(2.0f, 1.0f);
        var child = new Node2D();
        child.setPosition(3.0f, -0.5f);
        parent.addChild(child);
        Assertions.assertEquals(new Vector2(6.0f, 1.5f), child.toGlobal(new Vector2(1.0f, 1.0f)));
        Assertions.assertEquals(new Vector2(6.0f, 1.5f), child.toGlobal(1.0f, 1.0f));
    }

    @Test
    public void testAngleTo() {
        var node = new Node2D();
        node.setPosition(2.0f, 1.0f);
        Assertions.assertEquals(Math.PI / 4.0, node.angleTo(new Vector2(3.0f, 2.0f)), 1e-6);
        Assertions.assertEquals(Math.PI / 4.0, node.angleTo(3.0f, 2.0f), 1e-6);
    }

    @Test
    public void testLookAt() {
        var node = new Node2D();
        node.setPosition(2.0f, 1.0f);
        node.setRotation(0.1);
        node.lookAt(new Vector2(3.0f, 2.0f));
        Assertions.assertEquals(Math.PI / 4.0, node.rotation(), 1e-6);
        node.lookAt(2.0f, 5.0f);
        Assertions.assertEquals(Math.PI / 2.0, node.rotation(), 1e-6);
    }
}