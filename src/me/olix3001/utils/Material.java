package me.olix3001.utils;

import me.olix3001.pixeldata.Color;

public class Material {
    private Color Ka;
    private Color Kd;
    private Color Ks;
    private Color Ke;
    private float T;

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
        this.T = tr;
    }
    public void setD(float d) {
        this.T = 1f - d;
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
    public float getT() {
        return T;
    }

    public float getEmission() {
        return Ke.getLuminance();
    }
}
