package gamma.engine;

public interface EditorListener {

	default void onEditorInit() {}

	default void onEditorProcess() {}

	default void onEditorTerminate() {}
}
