package io.github.view.scene;

import io.github.view.core.Camera3D;
import io.github.view.core.Script;
import io.github.view.math.Vector3;
import io.github.view.utils.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class SceneLoader {

	public static Scene load(String file) {
		Scene scene = new Scene();
		SceneObject bunny = scene.createObject();
		loadObject("/bunny.yaml").forEach(script -> {
			bunny.addScript(sceneObject -> createScript(sceneObject, (String) script.remove("script"), script));
		});
		SceneObject camera = scene.createObject();
		camera.addScript(sceneObject -> createScript(sceneObject, "io.github.view.core.Position3D", Map.ofEntries(Map.entry("position", new Vector3(0.0f, 1.0f, 6.0f)))));
		((Camera3D) camera.addScript(sceneObject -> createScript(sceneObject, "io.github.view.core.Camera3D", Map.of()))).makeCurrent();
		SceneObject pointLight = scene.createObject();
		pointLight.addScript(sceneObject -> createScript(sceneObject, "io.github.view.core.Position3D", Map.ofEntries(Map.entry("position", new Vector3(0.0f, -10.0f, 0.0f)))));
		pointLight.addScript(sceneObject -> createScript(sceneObject, "io.github.view.core.PointLight3D", Map.of()));
		SceneObject cube = scene.createObject();
		cube.addScript(sceneObject -> createScript(sceneObject, "io.github.view.core.Position3D", Map.of()));
		cube.addScript(sceneObject -> createScript(sceneObject, "io.github.view.core.Rotation3D", Map.of()));
		cube.addScript(sceneObject -> createScript(sceneObject, "io.github.view.core.Scale3D", Map.ofEntries(Map.entry("scale", new Vector3(5.0f, 1.0f, 5.0f)))));
		cube.addScript(sceneObject -> createScript(sceneObject, "io.github.view.core.Transform3D", Map.of()));
		cube.addScript(sceneObject -> createScript(sceneObject, "io.github.view.core.StaticBody3D", Map.of()));
		return scene;
	}

	private static Script createScript(SceneObject sceneObject, String scriptClass, Map<String, Object> values) {
		try {
			Script script = (Script) Class.forName(scriptClass).getConstructor(SceneObject.class).newInstance(sceneObject);
			values.forEach((key, value) -> {
				try {
					Field field = script.getClass().getDeclaredField(key);
					field.setAccessible(true);
					field.set(script, value);
				} catch (NoSuchFieldException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			});
			return script;
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<Map<String, Object>> loadObject(String file) {
		return FileUtils.readFile(file, inputStream -> {
			return new Yaml().load(inputStream);
		});
	}
}
