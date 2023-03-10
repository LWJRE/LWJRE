package gamma.engine.core.utils;

import gamma.engine.core.scene.Component;

import javax.swing.*;

public interface EditorGuiComponent {

	JComponent guiRepresentation(Component self);
}
