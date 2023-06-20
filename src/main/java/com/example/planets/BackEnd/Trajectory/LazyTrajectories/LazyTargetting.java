package com.example.planets.BackEnd.Trajectory.LazyTrajectories;

import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.Trajectory.LazyTrajectories.LazyTrajectory;

public class LazyTargetting implements LazyTrajectory {

    private Model3D model; // save model being used for doing calculations
    private boolean finished = false;

    private double minDist = 2000.0; // minimun distance to end this section

    public LazyTargetting(Model3D model){
        this.model = model;
    }

    private double predictionTime = 3.0; // its the time its going to predict the position of titan by in advance
    // its in days


    @Override
    public double[] getCurrent() {
        // the goal distance has been reached
        if(model.getShip().getDistance(model.getShip().getTarget()) < minDist)
            finished = true;

        double[] result = new double[0];

        /*
        get position of titan in 3 days
         */


        return result;
    }

    @Override
    public boolean finished() {
        return finished;
    }
}
