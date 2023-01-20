package gamma.engine.core;

/**
 * Interface to be used in modules that need to do something on start, update, or terminate.
 * Modules should declare a {@code META-INF/services/gamma.engine.core.Module} file containing the name of all classes implementing this interface.
 *
 * @author Nico
 */
public interface Module {

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
