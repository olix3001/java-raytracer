package me.olix3001.utils;

import me.olix3001.pixeldata.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Material {
    private Color Ka;
    private Color Kd;
    private Color Ks;
    private Color Ke;
    private BufferedImage texture = null;

    public void setKa(Color ka) {
        Ka = ka;
    }
    public void setKd(Color kd) {
        Kd = kd;
    }
    public void setKe(Color ke) {
        Ke = ke;
    }
    public void setKs(Color ks) {
        Ks = ks;
    }
    public void setTr(float tr) {
        this.Ka.setAlpha(tr);
        this.Kd.setAlpha(tr);
    }
    public void setD(float d) {
        this.Ka.setAlpha(1f - d);
        this.Kd.setAlpha(1f - d);
    }

    public Color getKa() {
        return Ka;
    }
    public Color getKd() {
        return Kd;
    }
    public Color getKe() {
        return Ke;
    }
    public Color getKs() {
        return Ks;
    }
    public float getEmission() {
        return Ke.getLuminance();
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public boolean hasTexture() {
        return !(texture == null);
    }

    public void setTexture(String path, String mtlPath) {
        try {
            File f = new File(path);
            System.out.println("trying to load texture from: " + path + " (Absolute: " + f.isAbsolute() + ")");
            if (!f.isAbsolute()) {
                f = new File(new File(mtlPath).getParent(), path);
            }
            this.texture = ImageIO.read(f);
            System.out.println("texture loaded");
        } catch (IOException e) {
            e.printStackTrace();
            this.texture = null;
        }
    }
}
