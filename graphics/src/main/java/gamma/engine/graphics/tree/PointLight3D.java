package gamma.engine.graphics.tree;

import gamma.engine.graphics.RenderingSystem3D;
import gamma.engine.core.tree.Transform3D;
import gamma.engine.graphics.utils.Color;

public class PointLight3D extends Transform3D {

	private Color color = Color.WHITE;

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
		if(color == null) this.color = Color.BLACK;
		else this.color = color;
	}
}
