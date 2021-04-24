package me.olix3001.solids;

import me.olix3001.math.Vector3;
import me.olix3001.pixeldata.Color;
import me.olix3001.render.Intersection;
import me.olix3001.render.Ray;

public abstract class Solid {
    protected Vector3 position;
    protected Color color;
    protected float reflectivity;
    protected float emission;

    public Solid(Vector3 position, Color color, float reflectivity, float emission) {
        this.position = position;
        this.color = color;
        this.reflectivity = reflectivity;
        this.emission = emission;
    }

    public abstract Intersection calculateIntersection(Ray ray);
    public abstract Vector3 getNormalAt(Vector3 point);

    public Vector3 getPosition() {
        return position;
    }
    public void setPosition(Vector3 position) { this.position = position; }
    public void translate(Vector3 offset) { this.position = this.position.add(offset); }

    public Color getColor() {
        return color;
    }
    public Color getTextureColor(Vector3 point) {
        return getColor();
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public float getEmission() {
        return emission;
    }

    public int getObjectCount() { return 1; }
}
