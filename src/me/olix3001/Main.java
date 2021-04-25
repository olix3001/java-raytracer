package me.olix3001;

import me.olix3001.gui.Viewport;
import me.olix3001.math.Vector3;
import me.olix3001.other.Obj;
import me.olix3001.pixeldata.Color;
import me.olix3001.solids.Box;
import me.olix3001.solids.Model;
import me.olix3001.solids.Sphere;
import me.olix3001.special.CheckerboardPlane;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Raytracing");
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        Viewport viewport = new Viewport(frame, .25f);
        frame.add(viewport);

        // light sphere
        viewport.getScene().addSolid(new Sphere(viewport.getScene().getLight().getPosition(), .1f, Color.WHITE, 0, 1f));
        // light sphere

        viewport.getScene().addSolid(new Sphere(new Vector3(-.75f, 0, 0), .5f, Color.RED, .3f, 0f));
        viewport.getScene().addSolid(new Sphere(new Vector3(.75f, 0, 0), .5f, Color.GREEN, .3f, 0f));
        viewport.getScene().addSolid(new Box(new Vector3(0, .75f, .5f), .5f, Color.BLUE, .3f, 0f));

        viewport.getScene().addSolid(new CheckerboardPlane(-2f, 0f, 0f));
        //viewport.getScene().addSolid(new TexturedPlane(-2f, "planks.jpg",15f, 0f, 0f));

        //viewport.getScene().addSolid(new Model(new Vector3(0, 0, -1), new Obj("C:\\Users\\olimi\\Desktop\\test.obj", "C:\\Users\\olimi\\Desktop\\test.mtl"), 0.3f, 0.0f, false));

        frame.setVisible(true);

        viewport.MainLoop();
    }
}
