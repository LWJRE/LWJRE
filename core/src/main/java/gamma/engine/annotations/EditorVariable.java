package gamma.engine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this field should be exposed in the editor.
 * Fields with this annotation will be visible in the inspector when selecting an entity with a component of this type.
 * If the type of this field is not supported, only its name will be visible, and it will not be editable.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EditorVariable {

	/**
	 * The name that this field should have in the inspector.
	 * If this value is an empty string the inspector will show the name of the field.
	 *
	 * @return The name that this field should have in the inspector or an empty string
	 */
	String value() default "";

	/**
	 * The name of the setter method that the inspector should use when setting this field.
	 * The setter method must have only one argument of the same type of this field.
	 * If this value is an empty string the inspector will set the field directly.
	 *
	 * @return The name of the setter method that the inspector should use or an empty string
	 */
	String setter() default "";
}
