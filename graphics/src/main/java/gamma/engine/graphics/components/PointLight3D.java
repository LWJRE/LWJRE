package gamma.engine.graphics.components;

import gamma.engine.core.annotations.EditorVariable;
import gamma.engine.core.scene.Component;
import vecmatlib.color.Color;

public class PointLight3D extends Component {

	@EditorVariable("Color")
	public Color color = Color.White();
}
