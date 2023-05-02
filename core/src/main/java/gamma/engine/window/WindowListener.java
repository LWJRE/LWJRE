package gamma.engine.window;

public interface WindowListener {

	default void onWindowResized(int width, int height) {}

	default void onKeyInput(int key, int scancode, int action, int mods) {}

	default void onMouseButtonInput(int button, int action, int mods) {}

	default void onCursorInput(double x, double y) {}

	default void onMouseScrollInput(double x, double y) {}
}
