package io.github.ardentengine.opengl;

import io.github.ardentengine.core.math.Matrix2x3;
import io.github.ardentengine.core.math.Vector2;
import io.github.ardentengine.core.rendering.Texture;

// TODO: Make a better implementation of this thing

public record DrawData2D(Texture texture, Vector2 vertexOffset, Vector2 vertexScale, Vector2 uvOffset, Vector2 uvScale, Matrix2x3 transform) {
}