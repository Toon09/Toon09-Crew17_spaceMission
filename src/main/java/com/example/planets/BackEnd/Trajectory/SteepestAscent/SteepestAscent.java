package com.example.planets.BackEnd.Trajectory.SteepestAscent;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.NumericalMethods.RK4;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/*
find way to save closest distance from target in ship
 */

public class SteepestAscent implements TrajectoryPlanner {

    private final int numbOfSteps = 30;
    private final int numbOfStages;
    private final int numbOfDays;
    private final Model3D model;
    private String target;
    private String home = "earth";

    private double[][] trajectory;

    public SteepestAscent(Model3D model, int numbOfStages, String target, int numbOfDays){
        this.model = model.clone();
        this.numbOfStages = numbOfStages;
        this.numbOfDays = numbOfDays;
        this.target = target;


    }

    private void makeTrajectory(){

        double[][] state = new double[numbOfStages][5];

        // set initial values here // set [i][1] to be the duration only
        state = new double[][]{ {0, 30*60,
                    0, 0, 0},
                {3*24*60*60, 3*24*60*60 + 30*60, // after 3 days accelerate for 30 min
                    0, 0, 0}
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

            optimizer.addShips( 15*numbOfStages ); // you are not using number of stages bruh

            System.out.println("Lap: " + (count+1));

            System.out.println("start plan: " + Arrays.deepToString(state));


            // set states
            // in each stage go + and - each parameter
            for(int i=0; i < optimizer.getAmountOfShips()-1; i++){
                // state for the first ship

                ///////////////////////////////////////////////////////// ADD A CHANGE IN EVERY DIR
                double[][] temp = new double[][]{ {state[0][0], state[0][1],
                            state[0][4], state[0][3], state[0][4]},
                        {state[1][0], state[1][1],
                            state[1][2], state[1][3], state[1][4]}};

                for(int j=0; j<temp.length; j++)
                    for(int k=0; k<5; k++){
                        if(k>1)
                            temp[j][k] += 12.0*Math.random()-12.0/2.0; //chnges in acceleration

                        else{temp[j][k] += 5*24*60.0*60.0*(Math.random())-5*24*60*60.0/2.0; // changes in initial thrust times
                            if( k==1 && temp[j][1] < temp[j][0])
                                temp[j][1] = temp[j][0];
                        }

                    }

                optimizer.getShip(i).setPlan( temp );

            }

            System.out.println("optimizing");
            // run sim
            optimizer.updatePos(numbOfDays, 500.0, true);

            Spaceship champion = null;

            // get best plan made
            double cost = 0.0;
            System.out.println("in loop");
            for(int i=0; i< optimizer.getAmountOfShips(); i++){
                System.out.println("closest dist: " + optimizer.getShip(i).getClosestDistance());
                System.out.println("cost: " +optimizer.getShip(i).getCost());
                System.out.println("plan: " + Arrays.deepToString(optimizer.getShip(i).getPlan()));

                if( cost < optimizer.getShip(i).getCost() ){
                    System.out.println("new hottest single");
                    cost = optimizer.getShip(i).getCost();
                    state = optimizer.getShip(i).getPlan(); // gets plan with highest cost
                    champion = optimizer.getShip(i);
                }
            }

            System.out.println("\nfinal state: " + Arrays.deepToString(champion.getPlan()));

            CelestialBody tar = null;
            for(int i=0; i<this.model.size(); i++)
                if( target.equalsIgnoreCase(this.model.getBody(i).getName()) )
                    tar = this.model.getBody(i);

            System.out.println("closest dist: " + champion.getClosestDistance());
            System.out.println("used fuel: " + champion.getUsedFuel());
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
