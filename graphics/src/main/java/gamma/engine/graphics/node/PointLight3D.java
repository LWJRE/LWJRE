package gamma.engine.graphics.node;

import gamma.engine.core.annotation.EditorVariable;
import gamma.engine.graphics.RenderingSystem3D;
import gamma.engine.core.node.Transform3D;
import vecmatlib.color.Color;

public class PointLight3D extends Transform3D {

	@EditorVariable
	private Color color = Color.White();

	@Override
	protected void onEnterTree() {
		RenderingSystem3D.addLight(this);
		super.onEnterTree();
	}

	@Override
	protected void onExitTree() {
		RenderingSystem3D.removeLight(this);
		super.onExitTree();
	}

	public final Color getColor() {
		return this.color;
	}

	public final void setColor(Color color) {
		if(color == null) this.color = Color.Black();
		else this.color = color;
	}
}
