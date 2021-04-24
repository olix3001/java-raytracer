package me.olix3001.special;

import me.olix3001.math.Vector3;
import me.olix3001.pixeldata.Color;
import me.olix3001.solids.Plane;

public class CheckerboardPlane extends Plane {
    public CheckerboardPlane(float height, float reflectivity, float emission) {
        super(height, Color.BLACK, reflectivity, emission);
    }
    @Override
    public Color getTextureColor(Vector3 point) {
        if ((int) point.getX() % 2 == 0 ^ (int)point.getZ() % 2 != 0) {
            return Color.GRAY;
        } else {
            return Color.DARK_GRAY;
        }
    }
}
