package io.github.lwjre.engine.servers;

public interface EngineServer extends Comparable<EngineServer> {

	void init();

	void update();

	void terminate();

	@Override
	default int compareTo(EngineServer engineServer) {
		return Integer.compare(this.priority(), engineServer.priority());
	}

	default int priority() {
		return Integer.MAX_VALUE;
	}
}
