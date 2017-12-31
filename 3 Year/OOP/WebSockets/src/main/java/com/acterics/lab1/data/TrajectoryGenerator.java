package com.acterics.lab1.data;

import com.sun.javafx.geom.Vec3f;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Masha Kereb on 24-May-17.
 */
public class TrajectoryGenerator {
    private static final float E = 0.1f;

    private float surfaceY;
    private float radius;

    private Vec3f startPos;
    private Vec3f prevPos;
    private Vec3f direction;
    private float t = 0.1f;
    private float step = 0.5f;
    private static final Vec3f gravity = new Vec3f(0f, 0.09f, 0f);

    boolean hasNext = false;

    public TrajectoryGenerator(SceneParams params) {
        float force = params.getForce();
        float angle = toRad(45) - params.getAngle();
        this.surfaceY = params.getSurfaceY();
        this.radius = params.getBarrelRadius();
        this.direction = new Vec3f((float) (force * Math.cos(angle)), (float) (force * Math.sin(angle)), 0f);

        float positionX = (float) (params.getPositionX() + params.getBarrelHeight() * Math.cos(angle));
        float positionY = (float) (params.getPositionY() + params.getBarrelHeight() * Math.sin(angle));
        this.startPos = new Vec3f(positionX, positionY, 0);
        this.prevPos = startPos;
        this.hasNext = true;
    }

    public boolean hasNext(){
        return this.hasNext;
    }
    public List<Vec3f> getFullTrajectory() {
        List<Vec3f> trajectory = new LinkedList<Vec3f>();
        trajectory.add(startPos);
        while(this.hasNext) {
            trajectory.add(this.getNextTrajectoryPoint());
        }
        return trajectory;
    }

    public Vec3f getNextTrajectoryPoint(){
        if(!this.hasNext) return null;
        Vec3f newPos = new Vec3f(startPos);
        newPos.x += t*direction.x;
        newPos.y += t*direction.y - t*t*gravity.y;
        prevPos = newPos;
        if(prevPos.y  - radius  < surfaceY)
            this.hasNext = false;
        System.out.println(newPos);
        t += step;
        return newPos;
    }
    public void stop(){
        hasNext = false;
    }

    private static float toRad(float angle) {
        return (float) (angle * Math.PI / 180);
    }

    private static float toAngle(float rad) {
        return (float) (rad * 180 / Math.PI);
    }
}
