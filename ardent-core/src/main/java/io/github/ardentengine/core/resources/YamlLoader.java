package io.github.ardentengine.core.resources;

import io.github.ardentengine.core.logging.Logger;
import io.github.ardentengine.core.math.*;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.*;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.util.ServiceLoader;

/**
 * Resource loader used to load resources serialized as Yaml files.
 * <p>
 *     Only supports files containing a single Yaml document.
 *     If the Yaml file begins with a class tag, the resource returned from the {@link YamlLoader#load(String)} method will be an instance of that class.
 * </p>
 * <p>
 *     Custom deserializer can be added by implementing the {@link YamlMappingDeserializer} or the {@link YamlSequenceDeserializer} interfaces.
 *     Such deserializers must be registered as services in {@code META-INF/services}.
 * </p>
 * <p>
 *     Supports {@code .yaml} and {@code .yml} extensions.
 * </p>
 */
public class YamlLoader implements ResourceLoader {

    /**
     * Yaml object.
     */
    private final Yaml yaml;

    /**
     * Public no-args constructor necessary for resource loaders.
     * <p>
     *     This class should not be instantiated directly.
     *     Use {@link ResourceManager#getOrLoad(String)} instead.
     * </p>
     */
    public YamlLoader() {
        var loaderOptions = new LoaderOptions();
        loaderOptions.setAllowDuplicateKeys(false);
        loaderOptions.setTagInspector(tag -> true);
        var representer = new Representer(new DumperOptions());
        representer.getPropertyUtils().setSkipMissingProperties(true);
        this.yaml = new Yaml(new LoaderConstructor(loaderOptions), representer);
        this.yaml.setBeanAccess(BeanAccess.FIELD);
    }

    // TODO: Create tests for deserializing objects from yaml files

    @Override
    public Object load(String resourcePath) {
        try(var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            if(inputStream != null) {
                return this.yaml.load(inputStream);
            }
            Logger.error("Could not find resource " + resourcePath);
        } catch (IOException e) {
            Logger.error("Exception occurred while loading resource " + resourcePath, e);
        }
        return null;
    }

    @Override
    public String[] supportedExtensions() {
        return new String[] {".yaml", ".yml"};
    }

    private static class LoaderConstructor extends Constructor {

        public LoaderConstructor(LoaderOptions loadingConfig) {
            super(loadingConfig);
            this.yamlConstructors.put(new Tag("!getOrLoad"), new ConstructResource());
            this.yamlConstructors.put(new Tag("!vec2"), new ConstructVector2());
            this.yamlConstructors.put(new Tag("!vec3"), new ConstructVector3());
            this.yamlConstructors.put(new Tag("!vec4"), new ConstructVector4());
            this.yamlConstructors.put(new Tag("!vec2i"), new ConstructVector2i());
            this.yamlConstructors.put(new Tag("!vec3i"), new ConstructVector3i());
            this.yamlConstructors.put(new Tag("!vec4i"), new ConstructVector4i());
            this.yamlConstructors.put(new Tag("!color"), new ConstructColor());
            this.yamlConstructors.put(new Tag("!quaternion"), new ConstructQuaternion());
            this.yamlConstructors.put(new Tag("!class"), new ConstructClass());
            for(var deserializer : ServiceLoader.load(YamlDeserializer.class)) {
                var previous = this.yamlConstructors.putIfAbsent(new Tag(deserializer.getTag()), new CustomConstruct(deserializer));
                if(previous != null) {
                    Logger.error("Cannot add deserializer for " + deserializer.getTag() + " because " + previous + " already exists");
                }
            }
        }

        private class CustomConstruct extends AbstractConstruct {

            private final YamlDeserializer deserializer;

            private CustomConstruct(YamlDeserializer deserializer) {
                this.deserializer = deserializer;
            }

            @Override
            public Object construct(Node node) {
                if(node instanceof MappingNode && this.deserializer instanceof YamlMappingDeserializer) {
                    return ((YamlMappingDeserializer) this.deserializer).deserialize(constructMapping((MappingNode) node));
                } else if(node instanceof SequenceNode && this.deserializer instanceof YamlSequenceDeserializer) {
                    return ((YamlSequenceDeserializer) this.deserializer).deserialize(constructSequence((SequenceNode) node));
                }
                return null;
            }
        }

