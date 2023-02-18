package gamma.engine.core.scene;

import java.util.HashMap;
import java.util.Optional;
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
	private final ComponentsSet components = new ComponentsSet();
	/** This entity's parent */
	private transient Entity parent;

	/**
	 * Adds a child to this entity.
	 *
	 * @param entity The child to add
	 * @throws IllegalStateException if the given entity already has a parent
	 */
	public void addChild(Entity entity) {
		if(entity.parent == null) {
			int index = 0;
			while(this.children.containsKey("Entity" + index))
				index++;
			this.children.put("Entity" + index, entity);
			entity.parent = this;
		} else {
			throw new IllegalStateException("Cannot add entity " + entity + " as a child of " + this + " because it already has parent " + entity.parent);
		}
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
	 * Processes this entity.
	 * Children are processed first, then this entity's components are processed.
	 *
	 * @param delta Time elapsed since the previous frame
	 */
	public void process(float delta) {
		this.children.forEach((key, entity) -> {
			if(entity.parent == null)
				entity.parent = this;
			entity.process(delta);
		});
		this.components.forEach(component -> {
			if(component.entity == null)
				component.entity = this;
			component.process(delta);
		});
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
	 * Removes a child with the given key from this entity.
	 *
	 * @param key The child's key
	 * @return The child entity that was removed
	 * @throws IllegalStateException if this entity does not have a child with the given key
	 */
	public Entity removeChild(String key) {
		if(this.children.containsKey(key)) {
			Entity entity = this.children.remove(key);
			entity.parent = null;
			return entity;
		} else {
			throw new IllegalStateException("Entity " + this + " does not have a child with key " + key);
		}
	}

	/**
	 * Removes the given child from this entity.
	 *
	 * @param entity The child to remove
	 * @throws IllegalStateException if the given entity is not a child of this entity
	 */
	public void removeChild(Entity entity) {
		if(this.children.values().remove(entity)) {
			entity.parent = null;
		} else {
			throw new IllegalStateException("Entity " + entity + " is not a child of " + this);
		}
	}

	/**
	 * Gets this entity's parent or null if it does not have one.
	 *
	 * @return This entity's parent or null if it does not have one
	 */
	public Entity getParent() {
		return this.parent;
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
	 * Returns a {@link Stream} with all of this entity's children.
	 *
	 * @return A {@code Stream} containing all of this entity's children
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
	 * If this entity already has a component of that type, the old component will be replaced.
	 *
	 * @param component The component to add.
	 * @throws IllegalStateException if the given component already belongs to an entity
	 */
	public void addComponent(Component component) {
		if(component.entity == null) {
			// TODO: Remove entity from replaced component
			this.components.add(component);
			component.entity = this;
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
		return Optional.ofNullable(type.cast(this.components.get(type)));
	}

	/**
	 * Returns a {@link Stream} with all of this entity's components.
	 *
	 * @return A {@code Stream} containing all of this entity's components
	 */
	public Stream<Component> getComponents() {
		return this.components.stream();
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
	 * Removes a component of the given type from this entity.
	 *
	 * @param type Type of the component to remove
	 * @return The removed component
	 * @param <C> Type of the component to remove
	 * @throws IllegalStateException if this entity does not have a component of the given type
	 */
	public <C extends Component> C removeComponent(Class<C> type) {
		C component = type.cast(this.components.remove(type));
		if(component == null)
			throw new IllegalStateException("Entity " + this + " does not have a component of type " + type);
		component.entity = null;
		return component;
	}

	/**
	 * Removes the given component from this entity.
	 *
	 * @param component The component to remove
	 * @return True if the component was removed, otherwise false
	 */
	public boolean removeComponent(Component component) {
		if(this.components.remove(component)) {
			component.entity = null;
			return true;
		}
		return false;
	}
}
