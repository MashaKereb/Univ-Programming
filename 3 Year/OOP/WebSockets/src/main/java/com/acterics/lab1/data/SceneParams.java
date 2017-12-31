package com.acterics.lab1.data;

public class SceneParams {
    private float angle; // Gun angle

    private float force;  // Blast wave power

    private float barrelRadius;
    private float barrelHeight;

    private float positionX;
    private float positionY;

    private float surfaceY;

    public SceneParams(){}

    public SceneParams(float angle, float surfaceY, float force, float barrelRadius,
                       float barrelHeight, float positionX, float positionY) {
        this.angle = angle;
        this.surfaceY = surfaceY;
        this.force = force;
        this.barrelRadius = barrelRadius;
        this.barrelHeight = barrelHeight;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public float getBarrelHeight() {
        return barrelHeight;
    }

    public void setBarrelHeight(float barrelHeight) {
        this.barrelHeight = barrelHeight;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public float getForce() {
        return force;
    }

    public void setForce(float force) {
        this.force = force;
    }

    public float getBarrelRadius() {
        return barrelRadius;
    }

    public void setBarrelRadius(float barrelRadius) {
        this.barrelRadius = barrelRadius;
    }

    public float getSurfaceY() {
        return surfaceY;
    }

    public void setSurfaceY(float surfaceY) {
        this.surfaceY = surfaceY;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
