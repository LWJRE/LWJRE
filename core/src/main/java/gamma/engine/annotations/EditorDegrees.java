package gamma.engine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the value of this field should be converted to degrees when shown in the inspector.
 * The value shown in the inspector will be in degrees, the actual value of this field will be in radians.
 * This annotation is applicable to real types and real vectors.
 *
 * @see Math#toRadians(double)
 * @see Math#toDegrees(double)
 *
 * @author Nico
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EditorDegrees {
}
