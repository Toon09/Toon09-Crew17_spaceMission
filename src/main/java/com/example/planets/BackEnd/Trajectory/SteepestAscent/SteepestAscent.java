package com.example.planets.BackEnd.Trajectory.SteepestAscent;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.NumericalMethods.RK4;

import java.util.ArrayList;

public class SteepestAscent implements TrajectoryPlanner {

    private final int numbOfSteps = 100;
    private final int numbOfStages;
    private final int numbOfDays;
    private final Model3D model;
    private String target;

    private double[][] trajectory;

    public SteepestAscent(Model3D model, int numbOfStages, String target, int numbOfDays){
        this.model = model.clone();
        this.numbOfStages = numbOfStages;
        this.numbOfDays = numbOfDays;
        this.target = target;


    }

    private void makeTrajectory(){

        double[][] state = new double[numbOfStages][5];

        // set initial values here
        state = new double[][]{ {0, 60*30,
                    -1, 1, 4},
                {3*24*60*60, 3*24*60*60 + 60*30, // after 3 days accelerate for 30 min
                    5, 5, 5}
        };


        for(int count=0; count<numbOfSteps; count++){
            // clone of initial condition
            Model3D optimizer = model.clone( new RK4() );

            // gives planning to ship
            optimizer.getShip().setPlan(state);
            // set target planet
            for(int i=0; i<model.size(); i++)
                if( optimizer.getBody(i).getName().equalsIgnoreCase(target) )
                    optimizer.getShip().setTarget( model.getBody(i) );

            optimizer.addShips( 2*5*numbOfStages );

            System.out.println("Lap: " + (count+1));


            // set states
            // in each stage go + and - each parameter
            for(int i=0; i < optimizer.getAmountOfShips(); i++){
                // state for the first ship

                ///////////////////////////////////////////////////////// ADD A CHANGE IN EVERY DIR
                double[][] temp = new double[][]{ {state[0][0], state[0][1],
                            state[0][4], state[0][3], state[0][4]},
                        {state[1][0], state[1][1],
                            state[1][2], state[1][3], state[1][4]}};

                for(int j=0; j<temp.length; j++)
                    for(int k=0; k<5; k++){
                        if(k>1)
                            temp[j][k] += 1.5*Math.random()-1.5/2.0;
                        else
                            temp[j][k] += 3000.0*Math.random()-3000.0/2.0;
                    }


                optimizer.getShip(i).setPlan( temp );

            }

            // run sim
            optimizer.updatePos(numbOfDays, 100.0, true);

            Spaceship champion = null;

            // get best plan made
            double cost = 0.0;
            for(int i=0; i< optimizer.getAmountOfShips(); i++){
                if( cost < optimizer.getShip(i).getCost() ){
                    cost = optimizer.getShip(i).getCost();
                    state = optimizer.getShip(i).getPlan(); // gets plan with highest cost
                    champion = optimizer.getShip(i);
                }
            }

            CelestialBody tar = null;
            for(int i=0; i<this.model.size(); i++)
                if( target.equalsIgnoreCase(this.model.getBody(i).getName()) )
                    tar = this.model.getBody(i);

            System.out.println("distance: " + champion.getDistance(tar));
            System.out.println("cost: " + cost + "\n");
        }

        trajectory = state;

        System.out.println("done");

    }


    @Override
    public double[][] getTrajectory() {
        if( trajectory == null )
            makeTrajectory();

        return trajectory;
    }

}
