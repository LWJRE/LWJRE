package io.github.ardentengine.core.resources;

import java.util.List;

/**
 * Interface used to implement Yaml sequence deserializers.
 * <p>
 *     Used to deserialize sequence constructs.
 *     The {@link YamlSequenceDeserializer#deserialize(List)} method takes in a list representing the Yaml construct and should return the deserialized object.
 * </p>
 * <p>
 *     Yaml deserializers are loaded from the {@link YamlLoader} class using a service loader.
 *     Implementations of this interface must be registered as services in a file in {@code META-INF/services/io.github.ardentengine.core.resources.YamlDeserializer}.
 * </p>
 */
public non-sealed interface YamlSequenceDeserializer extends YamlDeserializer {

    /**
     * Deserializes the object.
     * <p>
     *     Takes in the Yaml sequence construct and returns the deserialized object.
     * </p>
     * <p>
     *     Should throw {@link ClassCastException} if one of the value is of the wrong type.
     * </p>
     *
     * @param sequence Yaml sequence construct.
     * @return The deserialized object.
     */
    Object deserialize(List<?> sequence);
}