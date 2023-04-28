package gamma.engine;

public interface ApplicationListener {

	default void onInit() {}

	default void onProcess() {}

	default void onTerminate() {}
}
