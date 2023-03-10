package gamma.engine.core.utils;

import gamma.engine.core.scene.Component;

import javax.swing.*;

// TODO: Replace this with an annotation

public interface EditorGuiComponent {

	JComponent guiRepresentation(Component self);
}
