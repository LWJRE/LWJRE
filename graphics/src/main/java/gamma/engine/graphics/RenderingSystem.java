package gamma.engine.graphics;

import gamma.engine.core.ApplicationListener;
import gamma.engine.core.window.WindowListener;
import gamma.engine.core.utils.YamlUtils;
import gamma.engine.graphics.resources.DeletableResource;
import gamma.engine.graphics.resources.Model;
import gamma.engine.graphics.resources.Shader;
import org.lwjgl.opengl.GL;

public final class RenderingSystem implements ApplicationListener, WindowListener {

	@Override
	public void onStart() {
		GL.createCapabilities();
		Graphics.clearColor(0.0f, 0.5f, 1.0f, 1.0f);
		Graphics.depthTest(true);
		YamlUtils.addScalarRepresent(Model.class, model -> model.path);
		YamlUtils.addScalarConstruct(Model.class, Model::getOrLoad);
		YamlUtils.addScalarConstruct(Shader.class, Shader::getOrLoad);
	}

	@Override
	public void onUpdate() {
		Graphics.clearFramebuffer();
	}

	@Override
	public void onResize(int width, int height) {
		// TODO: Give the user different options for resizing
		Graphics.setViewport(width, height);
	}

	@Override
	public void onTerminate() {
		DeletableResource.deleteAll();
	}
}