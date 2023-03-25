package gamma.engine.core.scene;

import gamma.engine.core.input.InputEvent;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * Represents an entity in the scene. An entity is a node in the scene tree.
 *
 * @author Nico
 */
public final class Entity {

	/** This entity's children */
	private final HashMap<String, Entity> children = new HashMap<>();
	/** Components attached to this entity */
	private final HashMap<Class<?>, Component> components = new HashMap<>();
	/** This entity's parent */
	private transient Entity parent;
	/** Set to true when this entity is being removed from the scene */
	private transient boolean removed = false;

	/**
	 * Processes this entity.
	 * Children are processed first, then this entity's components are processed.
	 *
	 * @param delta Time elapsed since the previous frame
	 */
	public void process(float delta) {
		// Process children first
		this.children.values().removeIf(entity -> {
			// Process child entity
			entity.process(delta);
			// Remove the child if it was removed in this frame
			if(entity.removed) {
				entity.parent = null;
				return true;
			}
			return false;
		});
		// Process this entity's components
		this.components.values().removeIf(component -> component.process(delta));
		// Remove all the components if the entity has been removed in this frame
		if(this.removed) {
			this.components.values().removeIf(component -> {
				component.onExit();
				return true;
			});
		}
	}

	/**
	 * Called when there is an input event.
	 * The event is first passed down to the children, then to all this entity's components.
	 *
	 * @param event The input event.
	 */
	public void input(InputEvent event) {
		this.children.forEach((key, entity) -> entity.input(event));
		this.components.forEach((key, component) -> component.onInput(event));
	}

	/**
	 * Called from the editor.
	 */
	public void editorProcess() {
		// TODO: Change editor process
	}

	/**
	 * Adds a child to this entity.
	 *
	 * @param entity The child to add
	 * @throws IllegalStateException if the given entity already has a parent
	 */
	public void addChild(Entity entity) {
		int index = 0;
		while(this.children.containsKey("Entity" + index))
			index++;
		this.addChild("Entity" + index, entity);
	}

	/**
	 * Adds a child to this entity with the given key.
	 *
	 * @param key The child's key
	 * @param entity The child to add
	 * @throws IllegalStateException if the given entity already has a parent or if this entity already has a child with the given key
	 */
	public void addChild(String key, Entity entity) {
		if(entity.parent == null) {
			if(!this.children.containsKey(key)) {
				this.children.put(key, entity);
				entity.parent = this;
			} else {
				throw new IllegalStateException("Entity " + this + " already has a child with key " + key);
			}
		} else {
			throw new IllegalStateException("Cannot add entity " + entity + " as a child of " + this + " because it already has parent " + entity.parent);
		}
	}

	/**
	 * Checks if this entity has a child with the given key.
	 *
	 * @param key The key to look for.
	 * @return True if this entity has a child with the given key, otherwise false
	 */
	public boolean hasChild(String key) {
		return this.children.containsKey(key);
	}

	/**
	 * Checks if the given entity is a child of this one.
	 *
	 * @param entity The child entity
	 * @return True if this entity is the given entity's parent, otherwise false
	 */
	public boolean hasChild(Entity entity) {
		return entity.parent == this;
	}

	/**
	 * Checks if this entity has a parent.
	 * An entity without a parent is either the root entity or an orphan.
	 *
	 * @return True if this entity has a parent, otherwise false
	 */
	public boolean hasParent() {
		return this.parent != null;
	}

	/**
	 * Checks if the given entity is this entity's parent.
	 *
	 * @param entity The parent entity
	 * @return True if this entity's parent is the given entity, otherwise false
	 */
	public boolean hasParent(Entity entity) {
		return this.parent == entity;
	}

	/**
	 * Gets this entity's parent or null if it does not have one.
	 *
	 * @return This entity's parent or null if it does not have one
	 */
	public Entity getParent() {
		return this.parent;
	}

	// TODO: Test for concurrent modification exception

	public void setParent(Entity parent) {
		if(this.parent != null)
			this.parent.children.values().remove(this);
		this.parent = null;
		parent.addChild(this);
	}

	public void setParent(String key, Entity parent) {
		if(this.parent != null)
			this.parent.children.values().remove(this);
		this.parent = null;
		parent.addChild(key, this);
	}

