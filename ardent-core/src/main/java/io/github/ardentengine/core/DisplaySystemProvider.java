package io.github.ardentengine.core;

/**
 * Service provider interface for the {@link DisplaySystem}.
 * <p>
 *     A dependency providing a display api must implement this interface to provide its implementation of the {@link DisplaySystem}.
 *     The implementation class must be declared as a service in a {@code META-INF/service} file.
 * </p>
 * <p>
 *     Implementations of this interface will be loaded using a service loader the first time {@link DisplaySystem#getInstance()} is called.
 *     The {@link DisplaySystemProvider#getDisplaySystem()} must return the implementation of the current display api.
 * </p>
 */
public interface DisplaySystemProvider {

    /**
     * Returns the instance of the {@link DisplaySystem}.
     * The returned object will be the one used by {@link DisplaySystem#getInstance()}.
     * <p>
     *     It is not mandatory that the returned object is a singleton instance.
     *     This method will be called the first time {@link DisplaySystem#getInstance()} is called and the returned object will become the {@code DisplaySystem} instance.
     * </p>
     *
     * @return An instance of {@code DisplaySystem}.
     */
    DisplaySystem getDisplaySystem();
}