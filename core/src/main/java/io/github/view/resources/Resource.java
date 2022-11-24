package io.github.view.resources;

import java.util.ArrayList;

public abstract class Resource {

	private static final ArrayList<Resource> RESOURCES = new ArrayList<>();

	public Resource() {
		RESOURCES.add(this);
	}

	protected abstract void delete();

	public static void cleanUp() {
		System.out.println("Deleting " + RESOURCES.size() + " resources");
		RESOURCES.forEach(Resource::delete);
	}
}
