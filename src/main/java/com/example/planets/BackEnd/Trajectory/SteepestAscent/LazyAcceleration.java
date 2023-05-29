package com.example.planets.BackEnd.Trajectory.SteepestAscent;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.NumericalMethods.RK4;


import java.util.Arrays;

/*
find way to save closest distance from target in ship
 */

public class LazyAcceleration implements TrajectoryPlanner {

    private final int numbOfSteps = 30;
    private final int numbOfStages;
    private final int numbOfDays;
    private final Model3D model;
    private String target;

    private double[][] trajectory;

    public LazyAcceleration(Model3D model, int numbOfStages, String target, int numbOfDays){
        this.model = model.clone();
        this.numbOfStages = numbOfStages;
        this.numbOfDays = numbOfDays;
        this.target = target;


    }

    private void makeTrajectory(){

        // clone of initial condition
        Model3D optimizer = model.clone( new RK4() );

        // setting up ship
        double[][] state = new double[1][5];

        state[0][0] = 0.0;
        state[0][1] = 30*60;
        for(int i=2; i<5; i++)
            state[0][i] = 0.0;

        optimizer.getShip().setPlan(state);

        // set target planet
        for(int i=0; i<model.size(); i++)
            if( optimizer.getBody(i).getName().equalsIgnoreCase(target) )
                optimizer.getShip().setTarget( model.getBody(i) );

        // run sim
        optimizer.updatePos(numbOfDays, 100.0, true);

        //get where titan will be
        double[] finalPos = optimizer.getShip().getTarget().getPos();

        //get directional vector
        double[] direction = new double[3];
        for(int i=0; i<3; i++)
            direction[i] = model.getShip().getPos()[i] - finalPos[i];

        double magnitude = 0.0;
        for(int i=0; i<3; i++)
            magnitude += direction[i]*direction[i];
        for(int i=0; i<3; i++)
            direction[i] = direction[i]/magnitude; //

        // treat it as a physics problem without planets
        // you have the direction and how long in each dir to go
        // first accelerate for 3 days or so in that dir

        state[0][1] = 60*60;

        magnitude = 1000.0;

        state[0][2] = direction[0] * magnitude;
        state[0][3] = direction[1] * magnitude;
        state[0][4] = direction[2] * magnitude;

        
        trajectory = state;

        System.out.println("Closest dist: " + optimizer.getShip().getClosestDistance() + "km");

    }


    @Override
    public double[][] getTrajectory() {
        if( trajectory == null )
            makeTrajectory();

        return trajectory;
    }


}
