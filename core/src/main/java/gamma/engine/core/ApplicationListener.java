package gamma.engine.core;

/**
 * Service interface to be implemented by classes that want to listen to application events.
 *
 * @author Nico
 */
public interface ApplicationListener {

	/**
	 * Called before the application starts.
	 */
	void onStart();

	/**
	 * Called every frame.
	 */
	void onUpdate();

	/**
	 * Called before the application is terminated.
	 */
	void onTerminate();
}
