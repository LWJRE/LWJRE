package gamma.engine.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EditorInt {

	int minValue() default Integer.MIN_VALUE;

	int maxValue() default Integer.MAX_VALUE;

	int stepSize() default 1;
}
