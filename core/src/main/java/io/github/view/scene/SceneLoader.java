package io.github.view.scene;

import io.github.view.core.Camera3D;
import io.github.view.core.Script;
import io.github.view.utils.FileUtils;
import io.github.view.utils.Reflection;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SceneLoader {

	public static Scene loadScene(String file) {
		Scene scene = new Scene();
		// TODO; Find a way to load things that does not require the root to be a list
		try {
			List<List<Map<String, Object>>> sceneYaml = FileUtils.parseYaml(file);
			sceneYaml.forEach(objectYaml -> {
				SceneObject sceneObject = scene.createObject();
				objectYaml.forEach(scriptYaml -> sceneObject.addScript(object -> {
					try {
						Script script = (Script) Reflection.instance(String.valueOf(scriptYaml.remove("script")), object);
						scriptYaml.forEach((key, value) -> {
							try {
								Reflection.setField(script, key, value);
							} catch (RuntimeException e) {
								e.printStackTrace();
							}
						});
						// TODO: Temporary
						if(script instanceof Camera3D camera)
							camera.makeCurrent();
						return script;
					} catch (RuntimeException e) {
						e.printStackTrace();
						return null;
					}
				}));
			});
		} catch (IOException e) {
			System.err.println("Error loading scene from file " + file);
			e.printStackTrace();
		}
		return scene;
	}

	// TODO: Allow scene files to reference object files
}
