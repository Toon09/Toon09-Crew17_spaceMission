package com.example.planets.BackEnd.Trajectory.SteepestAscent;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.Models.Model3D;

import java.util.ArrayList;

public class SteepestAscent implements TrajectoryPlanner {

    private final int numbOfSteps = 100;
    private final int numbOfStages;
    private Model3D model;
    private CelestialBody target;

    private ArrayList<double[]> trajectory;

    public SteepestAscent(Model3D model, int numbOfStages, String target){
        this.model = model.clone();
        this.numbOfStages = numbOfStages;

        for(int i=0; i<this.model.size(); i++)
            if( target.equalsIgnoreCase(this.model.getBody(i).getName()) )
                target = this.model.getBody(i).getName();


    }

    private void makeTrajectory(){

        System.out.println("at least running boo");
        // in the model remember the ship in the last index is the main one you look at
        // the rest are the swarm ones

        // take swarm step
        // calculate gradient with different starting of the rockets (using getShip(i).getCost())
        // move innit vals one by one by the value given times step
        ///////////// jacobian for step>>????
        // set max amount of steps or other criteria

        // if its acc is going over too much

        //make copy of copy lol
        Model3D optimizer;

        int gradientOrder = 3; // must be odd number since og ship is used

        for(int count=0; count<numbOfSteps; count++){
            // clone of initial condition
            optimizer = model.clone();

            // 5 parmeters per stage and in each you calculate a derivative around the last ship
            optimizer.addShips( (gradientOrder-1)*5*numbOfStages );

            // you calc the deriv


        }

    }


    @Override
    public ArrayList<double[]> getTrajectory() {
        if( trajectory == null )
            makeTrajectory();

        return trajectory;
    }

}
