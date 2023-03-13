package gamma.engine.core.window;

/**
 * Service interface to be implemented by classes that want to listen to window events.
 *
 * @author Nico
 */
public interface WindowListener {

	/**
	 * Event triggered when the window is resized.
	 *
	 * @param width New width of the window in pixels
	 * @param height New height of the window in pixels
	 */
	void onResize(int width, int height);
}
