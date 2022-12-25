package io.github.view.core;

import io.github.view.graphics.RenderingSystem3D;
import io.github.view.math.Color;

public class PointLight3D extends Position3D {

	private Color color = Color.WHITE;

	@Override
	protected void onStart() {
		RenderingSystem3D.addLight(this);
		super.onStart();
	}

	@Override
	protected void onExit() {
		RenderingSystem3D.removeLight(this);
		super.onExit();
	}

	public final Color getColor() {
		return this.color;
	}

	public final void setColor(Color color) {
		if(color == null) this.color = Color.BLACK;
		else this.color = color;
	}
}
