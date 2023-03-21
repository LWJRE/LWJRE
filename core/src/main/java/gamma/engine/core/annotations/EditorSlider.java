package gamma.engine.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the inspector should show this field as a slider in the inspector.
 * This annotation is applicable to integer and real types.
 * This annotation will not have any effect if the field is already annotated with {@link EditorRange}.
 *
 * @see EditorRange
 *
 * @author Nico
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EditorSlider {

	/**
	 * The maximum value that can be set to this field in the inspector.
	 * By default {@link Float#POSITIVE_INFINITY}.
	 *
	 * @return The maximum value that can be set to this field in the inspector
	 */
	float max() default Float.POSITIVE_INFINITY;

	/**
	 * The minimum value that can be set to this field in the inspector.
	 * By default {@link Float#NEGATIVE_INFINITY}.
	 *
	 * @return The minimum value that can be set to this field in the inspector
	 */
	float min() default Float.NEGATIVE_INFINITY;
}
