package io.github.ardentengine.core.scene;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestNode {

    @Test
    public void testAddChild() {
        var parent = new Node();
        var child = new Node();
        parent.addChild(child);
        Assertions.assertEquals(parent, child.getParent());
        parent.addChild(null); // TODO: Assert that an error is logged
        parent.addChild(child); // TODO: Assert that an error is logged
    }

    @Test
    public void testAddChildAtIndex() {
        var parent = new Node();
        var child0 = new Node();
        parent.addChild(child0);
        var child2 = new Node();
        parent.addChild(child2);
        var child3 = new Node();
        parent.addChild(child3);
        Assertions.assertEquals(List.of(child0, child2, child3), parent.getChildren());
        var child1 = new Node();
        parent.addChild(child1, 1);
        Assertions.assertEquals(List.of(child0, child1, child2, child3), parent.getChildren());
    }

    @Test
    public void testGetChildCount() {
        var parent = new Node();
        parent.addChild(new Node());
        parent.addChild(new Node());
        parent.addChild(new Node());
        Assertions.assertEquals(3, parent.getChildCount());
        parent.addChild(new Node());
        parent.addChild(new Node());
        Assertions.assertEquals(5, parent.getChildCount());
    }

    @Test
    public void testGetChildAtIndex() {
        var parent = new Node();
        var child0 = new Node();
        var child1 = new Node();
        var child2 = new Node();
        var child3 = new Node();
        parent.addChild(child0);
        parent.addChild(child1);
        parent.addChild(child2);
        parent.addChild(child3);
        Assertions.assertEquals(child0, parent.getChild(0));
        Assertions.assertEquals(child1, parent.getChild(1));
        Assertions.assertEquals(child2, parent.getChild(2));
        Assertions.assertEquals(child3, parent.getChild(3));
        Assertions.assertNull(parent.getChild(4)); // TODO: Assert that an error is logged
        Assertions.assertNull(parent.getChildOrNull(5));
    }

    private static class MockNode1 extends Node {

    }

    private static class MockNode2 extends Node {

    }

    private static class MockNode3 extends MockNode1 {

    }

    private static class MockNode4 extends Node {

    }

    @Test
    public void testGetChildOfType() {
        var parent = new Node();
        var child0 = new MockNode1();
        var child1 = new MockNode1();
        var child2 = new MockNode3();
        var child3 = new MockNode2();
        parent.addChild(child0);
        parent.addChild(child1);
        parent.addChild(child2);
        parent.addChild(child3);
        Assertions.assertEquals(child0, parent.getChild(MockNode1.class));
        Assertions.assertEquals(child3, parent.getChild(MockNode2.class));
        Assertions.assertEquals(child2, parent.getChild(MockNode3.class));
        Assertions.assertNull(parent.getChild(MockNode4.class)); // TODO: Assert that an error is logged
    }

    @Test
    public void testGetChildWithName() {
        var parent = new Node();
        parent.name = "Parent";
        var child0 = new Node();
        child0.name = "Josh";
        parent.addChild(child0);
        var child1 = new Node();
        child1.name = "Frank";
        parent.addChild(child1);
        var child2 = new Node();
        child2.name = "Aaron";
        parent.addChild(child2);
        Assertions.assertEquals(child0, parent.getChild("Josh"));
        Assertions.assertEquals(child1, parent.getChild("Frank"));
        Assertions.assertEquals(child2, parent.getChild("Aaron"));
        Assertions.assertNull(parent.getChild("I don't exist :)")); // TODO: Assert that an error is logged
    }

    @Test
    public void testGetChildOfTypeWithName() {
        var parent = new Node();
        parent.name = "Parent";
        var child1 = new MockNode1();
        child1.name = "Josh";
        parent.addChild(child1);
        var child2 = new MockNode2();
        child2.name = "Frank";
        parent.addChild(child2);
        var child3 = new MockNode1();
        child3.name = "Aaron";
        parent.addChild(child3);
        Assertions.assertEquals(child1, parent.getChild(MockNode1.class, "Josh"));
        Assertions.assertEquals(child2, parent.getChild(MockNode2.class, "Frank"));
        Assertions.assertEquals(child3, parent.getChild(MockNode1.class, "Aaron"));
        Assertions.assertNull(parent.getChild(MockNode2.class, "Aaron")); // TODO: Assert that an error is logged
    }

    @Test
    public void testRemoveChild() {
        var parent = new Node();
        var child1 = new Node();
        parent.addChild(child1);
        var child2 = new Node();
        parent.addChild(child2);
        Assertions.assertEquals(parent, child1.getParent());
        Assertions.assertEquals(parent, child2.getParent());
        parent.removeChild(child1);
        Assertions.assertNull(child1.getParent());
        parent.removeChild(new Node()); // TODO: Assert that an error is logged
    }

    @Test
    public void testRemoveChildAtIndex() {
        var parent = new Node();
        var child1 = new Node();
        parent.addChild(child1);
        var child2 = new Node();
        parent.addChild(child2);
        Assertions.assertEquals(parent, child1.getParent());
        Assertions.assertEquals(parent, child2.getParent());
        parent.removeChild(0);
        Assertions.assertNull(child1.getParent());
        parent.removeChild(1); // TODO: Assert that an error is logged
    }

    @Test
    public void testRemoveFromParent() {
        var parent = new Node();
        var child = new Node();
        parent.addChild(child);
        Assertions.assertEquals(parent, child.getParent());
        child.removeFromParent();
        Assertions.assertNull(child.getParent());
    }
}
