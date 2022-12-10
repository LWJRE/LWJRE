package io.github.view.core;

import java.util.HashMap;
import java.util.function.Function;

public final class SceneObject {

	private final HashMap<Class<? extends Script>, Script> scripts = new HashMap<>();
	public final Scene scene;

	SceneObject(Scene scene) {
		this.scene = scene;
	}

	public <S extends Script> S addScript(Function<SceneObject, S> constructor) {
		S script = constructor.apply(this);
		// TODO: Avoid inheritance causing duplicate scripts
		this.scripts.put(script.getClass(), script);
		return script;
	}

	public <S extends Script> S getScript(Class<S> type) {
		return type.cast(this.scripts.get(type));
	}

	public void process(float time) {
		this.scripts.values().removeIf(Script::wasRemoved);
		this.scripts.values().forEach(script -> {
			switch(script.getState()) {
				case NEW -> script.onStart();
				case READY -> script.onUpdate(time);
				case TO_BE_REMOVED -> script.onExit();
			}
		});
	}
}
