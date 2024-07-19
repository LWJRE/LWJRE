package io.github.ardentengine.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ServiceLoader;

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

    /**
     * Application constructor.
     * Loads the engine systems and calls {@link EngineSystem#initialize()}.
     */
    private Application() {
        instance = this;
        for(var engineSystem : ServiceLoader.load(EngineSystem.class)) {
            this.engineSystems.add(engineSystem);
        }
        Collections.sort(this.engineSystems);
        for(var engineSystem : this.engineSystems) {
            engineSystem.initialize();
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
            }
        }
        this.terminate();
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

    // TODO: Handle uncaught exceptions

    public static void main(String[] args) {
        new Application().run();
    }
}