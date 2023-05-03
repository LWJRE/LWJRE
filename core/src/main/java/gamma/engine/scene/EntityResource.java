package gamma.engine.scene;

import gamma.engine.resources.Resources;
import gamma.engine.utils.Reflection;

import java.util.ArrayList;
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

	public String name = "";
	/** Path to the base entity resource file if this entity uses another one as a base */
	public String base = "";
	/** Map containing the components to use when instantiating this entity */
	public final HashMap<String, HashMap<String, Object>> components = new HashMap<>();
	/** Map containing this entity's children */
	public final ArrayList<EntityResource> children = new ArrayList<>();

	/**
	 * Instantiates this resource.
	 * This method may be called an indefinite amount of times on the same instance to create different entities.
	 *
	 * @return A newly instantiated entity
	 */
	public final Entity instance() {
		Entity entity = new Entity();
		HashMap<Class<?>, Component> components = new HashMap<>();
		ArrayList<EntityResource> children = new ArrayList<>(this.children);
		if(this.base != null && !this.base.isEmpty()) {
			EntityResource base = getOrLoad(this.base);
			if(base.name != null && !base.name.isEmpty()) {
				entity.setName(base.name);
			}
			base.components.forEach((type, data) -> {
				Component component = instantiateComponent(type, data);
				Class<?> key = Component.getDirectSubclass(component.getClass());
				components.put(key, component);
			});
			children.addAll(base.children);
		}
		this.components.forEach((type, data) -> {
			Component component = instantiateComponent(type, data);
			Class<?> key = Component.getDirectSubclass(component.getClass());
			components.put(key, component);
		});
		if(this.name != null && !this.name.isEmpty()) {
			entity.setName(this.name);
		}
		components.values().forEach(entity::addComponent);
		children.forEach(resource -> entity.addChild(resource.instance()));
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

	/**
	 * Converts this entity resource to a {@link HashMap}. Used for serialization.
	 *
	 * @return A {@code HashMap} containing the values of {@link EntityResource#base}, {@link EntityResource#components}, and {@link EntityResource#children} if they are not empty
	 */
	public HashMap<String, Object> toMap() {
		HashMap<String, Object> map = new HashMap<>();
		if(this.base != null && !(this.base.isEmpty() || this.base.isBlank()))
			map.put("base", this.base);
		if(!this.components.isEmpty())
			map.put("components", this.components);
		if(!this.children.isEmpty())
			map.put("children", this.children);
		return map;
	}
}
