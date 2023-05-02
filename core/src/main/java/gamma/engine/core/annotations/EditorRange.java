package gamma.engine.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the inspector should limit the value of this field between a maximum and a minimum value.
 * If this annotation is present, the inspector will show this field as a "draggable" number input.
 * This annotation is applicable to float and int types and to integer and float vectors.
 *
 * @author Nico
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EditorRange {

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

	/**
	 * The speed at which this value can be changed by dragging the mouse in the inspector.
	 * This value represents the amount changed for every pixel.
	 *
	 * @return The amount by which the field's value should for every pixel in the inspector.
	 */
	float step() default 0.001f;
}
