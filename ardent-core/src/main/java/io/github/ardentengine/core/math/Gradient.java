package io.github.ardentengine.core.math;

import io.github.ardentengine.core.resources.YamlMappingDeserializer;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public final class Gradient {

    // TODO: Move the Gradient outside of the math package

    // TODO: Gradients being immutable cause too many allocations and copies of TreeMap. Make them mutable once the events system is implemented.

    private final TreeMap<Float, Color> points;

    private Gradient(TreeMap<Float, Color> points) {
        this.points = points;
    }

    public Gradient() {
        this(new TreeMap<>());
    }

    public Gradient withPoint(Color color, float offset) {
        var points = new TreeMap<>(this.points);
        points.put(offset, color);
        return new Gradient(points);
    }

    public Gradient withPoint(float offset, Color color) {
        return this.withPoint(color, offset);
    }

    public int getPointCount() {
        return this.points.size();
    }

    public Color sample(float offset) {
        if(this.points.isEmpty()) {
            return new Color(0.0f, 0.0f, 0.0f);
        }
        var exact = this.points.get(offset);
        if(exact != null) {
            return exact;
        }
        var firstEntry = this.points.firstEntry();
        if(offset < firstEntry.getKey()) {
            return firstEntry.getValue();
        }
        var lastEntry = this.points.lastEntry();
        if(offset > lastEntry.getKey()) {
            return lastEntry.getValue();
        }
        var maxBefore = this.points.floorEntry(offset);
        var minAfter = this.points.ceilingEntry(offset);
        return maxBefore.getValue().lerp(minAfter.getValue(), (offset - maxBefore.getKey()) / (minAfter.getKey() - maxBefore.getKey()));
    }

    public Color getColor(float offset) {
        if(this.points.isEmpty()) {
            return new Color(0.0f, 0.0f, 0.0f);
        }
        var exact = this.points.get(offset);
        if(exact != null) {
            return exact;
        }
        var maxBefore = this.points.floorEntry(offset);
        if(maxBefore != null) {
            return maxBefore.getValue();
        }
        return this.points.firstEntry().getValue();
    }

    public Gradient removePoint(float offset) {
        var points = new TreeMap<>(this.points);
        points.remove(offset);
        return new Gradient(points);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof Gradient gradient && Objects.equals(this.points, gradient.points);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.points);
    }

    public static Gradient between(Color start, Color end) {
        var points = new TreeMap<Float, Color>();
        points.put(0.0f, start);
        points.put(1.0f, end);
        return new Gradient(points);
    }

    public static class Deserializer implements YamlMappingDeserializer {

        @Override
        public Object deserialize(Map<Object, Object> map) {
            var gradient = new TreeMap<Float, Color>();
            for(var key : map.keySet()) {
                if(key instanceof Number offset && map.get(key) instanceof Color color) {
                    gradient.put(offset.floatValue(), color);
                }
            }
            return new Gradient(gradient);
        }

        @Override
        public Class<?> getTag() {
            return Gradient.class;
        }
    }
}