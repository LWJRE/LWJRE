package gamma.engine.core.utils;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlReader;
import vecmatlib.vector.Vec2f;
import vecmatlib.vector.Vec3f;
import vecmatlib.vector.Vec4f;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class YamlParser {

	private static final YamlConfig CONFIG = new YamlConfig();

	static {
		CONFIG.setPrivateFields(true);
		CONFIG.readConfig.setConstructorParameters(Vec2f.class, new Class[] {float.class, float.class}, new String[] {"x", "y"});
		CONFIG.readConfig.setConstructorParameters(Vec3f.class, new Class[] {float.class, float.class, float.class}, new String[] {"x", "y", "z"});
		CONFIG.readConfig.setConstructorParameters(Vec4f.class, new Class[] {float.class, float.class, float.class, float.class}, new String[] {"x", "y", "z", "w"});
	}

	public static Object readResource(String path) {
		try(InputStream inputStream = YamlParser.class.getResourceAsStream(path)) {
			if(inputStream == null)
				throw new FileNotFoundException("Could not find resource at path " + path);
			YamlReader reader = new YamlReader(new InputStreamReader(inputStream), CONFIG);
			Object result = reader.read();
			reader.close();
			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T readResource(String path, Class<T> type) {
		try(InputStream inputStream = YamlParser.class.getResourceAsStream(path)) {
			if(inputStream == null)
				throw new FileNotFoundException("Could not find resource at path " + path);
			YamlReader reader = new YamlReader(new InputStreamReader(inputStream), CONFIG);
			T result = type.cast(reader.read());
			reader.close();
			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// TODO: Config options
}
