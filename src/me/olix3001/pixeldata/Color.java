package me.olix3001.pixeldata;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class Color implements Serializable {
    private float red;
    private float green;
    private float blue;
    private float alpha = 0f;

    public Color(float red, float green, float blue) {
        if (red > 1F || green > 1F || blue > 1F)
            throw new IllegalArgumentException("Color parameter(s) outside of expected range");

        if (Float.isNaN(red) || Float.isNaN(green) || Float.isNaN(blue))
            throw new IllegalArgumentException("One or more color parameters are NaN");

        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color(float red, float green, float blue, float alpha) {
        if (red > 1F || green > 1F || blue > 1F || alpha > 1F || alpha < 0F)
            throw new IllegalArgumentException("Color parameter(s) outside of expected range");

        if (Float.isNaN(red) || Float.isNaN(green) || Float.isNaN(blue) || Float.isNaN(alpha))
            throw new IllegalArgumentException("One or more color parameters are NaN");

        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public float getRed() {
        return red;
    }

    public void setRed(float red) {
        this.red = red;
    }

    public float getGreen() {
        return green;
    }

    public void setGreen(float green) {
        this.green = green;
    }

    public float getBlue() {
        return blue;
    }

    public void setBlue(float blue) {
        this.blue = blue;
    }

    public Color multiply(Color other) {
        return new Color(red*other.red, green*other.green, blue*other.blue);
    }

    public Color multiply(float brightness) {
        brightness = Math.min(1, brightness);
        return new Color(red * brightness, green * brightness, blue * brightness);
    }

    public Color divide(float number) {
        return new Color(red / number, green / number, blue / number);
    }

    public Color add(Color other) {
        return new Color(Math.min(1, this.red+other.red), Math.min(1, this.green+other.green), Math.min(1, this.blue+other.blue));
    }

    public void addSelf(Color other) {
        this.red = Math.min(1, this.red+other.red);
        this.green = Math.min(1, this.green+other.green);
        this.blue = Math.min(1, this.blue+other.blue);
    }

    public Color add(float brightness) {
        return new Color(Math.min(1, red+brightness), Math.min(1, green+brightness), Math.min(1, blue+brightness));
    }

    public int getRGB() {
        int redPart = (int)(red*255);
        int greenPart = (int)(green*255);
        int bluePart = (int)(blue*255);

        // Shift bits to right place
        redPart = (redPart << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        greenPart = (greenPart << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        bluePart = bluePart & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | redPart | greenPart | bluePart; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

    public static Color fromRGB(int r, int g, int b) {
        return new Color(((float) r) / 255f, ((float) g) / 255f, ((float) b) / 255f);
    }

    public static Color fromValue(float v) {
        return new Color(v, v, v);
    }

    public float getLuminance() {
        return red*0.2126F + green*0.7152F + blue*0.0722F;
    }

    public static Color fromInt(int argb) {
        int b = (argb)&0xFF;
        int g = (argb>>8)&0xFF;
        int r = (argb>>16)&0xFF;
        int a = (argb>>24)&0xFF;

        return new Color(r/255F, g/255F, b/255F, 1f-(a/255F));
    }

    public java.awt.Color toAWTColor() {
        return new java.awt.Color(red, green, blue);
    }
    public static Color fromAWTColor(java.awt.Color c) {
        return new Color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 1f - (c.getAlpha() / 255f));
    }

    public void fromAWT(java.awt.Color c) {
        this.setRed(c.getRed() / 255f);
        this.setGreen(c.getGreen() / 255f);
        this.setBlue(c.getBlue() / 255f);
        this.setAlpha(1f - (c.getAlpha() / 255f));
    }

    private static float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

    public static Color lerp(Color a, Color b, float t) {
        return new Color(lerp(a.getRed(), b.getRed(), t), lerp(a.getGreen(), b.getGreen(), t), lerp(a.getBlue(), b.getBlue(), t));
    }
    @Override
    public String toString() {
        return "Color { r=" + (this.red * 255) + ", g=" + (this.green * 255) + ", b=" + (this.blue * 255) + " }";
    }

    public Color clone() {
        return new Color(this.red, this.green, this.blue, this.alpha);
    }

    public static final Color BLACK = new Color(0F,0F,0F);
    public static final Color WHITE = new Color(1F, 1F, 1F);
    public static final Color RED = new Color(1F, 0F, 0F);
    public static final Color GREEN = new Color(0F, 1F, 0F);
    public static final Color BLUE = new Color(0F, 0F, 1F);
    public static final Color SKY = Color.fromRGB(72, 191, 227);
    public static final Color GRAY = new Color(0.5F, 0.5F, 0.5F);
    public static final Color DARK_GRAY = new Color(0.2F, 0.2F, 0.2F);

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void removeAlpha() {
        this.setAlpha(0f);
    }
}
