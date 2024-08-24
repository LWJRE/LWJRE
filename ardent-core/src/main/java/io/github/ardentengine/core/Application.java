package io.github.ardentengine.core;

import io.github.ardentengine.core.input.Input;
import io.github.ardentengine.core.input.InputEvent;
import io.github.ardentengine.core.scene.Node;
import io.github.ardentengine.core.scene.SceneTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ServiceLoader;
import java.util.function.Consumer;

/**
 * Application class that contains the main method and handles the running of the application.
 * <p>
 *     The application loads the {@link EngineSystem}s using a {@link ServiceLoader} when it is first started,
 *     then calls {@link EngineSystem#initialize()} on all of them according to their {@link EngineSystem#priority()}.
 *     The {@link EngineSystem#process()} method is called in an infinite loop until {@link Application#quit()} is called.
 *     After that, the {@link EngineSystem#terminate()} method is called on every system in the reverse order.
 * </p>
 */
public final class Application {

    /** Reference to the main thread needed for {@link Application#isMainThread()}. */
    private static Thread mainThread;

    /**
     * Checks if the calling thread is the main thread.
     * Useful to access rendering functions that can only be accessed from the main thread.
     *
     * @return True if the current thread is the main thread, otherwise false.
     */
    public static boolean isMainThread() {
        return Thread.currentThread().equals(mainThread);
    }

    /** Singleton instance. */
    private static Application instance;

    public static long getProcessFrames() {
        return instance != null ? instance.processFrames : 0;
    }

    /**
     * Quits the application if it is running.
     * Causes all the engine's system to be terminated at the end of the current process loop.
     */
    public static void quit() {
        if(instance != null) {
            instance.running = false;
        }
    }

    /** List containing the engine systems. */
    private final ArrayList<EngineSystem> engineSystems = new ArrayList<>();

    /** True if the application is running. */
    private boolean running = true;
    private long processFrames = 0;

    /** The main scene tree. */
    private final SceneTree sceneTree = new SceneTree();
    /** Previous time obtained using {@link System#nanoTime()} and used to compute {@code delta}. */
    private long previousTime;

    /**
     * Application constructor.
     * Loads the engine systems and calls {@link EngineSystem#initialize()}.
     */
    private Application() {
        instance = this;
        for(var engineSystem : ServiceLoader.load(EngineSystemProvider.class)) {
            this.engineSystems.add(engineSystem.getEngineSystem());
        }
        Collections.sort(this.engineSystems);
        for(var engineSystem : this.engineSystems) {
            engineSystem.initialize();
        }
        Input.setEventDispatchFunction(this::input);
        var mainScene = ApplicationProperties.getString("application.run.mainScene");
        if(!mainScene.isEmpty()) {
            this.sceneTree.changeScene(mainScene);
        }
        this.previousTime = System.nanoTime();
    }

    /**
     * Recursive method used to process the scene tree.
     * Children are processed first.
     *
     * @param node The node to process.
     * @param delta Delta time.
     */
    private void process(Node node, float delta) {
        for(var child : node.getChildren()) {
            this.process(child, delta);
        }
        node.onUpdate(delta);
    }

    /**
     * Private method called at every iteration to process the current scene.
     */
    private void process() {
        var time = System.nanoTime();
        var delta = (time - this.previousTime) / 1_000_000_000.0f;
        if(this.sceneTree.getRoot() != null) {
            this.process(this.sceneTree.getRoot(), delta);
        }
        this.previousTime = time;
    }

    /**
     * Method used for {@link Input#setEventDispatchFunction(Consumer)}.
     * Sends input events to the scene tree.
     *
     * @param event The input event.
     */
    private void input(InputEvent event) {
        if(this.sceneTree.getRoot() != null) {
            this.input(this.sceneTree.getRoot(), event);
        }
    }

    /**
     * Recursive method used to send input events to the scene tree.
     * Input events are sent to children first.
     *
     * @param node The node to process.
     * @param event The input event.
     */
    private void input(Node node, InputEvent event) {
        for(var child : node.getChildren()) {
            this.input(child, event);
        }
        node.onInput(event);
    }

    /**
     * Terminates the application.
     * Calls {@link EngineSystem#terminate()}.
     */
    private void terminate() {
        Collections.reverse(this.engineSystems);
        for(var engineSystem : this.engineSystems) {
            engineSystem.terminate();
        }
    }

    /**
     * Running process of the application.
     * Runs in a loop until {@link Application#quit()} is called.
     * Calls {@link EngineSystem#process()}.
     */
    private void run() {
        while(this.running) {
            this.processFrames++;
            for(var engineSystem : this.engineSystems) {
                engineSystem.process();
                this.process();
            }
        }
        this.terminate();
    }

    // TODO: Handle uncaught exceptions and handle Ctrl+C sigint

    public static void main(String[] args) {
        mainThread = Thread.currentThread();
        new Application().run();
    }
}