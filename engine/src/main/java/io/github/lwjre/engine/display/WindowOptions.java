package io.github.lwjre.engine.display;

public class WindowOptions {

	private String title = "untitled";
	private int width = 400;
	private int height = 300;
	private boolean visible = false;
	private boolean resizable = true;
	private boolean decorated = true;
	private boolean focused = true;
	private boolean maximized = false;

	public WindowOptions title(String title) {
		this.title = title;
		return this;
	}

	public WindowOptions width(int width) {
		this.width = width;
		return this;
	}

	public WindowOptions height(int height) {
		this.height = height;
		return this;
	}

	public WindowOptions size(int width, int height) {
		return this.width(width).height(height);
	}

	public WindowOptions visible(boolean visible) {
		this.visible = visible;
		return this;
	}

	public WindowOptions resizable(boolean resizable) {
		this.resizable = resizable;
		return this;
	}

	public WindowOptions decorated(boolean decorated) {
		this.decorated = decorated;
		return this;
	}

	public WindowOptions focused(boolean focused) {
		this.focused = focused;
		return this;
	}

	public WindowOptions maximized(boolean maximized) {
		this.maximized = maximized;
		return this;
	}

	public String title() {
		return this.title;
	}

	public int width() {
		return this.width;
	}

	public int height() {
		return this.height;
	}

	public boolean visible() {
		return this.visible;
	}

	public boolean resizable() {
		return this.resizable;
	}

	public boolean decorated() {
		return this.decorated;
	}

	public boolean focused() {
		return this.focused;
	}

	public boolean maximized() {
		return this.maximized;
	}
}
