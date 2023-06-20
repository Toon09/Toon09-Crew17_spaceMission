package com.example.planets.BackEnd.Trajectory.TrajectoryOptimizers;

//this interface must be implemented by all methods which optimize the trajectory of the ship

public interface TrajectoryPlanner {

    public void makeTrajectory();

    public double[][] getTrajectory();

    // contains 1D array of:
    //[ 0:start of time interval, 1:acc. in x, 2:acc. in y, 2:acc. in z ]
    public void next();

    public double[] getCurrent();

    // sets all points where its going to change
    public void setTrajectory(double[][] trajectory);



}
