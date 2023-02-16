package gamma.engine.core.scene;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

public final class Entity {

	private final HashMap<String, Entity> children = new HashMap<>();
	private final ComponentsSet components = new ComponentsSet();
	private transient Entity parent;

	public void addChild(Entity entity) {
		if(entity.parent == null) {
			int index = 0;
			while(this.children.containsKey("Entity" + index))
				index++;
			this.addChild("Entity" + index, entity);
		} else {
			throw new IllegalStateException("Cannot add entity " + entity + " as a child of " + this + " because it already has parent " + entity.parent);
		}
	}

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

	public boolean hasChild(String key) {
		return this.children.containsKey(key);
	}

	public Entity removeChild(String key) {
		if(this.children.containsKey(key)) {
			Entity entity = this.children.remove(key);
			entity.parent = null;
			return entity;
		} else {
			throw new IllegalStateException("Entity " + this + " does not have a child with key " + key);
		}
	}

	public void removeChild(Entity entity) {
		if(this.children.values().remove(entity)) {
			entity.parent = null;
		} else {
			throw new IllegalStateException("Entity " + entity + " is not a child of " + this);
		}
	}

	public Entity getParent() {
		return this.parent;
	}

	public Optional<Entity> getChild(String key) {
		return Optional.ofNullable(this.children.get(key));
	}

	public Stream<Entity> getChildren() {
		return this.children.values().stream();
	}

	public int getChildCount() {
		return this.children.size();
	}

	public void addComponent(Component component) {
		if(component.entity == null) {
			this.components.add(component);
			component.entity = this;
		} else {
			throw new IllegalStateException("Component " + component + " already belongs to entity " + component.entity);
		}
	}

	public <C extends Component> Optional<C> getComponent(Class<C> type) {
		return Optional.ofNullable(type.cast(this.components.get(type)));
	}

	public Stream<Component> getComponents() {
		return this.components.stream();
	}

	public <C extends Component> Optional<C> getComponentInChildren(Class<C> type) {
		return this.getChildren()
				.map(entity -> entity.getComponent(type))
				.findFirst()
				.flatMap(c -> c);
	}

	public Stream<Component> getComponentsInChildren() {
		return this.getChildren().flatMap(Entity::getComponents);
	}

	public <C extends Component> Stream<C> getComponentsInChildren(Class<C> type) {
		return this.getChildren()
				.map(entity -> entity.getComponent(type))
				.filter(Optional::isPresent)
				.map(Optional::get);
	}

	public <C extends Component> Optional<C> getComponentInParent(Class<C> type) {
		return this.parent != null ? this.parent.getComponent(type) : Optional.empty();
	}

	public Stream<Component> getComponentsInParent() {
		return this.parent != null ? this.parent.getComponents() : Stream.empty();
	}

	// TODO: Remove component
}
