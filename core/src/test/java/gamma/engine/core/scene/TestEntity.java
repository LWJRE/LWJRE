package gamma.engine.core.scene;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class TestEntity {

	@Test
	public void testAddChild() {
		Entity parent = new Entity();
		Entity child = new Entity();
		parent.addChild(child);
		Assertions.assertEquals(Optional.of(child), parent.getChild("Entity0"));
		Assertions.assertEquals(parent, child.getParent());
	}

	@Test
	public void testChildAlreadyHasParent() {
		Entity parent = new Entity();
		Entity child = new Entity();
		parent.addChild(child);
		Assertions.assertThrows(IllegalStateException.class, () -> new Entity().addChild(child));
	}

	@Test
	public void testAddWithKey() {
		Entity parent = new Entity();
		Entity child = new Entity();
		parent.addChild("givenKey", child);
		Assertions.assertEquals(Optional.of(child), parent.getChild("givenKey"));
	}

	@Test
	public void testAddWithKeyAlreadyHasParent() {
		Entity parent = new Entity();
		Entity child = new Entity();
		parent.addChild("givenKey", child);
		Assertions.assertThrows(IllegalStateException.class, () -> new Entity().addChild("anotherKey", child));
	}

	@Test
	public void testAlreadyHasKey() {
		Entity parent = new Entity();
		Entity child1 = new Entity();
		Entity child2 = new Entity();
		parent.addChild("sameKey", child1);
		Assertions.assertThrows(IllegalStateException.class, () -> parent.addChild("sameKey", child2));
	}

	@Test
	public void testHasChildWithKey() {
		Entity parent = new Entity();
		Entity child = new Entity();
		parent.addChild("givenKey", child);
		Assertions.assertTrue(parent.hasChild("givenKey"));
		Assertions.assertFalse(parent.hasChild("notKey"));
	}

	@Test
	public void testHasChild() {
		Entity parent = new Entity();
		Entity child = new Entity();
		parent.addChild(child);
		Assertions.assertTrue(parent.hasChild(child));
		Assertions.assertFalse(parent.hasChild(new Entity()));
	}

	@Test
	public void testHasAnyParent() {
		Entity parent = new Entity();
		Entity child = new Entity();
		parent.addChild(child);
		Assertions.assertTrue(child.hasParent());
		Assertions.assertFalse(parent.hasParent());
	}

	@Test
	public void testHasParent() {
		Entity parent = new Entity();
		Entity child = new Entity();
		parent.addChild(child);
		Assertions.assertTrue(child.hasParent(parent));
		Assertions.assertFalse(child.hasParent(new Entity()));
		Assertions.assertFalse(parent.hasParent(new Entity()));
	}

	 @Test
	 public void testSetParent() {
		Entity parent1 = new Entity();
		Entity parent2 = new Entity();
		Entity child = new Entity();
		parent1.addChild(child);
		Assertions.assertTrue(child.hasParent(parent1));
		Assertions.assertFalse(child.hasParent(parent2));
		child.setParent(parent2);
		 Assertions.assertFalse(child.hasParent(parent1));
		 Assertions.assertTrue(child.hasParent(parent2));
	 }

	 @Test
	 public void testSetParentWithKey() {
		 Entity parent1 = new Entity();
		 Entity parent2 = new Entity();
		 Entity child = new Entity();
		 parent1.addChild("key", child);
		 Assertions.assertTrue(parent1.hasChild("key"));
		 child.setParent("newKey", parent2);
		 Assertions.assertFalse(parent1.hasChild("key"));
		 Assertions.assertTrue(parent2.hasChild("newKey"));
	 }

	@Test
	public void testGetChildren() {
		Entity parent = new Entity();
		Entity child1 = new Entity();
		Entity child2 = new Entity();
		parent.addChild(child1);
		parent.addChild(child2);
		List<Entity> children = parent.getChildren().toList();
		Assertions.assertTrue(children.contains(child1));
		Assertions.assertTrue(children.contains(child2));
	}

	@Test
	public void testChildCount() {
		Entity parent = new Entity();
		Entity child1 = new Entity();
		Entity child2 = new Entity();
		parent.addChild(child1);
		parent.addChild(child2);
		Assertions.assertEquals(2, parent.getChildCount());
		Assertions.assertEquals(0, child1.getChildCount());
	}

	private static class TestComponent extends Component {

	}

	private static class AnotherComponent extends Component {

	}

	@Test
	public void testAddComponent() {
		Entity entity = new Entity();
		TestComponent component = new TestComponent();
		entity.addComponent(component);
		Assertions.assertEquals(entity, component.entity);
	}

	@Test
	public void testAlreadyHasComponent() {
		Entity entity1 = new Entity();
		Entity entity2 = new Entity();
		TestComponent component = new TestComponent();
		entity1.addComponent(component);
		Assertions.assertThrows(IllegalStateException.class, () -> entity2.addComponent(component));
	}

	@Test
	public void testGetComponent() {
		Entity entity = new Entity();
		TestComponent component = new TestComponent();
		entity.addComponent(component);
		Assertions.assertEquals(Optional.of(component), entity.getComponent(TestComponent.class));
	}

	@Test
	public void testGetComponentEmptyOptional() {
		Entity entity = new Entity();
		TestComponent component = new TestComponent();
		entity.addComponent(component);
		Assertions.assertTrue(entity.getComponent(AnotherComponent.class).isEmpty());
	}

	@Test
	public void testGetComponents() {
		Entity entity = new Entity();
		TestComponent component1 = new TestComponent();
		AnotherComponent component2 = new AnotherComponent();
		entity.addComponent(component1);
		entity.addComponent(component2);
		List<Component> components = entity.getComponents().toList();
		Assertions.assertTrue(components.contains(component1));
		Assertions.assertTrue(components.contains(component2));
	}

	@Test
	public void testGetComponentInChildren() {
		Entity parent = new Entity();
		Entity child = new Entity();
		parent.addChild(child);
		TestComponent component = new TestComponent();
		child.addComponent(component);
		Assertions.assertEquals(Optional.of(component), parent.getComponentInChildren(TestComponent.class));
	}

	@Test
	public void testGetComponentsInChildren() {
		Entity parent = new Entity();
		Entity child = new Entity();
		parent.addChild(child);
		TestComponent component1 = new TestComponent();
		AnotherComponent component2 = new AnotherComponent();
		child.addComponent(component1);
		child.addComponent(component2);
		List<Component> components = parent.getComponentsInChildren().toList();
		Assertions.assertTrue(components.contains(component1));
		Assertions.assertTrue(components.contains(component2));
	}

	@Test
	public void testGetComponentsOfTypeInChildren() {
		Entity parent = new Entity();
		Entity child1 = new Entity();
		Entity child2 = new Entity();
		parent.addChild(child1);
		parent.addChild(child2);
		TestComponent component1 = new TestComponent();
		AnotherComponent component2 = new AnotherComponent();
		TestComponent component3 = new TestComponent();
		child1.addComponent(component1);
		child1.addComponent(component2);
		child2.addComponent(component3);
		List<TestComponent> components = parent.getComponentsInChildren(TestComponent.class).toList();
		Assertions.assertEquals(2, components.size());
		Assertions.assertTrue(components.contains(component1));
		Assertions.assertTrue(components.contains(component3));
	}

	@Test
	public void testGetComponentInParent() {
		Entity parent = new Entity();
		Entity child = new Entity();
		parent.addChild(child);
		TestComponent component = new TestComponent();
		parent.addComponent(component);
		Assertions.assertEquals(Optional.of(component), child.getComponentInParent(TestComponent.class));
	}

	@Test
	public void testGetComponentsInParent() {
		Entity parent = new Entity();
		Entity child = new Entity();
		parent.addChild(child);
		TestComponent component1 = new TestComponent();
		AnotherComponent component2 = new AnotherComponent();
		parent.addComponent(component1);
		parent.addComponent(component2);
		List<Component> components = child.getComponentsInParent().toList();
		Assertions.assertTrue(components.contains(component1));
		Assertions.assertTrue(components.contains(component2));
	}

	@Test
	public void testRemoveComponentOfType() {
		Entity entity = new Entity();
		TestComponent component = new TestComponent();
		entity.addComponent(component);
		Assertions.assertTrue(entity.removeComponent(TestComponent.class));
		entity.process(0.0f);
		Assertions.assertNull(component.entity);
	}

	@Test
	public void testRemoveComponentOfTypeThatEntityDoesNotHave() {
		Entity entity = new Entity();
		TestComponent component = new TestComponent();
		entity.addComponent(component);
		Assertions.assertFalse(entity.removeComponent(AnotherComponent.class));
		entity.process(0.0f);
		Assertions.assertNotNull(component.entity);
	}

	@Test
	public void testRemoveComponent() {
		Entity entity = new Entity();
		TestComponent component = new TestComponent();
		entity.addComponent(component);
		Assertions.assertTrue(entity.removeComponent(component));
		entity.process(0.0f);
		Assertions.assertNull(component.entity);
	}

	@Test
	public void testRemoveComponentDoesNotBelongToEntity() {
		Entity entity = new Entity();
		TestComponent component = new TestComponent();
		Assertions.assertFalse(entity.removeComponent(component));
	}

	@Test
	public void testRemoveFromScene() {
		Entity entity = new Entity();
		Entity child1 = new Entity();
		Entity child2 = new Entity();
		TestComponent component = new TestComponent();
		TestComponent component1 = new TestComponent();
		TestComponent component2 = new TestComponent();
		entity.addChild(child1);
		entity.addChild(child2);
		entity.addComponent(component);
		child1.addComponent(component1);
		child2.addComponent(component2);
		Assertions.assertFalse(entity.getComponent(TestComponent.class).isEmpty());
		Assertions.assertFalse(child1.getComponent(TestComponent.class).isEmpty());
		Assertions.assertFalse(child2.getComponent(TestComponent.class).isEmpty());
		Assertions.assertNotNull(child1.getParent());
		Assertions.assertNotNull(child2.getParent());
		entity.removeFromScene();
		entity.process(0.0f);
		Assertions.assertTrue(entity.getComponent(TestComponent.class).isEmpty());
		Assertions.assertTrue(child1.getComponent(TestComponent.class).isEmpty());
		Assertions.assertTrue(child2.getComponent(TestComponent.class).isEmpty());
		Assertions.assertNull(child1.getParent());
		Assertions.assertNull(child2.getParent());
	}
}
