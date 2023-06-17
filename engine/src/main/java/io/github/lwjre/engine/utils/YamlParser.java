package io.github.lwjre.engine.utils;

import io.github.lwjre.engine.resources.Resources;
import io.github.hexagonnico.vecmatlib.color.Color3f;
import io.github.hexagonnico.vecmatlib.color.Color4f;
import io.github.hexagonnico.vecmatlib.vector.*;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class YamlParser extends Constructor {

	private static final YamlParser INSTANCE;

	public static Object parse(String yamlString) {
		Yaml yaml = new Yaml(INSTANCE);
		yaml.setBeanAccess(BeanAccess.FIELD);
		return yaml.load(yamlString);
	}

	public static Object parseResource(String path) {
		Yaml yaml = new Yaml(INSTANCE);
		yaml.setBeanAccess(BeanAccess.FIELD);
		return FileUtils.readResource(path, yaml::load);
	}

	public static Object parseFile(String path) {
		Yaml yaml = new Yaml(INSTANCE);
		yaml.setBeanAccess(BeanAccess.FIELD);
		return FileUtils.readFile(path, yaml::load);
	}

	private YamlParser(LoaderOptions loaderOptions) {
		super(loaderOptions);
	}

	public static void scalarConstruct(String tag, Function<String, Object> function) {
		INSTANCE.yamlConstructors.put(new Tag(tag), new AbstractConstruct() {
			@Override
			public Object construct(Node node) {
				return function.apply(((ScalarNode) node).getValue());
			}
		});
	}

	public static void sequenceConstruct(String tag, Function<List<Object>, Object> function) {
		INSTANCE.yamlConstructors.put(new Tag(tag), new AbstractConstruct() {
			@Override
			public Object construct(Node node) {
				return function.apply(unboxList(((SequenceNode) node).getValue()));
			}
		});
	}

	public static void mappingConstruct(String tag, Function<Map<Object, Object>, Object> function) {
		INSTANCE.yamlConstructors.put(new Tag(tag), new AbstractConstruct() {
			@Override
			public Object construct(Node node) {
				return function.apply(unboxMap(((MappingNode) node).getValue()));
			}
		});
	}

	private static List<Object> unboxList(List<Node> list) {
		return list.stream().map(node -> {
			if(node instanceof ScalarNode scalarNode) {
				return scalarNode.getValue();
			} else if(node instanceof SequenceNode sequenceNode) {
				return unboxList(sequenceNode.getValue());
			} else if(node instanceof MappingNode mappingNode) {
				return unboxMap(mappingNode.getValue());
			} else {
				return null;
			}
		}).toList();
	}

	private static Map<Object, Object> unboxMap(List<NodeTuple> map) {
		HashMap<Object, Object> result = new HashMap<>();
		map.forEach(tuple -> {
			Object key = null;
			Object value = null;
			if(tuple.getKeyNode() instanceof ScalarNode scalarNode) {
				key = scalarNode.getValue();
			} else if(tuple.getKeyNode() instanceof SequenceNode sequenceNode) {
				key = unboxList(sequenceNode.getValue());
			} else if(tuple.getKeyNode() instanceof MappingNode mappingNode) {
				key = unboxMap(mappingNode.getValue());
			}
			if(tuple.getValueNode() instanceof ScalarNode scalarNode) {
				value = scalarNode.getValue();
			} else if(tuple.getValueNode() instanceof SequenceNode sequenceNode) {
				value = unboxList(sequenceNode.getValue());
			} else if(tuple.getValueNode() instanceof MappingNode mappingNode) {
				value = unboxMap(mappingNode.getValue());
			}
			result.put(key, value);
		});
		return result;
	}

	static {
		LoaderOptions loaderOptions = new LoaderOptions();
		loaderOptions.setTagInspector(tag -> true);
		loaderOptions.setAllowDuplicateKeys(false);
		INSTANCE = new YamlParser(loaderOptions);
		scalarConstruct("!getOrLoad", Resources::getOrLoad);
		sequenceConstruct("!Vec2f", list -> new Vec2f(Float.parseFloat(list.get(0).toString()), Float.parseFloat(list.get(1).toString())));
		sequenceConstruct("!Vec3f", list -> new Vec3f(Float.parseFloat(list.get(0).toString()), Float.parseFloat(list.get(1).toString()), Float.parseFloat(list.get(2).toString())));
		sequenceConstruct("!Vec4f", list -> new Vec4f(Float.parseFloat(list.get(0).toString()), Float.parseFloat(list.get(1).toString()), Float.parseFloat(list.get(2).toString()), Float.parseFloat(list.get(3).toString())));
		sequenceConstruct("!Vec2i", list -> new Vec2i(Integer.parseInt(list.get(0).toString()), Integer.parseInt(list.get(1).toString())));
		sequenceConstruct("!Vec3i", list -> new Vec3i(Integer.parseInt(list.get(0).toString()), Integer.parseInt(list.get(1).toString()), Integer.parseInt(list.get(2).toString())));
		sequenceConstruct("!Vec4i", list -> new Vec4i(Integer.parseInt(list.get(0).toString()), Integer.parseInt(list.get(1).toString()), Integer.parseInt(list.get(2).toString()), Integer.parseInt(list.get(3).toString())));
		sequenceConstruct("!Color3f", list -> new Color3f(Float.parseFloat(list.get(0).toString()), Float.parseFloat(list.get(1).toString()), Float.parseFloat(list.get(2).toString())));
		sequenceConstruct("!Color4f", list -> new Color4f(Float.parseFloat(list.get(0).toString()), Float.parseFloat(list.get(1).toString()), Float.parseFloat(list.get(2).toString()), Float.parseFloat(list.get(3).toString())));
	}
}
