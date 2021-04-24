package me.olix3001.special;

import me.olix3001.math.Vector3;
import me.olix3001.pixeldata.Color;
import me.olix3001.solids.Plane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TexturedPlane extends Plane {
    private BufferedImage image;
    private boolean loaded = false;
    private float size;

    public TexturedPlane(float height, String resourcePath, float size, float reflectivity, float emission, float transparency) {
        super(height, Color.BLACK, reflectivity, emission, transparency);
        this.size = size;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/resources/" + resourcePath));
            loaded = true;
        } catch (IOException e) {
            loaded = false;
        }
    }

    @Override
    public Color getTextureColor(Vector3 point) {
        if (!loaded) return getColor();
        return Color.fromInt(image.getRGB(Math.abs(((int) (point.getX()*size)) % image.getWidth()), Math.abs(((int) (point.getZ()*size)) % image.getHeight())));
    }
}
