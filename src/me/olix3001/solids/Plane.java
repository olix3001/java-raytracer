package me.olix3001.solids;

import me.olix3001.math.Vector3;
import me.olix3001.pixeldata.Color;
import me.olix3001.render.Intersection;
import me.olix3001.render.Ray;

public class Plane extends Solid {

    public Plane(float height, Color color, float reflectivity, float emission) {
        super(new Vector3(0, height, 0), color, reflectivity, emission);
    }

    @Override
    public Intersection calculateIntersection(Ray ray) {
        float t = -(ray.getOrigin().getY()-position.getY()) / ray.getDirection().getY();
        if (t > 0 && Float.isFinite(t))
        {
            return new Intersection(ray.getOrigin().add(ray.getDirection().multiply(t)), this);
        }

        return null;
    }

    @Override
    public Vector3 getNormalAt(Vector3 point) {
        return new Vector3(0, 1, 0);
    }
}
