package me.olix3001.render;

import me.olix3001.math.Vector3;

import java.io.Serializable;

public class Light implements Serializable {
    private Vector3 position;

    public Light(Vector3 position) {
        this.position = position;
    }


    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }
}
