package gamma.engine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this field should be shown in the editor as a resource.
 * If this annotation is present, the inspector will show this field as a text input for the resource's path.
 *
 * @author Nico
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EditorResource {

	/**
	 * Default resource path to use when no path is given to the inspector.
	 * If this value is an empty string the inspector will try to load the resource with an empty path.
	 *
	 * @return The path to the default resource or an empty string
	 */
	String defaultValue() default "";
}
