package gamma.engine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the inspector should show this field as a slider.
 * This annotation is applicable to float and int types and to float vectors.
 *
 * @author Nico
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EditorSlider {

	/**
	 * Minimum value of the slider.
	 *
	 * @return Minimum value of the slider
	 */
	float min();

	/**
	 * Maximum value of the slider.
	 *
	 * @return Maximum value of the slider
	 */
	float max();
}
