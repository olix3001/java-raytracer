package me.olix3001.math;

import me.olix3001.gui.ColorChooserButton;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Vector3 implements Serializable {
    private float x, y, z;

    public Vector3(float x, float y, float z) {
        if (Float.isNaN(x) || Float.isNaN(y) || Float.isNaN(z))
            throw new IllegalArgumentException("One or more parameters are NaN");

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static interface VectorChangedListener {
        public void vectorChanged(Vector3 v);
    }

    private List<VectorChangedListener> listeners = null;

    public void addVectorListener(VectorChangedListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
    }

    private void changed() {
        if (listeners != null) {
            for (VectorChangedListener l : listeners) {
                l.vectorChanged(this);
            }
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        changed();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        changed();
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
        changed();
    }

    public Vector3 add(Vector3 vec) {
        return new Vector3(this.x + vec.x, this.y + vec.y, this.z + vec.z);
    }

    public Vector3 subtract(Vector3 vec) {
        return new Vector3(this.x - vec.x, this.y - vec.y, this.z - vec.z);
    }

    public Vector3 multiply(float scalar) {
        return new Vector3(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Vector3 multiply(Vector3 vec) {
        return new Vector3(this.x * vec.x, this.y * vec.y, this.z * vec.z);
    }

    public Vector3 divide(Vector3 vec) {
        return new Vector3(this.x / vec.x, this.y / vec.y, this.z / vec.z);
    }

    public float length() {
        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    public Vector3 normalize() {
        float length = length();
        return new Vector3(this.x / length, this.y / length, this.z / length);
    }

    public void translate(Vector3 vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
    }

    public Vector3 rotateYP(float yaw, float pitch) {
        // Convert to radians
        double yawRads = Math.toRadians(yaw);
        double pitchRads = Math.toRadians(pitch);

        // Step one: Rotate around X axis (pitch)
        float _y = (float) (y*Math.cos(pitchRads) - z*Math.sin(pitchRads));
        float _z = (float) (y*Math.sin(pitchRads) + z*Math.cos(pitchRads));

        // Step two: Rotate around the Y axis (yaw)
        float _x = (float) (x*Math.cos(yawRads) + _z*Math.sin(yawRads));
        _z = (float) (-x*Math.sin(yawRads) + _z*Math.cos(yawRads));

        return new Vector3(_x, _y, _z);
    }

    public static float distance(Vector3 a, Vector3 b) {
        return (float) Math.sqrt(Math.pow(a.x - b.x, 2)+Math.pow(a.y - b.y, 2)+Math.pow(a.z - b.z, 2));
    }

    public static float dot(Vector3 a, Vector3 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }
    public static Vector3 cross(Vector3 a, Vector3 b) {
        return new Vector3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
    }

    public static Vector3 lerp(Vector3 a, Vector3 b, float t) {
        return a.add(b.subtract(a).multiply(t));
    }

    @Override
    public Vector3 clone() {
        return new Vector3(x, y, z);
    }

    @Override
    public String toString() {
        return "Vector3 { x=" + x + ", y=" + y + ", z=" + z + " }";
    }

    public float[] toArray() {
        return new float[]{x, y, z};
    }
}
