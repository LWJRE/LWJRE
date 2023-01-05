package engine.core.resources;

import java.util.ArrayList;

public abstract class GLResource {

	private static final ArrayList<GLResource> RESOURCES = new ArrayList<>();

	public GLResource() {
		RESOURCES.add(this);
	}

	protected abstract void delete();

	public static void cleanUp() {
		System.out.println("Deleting " + RESOURCES.size() + " resources");
		RESOURCES.forEach(GLResource::delete);
	}
}
