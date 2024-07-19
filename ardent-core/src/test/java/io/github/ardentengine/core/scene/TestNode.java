package io.github.ardentengine.core.scene;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestNode {

    @Test
    public void testEnterTree() {
        var sceneTree = new SceneTree();
        var node = new Node();
        Assertions.assertFalse(node.isInsideTree());
        node.enterTree(sceneTree);
        Assertions.assertTrue(node.isInsideTree());
        Assertions.assertEquals(sceneTree, node.getSceneTree());
    }

    @Test
    public void testExitTree() {
        var sceneTree = new SceneTree();
        var node = new Node();
        node.enterTree(sceneTree);
        Assertions.assertTrue(node.isInsideTree());
        node.exitTree();
        Assertions.assertFalse(node.isInsideTree());
    }

    @Test
    public void testSetParent() {
        var parent = new Node();
        var child = new Node();
        Assertions.assertNull(child.getParent());
        Assertions.assertFalse(parent.getChildren().contains(child));
        child.setParent(parent);
        Assertions.assertEquals(parent, child.getParent());
        Assertions.assertTrue(parent.getChildren().contains(child));
    }

    @Test
    public void testRemoveFromParent() {
        var parent = new Node();
        var child = new Node();
        child.setParent(parent);
        Assertions.assertEquals(parent, child.getParent());
        Assertions.assertTrue(parent.getChildren().contains(child));
        child.removeFromParent();
        Assertions.assertNull(child.getParent());
        Assertions.assertFalse(parent.getChildren().contains(child));
    }

    @Test
    public void testGetChildren() {
        var parent = new Node();
        var child1 = new Node();
        var child2 = new Node();
        var child3 = new Node();
        var child4 = new Node();
        var array = new Node[] {child1, child2, child3, child4};
        child1.setParent(parent);
        child2.setParent(parent);
        child3.setParent(parent);
        child4.setParent(parent);
        Assertions.assertArrayEquals(array, parent.getChildren().toArray(Node[]::new));
    }

    @Test
    public void testGetChildCount() {
        var parent = new Node();
        var child1 = new Node();
        var child2 = new Node();
        var child3 = new Node();
        var child4 = new Node();
        Assertions.assertEquals(0, parent.getChildCount());
        child1.setParent(parent);
        Assertions.assertEquals(1, parent.getChildCount());
        child2.setParent(parent);
        Assertions.assertEquals(2, parent.getChildCount());
        child3.setParent(parent);
        Assertions.assertEquals(3, parent.getChildCount());
        child4.setParent(parent);
        Assertions.assertEquals(4, parent.getChildCount());
    }

    @Test
    public void testGetChildAtIndex() {
        var parent = new Node();
        var child1 = new Node();
        var child2 = new Node();
        var child3 = new Node();
        child1.setParent(parent);
        child2.setParent(parent);
        child3.setParent(parent);
        Assertions.assertEquals(child1, parent.getChild(0));
        Assertions.assertEquals(child2, parent.getChild(1));
        Assertions.assertEquals(child3, parent.getChild(2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> parent.getChild(3));
    }

    private static class Node1 extends Node {

    }

    private static class Node2 extends Node {

    }

    private static class Node3 extends Node {

    }

    @Test
    public void testGetChildOfType() {
        var parent = new Node();
        var child1 = new Node1();
        var child2 = new Node2();
        child1.setParent(parent);
        child2.setParent(parent);
        Assertions.assertEquals(child2, parent.getChild(Node2.class));
        Assertions.assertNull(parent.getChild(Node3.class));
    }

    @Test
    public void testGetChildrenOfType() {
        var parent = new Node();
        var child1 = new Node1();
        var child2 = new Node2();
        var child3 = new Node1();
        var child4 = new Node3();
        var child5 = new Node2();
        child1.setParent(parent);
        child2.setParent(parent);
        child3.setParent(parent);
        child4.setParent(parent);
        child5.setParent(parent);
        var array = new Node[] {child2, child5};
        Assertions.assertArrayEquals(array, parent.getChildren(Node2.class).toArray(Node[]::new));
    }

    @Test
    public void testGetChildWithName() {
        var parent = new Node();
        var child1 = new Node();
        child1.name = "FirstNode";
        child1.setParent(parent);
        var child2 = new Node();
        child2.name = "SecondNode";
        child2.setParent(parent);
        var child3 = new Node();
        child3.name = "ThirdNode";
        child3.setParent(parent);
        Assertions.assertEquals(child2, parent.getChild("SecondNode"));
        Assertions.assertNull(parent.getChild("NonexistentNode"));
    }

    // TODO: Finish tests
}