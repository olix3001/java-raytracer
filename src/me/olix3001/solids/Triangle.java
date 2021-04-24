package me.olix3001.solids;

import me.olix3001.math.Vector3;
import me.olix3001.pixeldata.Color;
import me.olix3001.render.Intersection;
import me.olix3001.render.Ray;

public class Triangle extends Solid {

    private Vector3 position2;
    private Vector3 position3;
    public float EPSILON = 0.0000001f;

    public Triangle(Vector3 position, Vector3 position2, Vector3 position3, Color color, float reflectivity, float emission, float transparency) {
        super(position, color, reflectivity, emission, transparency);
        this.position2 = position2;
        this.position3 = position3;
    }

    @Override
    public Intersection calculateIntersection(Ray ray) {
        Vector3 rayOrigin = ray.getOrigin();
        Vector3 rayDirection = ray.getDirection();

        Vector3 edge1 = position2.subtract(position);
        Vector3 edge2 = position3.subtract(position);
        Vector3 h = Vector3.cross(rayDirection, edge2);
        float a = Vector3.dot(edge1, h);
        if (a > -EPSILON && a < EPSILON) {
            return null;
        }
        float f = 1f/a;
        Vector3 s = rayOrigin.subtract(position);
        float u = f * Vector3.dot(s, h);
        if (u < 0f || u > 1f) {
            return null;
        }
        Vector3 q = Vector3.cross(s, edge1);
        float v = f * Vector3.dot(rayDirection, q);
        if (v < 0f || u + v > 1f) {
            return null;
        }
        float t = f * Vector3.dot(edge2, q);
        if (t > EPSILON) {
            return new Intersection(rayOrigin.add(rayDirection.multiply(t)), this);
        } else {
            return null;
        }
    }

    @Override
    public Vector3 getNormalAt(Vector3 point) {
        Vector3 u = position2.subtract(position);
        Vector3 v = position3.subtract(position);

        return new Vector3(
                u.getY() * v.getZ() - u.getZ() * v.getY(),
                u.getZ() * v.getX() - u.getX() * v.getZ(),
                u.getX() * v.getY() - u.getY() * v.getX()
        ).normalize();
    }

    public Vector3 getPosition2() {
        return position2;
    }

    public Vector3 getPosition3() {
        return position3;
    }
}
