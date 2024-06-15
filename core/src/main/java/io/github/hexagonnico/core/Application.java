package io.github.hexagonnico.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ServiceLoader;

public final class Application {

    private static boolean running = true;
    private static long processFrames = 0;

    public static long getProcessFrames() {
        return processFrames;
    }

    public static void quit() {
        running = false;
    }

    public static void main(String[] args) {
        ArrayList<EngineSystem> engineSystems = new ArrayList<>();
        ServiceLoader.load(EngineSystem.class).forEach(engineSystems::add);
        Collections.sort(engineSystems);
        engineSystems.forEach(EngineSystem::initialize);
        while(running) {
            processFrames++;
            engineSystems.forEach(EngineSystem::process);
        }
        Collections.reverse(engineSystems);
        engineSystems.forEach(EngineSystem::terminate);
    }
}
