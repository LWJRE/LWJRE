package io.github.view.scene;

import io.github.view.core.Camera3D;
import io.github.view.core.Script;
import io.github.view.utils.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class SceneLoader {

	public static Scene loadScene(String file) {
		Scene scene = new Scene();
		// TODO; Find a way to load things that does not require the root to be a list
		List<List<Map<String, Object>>> sceneYaml = FileUtils.readFile(file, inputStream -> {
			return new Yaml().load(inputStream);
		});
		sceneYaml.forEach(objectYaml -> {
			SceneObject sceneObject = scene.createObject();
			objectYaml.forEach(scriptYaml -> {
				sceneObject.addScript(object -> createScript(object, (String) scriptYaml.remove("script"), scriptYaml));
			});
		});
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
			// TODO: Temporary
			if(script instanceof Camera3D camera)
				camera.makeCurrent();
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
