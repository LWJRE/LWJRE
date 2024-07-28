package io.github.ardentengine.core.resources;

import io.github.ardentengine.core.logging.Logger;
import io.github.scalamath.colorlib.*;
import io.github.scalamath.vecmatlib.*;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.constructor.SafeConstructor;
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
            this.yamlConstructors.put(new Tag("!Vec2f"), new ConstructVec2f());
            this.yamlConstructors.put(new Tag("!Vec3f"), new ConstructVec3f());
            this.yamlConstructors.put(new Tag("!Vec4f"), new ConstructVec4f());
            this.yamlConstructors.put(new Tag("!Vec2i"), new ConstructVec2i());
            this.yamlConstructors.put(new Tag("!Vec3i"), new ConstructVec3i());
            this.yamlConstructors.put(new Tag("!Vec4i"), new ConstructVec4i());
            this.yamlConstructors.put(new Tag("!Col3f"), new ConstructCol3f());
            this.yamlConstructors.put(new Tag("!Col4f"), new ConstructCol4f());
            this.yamlConstructors.put(new Tag("!Col1i"), new ConstructCol1i());
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
         * YAML construct used to deserialize a {@link Vec2f} from a sequence or mapping node.
         */
        private class ConstructVec2f extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof SequenceNode sequenceNode) {
                    var sequence = constructSequence(sequenceNode);
                    return new Vec2f(
                        !sequence.isEmpty() && sequence.get(0) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 1 && sequence.get(1) instanceof Number n ? n.floatValue() : 0.0f
                    );
                }
                return Vec2f.Zero();
            }
        }

        /**
         * YAML construct used to deserialize a {@link Vec2i} from a sequence or mapping node.
         */
        private class ConstructVec2i extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof SequenceNode sequenceNode) {
                    var sequence = constructSequence(sequenceNode);
                    return new Vec2i(
                        !sequence.isEmpty() && sequence.get(0) instanceof Number n ? n.intValue() : 0,
                        sequence.size() > 1 && sequence.get(1) instanceof Number n ? n.intValue() : 0
                    );
                }
                return Vec2i.Zero();
            }
        }

        /**
         * YAML construct used to deserialize a {@link Vec3f} from a sequence or mapping node.
         */
        private class ConstructVec3f extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof SequenceNode sequenceNode) {
                    var sequence = constructSequence(sequenceNode);
                    return new Vec3f(
                        !sequence.isEmpty() && sequence.get(0) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 1 && sequence.get(1) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 2 && sequence.get(2) instanceof Number n ? n.floatValue() : 0.0f
                    );
                }
                return Vec3f.Zero();
            }
        }

        /**
         * YAML construct used to deserialize a {@link Vec3i} from a sequence or mapping node.
         */
        private class ConstructVec3i extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof SequenceNode sequenceNode) {
                    var sequence = constructSequence(sequenceNode);
                    return new Vec3i(
                        !sequence.isEmpty() && sequence.get(0) instanceof Number n ? n.intValue() : 0,
                        sequence.size() > 1 && sequence.get(1) instanceof Number n ? n.intValue() : 0,
                        sequence.size() > 2 && sequence.get(2) instanceof Number n ? n.intValue() : 0
                    );
                }
                return Vec3i.Zero();
            }
        }

        /**
         * YAML construct used to deserialize a {@link Vec4f} from a sequence or mapping node.
         */
        private class ConstructVec4f extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof SequenceNode sequenceNode) {
                    var sequence = constructSequence(sequenceNode);
                    return new Vec4f(
                        !sequence.isEmpty() && sequence.get(0) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 1 && sequence.get(1) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 2 && sequence.get(2) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 3 && sequence.get(3) instanceof Number n ? n.floatValue() : 0.0f
                    );
                }
                return Vec4f.Zero();
            }
        }

        /**
         * YAML construct used to deserialize a {@link Vec4i} from a sequence or mapping node.
         */
        private class ConstructVec4i extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof SequenceNode sequenceNode) {
                    var sequence = constructSequence(sequenceNode);
                    return new Vec4i(
                        !sequence.isEmpty() && sequence.get(0) instanceof Number n ? n.intValue() : 0,
                        sequence.size() > 1 && sequence.get(1) instanceof Number n ? n.intValue() : 0,
                        sequence.size() > 2 && sequence.get(2) instanceof Number n ? n.intValue() : 0,
                        sequence.size() > 3 && sequence.get(3) instanceof Number n ? n.intValue() : 0
                    );
                }
                return Vec4i.Zero();
            }
        }

        /**
         * YAML construct used to deserialize a {@link Col3f} from a sequence or mapping node.
         */
        private class ConstructCol3f extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof SequenceNode sequenceNode) {
                    var sequence = constructSequence(sequenceNode);
                    return new Col3f(
                        !sequence.isEmpty() && sequence.get(0) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 1 && sequence.get(1) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 2 && sequence.get(2) instanceof Number n ? n.floatValue() : 0.0f
                    );
                }
                return new Col3f(0.0f, 0.0f, 0.0f);
            }
        }

        /**
         * YAML construct used to deserialize a {@link Col4f} from a sequence or mapping node.
         */
        private class ConstructCol4f extends AbstractConstruct {

            @Override
            public Object construct(Node node) {
                if(node instanceof SequenceNode sequenceNode) {
                    var sequence = constructSequence(sequenceNode);
                    return new Col4f(
                        !sequence.isEmpty() && sequence.get(0) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 1 && sequence.get(1) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 2 && sequence.get(2) instanceof Number n ? n.floatValue() : 0.0f,
                        sequence.size() > 3 && sequence.get(3) instanceof Number n ? n.floatValue() : 0.0f
                    );
                }
                return new Col4f(0.0f, 0.0f, 0.0f);
            }
        }

        /**
         * YAML construct used to deserialize a {@link Col1i} from a sequence or mapping node.
         */
        private class ConstructCol1i extends SafeConstructor.ConstructYamlInt {

            @Override
            public Object construct(Node node) {
                if(super.construct(node) instanceof Number number) {
                    return new Col1i(number.intValue());
                }
                return new Col1i(0);
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
                            gradient = gradient.addPoint(offset.floatValue(), color);
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