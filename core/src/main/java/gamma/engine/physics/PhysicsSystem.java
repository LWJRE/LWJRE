package gamma.engine.physics;

import gamma.engine.ApplicationListener;
import gamma.engine.components.CollisionObject3D;

public class PhysicsSystem implements ApplicationListener {

	private static final SweepAndPrune sweepAndPrune = new SweepAndPrune();

	// TODO: Find a way to do proper spatial subdivision

	public static void add(CollisionObject3D collider) {
		sweepAndPrune.add(collider);
	}

	public static void remove(CollisionObject3D collider) {
		sweepAndPrune.remove(collider);
	}

	@Override
	public void onProcess() {
//		long time = System.nanoTime();
		sweepAndPrune.resolveCollisions();
//		long delta = System.nanoTime() - time;
//		System.out.println("Total time " + (delta / 1_000_000_000.0f));
	}
}
