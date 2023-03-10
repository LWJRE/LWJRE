package gamma.engine.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EditorByte {

	byte minValue() default Byte.MIN_VALUE;

	byte maxValue() default Byte.MAX_VALUE;

	byte stepSize() default (byte) 1;
}
