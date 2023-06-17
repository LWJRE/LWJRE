package io.github.lwjre.engine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the inspector should use a default value for this resource type if the given path is empty.
 *
 * @author Nico
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DefaultResource {

	/**
	 * Path to the default resource.
	 *
	 * @return Path to the default resource
	 */
	String path();
}
