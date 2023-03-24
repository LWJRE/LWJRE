package gamma.engine.core.scene;

import gamma.engine.core.input.InputEvent;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Base class for all components.
 * The direct subclass of this class is the key used in the entity's components hash map.
 *
 * @author Nico
 */
public abstract class Component {

	/**
	 * The entity to which this component is attached
	 */
	transient Entity entity = null;

	/**
	 * Called when this component is added to an entity or when the entity enters the scene.
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
	 * Called when this component is removed from the entity it is attached to or when that entity is removed from the scene.
	 */
	protected void onExit() {

	}

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
	 * @param <C>  Type of the requested component
	 * @return An {@code Optional} representing a component attached to the same entity of the given type,
	 * or an empty {@code Optional} if the entity does not have any component of that type
	 * or if this component is not attached to any entity.
	 */
	public final <C extends Component> Optional<C> getComponent(Class<C> type) {
		return this.getEntity().flatMap(entity -> entity.getComponent(type));
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
		return this.getEntity().map(entity -> entity.requireComponent(type)).orElseThrow(() -> new RuntimeException("Component " + this + " does not belong to any entity"));
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
		return this.getEntity().flatMap(entity -> entity.getComponentInParent(type));
	}
}
