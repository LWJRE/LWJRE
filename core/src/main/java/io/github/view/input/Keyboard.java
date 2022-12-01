package io.github.view.input;

import org.lwjgl.glfw.GLFW;

import java.util.HashSet;

public final class Keyboard {

	private static final HashSet<Integer> KEYS = new HashSet<>();

	public static void registerKeyEvent(int key, int action) {
		if(action == GLFW.GLFW_PRESS)
			KEYS.add(key);
		else if(action == GLFW.GLFW_RELEASE)
			KEYS.remove(key);
	}

	public static boolean isKeyDown(int key) {
		return KEYS.contains(key);
	}
}
