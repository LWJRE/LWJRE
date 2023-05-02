package gamma.engine.core;

public interface ApplicationListener {

	default void onInit() {}

	default void onProcess() {}

	default void onTerminate() {}
}
