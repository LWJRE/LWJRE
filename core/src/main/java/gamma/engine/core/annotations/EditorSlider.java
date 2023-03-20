package gamma.engine.core.annotations;

public @interface EditorSlider {

	float max() default Float.POSITIVE_INFINITY;

	float min() default Float.NEGATIVE_INFINITY;
}
