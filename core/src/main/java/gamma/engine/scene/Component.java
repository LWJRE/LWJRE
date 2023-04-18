package gamma.engine.scene;

import gamma.engine.input.InputEvent;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Base class for all components.
 * The direct subclass of this class is the key used in the entity's components hash map.
 *
 * @author Nico
 */
public abstract class Component {

	/** The entity to which this component is attached */
	Entity entity = null;
	/** Current state of this component */
	private State state = State.NEW;

	/**
	 * Processes this component.
	 * Called from {@link Entity#process(float)}.
	 *
	 * @param delta Time elapsed since the previous frame
	 * @return True if this component has been removed from its entity, otherwise false
	 */
	protected final boolean process(float delta) {
		return this.process(() -> this.onUpdate(delta));
	}

	/**
	 * Processes this component while in the editor.
	 * Used for debugging.
	 *
	 * @return True if this component has been removed from its entity, otherwise false
	 */
	protected final boolean editorProcess() {
		return this.process(this::editorUpdate);
	}

	/**
	 * Processes this component.
	 *
	 * @param processFunction Either {@code () -> this.onUpdate(delta)} or {@code this::editorUpdate} to update the component
	 * @return True if this component has been removed from its entity, otherwise false
	 */
	private boolean process(Runnable processFunction) {
		// First frame the component spends in the scene
		if(this.state == State.NEW) {
			this.onStart();
			// Register that the component is ready
			this.state = State.READY;
		}
		// Process the component
		processFunction.run();
		// If the component was removed this frame
		if(this.state == State.REMOVED) {
			this.onExit();
			// Register that the component has exited the scene
			this.entity = null;
			// Set the component's state back to new for it to be added again
			this.state = State.NEW;
			// Return true to remove the component from its entity
			return true;
		}
		// Return false because the component has not been removed
		return false;
	}

	/**
	 * Called on the first frame that this component spends in the scene.
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

	/**
	 * Called when an input event occurs, such as a key press or a mouse click.
	 *
	 * @see InputEvent
	 *
	 * @param event The input event
	 */
	protected void onInput(InputEvent event) {

	}

	/**
	 * Called every frame when this component is in the editor.
	 */
	protected void editorUpdate() {

	}

	/**
	 * Called on the last frame that this component spends in the scene before it is removed.
	 */
	protected void onExit() {

	}

	/**
	 * Marks this component to be removed from its entity as soon as it is safe to do so.
	 */
	public final void removeComponent() {
		this.state = State.REMOVED;
	}

	public final Entity entity() {
		return this.entity;
	}

	/**
	 * Gets an {@link Optional} representing a component attached to the same entity of the given type,
	 * or an empty {@code Optional} if the entity does not have any component of that type
	 * or if this component is not attached to any entity.
	 *
	 * @param type Type of the requested component
	 * @param <C>  Type of the requested component
	 * @return An {@code Optional} representing a component attached to the same entity of the given type,
	 * or an empty {@code Optional} if the entity does not have any component of that type
	 * or if this component is not attached to any entity.
	 */
	public final <C extends Component> Optional<C> getComponent(Class<C> type) {
		return this.entity != null ? this.entity.getComponent(type) : Optional.empty();
	}

	/**
	 * Gets a component attached to the same entity of the given type.
	 *
	 * @param type Type of the requested component
	 * @param <C>  Type of the component to get
	 * @return A component of the requested type
	 * @throws NoSuchElementException if this entity does not have a component of the given type
	 * @throws RuntimeException       if this component does not belong to any entity
	 */
	public final <C extends Component> C requireComponent(Class<C> type) {
		if(this.entity == null)
			throw new RuntimeException("Component " + this + " does not belong to any entity");
		return this.entity.requireComponent(type);
	}

	/**
	 * Gets an {@link Optional} representing a component attached to the parent of the entity of the given type,
	 * or an empty {@code Optional} if the parent entity does not have any component of that type
	 * or if this component is not attached to any entity
	 * or if the entity does not have a parent.
	 *
	 * @param type Type of the requested component
	 * @param <C>  Type of the requested component
	 * @return an {@code Optional} representing a component attached to the parent of the entity of the given type,
	 * or an empty {@code Optional} if the parent entity does not have any component of that type
	 * or if this component is not attached to any entity
	 * or if the entity does not have a parent.
	 */
	public final <C extends Component> Optional<C> getComponentInParent(Class<C> type) {
		return this.entity != null ? this.entity.getComponentInParent(type) : Optional.empty();
	}

	// TODO: Finish "get component" methods

	public static Class<?> getDirectSubclass(Class<?> fromClass) {
		Class<?> superclass = fromClass.getSuperclass();
		if(superclass.equals(Object.class))
			throw new IllegalArgumentException("The given class is not a subclass of Component");
		if(superclass.equals(Component.class))
			return fromClass;
		return getDirectSubclass(superclass);
	}

	/**
	 * Used to keep track of the component's state.
	 */
	private enum State {
		/** The component just entered the scene */
		NEW,
		/** The component is in the scene and can be processed */
		READY,
		/** The component is being removed from the scene */
		REMOVED
	}
}
