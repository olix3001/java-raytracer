package me.olix3001.utils;

import me.olix3001.math.Vector2;
import me.olix3001.math.Vector3;
import me.olix3001.pixeldata.Color;

import java.util.List;

public class Face {
    private List<Vector3> vertices;
    private List<Vector2> UVs;
    private Color color;
    private float transparency;
    private float reflectivity;
    private float emission;
    private Material material;

    public Face(List<Vector3> vertices, List<Vector2> UVs, Color color, float transparency, float reflectivity, float emission) {
        this.vertices = vertices;
        this.UVs = UVs;
        this.material = new Material();
        this.color = color;
        this.transparency = transparency;
        this.reflectivity = reflectivity;
        this.emission = emission;
    }

    public Face(List<Vector3> vertices, List<Vector2> UVs, Material mtl) {
        this.vertices = vertices;
        this.UVs = UVs;
        this.color = mtl.getKd();
        this.transparency = mtl.getT();
        this.emission = mtl.getKe().getLuminance();

        this.material = mtl;
    }

    public List<Vector3> getVertices() {
        return vertices;
    }
    public List<Vector2> getUVs() {
        return UVs;
    }

    public Color getColor() {
        return color;
    }

    public float getTransparency() {
        return transparency;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public float getEmission() {
        return emission;
    }

    public Material getMaterial() {
        return material;
    }
}
