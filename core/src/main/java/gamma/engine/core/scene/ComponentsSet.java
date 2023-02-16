package gamma.engine.core.scene;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ComponentsSet extends AbstractSet<Component> implements Set<Component> {

	private final HashMap<Class<?>, Component> map = new HashMap<>();

	@Override
	public boolean add(Component component) {
		return this.map.put(getKey(component), component) == null;
	}

	public Component get(Class<?> type) {
		return this.map.get(getKey(type));
	}

	@Override
	public Iterator<Component> iterator() {
		return this.map.values().iterator();
	}

	@Override
	public int size() {
		return this.map.size();
	}

	private static Class<?> getKey(Component component) {
		return getKey(component.getClass());
	}

	private static Class<?> getKey(Class<?> type) {
		Class<?> superclass = type.getSuperclass();
		if(superclass.equals(Object.class))
			throw new IllegalArgumentException("Class " + type + " does not extend the Component class");
		if(superclass.equals(Component.class))
			return type;
		return getKey(superclass);
	}
}
