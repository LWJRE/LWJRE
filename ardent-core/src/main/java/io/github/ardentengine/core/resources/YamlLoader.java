package io.github.ardentengine.core.resources;

import io.github.ardentengine.core.logging.Logger;
import io.github.ardentengine.core.math.*;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.*;

import java.io.IOException;

/**
 * Resource loader used to load resources serialized as YAML files.
 * Supports {@code .yaml} and {@code .yml} extensions.
 * Only supports YAML files containing a single YAML document.
 * If the YAML file begins with a class tag, the resource returned from the {@link YamlLoader#load(String)} method will be an instance of that class.
 */
public class YamlLoader implements ResourceLoader {

    /**
     * YAML object.
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
        this.yaml = new Yaml(new LoaderConstructor(loaderOptions));
        this.yaml.setBeanAccess(BeanAccess.FIELD);
    }

    @Override
    public Object load(String resourcePath) {
        try(var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            if(inputStream == null) {
                Logger.error("Could not find resource " + resourcePath);
            } else {
                return this.yaml.load(inputStream);
            }
        } catch (IOException e) {
            Logger.error("Exception occurred while loading resource " + resourcePath, e);
        }
        return null;
    }

    @Override
    public String[] supportedExtensions() {
        return new String[] {".yaml", ".yml"};
    }

    /**
     * YAML constructor needed to deserialize elements with specific tags.
     */
    private static class LoaderConstructor extends Constructor {

        /**
         * Constructs a constructor.
         *
         * @param loadingConfig Loader options.
         */
        public LoaderConstructor(LoaderOptions loadingConfig) {
            super(loadingConfig);
            this.yamlConstructors.put(Tag.FLOAT, new ConstructActualFloat());
            this.yamlConstructors.put(new Tag("!getOrLoad"), new ConstructResource());
            this.yamlConstructors.put(new Tag("!Vector2"), new ConstructVector2());
            this.yamlConstructors.put(new Tag("!Vector3"), new ConstructVector3());
            this.yamlConstructors.put(new Tag("!Vector4"), new ConstructVector4());
            this.yamlConstructors.put(new Tag("!Vector2i"), new ConstructVector2i());
            this.yamlConstructors.put(new Tag("!Vector3i"), new ConstructVector3i());
            this.yamlConstructors.put(new Tag("!Vector4i"), new ConstructVector4i());
            this.yamlConstructors.put(new Tag("!Color"), new ConstructColor());
            this.yamlConstructors.put(new Tag("!Color"), new ConstructColor());
            this.yamlConstructors.put(new Tag("!Color"), new ConstructColor());
            this.yamlConstructors.put(new Tag("!Gradient"), new ConstructGradient());
            this.yamlConstructors.put(new Tag(Class.class), new ConstructClass());
        }

        private class ConstructActualFloat extends ConstructYamlFloat {

            @Override
            public Object construct(Node node) {
                var result = super.construct(node);
                if(result instanceof Number number) {
                    return number.floatValue();
                }
                return result;
            }
        }

        /**
         * YAML construct used to deserialize resources using {@link ResourceManager#getOrLoad(String)}.
         */
        private class ConstructResource extends ConstructScalar {

            @Override
            public Object construct(Node node) {
                return ResourceManager.getOrLoad(constructScalar((ScalarNode) node));
            }
        }

        /**
         * YAML construct used to deserialize a {@link Vector2} from a sequence or mapping node.
         */
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

        /**
         * YAML construct used to deserialize a {@link Vector2i} from a sequence or mapping node.
         */
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

        /**
         * YAML construct used to deserialize a {@link Vector3} from a sequence or mapping node.
         */
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

        /**
         * YAML construct used to deserialize a {@link Vector3i} from a sequence or mapping node.
         */
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

        /**
         * YAML construct used to deserialize a {@link Vector4} from a sequence or mapping node.
         */
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

        /**
         * YAML construct used to deserialize a {@link Vector4i} from a sequence or mapping node.
         */
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

        /**
         * YAML construct used to deserialize a {@link Color} from a sequence or mapping node.
         */
        private class ConstructColor extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof SequenceNode sequenceNode) {
                    var sequence = constructSequence(sequenceNode);
                    return new Color(
                        !sequence.isEmpty() && sequence.get(0) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 1 && sequence.get(1) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 2 && sequence.get(2) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 3 && sequence.get(3) instanceof Number n ? n.floatValue() : 0.0f
                    );
                }
                return new Color(0.0f, 0.0f, 0.0f);
            }
        }

        /**
         * YAML construct used to deserialize a {@link Gradient} from a mapping node whose keys are offsets and whose values are colors.
         */
        private class ConstructGradient extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                var gradient = new Gradient();
                if(node instanceof MappingNode mappingNode) {
                    var mapping = constructMapping(mappingNode);
                    for(var key : mapping.keySet()) {
                        if(key instanceof Number offset && mapping.get(key) instanceof Color color) {
                            gradient = gradient.withPoint(offset.floatValue(), color);
                        }
                    }
                }
                return gradient;
            }
        }

        private class ConstructClass extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                try {
                    return Class.forName(constructScalar((ScalarNode) node));
                } catch (ClassNotFoundException e) {
                    throw new YAMLException("Cannot construct class", e);
                }
            }
        }
    }
}