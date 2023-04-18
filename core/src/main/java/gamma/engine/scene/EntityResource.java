package gamma.engine.scene;

import gamma.engine.resources.Resource;
import gamma.engine.resources.Resources;
import gamma.engine.resources.YamlUtils;
import gamma.engine.utils.Reflection;

import java.util.HashMap;
import java.util.Map;

/**
 * Used for deserialization of entities. Entities are loaded from {@code .yaml} files as instances of {@code EntityResource}
 * and are then instantiated with {@link EntityResource#instance()}.
 *
 * @author Nico
 */
public class EntityResource implements Resource {

	// TODO: Write tests

	static {
		Resources.addLoader(EntityResource::load, ".yaml"); // TODO: Different .yaml resources?
		YamlUtils.addMappingRepresent(EntityResource.class, EntityResource::serialize);
	}

	/**
	 * Loads an entity from the classpath or returns the same instance if it was already loaded.
	 *
	 * @see Resources#getOrLoad(String)
	 *
	 * @param path Path to the entity resource file
	 * @return The requested entity resource
	 * @throws RuntimeException if the resource at the given path is not an entity resource
	 */
	public static EntityResource getOrLoad(String path) {
		Object resource = Resources.getOrLoad(path);
		if(resource instanceof EntityResource entityResource)
			return entityResource;
		throw new RuntimeException("Resource at path " + path + " is not an EntityResource");
	}

	/**
	 * Loads an entity resource from the classpath.
	 *
	 * @param path Path to the entity resource file
	 * @return The requested entity resource
	 */
	public static EntityResource load(String path) {
		return YamlUtils.parseResource(path, EntityResource.class);
	}

	/** Path to the base entity resource file if this entity uses another one as a base */
	public String base = "";
	/** Map containing the components to use when instantiating this entity */
	public final HashMap<String, HashMap<String, Object>> components = new HashMap<>();
	/** Map containing this entity's children */
	public final HashMap<String, EntityResource> children = new HashMap<>();

	/**
	 * Instantiates this resource.
	 * This method may be called an indefinite amount of times on the same instance to create different entities.
	 *
	 * @return A newly instantiated entity
	 */
	public final Entity instance() {
		Entity entity = new Entity();
		HashMap<Class<?>, Component> components = new HashMap<>();
		HashMap<String, EntityResource> children = new HashMap<>(this.children);
		if(!this.base.isEmpty()) {
			EntityResource base = getOrLoad(this.base);
			base.components.forEach((type, data) -> {
				Component component = instantiateComponent(type, data);
				Class<?> key = Component.getDirectSubclass(component.getClass());
				components.put(key, component);
			});
			children.putAll(base.children);
		}
		this.components.forEach((type, data) -> {
			Component component = instantiateComponent(type, data);
			Class<?> key = Component.getDirectSubclass(component.getClass());
			components.put(key, component);
		});
		components.values().forEach(entity::addComponent);
		children.forEach((key, resource) -> entity.addChild(key, resource.instance()));
		return entity;
	}

	/**
	 * Instantiates a component using reflection.
	 *
	 * @param type Name of the component's class
	 * @param data A map containing the fields to be set in the component
	 * @return A newly instantiated component
	 */
	private static Component instantiateComponent(String type, HashMap<String, Object> data) {
		Component component = (Component) Reflection.instantiate(type);
		data.forEach((field, value) -> Reflection.setField(component, field, value));
		return component;
	}

	private static Map<?, ?> serialize(EntityResource entityResource) {
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
