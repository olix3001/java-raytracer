package me.olix3001.other;

import me.olix3001.gui.Viewport;
import me.olix3001.render.Light;
import me.olix3001.render.Scene;
import me.olix3001.solids.Solid;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.List;

public class IOMenager {

    public static void save(File file, Scene scene) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(scene.getSolids());
            out.writeObject(scene.getLight());
            out.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("cannot save");
        }
    }

    public static void load(File file, Scene scene) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(inputStream);
            scene.setSolids((List<Solid>) in.readObject());
            scene.setLight((Light) in.readObject());
            in.close();
            inputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("cannot save");
        }
    }

    public static void askAndSave(Viewport viewport) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("RAYTRACING SCENE", "rtscene"));
        fileChooser.setSelectedFile(new File("raytracing.rtscene"));
        int result = fileChooser.showSaveDialog(viewport);
        if (result == JFileChooser.APPROVE_OPTION) {
            save(fileChooser.getSelectedFile(), viewport.getScene());
        }
    }

    public static void askAndLoad(Viewport viewport) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("RAYTRACING SCENE", "rtscene"));
        fileChooser.setSelectedFile(new File("raytracing.rtscene"));
        int result = fileChooser.showOpenDialog(viewport);
        if (result == JFileChooser.APPROVE_OPTION) {
           load(fileChooser.getSelectedFile(), viewport.getScene());
        }
    }
}
