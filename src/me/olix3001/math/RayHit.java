package me.olix3001.math;

import me.olix3001.render.Intersection;
import me.olix3001.render.Ray;
import me.olix3001.solids.Solid;

public class RayHit {
    private Ray ray;
    private Solid solid;
    private Intersection hitPos;
    private Vector3 normal;

    public RayHit(Ray ray, Solid solid, Intersection hitPos) {
        this.ray = ray;
        this.solid = solid;
        this.hitPos = hitPos;
        this.normal = solid.getNormalAt(hitPos.getStart());
    }

    public Ray getRay() {
        return ray;
    }

    public Solid getSolid() {
        return solid;
    }

    public Vector3 getHitPos() {
        return hitPos.getStart();
    }
    public Intersection getIntersection() {return hitPos;}

    public Vector3 getNormal() {
        return normal;
    }
}
