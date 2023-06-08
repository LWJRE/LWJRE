package gamma.engine.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the inspector should show this field as an angle slider.
 * The value in the inspector will be shown as degrees, but the field's value will be converted to radians.
 * This annotation is only applicable to float fields.
 *
 * @author Nico
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EditorAngle {

	/**
	 * The angle's minimum value in degrees. By default, -360 degrees.
	 *
	 * @return The angle's minimum value in degrees.
	 */
	float min() default -360.0f;

	/**
	 * The angle's maximum value in degrees. By default, -360 degrees.
	 *
	 * @return The angle's maximum value in degrees.
	 */
	float max() default 360.0f;
}
