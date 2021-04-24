package me.olix3001.utils;

import me.olix3001.pixeldata.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MtlLoader {
    private String path;
    private Map<String, Material> materials = new HashMap<>();
    private String currentMtl = null;
    private boolean textureTransparency;

    private boolean loaded = false;

    public MtlLoader(String path, boolean textureTransparency) {
        this.path = path;
        this.textureTransparency = textureTransparency;
    }

    public void load() throws FileNotFoundException {
        File objFile = new File(this.path);
        Scanner reader = new Scanner(objFile);

        while (reader.hasNextLine()) {
            String l = reader.nextLine();
            String[] parts = l.split(" ");

            // Material
            if (l.startsWith("newmtl ")) {
                materials.put(parts[1], new Material());
                currentMtl = parts[1];
            }

            // ambient
            if (l.startsWith("Ka ")) {
                if (currentMtl != null) {
                    materials.get(currentMtl).setKa(partsToColor(parts));
                }
            }
            // diffuse
            if (l.startsWith("Kd ")) {
                if (currentMtl != null) {
                    materials.get(currentMtl).setKd(partsToColor(parts));
                }
            }
            // specular
            if (l.startsWith("Ks ")) {
                if (currentMtl != null) {
                    materials.get(currentMtl).setKs(partsToColor(parts));
                }
            }
            // emissive
            if (l.startsWith("Ke ")) {
                if (currentMtl != null) {
                    materials.get(currentMtl).setKe(partsToColor(parts));
                }
            }
            // transparency
            if (l.startsWith("d ")) {
                if (currentMtl != null) {
                    materials.get(currentMtl).setD(Float.parseFloat(parts[1]));
                }
            }
            if (l.startsWith("Tr ")) {
                if (currentMtl != null) {
                    materials.get(currentMtl).setTr(Float.parseFloat(parts[1]));
                }
            }

            // UV map
            if (l.startsWith("map_Kd ")) {
                if (currentMtl != null) {
                    materials.get(currentMtl).setTexture(parts[1].replace("\\\\", "\\"), path);
                }
            }
        }
        reader.close();
        loaded = true;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setPath(String path) {
        this.path = path;
        loaded = false;
    }

    public void useTextureTransparency(boolean textureTransparency) {
        this.textureTransparency = textureTransparency;
        loaded = false;
    }

    public Map<String, Material> getMaterials() {
        return materials;
    }

    private Color partsToColor(String[] parts) {
        return new Color(
                Float.parseFloat(parts[1]),
                Float.parseFloat(parts[2]),
                Float.parseFloat(parts[3])
        );
    }
}
