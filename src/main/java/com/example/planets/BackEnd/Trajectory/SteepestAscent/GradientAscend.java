package com.example.planets.BackEnd.Trajectory.SteepestAscent;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.NumericalMethods.RK4;


import java.util.Arrays;

/*
find way to save closest distance from target in ship
 */

public class GradientAscend implements TrajectoryPlanner {

    private final int numbOfSteps = 30;
    private final int numbOfStages;
    private final int numbOfDays;
    private final Model3D model;
    private String target;

    private double[][] trajectory;

    public GradientAscend(Model3D model, int numbOfStages, String target, int numbOfDays){
        this.model = model.clone();
        this.numbOfStages = numbOfStages;
        this.numbOfDays = numbOfDays;
        this.target = target;


    }
    /*
    another method that calculates where titan will be and accelerates in that way only for some time
     */

    private void makeTrajectory(){

        double[][] state = new double[numbOfStages][5];

        state[0][0] = 0.0;
        state[0][1] = 30*60.0;

        for(int i=1; i<numbOfStages; i++){
            state[i][0] = numbOfDays*24*60*60 / ((double) numbOfStages);
            state[i][1] = state[i][0] + 60*60 +Math.random()*30*60;

        }


        for(int count=0; count<numbOfSteps; count++){
            // clone of initial condition
            Model3D optimizer = model.clone( new RK4() );

            // gives planning to ship
            optimizer.getShip().setPlan(state);

            // set target planet
            for(int i=0; i<model.size(); i++)
                if( optimizer.getBody(i).getName().equalsIgnoreCase(target) )
                    optimizer.getShip().setTarget( model.getBody(i) );

            optimizer.addShips( 20*numbOfStages ); // you are not using number of stages bruh

            System.out.println("Lap: " + (count+1));

            System.out.println("start plan: " + Arrays.deepToString(state));


            // set states
            // in each stage go + and - each parameter
            for(int i=0; i < optimizer.getAmountOfShips()-1; i++){
                // state for the first ship

                double[][] temp = new double[numbOfStages][5];
                for(int j=0; j<numbOfStages; j++){
                    for(int k=0; k<5; k++){
                        temp[j][k] = state[j][k];
                    }
                }

                ///////////// ADD GRADIENT HERE

                optimizer.getShip(i).setPlan( temp );

            }

            System.out.println("optimizing");
            // run sim
            optimizer.updatePos(numbOfDays, 500.0, true);

            Spaceship champion = null;

            // get best plan made
            double cost = optimizer.getShip().getCost();
            System.out.println("in loop");
            for(int i=0; i< optimizer.getAmountOfShips(); i++){
                System.out.println("closest dist: " + optimizer.getShip(i).getClosestDistance());
                System.out.println("cost: " +optimizer.getShip(i).getCost());
                System.out.println("plan: " + Arrays.deepToString(optimizer.getShip(i).getPlan()));

                if( cost <= optimizer.getShip(i).getCost() ){
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
