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
	public void testRemoveChildWithKey() {
		Entity parent = new Entity();
		Entity child = new Entity();
		parent.addChild("givenKey", child);
		Entity removed = parent.removeChild("givenKey");
		Assertions.assertEquals(child, removed);
		Assertions.assertNull(child.getParent());
	}

	@Test
	public void testRemoveChildWithoutKey() {
		Entity parent = new Entity();
		Entity child = new Entity();
		parent.addChild("givenKey", child);
		Assertions.assertThrows(IllegalStateException.class, () -> parent.removeChild("anotherKey"));
	}

	@Test
	public void testRemoveChild() {
		Entity parent = new Entity();
		Entity child = new Entity();
		parent.addChild(child);
		parent.removeChild(child);
		Assertions.assertNull(child.getParent());
	}

	@Test
	public void testRemoveIsNotChild() {
		Entity parent = new Entity();
		Entity child = new Entity();
		Assertions.assertThrows(IllegalStateException.class, () -> parent.removeChild(child));
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
		TestComponent removed = entity.removeComponent(TestComponent.class);
		Assertions.assertEquals(component, removed);
		Assertions.assertNull(component.entity);
	}

	@Test
	public void testRemoveDoesNotHaveComponent() {
		Entity entity = new Entity();
		TestComponent component = new TestComponent();
		entity.addComponent(component);
		Assertions.assertThrows(IllegalStateException.class, () -> entity.removeComponent(AnotherComponent.class));
	}

	@Test
	public void testRemoveComponent() {
		Entity entity = new Entity();
		TestComponent component = new TestComponent();
		entity.addComponent(component);
		Assertions.assertTrue(entity.removeComponent(component));
		Assertions.assertNull(component.entity);
		Assertions.assertFalse(entity.removeComponent(new AnotherComponent()));
	}
}
