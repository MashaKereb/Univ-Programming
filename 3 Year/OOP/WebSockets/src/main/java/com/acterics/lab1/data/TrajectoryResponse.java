package com.acterics.lab1.data;

import com.sun.javafx.geom.Vec3f;

import java.util.List;

public class TrajectoryResponse {
    // TODO: try to pass several responses
    private List<Vec3f> trajectory;

    public TrajectoryResponse(){}

    public TrajectoryResponse(List<Vec3f> trajectory) {
        this.trajectory = trajectory;
    }

    public List<Vec3f> getTrajectory() {
        return trajectory;
    }

    public void setTrajectory(List<Vec3f> trajectory) {
        this.trajectory = trajectory;
    }
}
