package io.github.ardentengine.core;

/**
 * Service provider interface for the {@link RenderingSystem}.
 * <p>
 *     A dependency providing a rendering api must implement this interface to provide its implementation of the {@link RenderingSystem}.
 *     The implementation class must be declared as a service in a {@code META-INF/service} file.
 * </p>
 * <p>
 *     Implementations of this interface will be loaded using a service loader the first time {@link RenderingSystem#getInstance()} is called.
 *     The {@link RenderingSystemProvider#getRenderingSystem()} must return the implementation of the current rendering api.
 * </p>
 */
public interface RenderingSystemProvider {

    /**
     * Returns the instance of the {@link RenderingSystem}.
     * The returned object will be the one used by {@link RenderingSystem#getInstance()}.
     * <p>
     *     It is not mandatory that the returned object is a singleton instance.
     *     This method will be called the first time {@link RenderingSystem#getInstance()} is called and the returned object will become the {@code RenderingSystem} instance.
     * </p>
     *
     * @return An instance of {@code RenderingSystem}.
     */
    RenderingSystem getRenderingSystem();
}