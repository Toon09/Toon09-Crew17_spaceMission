package com.example.planets.BackEnd.Trajectory.TrajectoryOptimizers;

//this interface must be implemented by all methods which optimize the trajectory of the ship

public interface TrajectoryPlanner {

    /**
     *
     * @return
     */
    public double[][] getTrajectory();

}
