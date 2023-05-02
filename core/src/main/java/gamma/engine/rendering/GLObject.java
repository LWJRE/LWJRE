package gamma.engine.rendering;

import java.util.ArrayList;

public abstract class GLObject {

	private static final ArrayList<GLObject> RESOURCES = new ArrayList<>();

	public GLObject() {
		RESOURCES.add(this);
	}

	protected abstract void delete();

	public static void deleteAll() {
		RESOURCES.forEach(GLObject::delete);
	}
}