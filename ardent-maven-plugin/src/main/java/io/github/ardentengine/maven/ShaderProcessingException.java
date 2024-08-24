package io.github.ardentengine.maven;

/**
 * Exception thrown to indicate that an error occurred while processing a shader file.
 */
public class ShaderProcessingException extends Exception {

    /**
     * Constructs a {@code ShaderProcessingException} with the given message.
     *
     * @param message The detail message.
     */
    public ShaderProcessingException(String message) {
        super(message);
    }
}