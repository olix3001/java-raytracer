package me.olix3001.math;

import me.olix3001.solids.Triangle;
import me.olix3001.utils.Face;

import java.util.ArrayList;
import java.util.List;

public class Triangulation {

    public static class Triangle {
        public Vector3 a;
        public Vector3 b;
        public Vector3 c;

        public Triangle(Vector3 a, Vector3 b, Vector3 c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public String toString() {
            return "Triangle { a=" + a.toString() + ", b=" + b.toString() + ", c=" + c.toString() + " }";
        }
    }

    public static List<Triangle> triangulatePolygon(List<Vector3> vertices) {
        List<Triangle> triangles = new ArrayList<>();
        boolean clockwise = isClockwise(vertices);
        int index = 0;

        while (vertices.size() > 2) {
            Vector3 p1 = vertices.get((index) % vertices.size());
            Vector3 p2 = vertices.get((index + 1) % vertices.size());
            Vector3 p3 = vertices.get((index + 2) % vertices.size());

            Vector3 v1 = new Vector3(p2.getX() - p1.getX(), p2.getY() - p1.getY(), p2.getZ() - p1.getZ());
            Vector3 v2 = new Vector3(p3.getX() - p1.getX(), p3.getY() - p1.getY(), p3.getZ() - p1.getZ());
            float cross = Vector3.cross(v1, v2).length();

            if ((!clockwise && cross <= 0 && validTriangle(p1, p2, p3, vertices)) |
                    (clockwise && cross >= 0 && validTriangle(p1, p2, p3, vertices))) {
                vertices.remove(p2);
                Triangle triangle = new Triangle(p1, p2, p3);
                triangles.add(triangle);
            } else {
                index++;
            }
        }

        return triangles;
    }

    public static List<Triangle> triangulateFace(Face face) {
        return triangulatePolygon(face.getVertices());
    }

    public static boolean isClockwise(List<Vector3> vertices) {
        int sum = 0;
        for (int i=0; i<vertices.size(); i++) {
            Vector3 p1 = vertices.get(i);
            Vector3 p2 = vertices.get((i + 1) % vertices.size());
            sum += (p2.getX() - p1.getX()) * (p2.getY() - p1.getY()) * (p2.getZ() - p1.getZ());
        }
        return sum >= 0;
    }

    public static boolean validTriangle(Vector3 p1, Vector3 p2, Vector3 p3, List<Vector3> vertices) {
        for (Vector3 p : vertices) {
            if (p != p1 && p != p2 && p != p3 && inTriangle(p, p1, p2, p3)) {
                return false;
            }
        }
        return true;
    }

    public static boolean inTriangle(Vector3 p, Vector3 a, Vector3 b, Vector3 c) {
//        float angles = 0;
//
//        Vector3 v1 = p.subtract(a); v1.normalize();
//        Vector3 v2 = p.subtract(b); v2.normalize();
//        Vector3 v3 = p.subtract(c); v3.normalize();
//
//        angles += Math.acos(Vector3.dot(v1, v2));
//        angles += Math.acos(Vector3.dot(v2, v3));
//        angles += Math.acos(Vector3.dot(v3, v1));
//
//        return (Math.abs(angles - 2*Math.PI) <= 90f);

        Vector3 a1 = a.subtract(p);
        Vector3 b1 = b.subtract(p);
        Vector3 c1 = c.subtract(p);

        Vector3 u = Vector3.cross(b1, c1);
        Vector3 v = Vector3.cross(c1, a1);
        Vector3 w = Vector3.cross(a1, b1);

        if (Vector3.dot(u, v) < 0f) {
            return false;
        }
        if (Vector3.dot(u, w) < 0f) {
            return false;
        }
        return true;
    }
    public static boolean inTriangle(me.olix3001.solids.Triangle triangle, Vector3 p) {
        return inTriangle(p, triangle.getPosition(), triangle.getPosition2(), triangle.getPosition3());
    }
}
