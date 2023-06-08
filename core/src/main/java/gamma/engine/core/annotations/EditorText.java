package gamma.engine.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides additional options to the editor for rendering text fields.
 * This annotation is applicable to fields of type {@link String}.
 *
 * @author Nico
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EditorText {

	/**
	 * Maximum length that the string can have. By default, this is 256 characters.
	 *
	 * @return The maximum length of the text
	 */
	int maxLength() default 256;

	/**
	 * If true, the inspector will show this field as a multiline text input.
	 *
	 * @return True to show the text field as a multiline text input
	 */
	boolean multiline() default false;

	/**
	 * Hint to show in the text input field in the inspector.
	 *
	 * @return Hint to show in the text input field in the inspector
	 */
	String hint() default "";
}
