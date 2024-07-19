package io.github.ardentengine.core;

/**
 * Interface representing an engine support system.
 * <p>
 *     Engine systems are low-level support systems that manage an aspect of the engine.
 *     The contain an {@link EngineSystem#initialize()} function that is called when the engine is stared up,
 *     an {@link EngineSystem#process()} method that is called at every process step,
 *     and a {@link EngineSystem#terminate()} method that is called when the engine is being shut down.
 * </p>
 * <p>
 *     Engine support systems are loaded from the main {@link Application} class.
 *     Modules providing an engine system must declare its implementation as a service.
 * </p>
 * <p>
 *     Engine systems can be given a {@link EngineSystem#priority()} to decide in which order they should be initialized.
 *     Engine systems with a higher priority are initialized first and processed first.
 *     The termination happens in the reverse order.
 * </p>
 */
public interface EngineSystem extends Comparable<EngineSystem> {

    /**
     * Initializes this system.
     * Called when the application starts.
     */
    void initialize();

    /**
     * Updates this system.
     * Called every iteration step.
     */
    void process();

    /**
     * Terminates this system.
     * Called before the application is terminated.
     */
    void terminate();

    /**
     * Determines the priority of this system.
     * A lower value means a higher priority.
     * The default value is 10.
     *
     * @return An integer value representing the priority of this system.
     */
    default int priority() {
        return 10;
    }

    @Override
    default int compareTo(EngineSystem engineSystem) {
        return Integer.compare(this.priority(), engineSystem.priority());
    }
}