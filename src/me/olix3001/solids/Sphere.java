package me.olix3001.solids;

import me.olix3001.gui.FloatSpinner;
import me.olix3001.gui.GuiUtils;
import me.olix3001.math.Vector3;
import me.olix3001.pixeldata.Color;
import me.olix3001.render.Intersection;
import me.olix3001.render.Ray;

import javax.swing.*;
import java.awt.*;

public class Sphere extends Solid {
    private float radius;

    public Sphere(Vector3 pos, float radius, Color color, float reflectivity, float emission) {
        super(pos, color, reflectivity, emission);
        this.radius = radius;

        this.name = "Sphere";
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

    @Override
    public void propertiesDialog(JFrame frame) {
        // name
        frame.add(GuiUtils.createNameEdit(this, frame));

        // position
        JLabel positionLabel = new JLabel("Position");
        frame.add(positionLabel);

        JPanel positionPanel = GuiUtils.createVectorEdition(this.position, true);
        positionPanel.setPreferredSize(new Dimension(frame.getWidth() - 30, 100));
        frame.add(positionPanel);

        // radius
        FloatSpinner radius = new FloatSpinner(this.radius);
        radius.addChangeListener((e) -> {
            this.radius = radius.getFloat();
        });
        radius.setPreferredSize(new Dimension(frame.getWidth() - 100, 25));
        JPanel radiusPanel = GuiUtils.createLabelComponentPair("Radius: ", radius);
        frame.add(radiusPanel);

        // reflectivity
        FloatSpinner reflectivity = new FloatSpinner(this.reflectivity);
        reflectivity.addChangeListener((e) -> {
            float v = reflectivity.getFloat();
            if (0 <= v && v <= 1f) {
                this.reflectivity = v;
            } else {
                this.reflectivity = 0f;
                reflectivity.setValue(this.reflectivity);
            }
        });
        reflectivity.setPreferredSize(new Dimension(frame.getWidth() - 100, 25));
        JPanel reflectivityPanel = GuiUtils.createLabelComponentPair("reflectivity: ", reflectivity);
        frame.add(reflectivityPanel);

        // emission
        FloatSpinner emission = new FloatSpinner(this.emission);
        emission.addChangeListener((e) -> {
            float v = emission.getFloat();
            if (0 <= v && v <= 1f) {
                this.emission = v;
            } else {
                this.emission = 0f;
                emission.setValue(this.emission);
            }
        });
        emission.setPreferredSize(new Dimension(frame.getWidth() - 100, 25));
        JPanel emissionPanel = GuiUtils.createLabelComponentPair("emission: ", emission);
        frame.add(emissionPanel);

        // color
        frame.add(GuiUtils.createColorPicker("Color: ", this.color));
    }
}
