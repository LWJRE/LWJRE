package io.github.ardentengine.core.math;

public final class MathUtils {

    /** Constant used to check if two values are approximately equal. */
    public static final double EPSILON = 1e-6;

    /**
     * Clamps the given value between the given minimum and maximum.
     * <p>
     *     Returns the given value if it is between the given maximum and minimum,
     *     the minimum if the given value is less than the minimum,
     *     or the maximum if the given value is greater than the maximum.
     * </p>
     *
     * @param value The value.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return The given value clamped between the given maximum and minimum.
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    /**
     * Clamps the given value between the given minimum and maximum.
     * <p>
     *     Returns the given value if it is between the given maximum and minimum,
     *     the minimum if the given value is less than the minimum,
     *     or the maximum if the given value is greater than the maximum.
     * </p>
     *
     * @param value The value.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return The given value clamped between the given maximum and minimum.
     */
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    /**
     * Clamps the given value between the given minimum and maximum.
     * <p>
     *     Returns the given value if it is between the given maximum and minimum,
     *     the minimum if the given value is less than the minimum,
     *     or the maximum if the given value is greater than the maximum.
     * </p>
     *
     * @param value The value.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return The given value clamped between the given maximum and minimum.
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    /**
     * Linearly interpolates between the two given values by the given weight and returns the result.
     * <p>
     *     The given weight must be between zero and one, representing the amount of interpolation.
     * </p>
     *
     * @param from The first value.
     * @param to The second value.
     * @param weight The weight of the interpolation between zero and one.
     * @return The result of linearly interpolating between the two given values.
     */
    public static float lerp(float from, float to, float weight) {
        return from + (to - from) * weight;
    }

    /**
     * Linearly interpolates between the two given values by the given weight and returns the result.
     * <p>
     *     The given weight must be between zero and one, representing the amount of interpolation.
     * </p>
     *
     * @param from The first value.
     * @param to The second value.
     * @param weight The weight of the interpolation between zero and one.
     * @return The result of linearly interpolating between the two given values.
     */
    public static double lerp(double from, double to, double weight) {
        return from + (to - from) * weight;
    }

    /**
     * The inverse of the {@link MathUtils#lerp(float, float, float)} function.
     * Determines where a value lies between two points.
     *
     * @param from The first value.
     * @param to The second value.
     * @param value A value between {@code from} and {@code to}.
     * @return A value between zero and one that describes where the given value lies between {@code from} and {@code to}.
     */
    public static float inverseLerp(float from, float to, float value) {
        return (value - from) / (to - from);
    }

    /**
     * The inverse of the {@link MathUtils#lerp(double, double, double)} function.
     * Determines where a value lies between two points.
     *
     * @param from The first value.
     * @param to The second value.
     * @param value A value between {@code from} and {@code to}.
     * @return A value between zero and one that describes where the given value lies between {@code from} and {@code to}.
     */
    public static double inverseLerp(double from, double to, double value) {
        return (value - from) / (to - from);
    }

    /**
     * Moves the first value toward the second value by the given delta without going past the final value.
     *
     * @param from The first value.
     * @param to The second value.
     * @param delta Delta amount.
     * @return The first value moved towards the second value by the given amount.
     */
    public static int moveToward(int from, int to, int delta) {
        return Math.abs(to - from) <= delta ? to : from + Integer.compare(to - from, 0) * delta;
    }

    /**
     * Moves the first value toward the second value by the given delta without going past the final value.
     *
     * @param from The first value.
     * @param to The second value.
     * @param delta Delta amount.
     * @return The first value moved towards the second value by the given amount.
     */
    public static Long moveToward(long from, long to, long delta) {
        return Math.abs(to - from) <= delta ? to : from + Long.compare(to - from, 0L) * delta;
    }

    /**
     * Moves the first value toward the second value by the given delta without going past the final value.
     *
     * @param from The first value.
     * @param to The second value.
     * @param delta Delta amount.
     * @return The first value moved towards the second value by the given amount.
     */
    public static float moveToward(float from, float to, float delta) {
        return Math.abs(to - from) <= delta ? to : from + Math.signum(to - from) * delta;
    }

    /**
     * Moves the first value toward the second value by the given delta without going past the final value.
     *
     * @param from The first value.
     * @param to The second value.
     * @param delta Delta amount.
     * @return The first value moved towards the second value by the given amount.
     */
    public static double moveToward(double from, double to, double delta) {
        return Math.abs(to - from) <= delta ? to : from + Math.signum(to - from) * delta;
    }

