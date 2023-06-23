package io.github.lwjre.engine.physics

import io.github.hexagonnico.vecmatlib.vector.Vec3f
import io.github.lwjre.engine.nodes.{DynamicBody3D, KinematicBody3D, RigidBody3D}

object CollisionSolver {

  def solve(collider: KinematicBody3D, normal: Vec3f, depth: Float): Unit = {
    collider.position = collider.position + normal * depth
    collider.velocity = collider.velocity.slide(normal)
  }

  def solve(colliderA: DynamicBody3D, normal: Vec3f, depth: Float): Unit = {
    colliderA.position = colliderA.position + normal * depth
    val impulse = -(1.0f + colliderA.restitution) * (-colliderA.velocity dot normal) / (1.0f / colliderA.mass)
    colliderA.applyImpulse(normal * -impulse)
  }

  def solve(colliderA: DynamicBody3D, colliderB: KinematicBody3D, normal: Vec3f, depth: Float): Unit = {
    colliderA.position = colliderA.position + normal * depth
    val relativeVelocity = colliderB.velocity - colliderA.velocity
    val impulse = -(1.0f + colliderA.restitution) * (relativeVelocity dot normal) / (1.0f / colliderA.mass)
    colliderA.applyImpulse(normal * -impulse)
  }

  def solve(colliderA: DynamicBody3D, colliderB: DynamicBody3D, normal: Vec3f, depth: Float): Unit = {
    colliderA.position = colliderA.position - normal * (depth / 2)
    colliderB.position = colliderB.position + normal * (depth / 2)
    val relativeVelocity = colliderB.velocity - colliderA.velocity
    if((relativeVelocity dot normal) <= 0.0f) {
      val restitution = math.min(colliderA.restitution, colliderB.restitution)
      val impulse = -(1.0f + restitution) * (relativeVelocity dot normal) / (1.0f / colliderA.mass + 1.0f / colliderB.mass)
      colliderA.applyImpulse(normal * -impulse)
      colliderB.applyImpulse(normal * impulse)
    }
  }

  def solve(colliderA: RigidBody3D, contactPoints: java.util.Collection[Vec3f], normal: Vec3f, depth: Float): Unit = {
    colliderA.position = colliderA.position + normal * depth
    val positionA = colliderA.globalPosition
    val linearVelocityA = colliderA.velocity
    val angularVelocityA = colliderA.angularVelocity
    contactPoints.forEach(point => {
      val radiusA = point - positionA
      val relativeAngularVelocity = -(angularVelocityA cross radiusA)
      val relativeVelocity = -linearVelocityA + relativeAngularVelocity
      val impulse = (relativeVelocity * -(1.0f + colliderA.restitution) dot normal) / (1.0f / colliderA.mass + (colliderA.inverseInertiaTensor * (radiusA cross normal) cross radiusA dot normal))
      colliderA.applyImpulse(normal * -impulse / contactPoints.size, radiusA)
    })
  }

  def solve(colliderA: RigidBody3D, colliderB: KinematicBody3D, contactPoints: java.util.Collection[Vec3f], normal: Vec3f, depth: Float): Unit = {
    colliderA.position = colliderA.position + normal * depth
    val positionA = colliderA.globalPosition
    val linearVelocityA = colliderA.velocity
    val linearVelocityB = colliderB.velocity
    val angularVelocityA = colliderA.angularVelocity
    contactPoints.forEach(point => {
      val radiusA = point - positionA
      val relativeAngularVelocity = -(angularVelocityA cross radiusA)
      val relativeVelocity = (linearVelocityB - linearVelocityA) + relativeAngularVelocity
      val impulse = (relativeVelocity * -(1.0f + colliderA.restitution) dot normal) / (1.0f / colliderA.mass + (colliderA.inverseInertiaTensor * (radiusA cross normal) cross radiusA dot normal))
      colliderA.applyImpulse(normal * -impulse / contactPoints.size, radiusA)
    })
  }

  def solve(colliderA: RigidBody3D, colliderB: DynamicBody3D, contactPoints: java.util.Collection[Vec3f], normal: Vec3f, depth: Float): Unit = {
    colliderA.position = colliderA.position + normal * depth
    val positionA = colliderA.globalPosition
    val linearVelocityA = colliderA.velocity
    val linearVelocityB = colliderB.velocity
    val angularVelocityA = colliderA.angularVelocity
    val restitution = math.min(colliderA.restitution, colliderB.restitution)
    contactPoints.forEach(point => {
      val radiusA = point - positionA
      val relativeAngularVelocity = -(angularVelocityA cross radiusA)
      val relativeVelocity = (linearVelocityB - linearVelocityA) + relativeAngularVelocity
      val impulse = (relativeVelocity * -(1.0f + restitution) dot normal) / (1.0f / colliderA.mass + 1.0f / colliderB.mass + (colliderA.inverseInertiaTensor * (radiusA cross normal) cross radiusA dot normal))
      colliderA.applyImpulse(normal * -impulse / contactPoints.size, radiusA)
      colliderB.applyImpulse(normal * impulse / contactPoints.size)
    })
  }

  def solve(colliderA: RigidBody3D, colliderB: RigidBody3D, contactPoints: java.util.Collection[Vec3f], normal: Vec3f, depth: Float): Unit = {
    val positionA = colliderA.globalPosition
    val positionB = colliderB.globalPosition
    val linearVelocityA = colliderA.velocity
    val linearVelocityB = colliderB.velocity
    val angularVelocityA = colliderA.angularVelocity
    val angularVelocityB = colliderB.angularVelocity
    val restitution = math.min(colliderA.restitution, colliderB.restitution)
    contactPoints.forEach(point => {
      val radiusA = point - positionA
      val radiusB = point - positionB
      val relativeAngularVelocity = (angularVelocityB cross radiusB) - (angularVelocityA cross radiusA)
      val relativeVelocity = (linearVelocityB - linearVelocityA) + relativeAngularVelocity
      if(relativeVelocity.dot(normal) <= 0.0f) {
        val impulse = (relativeVelocity * -(1.0f + restitution) dot normal) / (1.0f / colliderA.mass + 1.0f / colliderB.mass + (((colliderA.inverseInertiaTensor * (radiusA cross normal) cross radiusA) + (colliderB.inverseInertiaTensor * (radiusB cross normal) cross radiusB)) dot normal))
        colliderA.applyImpulse(normal * -impulse, radiusA)
        colliderB.applyImpulse(normal * impulse, radiusB)
      }
    })
  }
}