        private class ConstructResource extends ConstructScalar {

            @Override
            public Object construct(Node node) {
                if(node instanceof ScalarNode) {
                    return ResourceManager.getOrLoad(constructScalar((ScalarNode) node));
                }
                return null;
            }
        }

        private class ConstructVector2 extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof SequenceNode sequenceNode) {
                    var sequence = constructSequence(sequenceNode);
                    return new Vector2(
                        !sequence.isEmpty() && sequence.get(0) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 1 && sequence.get(1) instanceof Number n ? n.floatValue() : 0.0f
                    );
                }
                return Vector2.ZERO;
            }
        }

        private class ConstructVector2i extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof SequenceNode sequenceNode) {
                    var sequence = constructSequence(sequenceNode);
                    return new Vector2i(
                        !sequence.isEmpty() && sequence.get(0) instanceof Number n ? n.intValue() : 0,
                        sequence.size() > 1 && sequence.get(1) instanceof Number n ? n.intValue() : 0
                    );
                }
                return Vector2i.ZERO;
            }
        }

        private class ConstructVector3 extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof SequenceNode sequenceNode) {
                    var sequence = constructSequence(sequenceNode);
                    return new Vector3(
                        !sequence.isEmpty() && sequence.get(0) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 1 && sequence.get(1) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 2 && sequence.get(2) instanceof Number n ? n.floatValue() : 0.0f
                    );
                }
                return Vector3.ZERO;
            }
        }

        private class ConstructVector3i extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof SequenceNode sequenceNode) {
                    var sequence = constructSequence(sequenceNode);
                    return new Vector3i(
                        !sequence.isEmpty() && sequence.get(0) instanceof Number n ? n.intValue() : 0,
                        sequence.size() > 1 && sequence.get(1) instanceof Number n ? n.intValue() : 0,
                        sequence.size() > 2 && sequence.get(2) instanceof Number n ? n.intValue() : 0
                    );
                }
                return Vector3i.ZERO;
            }
        }

        private class ConstructVector4 extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof SequenceNode sequenceNode) {
                    var sequence = constructSequence(sequenceNode);
                    return new Vector4(
                        !sequence.isEmpty() && sequence.get(0) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 1 && sequence.get(1) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 2 && sequence.get(2) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 3 && sequence.get(3) instanceof Number n ? n.floatValue() : 0.0f
                    );
                }
                return Vector4.ZERO;
            }
        }

        private class ConstructVector4i extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof SequenceNode sequenceNode) {
                    var sequence = constructSequence(sequenceNode);
                    return new Vector4i(
                        !sequence.isEmpty() && sequence.get(0) instanceof Number n ? n.intValue() : 0,
                        sequence.size() > 1 && sequence.get(1) instanceof Number n ? n.intValue() : 0,
                        sequence.size() > 2 && sequence.get(2) instanceof Number n ? n.intValue() : 0,
                        sequence.size() > 3 && sequence.get(3) instanceof Number n ? n.intValue() : 0
                    );
                }
                return Vector4i.ZERO;
            }
        }

        private class ConstructColor extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof SequenceNode sequenceNode) {
                    var sequence = constructSequence(sequenceNode);
                    return new Color(
                        !sequence.isEmpty() && sequence.get(0) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 1 && sequence.get(1) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 2 && sequence.get(2) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 3 && sequence.get(3) instanceof Number n ? n.floatValue() : 1.0f
                    );
                }
                return Color.BLACK;
            }
        }

        private class ConstructQuaternion extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof SequenceNode sequenceNode) {
                    var sequence = constructSequence(sequenceNode);
                    return new Quaternion(
                        !sequence.isEmpty() && sequence.get(0) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 1 && sequence.get(1) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 2 && sequence.get(2) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 3 && sequence.get(3) instanceof Number n ? n.floatValue() : 0.0f
                    );
                }
                return Quaternion.ZERO;
            }
        }

        private class ConstructClass extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof ScalarNode) {
                    var name = constructScalar((ScalarNode) node);
                    try {
                        return Class.forName(name);
                    } catch (ClassNotFoundException e) {
                        Logger.error("Cannot find class " + name, e);
                    }
                }
                return null;
            }
        }
    }
}