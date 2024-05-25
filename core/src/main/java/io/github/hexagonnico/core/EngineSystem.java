package io.github.hexagonnico.core;

/**
 * Interface representing an engine support system.
 * <p>
 *     TODO: Explain what they are
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
