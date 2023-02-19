package gamma.engine.core.scene;

import java.util.Optional;

/**
 * Base component class.
 *
 * @author Nico
 */
public abstract class Component {

	/** The entity to which this component is attached */
	transient Entity entity;
	/** Current state of this component */
	private transient State currentState = State.NEW;

	/**
	 * Processes this component.
	 * If the component just entered the scene tree, this method will call the {@link Component#onStart()} method,
	 * otherwise il will call the {@link Component#onUpdate(float)} method.
	 *
	 * @param delta Time elapsed since the previous update.
	 */
	public final void process(float delta) {
		switch(this.currentState) {
			case NEW -> this.onStart();
			case READY -> this.onUpdate(delta);
		}
		this.currentState = State.READY;
	}

	/**
	 * Called on the first frame that this component spends in the scene tree.
	 */
	protected void onStart() {

	}

	/**
	 * Called every frame when the component is in the scene tree.
	 *
	 * @param delta Time elapsed since the previous update
	 */
	protected void onUpdate(float delta) {

	}

	// TODO: On Exit?

	/**
	 * Gets an {@link Optional} representing the entity to which this component is attached
	 * or an empty {@code Optional} if this component is not attached to any entity.
	 *
	 * @return An {@code Optional} representing the entity to which this component is attached
	 * or an empty {@code Optional} if this component is not attached to any entity
	 */
	public final Optional<Entity> getEntity() {
		return Optional.ofNullable(this.entity);
	}

	/**
	 * Gets an {@link Optional} representing a component attached to the same entity of the given type,
	 * or an empty {@code Optional} if the entity does not have any component of that type
	 * or if this component is not attached to any entity.
	 *
	 * @param type Type of the requested component
	 * @return An {@code Optional} representing a component attached to the same entity of the given type,
	 * or an empty {@code Optional} if the entity does not have any component of that type
	 * or if this component is not attached to any entity.
	 * @param <C> Type of the requested component
	 */
	public final <C extends Component> Optional<C> getComponent(Class<C> type) {
		return this.getEntity().flatMap(entity -> entity.getComponent(type));
	}

	/**
	 * Gets an {@link Optional} representing a component attached to the parent of the entity of the given type,
	 * or an empty {@code Optional} if the parent entity does not have any component of that type
	 * or if this component is not attached to any entity
	 * or if the entity does not have a parent.
	 *
	 * @param type Type of the requested component
	 * @return an {@code Optional} representing a component attached to the parent of the entity of the given type,
	 * or an empty {@code Optional} if the parent entity does not have any component of that type
	 * or if this component is not attached to any entity
	 * or if the entity does not have a parent.
	 * @param <C> Type of the requested component
	 */
	public final <C extends Component> Optional<C> getComponentInParent(Class<C> type) {
		return this.getEntity().flatMap(entity -> entity.getComponentInParent(type));
	}

	/**
	 * Component state.
	 */
	private enum State {
		NEW,
		READY
	}
}
