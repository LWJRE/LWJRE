package gamma.engine.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EditorFloat {

	float minValue() default Float.NEGATIVE_INFINITY;

	float maxValue() default Float.POSITIVE_INFINITY;

	float stepSize() default 0.01f;
}
