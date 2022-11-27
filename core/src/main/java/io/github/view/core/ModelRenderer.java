package io.github.view.core;

import io.github.view.resources.Mesh;
import io.github.view.resources.ModelLoader;
import io.github.view.resources.Shader;
import io.github.view.resources.ShaderProgram;

public class ModelRenderer extends Component3D {

	private Mesh mesh;
	private ShaderProgram shader;

	public ModelRenderer(Entity3D entity) {
		super(entity);
	}

	@Override
	public void prepareRendering() {
		super.prepareRendering();
		this.mesh = ModelLoader.getOrLoadObj("/models/bunny.obj");
		Shader vertexShader = Shader.getOrLoad("/shaders/test_vertex.glsl");
		Shader fragmentShader = Shader.getOrLoad("/shaders/test_fragment.glsl");
		this.shader = new ShaderProgram(vertexShader, fragmentShader);
	}

	@Override
	public void render() {
		super.render();
		ShaderProgram.start(this.shader);
		this.shader.loadUniform("transformation_matrix", this.entity.transform.matrix());
		this.shader.loadUniform("projection_matrix", Camera3D.currentProjection());
		this.shader.loadUniform("view_matrix", Camera3D.currentView());
		this.mesh.bind(Mesh.DrawableMesh::draw);
		ShaderProgram.stop();
	}
}
