package io.github.view.core;

import io.github.view.resources.Mesh;
import io.github.view.resources.Shader;
import io.github.view.resources.ShaderProgram;
import io.github.view.resources.Texture;

public class CubeRenderer extends Component3D {

	private Mesh cubeMesh;
	private ShaderProgram shader;
	private Texture texture;

	public CubeRenderer(Entity3D entity) {
		super(entity);
	}

	@Override
	public void onStart() {
		super.onStart();
		this.entity.transform.scale = this.entity.transform.scale.multipliedBy(-1);
	}

	@Override
	public void prepareRendering() {
		super.prepareRendering();
		this.cubeMesh = Mesh.createMesh3D(new float[] {
				-0.5f,0.5f,-0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f,

				-0.5f,0.5f,0.5f,
				-0.5f,-0.5f,0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,

				0.5f,0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,

				-0.5f,0.5f,-0.5f,
				-0.5f,-0.5f,-0.5f,
				-0.5f,-0.5f,0.5f,
				-0.5f,0.5f,0.5f,

				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,

				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f
		}, new int[] {
				0,1,3,
				3,1,2,
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14
		});
		Shader vertexShader = Shader.getOrLoad("/shaders/test_vertex.glsl");
		Shader fragmentShader = Shader.getOrLoad("/shaders/test_fragment.glsl");
		this.shader = new ShaderProgram(vertexShader, fragmentShader);
		this.texture = Texture.getOrLoad("/test.png");
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.entity.transform.rotation = this.entity.transform.rotation.plus(0.0f, 0.000001f, 0.0f);
		Camera3D.current().fov += 0.00001f;
	}

	@Override
	public void render() {
		super.render();
		ShaderProgram.start(this.shader);
		this.shader.loadUniform("transformation_matrix", this.entity.transform.matrix());
		this.shader.loadUniform("projection_matrix", Camera3D.currentProjection());
		this.shader.loadUniform("view_matrix", Camera3D.currentView());
		this.cubeMesh.bind(mesh -> {
			this.texture.bind();
			mesh.draw();
		});
		ShaderProgram.stop();
	}
}
