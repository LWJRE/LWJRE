package io.github.hexagonnico.core;

import io.github.hexagonnico.core.scene.SceneTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ServiceLoader;

public final class Application {

    private static final SceneTree SCENE_TREE = new SceneTree();
    private static boolean running = true;

    public static void changeScene() {
        // TODO: Scene parameter
        SCENE_TREE.changeScene();
    }

    public static void quit() {
        running = false;
    }

    public static void main(String[] args) {
        ArrayList<EngineSystem> engineSystems = new ArrayList<>();
        ServiceLoader.load(EngineSystem.class).forEach(engineSystems::add);
        Collections.sort(engineSystems);
        engineSystems.forEach(EngineSystem::initialize);
        SCENE_TREE.changeScene(); // TODO: Load main scene
        while(running) {
            engineSystems.forEach(EngineSystem::process);
            SCENE_TREE.process();
        }
        Collections.reverse(engineSystems);
        engineSystems.forEach(EngineSystem::terminate);
    }
}
