package me.olix3001.utils;

import me.olix3001.math.Vector2;
import me.olix3001.pixeldata.Color;

import java.awt.image.BufferedImage;

public class ImageUtils {
    public static Color TextureWithUV(BufferedImage image, Vector2 uv) {
        int x = Math.abs((int) (image.getWidth() * uv.getX()));
        int y = Math.abs((int) (image.getHeight() * uv.getY()));

        return Color.fromInt(image.getRGB(x % image.getWidth(), y % image.getHeight()));
    }
}
