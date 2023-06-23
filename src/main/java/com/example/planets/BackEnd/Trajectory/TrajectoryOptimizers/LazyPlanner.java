package com.example.planets.BackEnd.Trajectory.TrajectoryOptimizers;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.NumericalMethods.Euler;

import java.util.Arrays;

public class LazyPlanner implements TrajectoryPlanner{

    private String target;
    private String home;
    private Model3D model; // have this copy be same as the one being used and for calculations use a clone
    private boolean toTarget = true;
    private double phaseTime = 10000; // checks every 3 days
    private double slowPhaseTime = 20000; //check every 6 days
    private double targetDistance = 500000.0;
    private double lastAcc = 0;

    // both velocities that will be kept
    private double[] curretVel;
    private double[] nextVel;

    public LazyPlanner(Model3D model, String target, String home){
        this.model = model;

        this.target = target;
        this.home = home;
    }


    @Override
    public void makeTrajectory() { }

    // gets what has been done so far or just the last step ?
    @Override
    public double[][] getTrajectory() {
        return new double[][] {curretVel};
    }

    private boolean first = true;

    @Override
    public void next() { // always makes sure current and next is calcualted


    }

    /**
     * gets what should be done for the ship to reach the target planet with the model currently given,
     * so you must be
     * @param model state of the model when you want to plan the next move
     * @return
     */
    private double[] getToDo(Model3D model){
        CelestialBody targetPlanet;

        return null;
    }


    @Override
    public double[] getCurrent() { // returns current and sets next as new current
        if(curretVel == null){
            next();
        }

        // returns the current one and makes sure the next one is set as the new current one
        double[] result = curretVel;
        if(nextVel != null)
            curretVel = nextVel;

        // once the next is set as the current one, its set as null to prevent it being set again
        nextVel = null;

        return result;
    }

    @Override
    public void setTrajectory(double[][] trajectory) {
        curretVel = trajectory[0];
    }
}
