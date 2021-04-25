package me.olix3001.solids;

import me.olix3001.math.Vector3;
import me.olix3001.pixeldata.Color;
import me.olix3001.render.Intersection;
import me.olix3001.render.Ray;

import javax.swing.*;
import java.io.Serializable;

public abstract class Solid implements Serializable {
    protected Vector3 position;
    protected Color color;
    protected float reflectivity;
    protected float emission;
    protected String name = "unknown object";
    private Solid parent = this;

    public Solid(Vector3 position, Color color, float reflectivity, float emission) {
        this.position = position;
        this.color = color.clone();
        this.reflectivity = reflectivity;
        this.emission = emission;
    }

    public abstract Intersection calculateIntersection(Ray ray);
    public abstract Vector3 getNormalAt(Vector3 point);

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

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

    public Solid getParent() {
        return this.parent;
    }
    public void setParent(Solid parent) {
        this.parent = parent;
    }

    public void propertiesDialog(JFrame frame) {
        JLabel label = new JLabel("This object doesn't support properties");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalAlignment(JLabel.CENTER);
        frame.add(label);
        label.setVisible(true);
    }

    public boolean canDelete() {
        return true;
    }
}
