package me.olix3001.render;

import me.olix3001.math.Vector3;
import me.olix3001.solids.Solid;

public class Intersection {

    private Vector3 start;
    private Vector3 end;
    private Solid solid;

    public Intersection(Vector3 start, Vector3 end, Solid solid) {
        this.start = start;
        this.end = end;
        this.solid = solid;
    }

    public Intersection(Vector3 v, Solid solid) {
        this.start = v;
        this.end = v;
        this.solid = solid;
    }

    public Vector3 getStart() {
        return start;
    }

    public Vector3 getEnd() {
        return end;
    }

    public Solid getSolid() {
        return solid;
    }
}
