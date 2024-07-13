package io.github.ardentengine.core.util;

/**
 * Exception thrown from the {@link ReflectionUtils} class to indicate that a reflection error occurred.
 */
public class ReflectionException extends RuntimeException {

    /**
     * Constructs a new reflection exception with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause The cause.
     */
    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new reflection exception with the specified cause.
     *
     * @param cause The cause.
     */
    public ReflectionException(Throwable cause) {
        super(cause);
    }
}
