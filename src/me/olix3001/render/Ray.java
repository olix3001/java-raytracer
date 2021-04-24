package me.olix3001.render;

import me.olix3001.math.Vector3;

public class Ray {
    public Vector3 origin;
    public Vector3 direction;

    public Ray(Vector3 origin, Vector3 direction) {
        this.origin = origin;
        if (direction.length() != 1) {
            direction.normalize();
        }
        this.direction = direction;
    }

    public Vector3 getOrigin() {
        return origin;
    }
    public Vector3 getDirection() {
        return direction;
    }
}
