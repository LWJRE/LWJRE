package io.github.ardentengine.core.resources;

/**
 * Interface used to implement Yaml deserializers for classes more complex than a POJO.
 * <p>
 *     Yaml deserializers are loaded from the {@link YamlLoader} class using a service loader.
 *     Implementations of this interface must be registered as services in a file in {@code META-INF/services/}.
 * </p>
 */
public sealed interface YamlDeserializer permits YamlMappingDeserializer, YamlSequenceDeserializer {

    /**
     * Returns the class tag to be used in the Yaml file.
     * Should return the class of the object that is returned by the {@code deserialize} method.
     *
     * @return The class tag to be used in the Yaml file.
     */
    Class<?> getTag();
}