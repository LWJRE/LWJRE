package gamma.engine.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EditorRange {

	float max() default Float.POSITIVE_INFINITY;

	float min() default Float.NEGATIVE_INFINITY;

	float step() default 0.001f;
}