    /**
     * Maps the given value from the first range to the second range.
     * <p>
     *     If the given value is outside the first range, the resulting value will be outside the second range.
     * </p>
     *
     * @param value The value to remap.
     * @param min1 The minimum of the first range.
     * @param max1 The maximum of the first range.
     * @param min2 The minimum of the second range.
     * @param max2 The maximum of the second range.
     * @return The remapped value.
     */
    public static int map(int value, int min1, int max1, int min2, int max2) {
        return (value - min1) * (max2 - min2) / (max1 - min1) + min2;
    }

    /**
     * Maps the given value from the first range to the second range.
     * <p>
     *     If the given value is outside the first range, the resulting value will be outside the second range.
     * </p>
     *
     * @param value The value to remap.
     * @param min1 The minimum of the first range.
     * @param max1 The maximum of the first range.
     * @param min2 The minimum of the second range.
     * @param max2 The maximum of the second range.
     * @return The remapped value.
     */
    public static Long map(long value, long min1, long max1, long min2, long max2) {
        return (value - min1) * (max2 - min2) / (max1 - min1) + min2;
    }

    /**
     * Maps the given value from the first range to the second range.
     * <p>
     *     If the given value is outside the first range, the resulting value will be outside the second range.
     * </p>
     *
     * @param value The value to remap.
     * @param min1 The minimum of the first range.
     * @param max1 The maximum of the first range.
     * @param min2 The minimum of the second range.
     * @param max2 The maximum of the second range.
     * @return The remapped value.
     */
    public static float map(float value, float min1, float max1, float min2, float max2) {
        return (value - min1) * (max2 - min2) / (max1 - min1) + min2;
    }

    /**
     * Maps the given value from the first range to the second range.
     * <p>
     *     If the given value is outside the first range, the resulting value will be outside the second range.
     * </p>
     *
     * @param value The value to remap.
     * @param min1 The minimum of the first range.
     * @param max1 The maximum of the first range.
     * @param min2 The minimum of the second range.
     * @param max2 The maximum of the second range.
     * @return The remapped value.
     */
    public static double map(double value, double min1, double max1, double min2, double max2) {
        return (value - min1) * (max2 - min2) / (max1 - min1) + min2;
    }

    /**
     * Returns a sigmoid-like interpolation of the given value between zero and one,
     * based on where it lies with respect to the given edges.
     * <p>
     *     The return value is zero if the given value is less than the minimum,
     *     one if the given value is greater than the maximum.
     * </p>
     *
     * @param from The lower bound of the range.
     * @param to The upper bound of the range.
     * @param value The given value.
     * @return A sigmoid-like interpolation of the given value between zero and one.
     */
    public static float smoothstep(float from, float to, float value) {
        var s = clamp((value - from) / (to - from), 0.0f, 1.0f);
        return s * s * (3.0f - 2.0f * s);
    }

    /**
     * Returns a sigmoid-like interpolation of the given value between zero and one,
     * based on where it lies with respect to the given edges.
     * <p>
     *     The return value is zero if the given value is less than the minimum,
     *     one if the given value is greater than the maximum.
     * </p>
     *
     * @param from The lower bound of the range.
     * @param to The upper bound of the range.
     * @param value The given value.
     * @return A sigmoid-like interpolation of the given value between zero and one.
     */
    public static double smoothstep(double from, double to, double value) {
        var s = clamp((value - from) / (to - from), 0.0, 1.0);
        return s * s * (3.0 - 2.0 * s);
    }

    /**
     * Returns the point at the given {@code t} on the quadratic Bézier curve defined by the given points.
     *
     * @param from The starting point of the Bézier curve.
     * @param to The end point of the Bézier curve.
     * @param control The control point of the Bézier curve.
     * @param t The interpolation weight. Must be between zero and one.
     * @return The point at the given {@code t} on the quadratic Bézier curve defined by the given points.
     */
    public static float bezierInterpolate(float from, float to, float control, float t) {
        var w = 1.0f - t;
        return w * w * from + 2.0f * w * t * control + t * t * to;
    }

    /**
     * Returns the point at the given {@code t} on the quadratic Bézier curve defined by the given points.
     *
     * @param from The starting point of the Bézier curve.
     * @param to The end point of the Bézier curve.
     * @param control The control point of the Bézier curve.
     * @param t The interpolation weight. Must be between zero and one.
     * @return The point at the given {@code t} on the quadratic Bézier curve defined by the given points.
     */
    public static double bezierInterpolate(double from, double to, double control, double t) {
        var w = 1.0 - t;
        return w * w * from + 2.0 * w * t * control + t * t * to;
    }

