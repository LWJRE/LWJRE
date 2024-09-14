package io.github.ardentengine.core.input;

import io.github.scalamath.vecmatlib.Vec2f;

public interface InputEventMouse extends InputEvent {

    Vec2f position();
}