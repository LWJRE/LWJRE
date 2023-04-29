package gamma.engine.window;

import gamma.engine.input.InputEvent;

public interface WindowListener {

	default void onWindowInput(InputEvent event) {}

	default void onWindowResized(int width, int height) {}
}
