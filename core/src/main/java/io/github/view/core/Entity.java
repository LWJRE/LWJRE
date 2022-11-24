package io.github.view.core;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public final class Entity extends TreeNode {

	private final CopyOnWriteArrayList<Component> components = new CopyOnWriteArrayList<>();

	public void addComponent(Function<Entity, Component> constructor) {
		Component component = constructor.apply(this);
		this.components.add(component);
	}

	@Override
	public void process() {
		super.process();
		this.components.removeIf(Component::wasRemoved);
		this.components.forEach(component -> {
			switch(component.getProcessState()) {
				case NEW -> component.onStart();
				case READY -> component.onUpdate();
				case TO_BE_REMOVED -> component.onExit();
			}
		});
	}

	@Override
	public void render() {
		super.render();
		this.components.removeIf(Component::wasRemoved);
		this.components.forEach(component -> {
			switch(component.getRenderingState()) {
				case NEW -> component.prepareRendering();
				case READY -> component.render();
				case TO_BE_REMOVED -> component.cleanUpRendering();
			}
		});
	}
}
