package gamma.engine.physics;

import gamma.engine.ApplicationListener;
import gamma.engine.components.BoundingBox3D;
import gamma.engine.components.CollisionObject3D;

public class PhysicsSystem implements ApplicationListener {

	private static final SweepAndPrune[] SWEEP_AND_PRUNE = {
			new SweepAndPrune(),
			new SweepAndPrune(),
			new SweepAndPrune(),
			new SweepAndPrune(),
			new SweepAndPrune(),
			new SweepAndPrune(),
			new SweepAndPrune(),
			new SweepAndPrune()
	};

	// TODO: Colliders need to be re-added every frame because they move

	public static void add(CollisionObject3D collider) {
		collider.getComponent(BoundingBox3D.class).map(BoundingBox3D::globalPositionVertices).ifPresent(vertices -> {
			if(vertices.stream().anyMatch(vertex -> vertex.x() >= 0.0f && vertex.y() >= 0.0f && vertex.z() >= 0.0f)) {
				SWEEP_AND_PRUNE[0].add(collider);
			}
			if(vertices.stream().anyMatch(vertex -> vertex.x() <= 0.0f && vertex.y() >= 0.0f && vertex.z() >= 0.0f)) {
				SWEEP_AND_PRUNE[1].add(collider);
			}
			if(vertices.stream().anyMatch(vertex -> vertex.x() >= 0.0f && vertex.y() <= 0.0f && vertex.z() >= 0.0f)) {
				SWEEP_AND_PRUNE[2].add(collider);
			}
			if(vertices.stream().anyMatch(vertex -> vertex.x() >= 0.0f && vertex.y() >= 0.0f && vertex.z() <= 0.0f)) {
				SWEEP_AND_PRUNE[3].add(collider);
			}
			if(vertices.stream().anyMatch(vertex -> vertex.x() <= 0.0f && vertex.y() >= 0.0f && vertex.z() <= 0.0f)) {
				SWEEP_AND_PRUNE[4].add(collider);
			}
			if(vertices.stream().anyMatch(vertex -> vertex.x() <= 0.0f && vertex.y() <= 0.0f && vertex.z() >= 0.0f)) {
				SWEEP_AND_PRUNE[5].add(collider);
			}
			if(vertices.stream().anyMatch(vertex -> vertex.x() >= 0.0f && vertex.y() <= 0.0f && vertex.z() <= 0.0f)) {
				SWEEP_AND_PRUNE[6].add(collider);
			}
			if(vertices.stream().anyMatch(vertex -> vertex.x() <= 0.0f && vertex.y() <= 0.0f && vertex.z() <= 0.0f)) {
				SWEEP_AND_PRUNE[7].add(collider);
			}
		});
	}

	public static void remove(CollisionObject3D collider) {
		for(SweepAndPrune sweepAndPrune : SWEEP_AND_PRUNE) {
			sweepAndPrune.remove(collider);
		}
	}

	@Override
	public void onProcess() {
		// TODO: Start threads on init and stop on terminate
		long time = System.nanoTime();
		Thread[] threads = new Thread[8];
		for(int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(SWEEP_AND_PRUNE[i]::resolveCollisions);
			threads[i].start();
		}
		for(Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long delta = System.nanoTime() - time;
		System.out.println("Total time " + (delta / 1_000_000_000.0f));
	}
}