	/**
	 * Gets an {@link Optional} representing the child with the given key
	 * or an empty {@code Optional} if this entity does not have a child with the given key.
	 *
	 * @param key The child's key
	 * @return An {@code Optional} representing the child with the given key
	 * or an empty {@code Optional} if this entity does not have a child with the given key.
	 */
	public Optional<Entity> getChild(String key) {
		return Optional.ofNullable(this.children.get(key));
	}

	/**
	 * Performs the given action on all this entity's children.
	 *
	 * @param action A {@link BiConsumer} taking the child's key and the child.
	 */
	public void forEachChild(BiConsumer<String, Entity> action) {
		this.children.forEach(action);
	}

	/**
	 * Returns this entity's children as a {@link Stream} for easy iteration.
	 * To get an unmodifiable list of the children use {@link Stream#toList()}.
	 *
	 * @return A {@code Stream} of this entity's children
	 */
	public Stream<Entity> getChildren() {
		return this.children.values().stream();
	}

	/**
	 * Gets the number of children of this entity.
	 *
	 * @return The number of children of this entity
	 */
	public int getChildCount() {
		return this.children.size();
	}

	/**
	 * Adds the given component to this entity.
	 * The component's {@link Component#onStart()} method will be called on the first frame that the component spends in the scene.
	 * If this entity already has a component of the same type, it must be removed first.
	 *
	 * @param component The component to add.
	 * @throws IllegalStateException if the given component already belongs to an entity or if this entity already has a component of the given type
	 */
	public void addComponent(Component component) {
		if(component.entity == null) {
			Class<?> type = getKey(component.getClass());
			if(!this.components.containsKey(type)) {
				this.components.put(type, component);
				component.entity = this;
			} else {
				throw new IllegalStateException("Entity " + this + " already has a component of type " + type);
			}
		} else {
			throw new IllegalStateException("Component " + component + " already belongs to entity " + component.entity);
		}
	}

	/**
	 * Gets an {@link Optional} representing a component of this entity
	 * or an empty {@code Optional} if this entity does not have a component of the given type.
	 *
	 * @param type Type of the requested component
	 * @return An {@code Optional} representing a component of this entity
	 * or an empty {@code Optional} if this entity does not have a component of the given type
	 * @param <C> Type of the component to get
	 */
	public <C extends Component> Optional<C> getComponent(Class<C> type) {
		return Optional.ofNullable(type.cast(this.components.get(getKey(type))));
	}

	/**
	 * Gets a component from this entity.
	 *
	 * @param type Type of the requested component
	 * @return A component of the requested type
	 * @param <C> Type of the component to get
	 * @throws NoSuchElementException if this entity does not have a component of the given type
	 */
	public <C extends Component> C requireComponent(Class<C> type) {
		return this.getComponent(type).orElseThrow(() -> new NoSuchElementException("Entity " + this + " does not have a component of type " + type));
	}

	// TODO: Add other 'requireComponent' methods

	/**
	 * Returns a {@link Stream} with all of this entity's components.
	 *
	 * @return A {@code Stream} containing all of this entity's components
	 */
	public Stream<Component> getComponents() {
		return this.components.values().stream();
	}

	/**
	 * Gets an {@link Optional} representing the first component of the given type in one of this entity's children
	 * or an empty {@code Optional} if this entity does not have any children
	 * or if none of them has a component of the given type.
	 *
	 * @param type Type of the requested component
	 * @return An {@link Optional} representing the first component of the given type in one of this entity's children
	 * or an empty {@code Optional} if this entity does not have any children
	 * or if none of them has a component of the given type
	 * @param <C> Type of the component to get
	 */
	public <C extends Component> Optional<C> getComponentInChildren(Class<C> type) {
		return this.getChildren()
				.map(entity -> entity.getComponent(type))
				.findFirst()
				.flatMap(c -> c);
	}

	/**
	 * Returns a {@link Stream} with all the components in this entity's children.
	 *
	 * @return A {@code Stream} containing all the components in this entity's children
	 */
	public Stream<Component> getComponentsInChildren() {
		return this.getChildren().flatMap(Entity::getComponents);
	}

