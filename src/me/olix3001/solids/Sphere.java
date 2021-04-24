package me.olix3001.solids;

import me.olix3001.math.Vector3;
import me.olix3001.pixeldata.Color;
import me.olix3001.render.Intersection;
import me.olix3001.render.Ray;

public class Sphere extends Solid {
    private float radius;

    public Sphere(Vector3 pos, float radius, Color color, float reflectivity, float emission, float transparency) {
        super(pos, color, reflectivity, emission, transparency);
        this.radius = radius;
    }

    @Override
    public Intersection calculateIntersection(Ray ray) {
        float t = Vector3.dot(position.subtract(ray.getOrigin()), ray.getDirection());
        Vector3 p = ray.getOrigin().add(ray.getDirection().multiply(t));

        float y = position.subtract(p).length();
        if (y < radius) {
            float x = (float) Math.sqrt(radius * radius - y * y);
            float t0 = t+x;
            float t1 = t-x;
            if (t1 > 0) return new Intersection(ray.getOrigin().add(ray.getDirection().multiply(t1)), ray.getOrigin().add(ray.getDirection().multiply(t0)), this);
            else return null;
        } else {
            return null;
        }
    }

    @Override
    public Vector3 getNormalAt(Vector3 point) {
        return point.subtract(position).normalize();
    }
}
