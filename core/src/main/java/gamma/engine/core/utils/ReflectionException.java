package gamma.engine.core.utils;

/**
 * Unchecked exception used in {@link Reflection} class to wrap checked reflection exceptions.
 *
 * @author Nico
 */
public class ReflectionException extends RuntimeException {

	/**
	 * Constructs a new reflection exception with the given cause.
	 *
	 * @param message The detail message
	 * @param cause The cause of the exception
	 *
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */

	public ReflectionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new reflection exception with the given cause.
	 *
	 * @param cause The cause of the exception
	 *
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public ReflectionException(Throwable cause) {
		super(cause);
	}
}
