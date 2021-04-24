package me.olix3001.utils;

import me.olix3001.math.Vector3;
import me.olix3001.pixeldata.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ObjLoader {
    private String path;
    private Map<String, Material> materials = null;
    private String currentMtl = null;

    public static Color[] colors = new Color[]{Color.BLUE, Color.GREEN, Color.RED, Color.fromRGB(255, 214, 10), Color.fromRGB(116, 0, 184)};

    private boolean loaded = false;
    private List<Vector3> vertices;
    private List<Face> faces;

    public ObjLoader(String path) {
        this.path = path;
    }
    public ObjLoader(String path, MtlLoader mtlLoader) throws RuntimeException {
        this.path = path;
        if (!mtlLoader.isLoaded()) {
            try {
                mtlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        if (mtlLoader.isLoaded()) {
            materials = mtlLoader.getMaterials();
        } else {
            throw new RuntimeException("Cannot load material");
        }
    }

    public void load() throws FileNotFoundException {
        File objFile = new File(this.path);
        Scanner reader = new Scanner(objFile);

        vertices = new ArrayList<>();
        faces = new ArrayList<>();
        int j = 0;

        while (reader.hasNextLine()) {
            String l = reader.nextLine();
            String[] parts = l.split(" ");

            // Material
            if (l.startsWith("usemtl ")) {
                if (materials != null) {
                    currentMtl = parts[1];
                }
            }

            // Vertex
            if (l.startsWith("v ")) {
                vertices.add(new Vector3(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3])));
            }

            // Face
            if (l.startsWith("f ")) {
                List<Vector3> f = new ArrayList<>();
                for (int i=1; i<parts.length; i++) {
                    String[] face = parts[i].split("/");
                    f.add(vertices.get(Integer.parseInt(face[0]) - 1));
                }
                if (currentMtl == null) {
                    faces.add(new Face(f, colors[j % colors.length], 0f, 0f, 0f));
                } else {
                    faces.add(new Face(f, materials.get(currentMtl)));
                }
            }

            j++;
        }
        reader.close();
        loaded = true;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public List<Vector3> getVertices() {
        return vertices;
    }

    public List<Face> getFaces() {
        return faces;
    }
}
