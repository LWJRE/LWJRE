package gamma.engine.graphics;

import gamma.engine.core.ApplicationListener;
import gamma.engine.core.WindowListener;
import gamma.engine.core.scene.Scene;
import gamma.engine.core.utils.YamlParser;
import gamma.engine.graphics.resources.DeletableResource;
import gamma.engine.graphics.resources.Shader;
import org.lwjgl.opengl.GL;

public final class RenderingSystem implements ApplicationListener, WindowListener {

	private Scene temp;

	@Override
	public void onStart() {
		GL.createCapabilities();
		Graphics.clearColor(0.0f, 0.5f, 1.0f, 1.0f);
		YamlParser.addMappingConstructor(Shader.class, Shader::deserialize);
		this.temp = YamlParser.loadResource("/test.yaml", Scene.class);
	}

	@Override
	public void onUpdate() {
		Graphics.clearFramebuffer();
		this.temp.process();
//		RenderingSystem3D.renderingProcess();
	}

	@Override
	public void onResize(int width, int height) {
		// TODO: Give the user different options for resizing
		Graphics.setViewport(width, height);
	}

	@Override
	public void onTerminate() {
		System.out.println(YamlParser.serialize(this.temp));
		DeletableResource.deleteAll();
	}
}