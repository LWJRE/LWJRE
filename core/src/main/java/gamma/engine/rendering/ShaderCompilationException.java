package gamma.engine.rendering;

/**
 * Signals that there was a compilation error in a shader.
 *
 * @author Nico
 */
public class ShaderCompilationException extends Exception {

	/**
	 * Constructs a {@code ShaderCompilationException} with the given message.
	 *
	 * @param message The detail message
	 */
	public ShaderCompilationException(String message) {
		super(message);
	}
}
