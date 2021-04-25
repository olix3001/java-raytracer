package me.olix3001.gui;

import me.olix3001.math.RayHit;
import me.olix3001.math.Vector2;
import me.olix3001.math.Vector3;
import me.olix3001.other.IOMenager;
import me.olix3001.other.Obj;
import me.olix3001.pixeldata.Color;
import me.olix3001.render.Camera;
import me.olix3001.render.Ray;
import me.olix3001.render.Renderer;
import me.olix3001.render.Scene;
import me.olix3001.solids.Box;
import me.olix3001.solids.Model;
import me.olix3001.solids.Solid;
import me.olix3001.solids.Sphere;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class OptionsPopup extends JPopupMenu {

    private Vector2 screenPosition;
    private Viewport viewport;
    private Vector3 hitPosition;

    public void addObject(Solid s) {
        this.viewport.getScene().addSolid(s);
        PropertiesDialog.get().editProperties(s);
    }

    public RayHit raycastFromPixel(Vector2 p, Viewport viewport) {
        float[] uv = Renderer.getNormalizedScreenCoordinates((int) p.getX(), (int) p.getY(), viewport.getWidth(), viewport.getHeight());

        Scene scene = viewport.getScene();
        Vector3 eyePos = new Vector3(0, 0, (float) (-1/Math.tan(Math.toRadians(scene.getCamera().getFov()/2))));
        Camera cam = scene.getCamera();
        Vector3 rayDir = new Vector3(uv[0], uv[1], 0).subtract(eyePos).normalize().rotateYP(cam.getYaw(), cam.getPitch());
        RayHit hit = scene.raycast(new Ray(eyePos.add(cam.getPosition()), rayDir));
        return hit;
    }

    public OptionsPopup(Vector2 screenPosition, Viewport viewport) {
        super();
        this.screenPosition = screenPosition;
        this.viewport = viewport;
        RayHit hit = raycastFromPixel(screenPosition, viewport);

        JMenu IOMenu = new JMenu("IO");
        // save
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener((e) -> {
            IOMenager.askAndSave(viewport);
        });
        IOMenu.add(save);
        // load
        JMenuItem load = new JMenuItem("Load");
        load.addActionListener((e) -> {
            IOMenager.askAndLoad(viewport);
        });
        IOMenu.add(load);
        // save json
        JMenuItem saveJSON = new JMenuItem("Save json");
        // TODO: IMPLEMENT
        saveJSON.setEnabled(false);
        saveJSON.addActionListener((e) -> {
        });
        IOMenu.add(saveJSON);
        // load json
        JMenuItem loadJSON = new JMenuItem("Load json");
        // TODO: IMPLEMENT
        loadJSON.setEnabled(false);
        loadJSON.addActionListener((e) -> {
        });
        IOMenu.add(loadJSON);

        add(IOMenu);
        if (hit == null) {
            show(viewport, (int) screenPosition.getX(), (int) screenPosition.getY());
            return;
        }
        this.hitPosition = hit.getHitPos();

        JMenu newObject = new JMenu("New object");
        // objects
        JMenuItem sphere = new JMenuItem("Sphere");
        sphere.addActionListener((e) -> {
            addObject(new Sphere(this.hitPosition, .1f, Color.WHITE, 0f, 0f));
        });
        newObject.add(sphere);
        JMenuItem box = new JMenuItem("Box");
        box.addActionListener((e) -> {
            addObject(new Box(this.hitPosition, .1f, Color.WHITE, 0f, 0f));
        });
        newObject.add(box);
        JMenuItem object = new JMenuItem(".obj");
        object.addActionListener((e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("WAVEFRONT FILE", "obj"));
            fileChooser.setSelectedFile(new File("model.obj"));
            int result = fileChooser.showOpenDialog(viewport);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                addObject(new Model(this.hitPosition, new Obj(file.getAbsolutePath()), .2f, .1f, false));
            }
        });
        newObject.add(object);
        newObject.add(box);

        // Edit object properties
        JMenuItem properties = new JMenuItem("properties");
        properties.addActionListener((e) -> {
            PropertiesDialog.get().editProperties(hit.getSolid().getParent());
        });

        // delete object
        JMenuItem delete = new JMenuItem("delete");
        delete.addActionListener((e) -> {
            viewport.getScene().removeSolid(hit.getSolid().getParent());
        });
        if (!hit.getSolid().getParent().canDelete()) {
            delete.setEnabled(false);
        }

        // adding
        add(newObject);
        add(properties);
        add(delete);

        // show
        show(viewport, (int) screenPosition.getX(), (int) screenPosition.getY());
    }
}
