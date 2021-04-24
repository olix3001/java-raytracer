package me.olix3001.render;

import me.olix3001.math.Vector3;

public class Camera {
    private Vector3 position;
    private float yaw, pitch, fov;

    public Camera() {
        this.position = new Vector3(0, 0, -2);
        this.yaw = 0;
        this.pitch = 0;
        this.fov = 60;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getFov() {
        return fov;
    }

    public void setFov(float fov) {
        this.fov = fov;
    }
}
