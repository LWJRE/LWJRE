package gamma.engine.core.scene;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class TestComponent {

	@Test
	public void testGetEntity() {
		Entity entity = new Entity();
		Component1 component = new Component1();
		entity.addComponent(component);
		Assertions.assertEquals(Optional.of(entity), component.getEntity());
	}

	@Test
	public void testGetEntityEmpty() {
		Component1 component = new Component1();
		Assertions.assertTrue(component.getEntity().isEmpty());
	}

	@Test
	public void testGetComponent() {
		Entity entity = new Entity();
		Component1 component1 = new Component1();
		Component2 component2 = new Component2();
		entity.addComponent(component1);
		entity.addComponent(component2);
		Assertions.assertEquals(Optional.of(component2), component1.getComponent(Component2.class));
	}

	@Test
	public void testGetComponentEmpty() {
		Entity entity = new Entity();
		Component1 component1 = new Component1();
		entity.addComponent(component1);
		Assertions.assertTrue(component1.getComponent(Component2.class).isEmpty());
	}

	@Test
	public void testGetComponentInParent() {
		Entity parent = new Entity();
		Entity child = new Entity();
		parent.addChild(child);
		Component1 component1 = new Component1();
		Component2 component2 = new Component2();
		child.addComponent(component1);
		parent.addComponent(component2);
		Assertions.assertEquals(Optional.of(component2), component1.getComponentInParent(Component2.class));
	}

	@Test
	public void testGetComponentInParentEmpty() {
		Entity entity = new Entity();
		Component1 component1 = new Component1();
		entity.addComponent(component1);
		Assertions.assertTrue(component1.getComponentInParent(Component2.class).isEmpty());
	}

	private static class Component1 extends Component {

	}

	private static class Component2 extends Component {

	}
}