    /**
     * Returns the derivative at the given {@code t} on the quadratic Bézier curve defined by the given points.
     *
     * @param from The starting point of the Bézier curve.
     * @param to The end point of the Bézier curve.
     * @param control The control point of the Bézier curve.
     * @param t The interpolation weight. Must be between zero and one.
     * @return The derivative at the given {@code t} on the quadratic Bézier curve defined by the given points.
     */
    public static float bezierDerivative(float from, float to, float control, float t) {
        return 2.0f * (1 - 0f - t) * (control - from) + 2.0f * t * (to - control);
    }

    /**
     * Returns the derivative at the given {@code t} on the quadratic Bézier curve defined by the given points.
     *
     * @param from The starting point of the Bézier curve.
     * @param to The end point of the Bézier curve.
     * @param control The control point of the Bézier curve.
     * @param t The interpolation weight. Must be between zero and one.
     * @return The derivative at the given {@code t} on the quadratic Bézier curve defined by the given points.
     */
    public static double bezierDerivative(double from, double to, double control, double t) {
        return 2.0 * (1 - 0 - t) * (control - from) + 2.0 * t * (to - control);
    }

    /**
     * Returns the point at the given {@code t} on the cubic Bézier curve defined by the given points.
     *
     * @param from The starting point of the Bézier curve.
     * @param to The end point of the Bézier curve.
     * @param control1 The first control point of the Bézier curve.
     * @param control2 The second control point of the Bézier curve.
     * @param t The interpolation weight. Must be between zero and one.
     * @return The point at the given {@code t} on the cubic Bézier curve defined by the given points.
     */
    public static float bezierInterpolate(float from, float to, float control1, float control2, float t) {
        var w = 1.0f - t;
        var ww = w * w;
        var tt = t * t;
        return w * ww * from + 3.0f * ww * t * control1 + 3.0f * w * tt * control2 + t * tt * to;
    }

    /**
     * Returns the point at the given {@code t} on the cubic Bézier curve defined by the given points.
     *
     * @param from The starting point of the Bézier curve.
     * @param to The end point of the Bézier curve.
     * @param control1 The first control point of the Bézier curve.
     * @param control2 The second control point of the Bézier curve.
     * @param t The interpolation weight. Must be between zero and one.
     * @return The point at the given {@code t} on the cubic Bézier curve defined by the given points.
     */
    public static double bezierInterpolate(double from, double to, double control1, double control2, double t) {
        var w = 1.0 - t;
        var ww = w * w;
        var tt = t * t;
        return w * ww * from + 3.0 * ww * t * control1 + 3.0 * w * tt * control2 + t * tt * to;
    }

    /**
     * Returns the derivative at the given {@code t} on the cubic Bézier curve defined by the given points.
     *
     * @param from The starting point of the Bézier curve.
     * @param to The end point of the Bézier curve.
     * @param control1 The first control point of the Bézier curve.
     * @param control2 The second control point of the Bézier curve.
     * @param t The interpolation weight. Must be between zero and one.
     * @return The derivative at the given {@code t} on the cubic Bézier curve defined by the given points.
     */
    public static float bezierDerivative(float from, float to, float control1, float control2, float t) {
        var w = 1.0f - t;
        return 3.0f * w * w * (control1 - from) + 6.0f * w * t * (control2 - control1) + 3.0f * t * t * (to - control2);
    }

    /**
     * Returns the derivative at the given {@code t} on the cubic Bézier curve defined by the given points.
     *
     * @param from The starting point of the Bézier curve.
     * @param to The end point of the Bézier curve.
     * @param control1 The first control point of the Bézier curve.
     * @param control2 The second control point of the Bézier curve.
     * @param t The interpolation weight. Must be between zero and one.
     * @return The derivative at the given {@code t} on the cubic Bézier curve defined by the given points.
     */
    public static double bezierDerivative(double from, double to, double control1, double control2, double t) {
        var w = 1.0 - t;
        return 3.0 * w * w * (control1 - from) + 6.0 * w * t * (control2 - control1) + 3.0 * t * t * (to - control2);
    }

    /**
     * Checks if the given values are approximately equal using {@link MathUtils#EPSILON}.
     *
     * @param a The first value.
     * @param b The second value.
     * @return True if the two values are approximately equal, otherwise false.
     */
    public static Boolean equalsApprox(float a, float b) {
        return a == b || Math.abs(a - b) < Math.max(EPSILON, EPSILON * Math.abs(a));
    }

    /**
     * Checks if the given values are approximately equal using {@link MathUtils#EPSILON}.
     *
     * @param a The first value.
     * @param b The second value.
     * @return True if the two values are approximately equal, otherwise false.
     */
    public static Boolean equalsApprox(double a, double b) {
        return a == b || Math.abs(a - b) < Math.max(EPSILON, EPSILON * Math.abs(a));
    }

    private MathUtils() {

    }
}