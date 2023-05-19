package gamma.engine.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestNodeResource {

	@Test
	public void testNodeResource() {
		NodeResource resource = NodeResource.getOrLoad("testNodeResource.yaml");
		if(resource.instantiate() instanceof MockNode node) {
			Assertions.assertEquals("Hello!", node.string);
			Assertions.assertEquals(42, node.number);
			if(node.getChild("child") instanceof MockNode child) {
				Assertions.assertEquals("Test", child.string);
				Assertions.assertEquals(1, child.number);
			} else {
				Assertions.fail();
			}
		} else {
			Assertions.fail();
		}
	}

	private static class MockNode extends Node {

		@SuppressWarnings("FieldMayBeFinal")
		private String string = "Hello!";

		@SuppressWarnings("FieldMayBeFinal")
		private int number = 42;
	}
}
