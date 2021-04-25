package me.olix3001.render;

import me.olix3001.math.RayHit;
import me.olix3001.math.Vector3;
import me.olix3001.pixeldata.Color;
import me.olix3001.pixeldata.PixelData;
import me.olix3001.solids.Solid;

import java.awt.*;

public class Renderer {
    private static final int MAX_REFLECTION_BOUNCES = 5;
    private static final float GLOBAL_ILLUMINATION = .2f;

    public static void renderScene(Scene scene, Graphics gfx, int w, int h, float resolution) {
        int blockSize = Math.max(1, (int) (1/resolution));

        for(int x=0; x<w; x+=blockSize) {
            for (int y=0; y<h; y+=blockSize) {
                float[] uv = getNormalizedScreenCoordinates(x, y, w, h);
                PixelData pixelData = computePixelInfo(scene, uv[0], uv[1]);

                gfx.setColor(pixelData.getColor().toAWTColor());
                gfx.fillRect(x, y, blockSize, blockSize);
            }
        }
    }

    public static float[] getNormalizedScreenCoordinates(int x, int y, int w, int h) {
        float u = 0, v = 0;
        if (w > h) {
            u = (float)(x - w/2+h/2) / h * 2 - 1;
            v =  -((float) y / h * 2 - 1);
        } else {
            u = (float)x / w * 2 - 1;
            v =  -((float) (y - h/2+w/2) / w * 2 - 1);
        }

        return new float[]{u, v};
    }

    public static PixelData computePixelInfo(Scene scene, float u, float v) {
        Vector3 eyePos = new Vector3(0, 0, (float) (-1/Math.tan(Math.toRadians(scene.getCamera().getFov()/2))));
        Camera cam = scene.getCamera();

        Vector3 rayDir = new Vector3(u, v, 0).subtract(eyePos).normalize().rotateYP(cam.getYaw(), cam.getPitch());
        RayHit hit = scene.raycast(new Ray(eyePos.add(cam.getPosition()), rayDir));
        if (hit != null) {
            return computePixelInfoFromHit(scene, hit, MAX_REFLECTION_BOUNCES);
        } else {
            return new PixelData(Color.SKY, Float.POSITIVE_INFINITY, 0);
        }

    }

    public static PixelData computePixelInfoFromHit(Scene scene, RayHit hit, int recursionLimit) {
        Vector3 hitPos = hit.getHitPos();
        Solid hitSolid = hit.getSolid();
        Color hitColor = hit.getSolid().getTextureColor(hitPos);
        float emission = hitSolid.getEmission();
        float reflectivity = hitSolid.getParent().getReflectivity();
        float solidTransparency = hitColor.getAlpha();

        // Diffuse shading
        float brightness = getDiffuseBrightness(scene, hit);
        // Specular shading
        float specularBrightness = getSpecularBrightness(scene, hit);

        // REFLECTION
        PixelData reflection = new PixelData(Color.SKY, 0f, 0f);
        if (hitSolid.getReflectivity() != 0) {
            Vector3 rayDir = hit.getRay().getDirection();
            Vector3 reflectionDir = rayDir.subtract(hit.getNormal().multiply(2 * Vector3.dot(rayDir, hit.getNormal())));
            RayHit reflectionHit = recursionLimit > 0 ? scene.raycast(new Ray(hitPos.add(reflectionDir.multiply(0.001f)), reflectionDir)) : null;
            if (reflectionHit != null && reflectionHit.getSolid() != hitSolid) {
                reflection = computePixelInfoFromHit(scene, reflectionHit, recursionLimit - 1);
            }

            hitColor = Color.lerp(hitColor, reflection.getColor(), reflectivity);
        }

        // TRANSPARENCY
        if (solidTransparency != 0) {
            PixelData transparency = null;
            Vector3 tRayDir = hit.getRay().getDirection();
            Vector3 tRayStart = hit.getIntersection().getEnd().add(tRayDir.multiply(0.001f));
            RayHit transparencyHit = recursionLimit > 0 ? scene.raycast(new Ray(tRayStart, tRayDir)) : null;
            if (transparencyHit != null && transparencyHit.getSolid() != hitSolid) {
                transparency = computePixelInfoFromHit(scene, transparencyHit, recursionLimit-1);
            } else {
                transparency = new PixelData(Color.SKY, 1f, 0f);
            }

            hitColor = Color.lerp(hitColor, transparency.getColor(), solidTransparency);
        }

        Color pixelColor = hitColor.multiply(brightness / (1f-solidTransparency)) // diffuse shading
                .add(specularBrightness * (1f-solidTransparency)) // specular shading
                .add(hitColor.multiply(emission)); // emission

        return new PixelData(pixelColor, Vector3.distance(scene.getCamera().getPosition(), hitPos), Math.min(1, emission + reflection.getEmission() * reflectivity + specularBrightness));
    }

    public static float getDiffuseBrightness(Scene scene, RayHit hit) {
        Light sceneLight = scene.getLight();

        // Raytrace light to create a shadow
        RayHit lightBlocker = scene.raycast(new Ray(sceneLight.getPosition(), hit.getHitPos().subtract(sceneLight.getPosition()).normalize()));
        if (lightBlocker != null && lightBlocker.getSolid() != hit.getSolid()) {
            return GLOBAL_ILLUMINATION;
        } else {
            return Math.max(GLOBAL_ILLUMINATION, Math.min(1, Vector3.dot(hit.getNormal(), sceneLight.getPosition().subtract(hit.getHitPos()))));
        }
    }

    public static float getSpecularBrightness(Scene scene, RayHit hit) {
        Vector3 hitPos = hit.getHitPos();
        Vector3 cameraDir = scene.getCamera().getPosition().subtract(hitPos).normalize();
        Vector3 lightDirection = hitPos.subtract(scene.getLight().getPosition()).normalize();
        Vector3 reflectionVector = lightDirection.subtract(hit.getNormal().multiply(2*Vector3.dot(lightDirection, hit.getNormal())));

        float specularF = Math.max(0, Math.min(1, Vector3.dot(reflectionVector, cameraDir)));
        return (float) Math.pow(specularF, 2) * hit.getSolid().getReflectivity();
    }
}
