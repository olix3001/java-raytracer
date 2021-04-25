package me.olix3001.solids;

import me.olix3001.gui.FloatSpinner;
import me.olix3001.gui.GuiUtils;
import me.olix3001.math.Vector3;
import me.olix3001.pixeldata.Color;
import me.olix3001.render.Intersection;
import me.olix3001.render.Ray;

import javax.swing.*;
import java.awt.*;

public class Box extends Solid {
    private Vector3 min, max;
    private float scale;

    public Box(Vector3 position, float scale, Color color, float reflectivity, float emission) {
        super(position, color, reflectivity, emission);
        setScale(scale);
        this.scale = scale;

        this.name = "Box";
    }

    public void setScale(float scale) {
        Vector3 v = new Vector3(scale, scale, scale);
        this.max = position.add(v.multiply(0.5F));
        this.min = position.subtract(v.multiply(0.5F));
    }

    @Override
    public Intersection calculateIntersection(Ray ray) {
        float t1,t2,tnear = Float.NEGATIVE_INFINITY,tfar = Float.POSITIVE_INFINITY,temp;
        boolean intersectFlag = true;
        float[] rayDirection = ray.getDirection().toArray();
        float[] rayOrigin = ray.getOrigin().toArray();
        float[] b1 = min.toArray();
        float[] b2 = max.toArray();

        for(int i =0 ;i < 3; i++){
            if(rayDirection[i] == 0){
                if(rayOrigin[i] < b1[i] || rayOrigin[i] > b2[i])
                    intersectFlag = false;
            }
            else{
                t1 = (b1[i] - rayOrigin[i])/rayDirection[i];
                t2 = (b2[i] - rayOrigin[i])/rayDirection[i];
                if(t1 > t2){
                    temp = t1;
                    t1 = t2;
                    t2 = temp;
                }
                if(t1 > tnear)
                    tnear = t1;
                if(t2 < tfar)
                    tfar = t2;
                if(tnear > tfar)
                    intersectFlag = false;
                if(tfar < 0)
                    intersectFlag = false;
            }
        }
        if(intersectFlag)
            return new Intersection(ray.getOrigin().add(ray.getDirection().multiply(tnear)), ray.getOrigin().add(ray.getDirection().multiply(tfar)), this);
        else
            return null;
    }

    @Override
    public Vector3 getNormalAt(Vector3 point) {
        float[] direction = point.subtract(position).toArray();
        float biggestValue = Float.NaN;

        for (int i = 0; i<3; i++) {
            if (Float.isNaN(biggestValue) || biggestValue < Math.abs(direction[i])) {
                biggestValue = Math.abs(direction[i]);
            }
        }

        if (biggestValue == 0) {
            return new Vector3(0, 0, 0);
        } else {
            for (int i = 0; i<3; i++) {
                if (Math.abs(direction[i]) == biggestValue) {
                    float[] normal = new float[] {0,0,0};
                    normal[i] = direction[i] > 0 ? 1 : -1;

                    return new Vector3(normal[0], normal[1], normal[2]);
                }
            }
        }

        return new Vector3(0, 0, 0);
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

        // size
        FloatSpinner scale = new FloatSpinner(this.scale);
        scale.addChangeListener((e) -> {
            this.scale = scale.getFloat();
            setScale(this.scale);
        });
        scale.setPreferredSize(new Dimension(frame.getWidth() - 100, 25));
        JPanel scalePanel = GuiUtils.createLabelComponentPair("Scale: ", scale);
        frame.add(scalePanel);

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
