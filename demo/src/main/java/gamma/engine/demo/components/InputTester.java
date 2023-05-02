package gamma.engine.demo.components;

import gamma.engine.core.input.InputEvent;
import gamma.engine.core.scene.Component;

public class InputTester extends Component {

	@Override
	protected void onInput(InputEvent event) {
		System.out.println(event);
		super.onInput(event);
	}
}
