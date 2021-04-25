package me.olix3001.render;

import me.olix3001.math.RayHit;
import me.olix3001.math.Vector3;
import me.olix3001.solids.Solid;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private Camera camera;
    private Light light;
    private List<Solid> solids;

    public Scene() {
        this.solids = new ArrayList<Solid>();
        this.camera = new Camera();
        this.light = new Light(new Vector3(.3f, 3f, -1.2f));
    }

    public void addSolid(Solid solid) {
        this.solids.add(solid);
    }

    public List<Solid> getSolids() {
        return solids;
    }

    public void setSolids(List<Solid> solids) {
        this.solids = solids;
    }

    public void removeSolid(Solid s) {
        solids.remove(s);
    }

    public void clearSolids() {
        this.solids.clear();
    }

    public RayHit raycast(Ray ray) {

        RayHit closestHit = null;
        for (Solid solid : solids.toArray(new Solid[0])) {
            if (solid == null)
                continue;

            Intersection hitPos = solid.calculateIntersection(ray);
            if (hitPos != null && (closestHit == null || Vector3.distance(closestHit.getHitPos(), ray.getOrigin()) > Vector3.distance(hitPos.getStart(), ray.getOrigin()))) {
                closestHit = new RayHit(ray, hitPos.getSolid(), hitPos);
            }
        }
        return closestHit;
    }

    public Camera getCamera() {
        return camera;
    }

    public Light getLight() {
        return light;
    }

    public int countSolids() {
        int c = 0;
        for (Solid s : solids) {
            c += s.getObjectCount();
        }
        return c;
    }
}
