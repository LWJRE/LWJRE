package io.github.lwjre.engine.nodes

import io.github.hexagonnico.vecmatlib.vector.Vec3f

class TestScalaNode extends Node3D {

  override protected def onEnter(): Unit = {
    println("Hello from scala node!")
    println(this.position + Vec3f(1.0f, 1.0f, 2.0f));
    super.onEnter()
  }
}
