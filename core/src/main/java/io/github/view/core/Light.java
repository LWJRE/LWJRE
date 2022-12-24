package io.github.view.core;

import io.github.view.math.Color;

public class Light extends Node {

	private Color color = Color.WHITE;

	public final Color getColor() {
		return this.color;
	}

	public final void setColor(Color color) {
		if(color == null) this.color = Color.BLACK;
		else this.color = color;
	}
}
