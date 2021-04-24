package me.olix3001.solids;

import me.olix3001.math.Triangulation;
import me.olix3001.math.Vector2;
import me.olix3001.math.Vector3;
import me.olix3001.pixeldata.Color;
import me.olix3001.utils.ImageUtils;
import me.olix3001.utils.Material;

public class TexturedTriangle extends Triangle {
    private Vector2 uv1;
    private Vector2 uv2;
    private Vector2 uv3;
    private Material mtl;
    private boolean textureTransparency;

    public TexturedTriangle(Vector3 position, Vector3 position2, Vector3 position3, Vector2 uv1, Vector2 uv2, Vector2 uv3, Color color, float reflectivity, float emission, Material mtl, boolean textureTransparency) {
        super(position, position2, position3, color, reflectivity, emission);
        this.uv1 = uv1;
        this.uv2 = uv2;
        this.uv3 = uv3;
        this.mtl = mtl;
        this.textureTransparency = textureTransparency;
    }

    @Override
    public Color getTextureColor(Vector3 point) {
        Vector2 uv = Triangulation.toUV(this, point);
        Color c = ImageUtils.TextureWithUV(this.mtl.getTexture(), uv);
        if (!textureTransparency)
            c.removeAlpha();
        return c;
    }

    public Vector2 getUv1() {
        return uv1;
    }

    public Vector2 getUv2() {
        return uv2;
    }

    public Vector2 getUv3() {
        return uv3;
    }
}
