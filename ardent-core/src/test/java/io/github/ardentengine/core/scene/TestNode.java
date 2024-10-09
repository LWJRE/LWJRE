package io.github.ardentengine.core.scene;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

public class TestNode {

    @Test
    public void testIsInsideTree() {
        var sceneTree = new SceneTree();
        var node = new Node();
        Assertions.assertFalse(node.isInsideTree());
        node.enterTree(sceneTree);
        Assertions.assertTrue(node.isInsideTree());
    }

    @Test
    public void testGetSceneTree() {
        var sceneTree = new SceneTree();
        var node = new Node();
        Assertions.assertNull(node.sceneTree());
        node.enterTree(sceneTree);
        Assertions.assertEquals(sceneTree, node.sceneTree());
    }

    @Test
    public void testAddChild() {
        var parent = new Node();
        var child = new Node();
        parent.addChild(child);
        Assertions.assertEquals(parent, child.parent());
        Assertions.assertTrue(parent.children().contains(child));
    }

    @Test
    public void testAddChildSameNode() {
        var node = new Node();
        Assertions.assertThrows(IllegalArgumentException.class, () -> node.addChild(node));
    }

    @Test
    public void testAddChildAlreadyHasParent() {
        var parent1 = new Node();
        var parent2 = new Node();
        var child1 = new Node();
        var child2 = new Node();
        parent1.addChild(child1);
        parent2.addChild(child2);
        Assertions.assertThrows(IllegalArgumentException.class, () -> parent1.addChild(child2));
    }

    @Test
    public void testAddChildRoot() {
        var root = new Node();
        var parent = new Node();
        var child = new Node();
        root.addChild(parent);
        parent.addChild(child);
        root.enterTree(new SceneTree());
        Assertions.assertNull(root.parent());
        Assertions.assertThrows(IllegalArgumentException.class, () -> parent.addChild(root));
    }

    @Test
    public void testAddChildAncestor() {
        var first = new Node();
        var second = new Node();
        var third = new Node();
        first.addChild(second);
        second.addChild(third);
        Assertions.assertThrows(IllegalArgumentException.class, () -> third.addChild(first));
    }

    @Test
    public void testGetChildren() {
        var parent = new Node();
        var first = new Node();
        var second = new Node();
        var third = new Node();
        parent.addChild(first);
        parent.addChild(second);
        parent.addChild(third);
        Assertions.assertIterableEquals(List.of(first, second, third), parent.children());
    }

    @Test
    public void testGetChildCount() {
        var parent = new Node();
        Assertions.assertEquals(0, parent.childCount());
        var child1 = new Node();
        var child2 = new Node();
        parent.addChild(child1);
        parent.addChild(child2);
        Assertions.assertEquals(2, parent.childCount());
        child1.removeFromTree();
        Assertions.assertEquals(1, parent.childCount());
    }

