package gamma.engine.graphics;

import gamma.engine.core.ApplicationListener;
import gamma.engine.core.resources.Resources;
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
		Resources.addLoader(Shader.SHADER_LOADER, ".glsl");
		Resources.addLoader(Model.MODEL_LOADER, ".obj");
		YamlUtils.addScalarConstruct(Shader.class, Shader::getOrLoad);
		YamlUtils.addScalarConstruct(Model.class, Model::getOrLoad);
		YamlUtils.addScalarRepresent(Shader.class, Resources::pathOf);
		YamlUtils.addScalarRepresent(Model.class, Resources::pathOf);
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