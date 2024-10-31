package io.github.ardentengine.core.resources;

import java.util.Map;

/**
 * Interface used to implement Yaml mapping deserializers.
 * <p>
 *     Used to deserialize mapping constructs.
 *     The {@link YamlMappingDeserializer#deserialize(Map)} method takes in a map representing the Yaml construct and should return the deserialized object.
 * </p>
 * <p>
 *     Yaml deserializers are loaded from the {@link YamlLoader} class using a service loader.
 *     Implementations of this interface must be registered as services in a file in {@code META-INF/services/io.github.ardentengine.core.resources.YamlDeserializer}.
 * </p>
 */
public non-sealed interface YamlMappingDeserializer extends YamlDeserializer {

    /**
     * Deserializes the object.
     * <p>
     *     Takes in the Yaml mapping construct and returns the deserialized object.
     * </p>
     * <p>
     *     Should throw {@link ClassCastException} if one of the value is of the wrong type.
     * </p>
     *
     * @param map Yaml mapping construct.
     * @return The deserialized object.
     */
    Object deserialize(Map<Object, Object> map);
}