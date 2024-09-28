package io.github.ardentengine.core.math;

import java.io.Serializable;

/**
 * A color represented in the RGBA format by a red, green, blue, and alpha component.
 * <p>
 *     Each component is a float in the range from zero to one.
 *     Values outside this range are allowed.
 * </p>
 *
 * @param r The red component of the color.
 * @param g The green component of the color.
 * @param b The blue component of the color.
 * @param a The alpha component of the color.
 */
public record Color(float r, float g, float b, float a) implements Serializable {

    /**
     * Constructs a color with the given RGB values and an alpha value of one.
     *
     * @param r The red component of the color.
     * @param g The green component of the color.
     * @param b The blue component of the color.
     */
    public Color(float r, float g, float b) {
        this(r, g, b, 1.0f);
    }

    /**
     * Constructs a color from the four given components in range from 0 to 255.
     * Values outside the range are not clamped.
     *
     * @param r The red component of the color.
     * @param g The green component of the color.
     * @param b The blue component of the color.
     * @param a The alpha component of the color.
     */
    public Color(int r, int g, int b, int a) {
        this(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
    }

    /**
     * Constructs a color with the given RGB values and an alpha value of 255.
     *
     * @param r The red component of the color.
     * @param g The green component of the color.
     * @param b The blue component of the color.
     */
    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    /**
     * Returns the red component of this color as an integer in the range from 0 to 255.
     * Values outside the range are clamped.
     *
     * @return The red component of this color as an integer in the range from 0 to 255.
     */
    public int r8() {
        return Math.round(MathUtils.clamp(this.r(), 0.0f, 1.0f) * 255.0f);
    }

    /**
     * Returns the green component of this color as an integer in the range from 0 to 255.
     * Values outside the range are clamped.
     *
     * @return The green component of this color as an integer in the range from 0 to 255.
     */
    public int g8() {
        return Math.round(MathUtils.clamp(this.g(), 0.0f, 1.0f) * 255.0f);
    }

    /**
     * Returns the blue component of this color as an integer in the range from 0 to 255.
     * Values outside the range are clamped.
     *
     * @return The blue component of this color as an integer in the range from 0 to 255.
     */
    public int b8() {
        return Math.round(MathUtils.clamp(this.b(), 0.0f, 1.0f) * 255.0f);
    }

    /**
     * Returns the alpha component of this color as an integer in the range from 0 to 255.
     * Values outside the range are clamped.
     *
     * @return The alpha component of this color as an integer in the range from 0 to 255.
     */
    public int a8() {
        return Math.round(MathUtils.clamp(this.a(), 0.0f, 1.0f) * 255.0f);
    }

    /**
     * Returns an integer representing this color in the RGBA format.
     * Values outside the range are clamped.
     *
     * @return An integer representing this color in the RGBA format.
     */
    public int rgba() {
        return (this.r8() << 24) | (this.g8() << 16) | (this.b8() << 8) | this.a8();
    }

    /**
     * Returns an integer representing this color in the ARGB format.
     * Values outside the range are clamped.
     *
     * @return An integer representing this color in the ARGB format.
     */
    public int argb() {
        return (this.a8() << 24) | (this.r8() << 16) | (this.g8() << 8) | this.b8();
    }

    /**
     * Returns the HSV hue of this color in the range from zero to one.
     *
     * @return The HSV hue of this color in the range from zero to one.
     */
    public float h() {
        var min = Math.min(Math.min(this.r(), this.g()), this.b());
        var max = Math.max(Math.max(this.r(), this.g()), this.b());
        var delta = max - min;
        if(delta != 0.0f) {
            var h = 0.0f;
            if(this.r() == max) {
                h = (this.g() - this.b()) / delta;
            } else if(this.g() == max) {
                h = 2.0f + (this.b() - this.r()) / delta;
            } else {
                h = 4.0f + (this.r() - this.g()) / delta;
            }
            if (h < 0.0f) {
                return h / 6.0f + 1.0f;
            }
            return h / 6.0f;
        }
        return 0.0f;
    }

    /**
     * Returns the HSV saturation of this color in the range from zero to one.
     *
     * @return The HSV saturation of this color in the range from zero to one.
     */
    public float s() {
        var min = Math.min(Math.min(this.r(), this.g()), this.b());
        var max = Math.max(Math.max(this.r(), this.g()), this.b());
        return max == 0.0f ? 0.0f : (max - min) / max;
    }

    /**
     * Returns the HSV brightness of this color in the range from zero to one.
     *
     * @return The HSV brightness of this color in the range from zero to one.
     */
    public float v() {
        return Math.max(Math.max(this.r(), this.g()), this.b());
    }

    /**
     * Adds the given values to each component of this color and returns the result.
     *
     * @param r The red component to add.
     * @param g The green component to add.
     * @param b The blue component to add.
     * @param a The alpha component to add.
     * @return The sum between this color and the given components.
     */
    public Color plus(float r, float g, float b, float a) {
        return new Color(this.r() + r, this.g() + g, this.b() + b, this.a() + a);
    }

    /**
     * Adds the given values to each component of this color and returns the result.
     *
     * @param r The red component to add.
     * @param g The green component to add.
     * @param b The blue component to add.
     * @return The sum between this color and the given components.
     */
    public Color plus(float r, float g, float b) {
        return this.plus(r, g, b, 0.0f);
    }

    /**
     * Adds each component of this color with the components of the given color and returns the result.
     *
     * @param c The color to add.
     * @return The sum between this color and the given one.
     */
    public Color plus(Color c) {
        return this.plus(c.r(), c.g(), c.b(), c.a());
    }

    /**
     * Subtracts the given values from each component of this color and returns the result.
     *
     * @param r The red component to subtract.
     * @param g The green component to subtract.
     * @param b The blue component to subtract.
     * @param a The alpha component to subtract.
     * @return The subtraction between this color and the given components.
     */
    public Color minus(float r, float g, float b, float a) {
        return new Color(this.r() - r, this.g() - g, this.b() - b, this.a() - a);
    }

    /**
     * Subtracts the given values from each component of this color and returns the result.
     *
     * @param r The red component to subtract.
     * @param g The green component to subtract.
     * @param b The blue component to subtract.
     * @return The subtraction between this color and the given components.
     */
    public Color minus(float r, float g, float b) {
        return this.minus(r, g, b, 0.0f);
    }

    /**
     * Subtracts each component of the given color from the components of this color and returns the result.
     *
     * @param c The color to subtract.
     * @return The subtraction between this color and the given one.
     */
    public Color minus(Color c) {
        return this.minus(c.r(), c.g(), c.b(), c.a());
    }

    /**
     * Multiplies each component of this color with the given values and returns the result.
     *
     * @param r The red component to multiply.
     * @param g The green component to multiply.
     * @param b The blue component to multiply.
     * @param a The alpha component to multiply.
     * @return The component-wise product between this color and the given values.
     */
    public Color multiply(float r, float g, float b, float a) {
        return new Color(this.r() * r, this.g() * g, this.b() * b, this.a() * a);
    }

    /**
     * Multiplies each component of this color with each component of the given one and returns the result.
     *
     * @param c The color to multiply this one by.
     * @return The component-wise product between this color and the given one.
     */
    public Color multiply(Color c) {
        return this.multiply(c.r(), c.g(), c.b(), c.a());
    }

    /**
     * Multiplies each component of this color with the given value and returns the result.
     *
     * @param f The value to multiply this color by.
     * @return The product between this color and the given value.
     */
    public Color multiply(float f) {
        return this.multiply(f, f, f, f);
    }

    /**
     * Divides each component of this color by the given values and returns the result.
     *
     * @param r The divisor of the red component.
     * @param g The divisor of the green component.
     * @param b The divisor of the blue component.
     * @param a The divisor of the alpha component.
     * @return The component-wise division between this color and the given values.
     */
    public Color divide(float r, float g, float b, float a) {
        return new Color(this.r() / r, this.g() / g, this.b() / b, this.a() / a);
    }

    /**
     * Divides each component of this color by each component of the given one and returns the result.
     *
     * @param c The color to divide this one by.
     * @return The component-wise division between this color and the given one.
     */
    public Color divide(Color c) {
        return this.divide(c.r(), c.g(), c.b(), c.a());
    }

    /**
     * Divides each component of this color by the given value and returns the result.
     *
     * @param f The value to divide this color by.
     * @return The division between this color and the given value.
     */
    public Color divide(float f) {
        return this.divide(f, f, f, f);
    }

    /**
     * Blends this color and the given components and returns the result.
     *
     * @param r The red component of the color to blend this one with.
     * @param g The green component of the color to blend this one with.
     * @param b The blue component of the color to blend this one with.
     * @param a The alpha component of the color to blend this one with.
     * @return The color resulting from overlaying this color over the given one.
     */
    public Color blend(float r, float g, float b, float a) {
        var sa = 1.0f - a;
        var ra = this.a() * sa + a;
        return ra == 0.0f ? new Color(0.0f, 0.0f, 0.0f, 0.0f) : new Color(
            (this.r * this.a * sa + r * a) / ra,
            (this.g * this.a * sa + g * a) / ra,
            (this.b * this.a * sa + b * a) / ra, ra
        );
    }

    /**
     * Blends this color and the given one and returns the result.
     *
     * @param c The color to blend this one with.
     * @return The color resulting from overlaying this color over the given one.
     */
    public Color blend(Color c) {
        return this.blend(c.r(), c.g(), c.b(), c.a());
    }

    /**
     * Returns this color with its {@link Color#r()}, {@link Color#g()}, and {@link Color#b()} components inverted.
     *
     * @return The inverse of this color.
     */
    public Color inverted() {
        return new Color(1.0f - this.r, 1.0f - this.g, 1.0f - this.b, this.a);
    }

    /**
     * Returns the perceived brightness of this color using the RGB to Luma conversion formula.
     * <p>
     *     Colors with a luminance smaller than 0.5 can be generally considered dark.
     * </p>
     *
     * @return The luminance of this color.
     */
    public float luminance() {
        return 0.2126f * this.r() + 0.7152f * this.g() + 0.0722f * this.b();
    }

    /**
     * Makes this color darker by the given amount and returns the result.
     *
     * @param k The ratio from zero to one.
     * @return A color resulting from making this color darker by the given amount.
     * @see Color#lighter(float)
     */
    public Color darker(float k) {
        k = 1.0f - k;
        return new Color(this.r() * k, this.g() * k, this.b() * k, this.a());
    }

    /**
     * Makes this color lighter by the given amount and returns the result.
     *
     * @param k The ratio from zero to one.
     * @return A color resulting from making this color lighter by the given amount.
     * @see Color#darker(float)
     */
    public Color lighter(float k) {
        return new Color(this.r() + (1.0f - this.r()) * k, this.g() + (1.0f - this.g()) * k, this.b() + (1.0f - this.b()) * k, this.a());
    }

    /**
     * Computes the linear interpolation between this color and the given one by the given weight and returns the result.
     * <p>
     *     The given weight must be in the range from zero to one, representing the amount of interpolation.
     * </p>
     *
     * @param to The color to interpolate to.
     * @param weight The weight of the interpolation between zero and one.
     * @return The result of linearly interpolating between this color and the given one.
     */
    public Color lerp(Color to, float weight) {
        return new Color(
            MathUtils.lerp(this.r(), to.r(), weight),
            MathUtils.lerp(this.g(), to.g(), weight),
            MathUtils.lerp(this.b(), to.b(), weight),
            MathUtils.lerp(this.a(), to.a(), weight)
        );
    }

    /**
     * Checks if the RGB components of this color are equal to the given ones.
     * The alpha component is ignored.
     *
     * @param r The red component of the color.
     * @param g The green component of the color.
     * @param b The blue component of the color.
     * @return True if the components of this color are equal to the given ones, otherwise false.
     */
    public boolean equals(float r, float g, float b) {
        return this.r() == r && this.g() == g && this.b() == b;
    }

    /**
     * Checks if the components of this color are equal to the given ones.
     *
     * @param r The red component of the color.
     * @param g The green component of the color.
     * @param b The blue component of the color.
     * @param a The alpha component of the color.
     * @return True if the components of this color are equal to the given ones, otherwise false.
     */
    public boolean equals(float r, float g, float b, float a) {
        return this.equals(r, g, b) && this.a() == a;
    }

    /**
     * Checks if the components of this color are approximately equal to the given ones using {@link MathUtils#EPSILON}.
     * The alpha component is ignored.
     *
     * @param r The red component of the color.
     * @param g The green component of the color.
     * @param b The blue component of the color.
     * @return True if the components of this color are approximately equal to the given ones, otherwise false.
     * @see MathUtils#equalsApprox(float, float)
     */
    public boolean equalsApprox(float r, float g, float b) {
        return MathUtils.equalsApprox(this.r(), r) && MathUtils.equalsApprox(this.g(), g) && MathUtils.equalsApprox(this.b(), b);
    }

    /**
     * Checks if the components of this color are approximately equal to the given ones using {@link MathUtils#EPSILON}.
     *
     * @param r The red component of the color in the range from zero to one.
     * @param g The green component of the color in the range from zero to one.
     * @param b The blue component of the color in the range from zero to one.
     * @param a The alpha component of the color in the range from zero to one.
     * @return True if the components of this color are approximately equal to the given ones, otherwise false.
     * @see MathUtils#equalsApprox(float, float)
     */
    public boolean equalsApprox(float r, float g, float b, float a) {
        return this.equalsApprox(r, g, b) && MathUtils.equalsApprox(this.a(), a);
    }

    /**
     * Checks if the components of this color are approximately equal to the components of the given one using {@link MathUtils#EPSILON}.
     *
     * @param c The second color.
     * @return True if the components of this color are approximately equal to the components of the given one, otherwise false.
     */
    public boolean equalsApprox(Color c) {
        return this.equalsApprox(c.r(), c.g(), c.b(), c.a());
    }

    /**
     * Checks if this color is approximately equal to the given one in the RGBA format.
     *
     * @param rgba The value in the RGBA format to compare this color to.
     * @return True if this color is approximately equal to the given one, otherwise false.
     */
    public boolean equalsApprox(int rgba) {
        return this.equalsApprox(((rgba >> 24) & 0xff) / 255.0f, ((rgba >> 16) & 0xff) / 255.0f, ((rgba >> 8) & 0xff) / 255.0f, (rgba & 0xff) / 255.0f);
    }

    /**
     * Constructs a color from the given integer value in the RGBA format.
     *
     * @param value The color value in RGBA format.
     * @return A new color constructed from the given integer in the RGBA format.
     */
    public static Color rgba(int value) {
        return new Color((value >> 24) & 0xff, (value >> 16) & 0xff, (value >> 8) & 0xff, value & 0xff);
    }

    /**
     * Constructs a color from the given integer value in the ARGB format.
     *
     * @param value The color value in ARGB format.
     * @return A new color constructed from the given integer in the ARGB format.
     */
    public static Color argb(int value) {
        return new Color((value >> 16) & 0xff, (value >> 8) & 0xff, value & 0xff, (value >> 24) & 0xff);
    }

    /**
     * Constructs a color from the given components in the HSV format.
     *
     * @param h The hue of the color.
     * @param s The saturation of the color.
     * @param v The lightness (value) of the color.
     * @param a The alpha component of the color.
     * @return A new color constructed from the given components in the HSV format.
     */
    public static Color hsv(float h, float s, float v, float a) {
        var i = (int) Math.floor(h * 6.0f);
        var f = h * 6.0f - i;
        var p = v * (1.0f - s);
        var q = v * (1.0f - f * s);
        var t = v * (1.0f - (1.0f - f) * s);
        return switch (i % 6) {
            case 0 -> new Color(v, t, p, a);
            case 1 -> new Color(q, v, p, a);
            case 2 -> new Color(p, v, t, a);
            case 3 -> new Color(p, q, v, a);
            case 4 -> new Color(t, p, v, a);
            case 5 -> new Color(v, p, q, a);
            default -> throw new IllegalStateException("Unexpected value: " + i % 6);
        };
    }

    /**
     * Constructs a color from the given components in the HSV format and sets the alpha component to one.
     *
     * @param h The hue of the color.
     * @param s The saturation of the color.
     * @param v The lightness (value) of the color.
     * @return A new color constructed from the given components in the HSV format.
     */
    public static Color hsv(float h, float s, float v) {
        return hsv(h, s, v, 1.0f);
    }
}