	/**
	 * Returns a {@link Stream} with all the components of the given type in this entity's children.
	 *
	 * @param type Type of the requested component
	 * @return A {@code Stream} with all the components of the given type in this entity's children
	 * @param <C> Type of the component to get
	 */
	public <C extends Component> Stream<C> getComponentsInChildren(Class<C> type) {
		return this.getChildren()
				.map(entity -> entity.getComponent(type))
				.filter(Optional::isPresent)
				.map(Optional::get);
	}

	/**
	 * Gets an {@link Optional} representing a component of this entity's parent
	 * or an empty {@code Optional} if this entity does not have a parent
	 * or if its parent does not have a component of the given type.
	 *
	 * @param type Type of the requested component
	 * @return An {@code Optional} representing a component of this entity's parent
	 * or an empty {@code Optional} if this entity does not have a parent
	 * or if its parent does not have a component of the given type
	 * @param <C> Type of the component to get
	 */
	public <C extends Component> Optional<C> getComponentInParent(Class<C> type) {
		return this.parent != null ? this.parent.getComponent(type) : Optional.empty();
	}

	/**
	 * Returns a {@link Stream} with all the components in this entity's parent.
	 *
	 * @return A {@code Stream} containing all the components in this entity's parent
	 */
	public Stream<Component> getComponentsInParent() {
		return this.parent != null ? this.parent.getComponents() : Stream.empty();
	}

	/**
	 * Marks a component of the given type to be removed from this entity.
	 * The component's {@link Component#onExit()} method will be called on the last frame that the component spends in the scene.
	 * This method does nothing if this entity does not have a component of the given type.
	 *
	 * @param type The type of the component to remove
	 * @return True if a component was removed, false if this entity does not have a component of the given type
	 */
	public boolean removeComponent(Class<? extends Component> type) {
		return this.getComponent(type).map(component -> {
			component.removeComponent();
			return true;
		}).orElse(false);
	}

	/**
	 * Marks the given component to be removed from this entity.
	 * Calling this method is equivalent to calling {@link Component#removeComponent()}.
	 * The component's {@link Component#onExit()} method will be called on the last frame that the component spends in the scene.
	 * This method does nothing if the given component is not attached to this entity.
	 *
	 * @param component The component to remove
	 * @return True if the component was removed, false if the given component is not attached to this entity
	 */
	public boolean removeComponent(Component component) {
		if(this.components.containsValue(component)) {
			component.removeComponent();
			return true;
		}
		return false;
	}

	/**
	 * Marks this entity and all of its children to be removed from the scene tree at the end of the process frame.
	 * When an entity is removed, its parent is set to null and {@link Component#onExit()} is called on all its components.
	 * Note that an entity that is removed from the scene tree it is not meant to be added again.
	 * Use {@link Entity#setParent(Entity)} to change the entity's parent.
	 */
	public void removeFromScene() {
		this.children.values().forEach(Entity::removeFromScene);
		this.removed = true;
	}

	/**
	 * Serializes the given entity. Used to write entity to {@code .yaml} files.
	 *
	 * @param entity The entity to serialize
	 * @return An unmodifiable map with the key "children" mapping to a copy of this entity's children map
	 * and a key "components" mapping to a copy of this entity's component list
	 */
	public static Map<String, Object> serialize(Entity entity) {
		return Map.of("children", new HashMap<>(entity.children), "components", entity.getComponents().toList());
	}

	/**
	 * Deserializes an entity. Used to read entities from {@code .yaml} files.
	 *
	 * @param map A map containing the entity's children and components
	 * @return The deserialized entity
	 */
	public static Entity deserialize(Map<?, ?> map) {
		Entity entity = new Entity();
		Optional.ofNullable((List<?>) map.get("components")).ifPresent(components -> components.forEach(obj -> {
			if(obj instanceof Component component)
				entity.addComponent(component);
		}));
		Optional.ofNullable(((Map<?, ?>) map.get("children"))).ifPresent(children -> children.forEach((key, value) -> {
			if(value instanceof Entity child && key instanceof String string)
				entity.addChild(string, child);
		}));
		return entity;
	}

	/**
	 * Gets the component key from a class.
	 *
	 * @param from Class to start from
	 * @return Component key
	 */
	private static Class<?> getKey(Class<?> from) {
		Class<?> superclass = from.getSuperclass();
		if(superclass.equals(Object.class))
			throw new IllegalArgumentException("The given class is not a subclass of Component");
		if(superclass.equals(Component.class))
			return from;
		return getKey(superclass);
	}
}
