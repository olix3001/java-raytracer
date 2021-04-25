package me.olix3001.solids;

import me.olix3001.gui.FloatSpinner;
import me.olix3001.gui.GuiUtils;
import me.olix3001.gui.Viewport;
import me.olix3001.math.Triangulation;
import me.olix3001.math.Vector3;
import me.olix3001.other.Obj;
import me.olix3001.pixeldata.Color;
import me.olix3001.render.Intersection;
import me.olix3001.render.Ray;
import me.olix3001.utils.Face;
import me.olix3001.utils.MtlLoader;
import me.olix3001.utils.ObjLoader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Model extends Solid {

    List<Triangle> triangles;
    private float size;
    private boolean textureTransparency;
    private Obj obj;

    public Model(Vector3 position, Obj obj, float size, float reflectivity, boolean textureTransparency) {
        super(position, Color.BLACK, reflectivity, 0f);
        this.size = size;
        this.textureTransparency = textureTransparency;
        this.obj = obj;
        load();
    }

    private void load() {
        List<Triangle> temp = new ArrayList<>();
        ObjLoader loader = null;
        if (obj.hasMtl()) {
            loader = new ObjLoader(obj.getObjPath(), new MtlLoader(obj.getMtlPath(), textureTransparency));
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
                    if (f.getMaterial().hasTexture()) {
                        Triangle triangle = new TexturedTriangle(
                                position.add(t.a.multiply(size)),
                                position.add(t.b.multiply(size)),
                                position.add(t.c.multiply(size)),
                                t.uva,
                                t.uvb,
                                t.uvc,
                                f.getColor(),
                                this.reflectivity,
                                f.getEmission(),
                                f.getMaterial(),
                                textureTransparency);
                        triangle.setParent(this);
                        temp.add(triangle);
                    } else {
                        Triangle triangle = new Triangle(
                                position.add(t.a.multiply(size)),
                                position.add(t.b.multiply(size)),
                                position.add(t.c.multiply(size)),
                                f.getColor(), this.reflectivity,
                                f.getEmission());
                        triangle.setParent(this);
                        temp.add(triangle);
                    }
                    i++;
                }
            }
            this.triangles = temp;
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

    public void reload() {
        load();
    }

    public void askForMaterial() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("MATERIAL FILE", "mtl"));
        fileChooser.setSelectedFile(new File("model.mtl"));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            this.obj.setMtlPath(file.getAbsolutePath());
        }
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
        FloatSpinner size = new FloatSpinner(this.size);
        size.addChangeListener((e) -> {
            this.size = size.getFloat();
        });
        size.setPreferredSize(new Dimension(frame.getWidth() - 100, 25));
        JPanel scalePanel = GuiUtils.createLabelComponentPair("Scale: ", size);
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

        // material
        JButton material = new JButton(this.obj.hasMtl() ? new File(this.obj.getMtlPath()).getName() : "Select material");
        material.addActionListener((e) -> {
            askForMaterial();
            material.setText(this.obj.hasMtl() ? new File(this.obj.getMtlPath()).getName() : "Select material");
        });
        frame.add(material);

        // reload
        JButton reload = new JButton("Reload object");
        reload.addActionListener((e) -> {
            reload();
        });
        frame.add(reload);
    }
}
