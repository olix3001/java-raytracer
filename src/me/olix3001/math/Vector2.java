package me.olix3001.math;

public class Vector2 {
    private float x, y;

    public Vector2(float x, float y) {
        if (Float.isNaN(x) || Float.isNaN(y))
            throw new IllegalArgumentException("One or more parameters are NaN");

        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Vector2 add(Vector2 vec) {
        return new Vector2(this.x + vec.x, this.y + vec.y);
    }

    public Vector2 subtract(Vector2 vec) {
        return new Vector2(this.x - vec.x, this.y - vec.y);
    }

    public Vector2 multiply(float scalar) {
        return new Vector2(this.x * scalar, this.y * scalar);
    }

    public Vector2 multiply(Vector2 vec) {
        return new Vector2(this.x * vec.x, this.y * vec.y);
    }

    public Vector2 divide(Vector2 vec) {
        return new Vector2(this.x / vec.x, this.y / vec.y);
    }

    public float length() {
        return (float) Math.sqrt(x*x + y*y);
    }

    public Vector2 normalize() {
        float length = length();
        return new Vector2(this.x / length, this.y / length);
    }

    public void translate(Vector2 vec) {
        this.x += vec.x;
        this.y += vec.y;
    }

    public static float distance(Vector2 a, Vector2 b) {
        return (float) Math.sqrt(Math.pow(a.x - b.x, 2)+Math.pow(a.y - b.y, 2));
    }

    public static float dot(Vector2 a, Vector2 b) {
        return a.x * b.x + a.y * b.y;
    }
    public static float cross(Vector2 a, Vector2 b) {
        return (a.x*b.y) - (a.y*b.x);
    }

    public static Vector2 lerp(Vector2 a, Vector2 b, float t) {
        return a.add(b.subtract(a).multiply(t));
    }

    @Override
    public Vector2 clone() {
        return new Vector2(x, y);
    }

    @Override
    public String toString() {
        return "Vector2 { x=" + x + ", y=" + y + " }";
    }

    public float[] toArray() {
        return new float[]{x, y};
    }
}
