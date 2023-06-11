package io.github.lwjre.resources;

import java.util.ArrayList;

public abstract class GLResource {

	/** List of all the created resources */
	private static final ArrayList<GLResource> RESOURCES = new ArrayList<>();

	/**
	 * Adds this resource to the list of resources.
	 */
	public GLResource() {
		RESOURCES.add(this);
	}

	/**
	 * Deletes this resource when it is no longer needed.
	 * This method is called in {@link GLResource#deleteAll()} and should generally not be called in other places.
	 */
	protected abstract void delete();

	/**
	 * Deletes all the loaded resources.
	 * Called when the application is terminated.
	 */
	public static void deleteAll() {
		RESOURCES.forEach(GLResource::delete);
	}
}