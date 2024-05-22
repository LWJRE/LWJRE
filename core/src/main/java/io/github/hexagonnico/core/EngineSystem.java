package io.github.hexagonnico.core;

public interface EngineSystem extends Comparable<EngineSystem> {

    void initialize();

    void process();

    void terminate();

    default int priority() {
        return 1;
    }

    @Override
    default int compareTo(EngineSystem engineSystem) {
        return Integer.compare(this.priority(), engineSystem.priority());
    }
}
