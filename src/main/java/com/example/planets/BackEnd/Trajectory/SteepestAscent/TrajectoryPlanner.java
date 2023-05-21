package com.example.planets.BackEnd.Trajectory.SteepestAscent;

//this interface must be implemented by all methods which optimize the trajectory of the ship

import java.util.ArrayList;

public interface TrajectoryPlanner {

    /**
     *
     * @return
     */
    public ArrayList<double[]> getTrajectory();

}
