package me.olix3001.solids;

import me.olix3001.math.Triangulation;
import me.olix3001.math.Vector3;
import me.olix3001.other.Obj;
import me.olix3001.pixeldata.Color;
import me.olix3001.render.Intersection;
import me.olix3001.render.Ray;
import me.olix3001.utils.Face;
import me.olix3001.utils.MtlLoader;
import me.olix3001.utils.ObjLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Model extends Solid {

    List<Triangle> triangles = new ArrayList<>();
    private float size;

    public Model(Vector3 position, Obj obj, float size, float reflectivity, float emission, float transparency) {
        super(position, Color.BLACK, reflectivity, emission, transparency);
        this.size = size;

        ObjLoader loader = null;
        if (obj.hasMtl()) {
            loader = new ObjLoader(obj.getObjPath(), new MtlLoader(obj.getMtlPath()));
        } else {
            loader = new ObjLoader(obj.getObjPath());
        }
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (loader.isLoaded()) {
            int i = 0;
            for (Face f : loader.getFaces()) {
                for (Triangulation.Triangle t : Triangulation.triangulateFace(f)) {
                    triangles.add(new Triangle(position.add(t.a.multiply(size)), position.add(t.b.multiply(size)), position.add(t.c.multiply(size)), f.getColor(), this.reflectivity, f.getEmission(), f.getTransparency()));
                    i++;
                }
            }
        } else {
            throw new RuntimeException("Cannot load object");
        }
    }

    @Override
    public Intersection calculateIntersection(Ray ray) {
        Intersection closest = null;
        float closestDistance = Float.POSITIVE_INFINITY;
        for (Triangle t : triangles) {
            Intersection i = t.calculateIntersection(ray);
            if (i == null) continue;
            float distance = Vector3.distance(i.getStart(), ray.getOrigin());
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = i;
            }
        }
        return closest;
    }

    @Override
    public Vector3 getNormalAt(Vector3 point) {
        return new Vector3(0, 0, 0);
    }

    @Override
    public Color getTextureColor(Vector3 point) {
        return new Color(0, 0, 0);
    }

    @Override
    public int getObjectCount() {
        return triangles.size();
    }

    @Override
    public void setPosition(Vector3 position) {
        Vector3 diff = position.subtract(this.position);
        this.position = position;
        for (Triangle t : triangles) {
            t.setPosition(position.add(diff));
        }
    }
    @Override
    public void translate(Vector3 offset) {
        this.position = this.position.add(offset);
        for (Triangle t : triangles) {
            t.setPosition(position.add(offset));
        }
    }
}
