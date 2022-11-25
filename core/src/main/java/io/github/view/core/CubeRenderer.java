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
	public void render() {
		super.render();
		ShaderProgram.start(this.shader);
		this.cubeMesh.bind(mesh -> {
			this.texture.bind();
			mesh.draw();
		});
		ShaderProgram.stop();
	}
}
