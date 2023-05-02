package gamma.demo.components;

import gamma.engine.input.InputEvent;
import gamma.engine.scene.Component;

public class InputTester extends Component {

	@Override
	protected void onInput(InputEvent event) {
		System.out.println(event);
		super.onInput(event);
	}
}
