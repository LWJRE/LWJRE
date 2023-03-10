package gamma.engine.core.utils;

import javax.swing.*;
import java.lang.reflect.Field;

public interface EditorGuiField {

	JComponent guiRepresent(Field field, Object owner);
}
