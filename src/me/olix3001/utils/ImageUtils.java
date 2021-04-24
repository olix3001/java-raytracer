package me.olix3001.utils;

import me.olix3001.math.Vector2;
import me.olix3001.pixeldata.Color;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageUtils {
    public static Color TextureWithUV(BufferedImage image, Vector2 uv) {
        int x = Math.abs((int) (image.getWidth() * uv.getX()));
        int y = Math.abs((int) (image.getHeight() * uv.getY()));

        return Color.fromInt(image.getRGB(x % image.getWidth(), y % image.getHeight()));
    }

    public static BufferedImage scale(BufferedImage image, float scale) {
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp transformOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage scaled = new BufferedImage((int) (image.getWidth() * scale), (int) (image.getHeight() * scale), BufferedImage.TYPE_INT_RGB);
        transformOp.filter(image, scaled);
        return scaled;
    }
}
