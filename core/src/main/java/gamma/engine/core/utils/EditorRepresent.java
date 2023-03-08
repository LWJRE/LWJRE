package gamma.engine.core.utils;

import javax.swing.*;
import java.lang.reflect.Field;

public interface EditorRepresent {

	JComponent guiRepresent(Field field, Object owner);
}
