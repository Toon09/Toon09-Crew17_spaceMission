package com.example.planets.BackEnd.Trajectory.TrajectoryOptimizers;

import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.NumericalMethods.RK4;


import java.util.Arrays;

/*
find way to save closest distance from target in ship
 */

public class GradientAscend implements TrajectoryPlanner {
    private int stage = 0;

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


    private void optimizeTrajectory(){

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

            // 4 bc we are using the 4 point formual
            // 5 bc we have 5 parameters per thrust point
            optimizer.addShips( 4*5*numbOfStages ); // you are not using number of stages bruh

            System.out.println("Lap: " + (count+1));

            System.out.println("start plan: " + Arrays.deepToString(state));


            double step = 1.0;

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

            // here do the gradient step

        }

        trajectory = state;

        System.out.println("done");

    }


    @Override
    public void makeTrajectory() {
        if( trajectory == null )
            optimizeTrajectory();
    }

    @Override
    public double[][] getTrajectory() {
        return trajectory;
    }


    @Override
    public void next() {
        stage++;
    }

    @Override
    public double[] getCurrent() {
        if(stage>= trajectory.length)
            return new double[] {0,0,0,0};
        return trajectory[stage];
    }

    @Override
    public void setTrajectory(double[][] trajectory) {
        this.trajectory = trajectory;
    }


}
