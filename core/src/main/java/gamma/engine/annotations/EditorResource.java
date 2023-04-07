package gamma.engine.annotations;

import gamma.engine.resources.Resource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EditorResource {

	Class<? extends Resource> type();

	String defaultValue() default "";
}
