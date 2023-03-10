package gamma.engine.core.utils;

import javax.swing.*;
import java.lang.reflect.Field;

// TODO: Replace this with an annotation

public interface EditorGuiField {

	JComponent guiRepresent(Field field, Object owner);
}
