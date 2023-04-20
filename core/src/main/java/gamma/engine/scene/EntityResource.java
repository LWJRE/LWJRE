package gamma.engine.scene;

import gamma.engine.resources.Resources;
import gamma.engine.utils.Reflection;

import java.util.HashMap;

/**
 * Class that represents an entity resource used to load entities and scenes from files.
 * Actual entities can be created with {@link EntityResource#instance()}.
 *
 * @author Nico
 */
public class EntityResource {

	// TODO: Write tests

	/**
	 * Loads the entity resource at the given path in the classpath or returns the same instance if it was already loaded.
	 * Loaded entity resources are stored in a hash map for immediate access after they were loaded for the first time.
	 * If the given path is null or an empty string, or if the resource at the given path is not an entity resource, the entity resource returned will be empty.
	 *
	 * @param path Path of the entity resource to load
	 * @return The requested entity resource
	 */
	public static EntityResource getOrLoad(String path) {
		if(path != null && !(path.isEmpty() || path.isBlank())) {
			Object resource = Resources.getOrLoad(path);
			if(resource instanceof EntityResource entityResource) {
				return entityResource;
			}
		}
		return new EntityResource();
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
}
