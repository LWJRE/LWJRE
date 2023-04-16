package gamma.engine.scene;

import gamma.engine.resources.Resource;
import gamma.engine.resources.Resources;
import gamma.engine.resources.YamlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EntityResource implements Resource {

	static {
		Resources.addLoader(EntityResource::load, ".yaml"); // TODO: Different .yaml resources?
		YamlUtils.addMappingRepresent(EntityResource.class, EntityResource::serialize);
	}

	public static EntityResource getOrLoad(String path) {
		Object resource = Resources.getOrLoad(path);
		if(resource instanceof EntityResource entityResource)
			return entityResource;
		throw new RuntimeException("Resource at path " + path + " is not an EntityResource");
	}

	public static EntityResource load(String path) {
		return YamlUtils.parseResource(path, EntityResource.class);
	}

	private String base = "";
	private final ArrayList<Component> components = new ArrayList<>();
	private final HashMap<String, EntityResource> children = new HashMap<>();

	public final Entity instance() {
		Entity entity = new Entity();
		HashMap<Class<?>, Component> components = new HashMap<>();
		HashMap<String, EntityResource> children = new HashMap<>(this.children);
		if(!this.base.isEmpty()) {
			EntityResource base = getOrLoad(this.base);
			base.components.forEach(component -> {
				Class<?> key = Component.getDirectSubclass(component.getClass());
				components.put(key, component);
			});
			children.putAll(base.children);
		}
		this.components.forEach(component -> {
			Class<?> key = Component.getDirectSubclass(component.getClass());
			components.put(key, component);
		});
		components.values().forEach(entity::addComponent);
		children.forEach((key, resource) -> entity.addChild(key, resource.instance()));
		return entity;
	}

	public static Map<?, ?> serialize(EntityResource entityResource) {
		HashMap<String, Object> map = new HashMap<>();
		if(!entityResource.base.isEmpty())
			map.put("base", entityResource.base);
		if(!entityResource.components.isEmpty())
			map.put("components", entityResource.components);
		if(!entityResource.children.isEmpty())
			map.put("children", entityResource.children);
		return map;
	}
}
