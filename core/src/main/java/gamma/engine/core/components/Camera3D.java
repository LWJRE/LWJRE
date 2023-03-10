package gamma.engine.core.components;

import gamma.engine.core.Application;
import gamma.engine.core.scene.Component;
import gamma.engine.core.utils.EditorGuiComponent;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec2i;
import vecmatlib.vector.Vec3f;

import javax.swing.*;
import java.awt.*;
import java.util.NoSuchElementException;

/**
 * Component representing a 3D camera.
 *
 * @author Nico
 */
public class Camera3D extends Component implements EditorGuiComponent {

	/** Reference to the current camera */
	private static Camera3D currentCamera;

	/**
	 * Gets the current camera.
	 * If no camera is current, return a default camera.
	 *
	 * @return The current camera or a default camera if no camera is current
	 */
	public static Camera3D getCurrent() {
		return currentCamera != null ? currentCamera : new Camera3D();
	}

	/** Weather this is the current camera or not */
	private boolean current = false;

	/** Field of view */
	public float fov = 1.22173f; // 70.0f deg
	/** Near distance plane */
	public float nearPlane = 0.1f;
	/** Far distance plane */
	public float farPlane = 1000.0f;

	@Override
	protected void onStart() {
		super.onStart();
		if(this.current)
			this.makeCurrent();
	}

	/**
	 * Sets this as the current camera.
	 */
	public final void makeCurrent() {
		getCurrent().current = false;
		currentCamera = this;
		this.current = true;
	}

	/**
	 * Gets the global position of the camera.
	 * Same as {@link Transform3D#globalPosition()}.
	 * @return The global position of the camera
	 * @throws NoSuchElementException if this entity does not have a transform component
	 * @throws RuntimeException if this component does not belong to any entity
	 */
	public Vec3f globalPosition() {
		return this.requireComponent(Transform3D.class).globalPosition();
	}

	public Mat4f projectionMatrix() {
		Vec2i windowSize = Application.window().getSize();
		float m00 = 1.0f / (float) Math.tan(fov / 2.0f);
		float m11 = m00 * ((float) windowSize.x() / windowSize.y());
		float m22 = -(farPlane + nearPlane) / (farPlane - nearPlane);
		float m23 = -(2 * farPlane * nearPlane) / (farPlane - nearPlane);
		return new Mat4f(
				m00, 0.0f, 0.0f, 0.0f,
				0.0f, m11, 0.0f, 0.0f,
				0.0f, 0.0f, m22, m23,
				0.0f, 0.0f, -1.0f, 0.0f
		);
	}

	public Mat4f viewMatrix() {
		return Mat4f.translation(this.globalPosition().negated()).multiply(this.requireComponent(Transform3D.class).localRotation());
	}

	@Override
	public JComponent guiRepresentation(Component self) {
		Camera3D camera = (Camera3D) self;
		JPanel fieldsPanel = new JPanel(new GridLayout(0, 2, 1, 1));
		fieldsPanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
		JLabel currentLabel = new JLabel("Current");
		currentLabel.setFont(currentLabel.getFont().deriveFont(Font.PLAIN));
		fieldsPanel.add(currentLabel);
		JCheckBox currentCheckbox = new JCheckBox("", camera.current);
		currentCheckbox.addActionListener(actionEvent -> camera.current = currentCheckbox.isSelected());
		fieldsPanel.add(currentCheckbox);
		JLabel fovLabel = new JLabel("FOV");
		fovLabel.setFont(fovLabel.getFont().deriveFont(Font.PLAIN));
		fieldsPanel.add(fovLabel);
		JSpinner fovSpinner = new JSpinner(new SpinnerNumberModel(camera.fov, 0.0f, 360.0f, 0.01f));
		fovSpinner.setMaximumSize(new Dimension(fovSpinner.getPreferredSize().width, 20));
		fovSpinner.addChangeListener(changeEvent -> camera.fov = ((Double) fovSpinner.getValue()).floatValue());
		fieldsPanel.add(fovSpinner);
		JLabel nearPlaneLabel = new JLabel("Near plane");
		nearPlaneLabel.setFont(nearPlaneLabel.getFont().deriveFont(Font.PLAIN));
		fieldsPanel.add(nearPlaneLabel);
		JSpinner nearPlaneSpinner = new JSpinner(new SpinnerNumberModel(camera.nearPlane, 0.0f, Float.POSITIVE_INFINITY, 0.01f));
		nearPlaneSpinner.setMaximumSize(new Dimension(nearPlaneSpinner.getPreferredSize().width, 20));
		nearPlaneSpinner.addChangeListener(changeEvent -> camera.nearPlane = ((Double) nearPlaneSpinner.getValue()).floatValue());
		fieldsPanel.add(nearPlaneSpinner);
		JLabel farPlaneLabel = new JLabel("Far plane");
		farPlaneLabel.setFont(farPlaneLabel.getFont().deriveFont(Font.PLAIN));
		fieldsPanel.add(farPlaneLabel);
		JSpinner farPlaneSpinner = new JSpinner(new SpinnerNumberModel(camera.farPlane, 0.0f, Float.POSITIVE_INFINITY, 0.01f));
		farPlaneSpinner.setMaximumSize(new Dimension(farPlaneSpinner.getPreferredSize().width, 20));
		farPlaneSpinner.addChangeListener(changeEvent -> camera.farPlane = ((Double) farPlaneSpinner.getValue()).floatValue());
		fieldsPanel.add(farPlaneSpinner);
		fieldsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, fieldsPanel.getPreferredSize().height));
		return fieldsPanel;
	}
}
