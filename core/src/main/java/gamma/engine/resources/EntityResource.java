package gamma.engine.resources;

import gamma.engine.scene.Component;
import gamma.engine.scene.Entity;
import gamma.engine.utils.YamlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EntityResource {

	public String base = "";
	private final ArrayList<Component> components = new ArrayList<>();
	private final HashMap<String, EntityResource> children = new HashMap<>();

	public final Entity instance() {
		Entity entity = new Entity();
		HashMap<Class<?>, Component> components = new HashMap<>();
		HashMap<String, EntityResource> children = new HashMap<>(this.children);
		if(!this.base.isEmpty()) {
			EntityResource base = YamlUtils.parseResource(this.base, EntityResource.class);
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
