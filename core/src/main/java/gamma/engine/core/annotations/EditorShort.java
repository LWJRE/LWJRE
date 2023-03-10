package gamma.engine.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EditorShort {

	short minValue() default Short.MIN_VALUE;

	short maxValue() default Short.MAX_VALUE;

	short stepSize() default (short) 1;
}
