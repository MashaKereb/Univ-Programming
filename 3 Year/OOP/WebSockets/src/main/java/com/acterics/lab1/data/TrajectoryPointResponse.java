package com.acterics.lab1.data;

import com.sun.javafx.geom.Vec3f;

/**
 * Created by Masha Kereb on 25-May-17.
 */
public class TrajectoryPointResponse {
    // TODO: try to pass several responses
    private Vec3f trajectoryPoint;

    public TrajectoryPointResponse(){}

    public TrajectoryPointResponse(Vec3f trajectoryPoint) {
        this.trajectoryPoint = trajectoryPoint;
    }

    public Vec3f getTrajectoryPoint() {
        return trajectoryPoint;
    }

    public void setTrajectoryPoint(Vec3f trajectoryPoint) {
        this.trajectoryPoint = trajectoryPoint;
    }
}

