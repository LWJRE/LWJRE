package gamma.engine.rendering;

import java.util.ArrayList;

/**
 * Class representing a resource that can be deleted when the application terminates.
 *
 * @author Nico
 */
public abstract class DeletableResource {

	/** List of all the created resources */
	private static final ArrayList<DeletableResource> RESOURCES = new ArrayList<>();

	/**
	 * Adds this resource to the list of resources.
	 */
	public DeletableResource() {
		RESOURCES.add(this);
	}

	/**
	 * Deletes this resource when it is no longer needed.
	 * This method is called in {@link DeletableResource#deleteAll()} and should generally not be called in other places.
	 */
	protected abstract void delete();

	/**
	 * Deletes all the loaded resources.
	 * Called when the application is terminated.
	 */
	public static void deleteAll() {
		RESOURCES.forEach(DeletableResource::delete);
	}
}