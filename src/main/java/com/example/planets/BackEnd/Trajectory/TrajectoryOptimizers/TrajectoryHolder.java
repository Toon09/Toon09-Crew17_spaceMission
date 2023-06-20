package com.example.planets.BackEnd.Trajectory.TrajectoryOptimizers;

public class TrajectoryHolder implements TrajectoryPlanner{

    private double[][] trajectory;
    private int stage = 0;
    @Override
    public void makeTrajectory() {}

    @Override
    public double[][] getTrajectory() {
        return trajectory;
    }

    @Override
    public void next() {
        if(stage!= trajectory.length)
            stage++;
    }

    @Override
    public double[] getCurrent() {
        if(stage>= trajectory.length)
            return new double[] {0,0,0,0};
        return trajectory[stage];
    }

    @Override
    public void setTrajectory(double[][] trajectory) {
        this.trajectory = trajectory;
    }
}
