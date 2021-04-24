package me.olix3001.utils;

import me.olix3001.math.Vector3;
import me.olix3001.pixeldata.Color;

import java.util.List;

public class Face {
    private List<Vector3> vertices;
    private Color color;
    private float transparency;
    private float reflectivity;
    private float emission;

    public Face(List<Vector3> vertices, Color color, float transparency, float reflectivity, float emission) {
        this.vertices = vertices;
        this.color = color;
        this.transparency = transparency;
        this.reflectivity = reflectivity;
        this.emission = emission;
    }

    public Face(List<Vector3> vertices, Material mtl) {
        this.vertices = vertices;
        this.color = mtl.getKd();
        this.transparency = mtl.getT();
        this.emission = mtl.getKe().getLuminance();
    }

    public List<Vector3> getVertices() {
        return vertices;
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
}
