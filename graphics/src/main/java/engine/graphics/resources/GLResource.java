package engine.graphics.resources;

import org.lwjgl.opengl.GL;

import java.util.ArrayList;

public abstract class GLResource {

	static {
		// TODO: This should not be here but it works for now
		GL.createCapabilities();
	}

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
