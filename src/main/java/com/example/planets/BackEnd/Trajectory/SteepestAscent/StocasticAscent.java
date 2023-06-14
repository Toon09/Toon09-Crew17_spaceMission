package com.example.planets.BackEnd.Trajectory.SteepestAscent;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.NumericalMethods.RK4;

import java.util.Arrays;


/*
change cost function so that it takes closest dist and actual dist as input
 */
public class StocasticAscent implements TrajectoryPlanner {

    private final int numbOfSteps = 100;
    private final int numbOfStages;
    private final int numbOfDays;
    private final Model3D model;
    private String target;

    private double[][] trajectory;

    public StocasticAscent(Model3D model, int numbOfStages, String target, int numbOfDays){
        this.model = model.clone();
        this.numbOfStages = numbOfStages;
        this.numbOfDays = numbOfDays;
        this.target = target;


    }

    private void makeTrajectory(){

        double[][] state = new double[numbOfStages][5];

        state[0][0] = 0.0;
        state[0][1] = 30*60.0;

        for(int i=0; i<numbOfStages; i++){
            state[i][0] = i*numbOfDays*24*60*60 / ((double) numbOfStages);
            state[i][1] = 60*60 +Math.random()*30*60;

        }

        int individuals = 100;

        System.out.println("numb of generations: " + numbOfSteps + ", numb of individuals: " + individuals + "\n");

        System.out.println("generation, used fuel, closest distance, time[ns]");

        double chrono = 0.0;

        for(int count=0; count<numbOfSteps; count++){

            double delta = System.nanoTime();

            // clone of initial condition
            Model3D optimizer = model.clone( new RK4() );

            // gives planning to ship
            optimizer.getShip().setPlan(state);

            // set target planet
            for(int i=0; i<model.size(); i++)
                if( optimizer.getBody(i).getName().equalsIgnoreCase(target) )
                    optimizer.getShip().setTarget( model.getBody(i) );

            optimizer.addShips( individuals ); // you are not using number of stages bruh

            // set states
            // in each stage go + and - each parameter
            for(int i=0; i < optimizer.getAmountOfShips()-1; i++){
                // state for the first ship

                ///////////////////////////////////////////////////////// ADD A CHANGE IN EVERY DIR
                double[][] temp = new double[numbOfStages][5];
                for(int j=0; j<numbOfStages; j++){
                    for(int k=0; k<5; k++){
                        temp[j][k] = state[j][k];
                    }
                }

                for(int j=0; j<temp.length; j++){
                    for(int k=2; k<5; k++)
                            temp[j][k] += 10.5*Math.random()-10.5/2.0; // turn value into function of generations

                    temp[j][0] += 15*24*60.0*60.0*(Math.random())-15*24*60*60.0/2.0; // changes in initial thrust times
                    temp[j][1] += 60.0*60.0*(Math.random())-60*60.0/2.0; // changes in thrust length
                    if( temp[j][1]<0.0 ) // avoids negative thrust
                        temp[j][1] = 0.0;
                }

                optimizer.getShip(i).setPlan( temp );

            }

            //System.out.println("optimizing");
            // run sim
            optimizer.updatePos(numbOfDays, 500.0, true);

            Spaceship champion = optimizer.getShip();

            // get best plan made
            double cost = optimizer.getShip().getCost();
            //System.out.println("in loop");
            for(int i=0; i< optimizer.getAmountOfShips(); i++){
                System.out.println("plan: " + Arrays.deepToString(optimizer.getShip(i).getPlan()));
                System.out.println("closest dist: " + optimizer.getShip(i).getClosestDistance());
                System.out.println("cost: " +optimizer.getShip(i).getCost());

                if( cost >= optimizer.getShip(i).getCost() ){
                    System.out.println("new hottest single\n");
                    cost = optimizer.getShip(i).getCost();
                    state = optimizer.getShip(i).getPlan(); // gets plan with highest cost
                    champion = optimizer.getShip(i);
                }

            }

            delta = System.nanoTime() - delta;
            chrono += delta;

            System.out.println( (count+1) + ", " + champion.getUsedFuel() + ", " + champion.getClosestDistance() + ", " + chrono);
        } ////

        trajectory = state;

    }


    @Override
    public double[][] getTrajectory() {
        if( trajectory == null )
            makeTrajectory();

        return trajectory;
    }


}