    @Test
    public void testAddChildAtIndex() {
        var parent = new Node();
        var child1 = new Node();
        var child2 = new Node();
        var child3 = new Node();
        var child4 = new Node();
        parent.addChild(child2);
        parent.addChild(child1, 0);
        parent.addChild(child4, 2);
        parent.addChild(child3, -2);
        Assertions.assertEquals(child1, parent.getChild(0));
        Assertions.assertEquals(child2, parent.getChild(1));
        Assertions.assertEquals(child3, parent.getChild(2));
        Assertions.assertEquals(child4, parent.getChild(3));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> parent.addChild(new Node(), 5));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> parent.addChild(new Node(), -6));
    }

    @Test
    public void testRemoveFromTree() {
        var sceneTree = new SceneTree();
        var parent = new Node();
        var child = new Node();
        parent.addChild(child);
        parent.enterTree(sceneTree);
        Assertions.assertTrue(parent.isInsideTree());
        Assertions.assertTrue(child.isInsideTree());
        Assertions.assertEquals(parent, child.parent());
        child.removeFromTree();
        Assertions.assertTrue(parent.isInsideTree());
        Assertions.assertFalse(child.isInsideTree());
        Assertions.assertNull(child.parent());
    }

    @Test
    public void testSetParent() {
        var root = new Node();
        var parent1 = new Node();
        var parent2 = new Node();
        root.addChild(parent1);
        root.addChild(parent2);
        var child = new Node();
        child.setParent(parent1);
        Assertions.assertEquals(parent1, child.parent());
        Assertions.assertIterableEquals(List.of(child), parent1.children());
        Assertions.assertTrue(parent2.children().isEmpty());
        child.setParent(parent2);
        Assertions.assertEquals(parent2, child.parent());
        Assertions.assertIterableEquals(List.of(child), parent2.children());
        Assertions.assertTrue(parent1.children().isEmpty());
    }

    @Test
    public void testGetChildAtIndex() {
        var parent = new Node();
        var child1 = new Node();
        var child2 = new Node();
        var child3 = new Node();
        var child4 = new Node();
        parent.addChild(child1);
        parent.addChild(child2);
        parent.addChild(child3);
        parent.addChild(child4);
        Assertions.assertEquals(child1, parent.getChild(0));
        Assertions.assertEquals(child2, parent.getChild(1));
        Assertions.assertEquals(child3, parent.getChild(2));
        Assertions.assertEquals(child4, parent.getChild(3));
    }

    private static class TestNodeOne extends Node {

    }

    private static class TestNodeTwo extends Node {

    }

    @Test
    public void testGetChildByType() {
        var parent = new Node();
        var child1 = new TestNodeOne();
        var child2 = new TestNodeTwo();
        parent.addChild(child1);
        parent.addChild(child2);
        Assertions.assertEquals(child1, parent.getChild(TestNodeOne.class));
        Assertions.assertEquals(child2, parent.getChild(TestNodeTwo.class));
    }

    private static class TestNodeThree extends Node {

    }

    @Test
    public void testGetChildByTypeNoSuchChild() {
        var parent = new Node();
        var child1 = new TestNodeOne();
        var child2 = new TestNodeTwo();
        parent.addChild(child1);
        parent.addChild(child2);
        Assertions.assertNull(parent.getChildOrNull(TestNodeThree.class));
        Assertions.assertThrows(NoSuchElementException.class, () -> parent.getChild(TestNodeThree.class));
    }

    private static class TestNodeFour extends TestNodeTwo {

    }

    @Test
    public void testGetChildrenByType() {
        var parent = new Node();
        var child1 = new TestNodeOne();
        var child2 = new TestNodeTwo();
        var child3 = new TestNodeThree();
        var child4 = new TestNodeFour();
        parent.addChild(child1);
        parent.addChild(child2);
        parent.addChild(child3);
        parent.addChild(child4);
        Assertions.assertIterableEquals(List.of(child2, child4), parent.getChildren(TestNodeTwo.class));
    }

    @Test
    public void testSetName() {
        var node = new Node();
        node.setName("Josh");
        Assertions.assertEquals("Josh", node.name());
        Assertions.assertThrows(NullPointerException.class, () -> node.setName(null));
    }

    @Test
    public void testFindChild() {
        var parent = new Node();
        var child1 = new Node();
        var child2 = new Node();
        var child3 = new Node();
        child1.setName("Child1");
        child2.setName("Child2");
        child3.setName("Child3");
        parent.addChild(child1);
        parent.addChild(child2);
        parent.addChild(child3);
        Assertions.assertEquals(child1, parent.findChild(node -> node.name().equals("Child1")));
        Assertions.assertEquals(child2, parent.findChild(node -> node.name().equals("Child2")));
        Assertions.assertEquals(child3, parent.findChild(node -> node.name().equals("Child3")));
        Assertions.assertNull(parent.findChild(node -> node.name().equals("Josh")));
    }

    @Test
    public void testFindChildren() {
        var parent = new Node();
        var child1 = new Node();
        var child2 = new Node();
        var child3 = new Node();
        var child4 = new Node();
        var child5 = new Node();
        parent.addChild(child1);
        parent.addChild(child2);
        parent.addChild(child3);
        parent.addChild(child4);
        parent.addChild(child5);
        child2.setName("TestFindChildren");
        child3.setName("TestFindChildren");
        child5.setName("TestFindChildren");
        Assertions.assertIterableEquals(List.of(child2, child3, child5), parent.findChildren(node -> node.name().equals("TestFindChildren")));
        Assertions.assertTrue(parent.findChildren(node -> node.name().equals("Josh")).isEmpty());
    }

    @Test
    public void testGetSiblingIndex() {
        var parent = new Node();
        var child1 = new Node();
        var child2 = new Node();
        var child3 = new Node();
        var child4 = new Node();
        parent.addChild(child1);
        parent.addChild(child2);
        parent.addChild(child3);
        parent.addChild(child4);
        Assertions.assertEquals(0, child1.siblingIndex());
        Assertions.assertEquals(1, child2.siblingIndex());
        Assertions.assertEquals(2, child3.siblingIndex());
        Assertions.assertEquals(3, child4.siblingIndex());
        Assertions.assertEquals(-1, parent.siblingIndex());
    }

    @Test
    public void testSetSiblingIndex() {
        var parent = new Node();
        var child1 = new Node();
        var child2 = new Node();
        var child3 = new Node();
        parent.addChild(child1);
        parent.addChild(child2);
        parent.addChild(child3);
        Assertions.assertTrue(child1.setSiblingIndex(2));
        Assertions.assertIterableEquals(List.of(child2, child3, child1), parent.children());
        Assertions.assertEquals(2, child1.siblingIndex());
        Assertions.assertTrue(child3.setSiblingIndex(-1));
        Assertions.assertIterableEquals(List.of(child2, child1, child3), parent.children());
        Assertions.assertEquals(2, child3.siblingIndex());
    }

    @Test
    public void testSetSiblingIndexOutOfBounds() {
        var parent = new Node();
        var child1 = new Node();
        var child2 = new Node();
        var child3 = new Node();
        parent.addChild(child1);
        parent.addChild(child2);
        parent.addChild(child3);
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> child1.setSiblingIndex(3));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> child1.setSiblingIndex(-4));
    }

    @Test
    public void testSetSiblingIndexWithoutParent() {
        var node = new Node();
        Assertions.assertFalse(node.setSiblingIndex(2));
        Assertions.assertEquals(-1, node.siblingIndex());
    }

    @Test
    public void testFindNode() {
        var root = new Node();
        root.setName("Root");
        var node1 = new Node();
        node1.setName("Node1");
        var node2 = new Node();
        node2.setName("Node2");
        root.addChild(node1);
        root.addChild(node2);
        var node3 = new Node();
        node3.setName("Node3");
        var node4 = new Node();
        node4.setName("Node4");
        node1.addChild(node3);
        node1.addChild(node4);
        Assertions.assertEquals(node1, root.findNode(node -> node.name().equals("Node1")));
        Assertions.assertEquals(node2, root.findNode(node -> node.name().equals("Node2")));
        Assertions.assertEquals(node3, root.findNode(node -> node.name().equals("Node3")));
        Assertions.assertEquals(node4, root.findNode(node -> node.name().equals("Node4")));
        Assertions.assertNull(root.findNode(node -> node.name().equals("Node5")));
        Assertions.assertNull(node1.findNode(node -> node.name().equals("Node2")));
    }

    @Test
    public void testFindNodes() {
        var root = new Node();
        root.setName("Root");
        var node1 = new Node();
        node1.setName("Node1");
        var node2 = new Node();
        node2.setName("Target");
        root.addChild(node1);
        root.addChild(node2);
        var node3 = new Node();
        node3.setName("Node3");
        var node4 = new Node();
        node4.setName("Target");
        node1.addChild(node3);
        node1.addChild(node4);
        Assertions.assertIterableEquals(List.of(node4, node2), root.findNodes(node -> node.name().equals("Target")));
        Assertions.assertTrue(root.findNodes(node -> node.name().equals("Josh")).isEmpty());
    }

    @Test
    public void testToString() {
        var node = new Node();
        Assertions.assertTrue(node.toString().startsWith("Node@"));
        node.setName("Josh");
        Assertions.assertTrue(node.toString().startsWith("Josh@"));
    }
}