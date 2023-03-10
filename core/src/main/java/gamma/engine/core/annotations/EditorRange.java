package gamma.engine.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EditorRange {

	double max() default Double.POSITIVE_INFINITY;

	double min() default Double.NEGATIVE_INFINITY;

	double step() default 1.0;
}
