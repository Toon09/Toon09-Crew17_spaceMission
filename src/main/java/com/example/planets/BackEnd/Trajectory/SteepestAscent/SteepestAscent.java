package com.example.planets.BackEnd.Trajectory.SteepestAscent;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.NumericalMethods.RK4;

import java.util.ArrayList;
import java.util.Random;

public class SteepestAscent implements TrajectoryPlanner {

    private final int numbOfSteps = 100;
    private final int numbOfStages;
    private int numbOfDays;
    private Model3D model;
    private CelestialBody target;

    private double[][] trajectory;

    public SteepestAscent(Model3D model, int numbOfStages, String target, int numbOfDays) {
        this.model = model.clone();
        this.numbOfStages = numbOfStages;
        this.numbOfDays = numbOfDays;

        for (int i = 0; i < this.model.size(); i++)
            if (target.equalsIgnoreCase(this.model.getBody(i).getName()))
                target = this.model.getBody(i).getName();


    }

    private void makeTrajectory() {

        //make copy of copy lol
        Model3D optimizer = model.clone();

        double[][] state = new double[numbOfStages][5];

        // set initial values here
        state = new double[][]{{0, 60 * 30,
                11, 11, 11},
                {3 * 24 * 60 * 60, 3 * 24 * 60 * 60 + 60 * 30, // after 3 days accelerate for 30 min
                        11, 11, 11}
        };


        for (int count = 0; count < numbOfSteps; count++) {
            // clone of initial condition
            optimizer = model.clone(new RK4());

            optimizer.getShip().setPlan(state);
            optimizer.addShips(2 * 5 * numbOfStages);

            System.out.println("total i: " + optimizer.getAmountOfShips());
            Random random = new Random();
            // set states
            // in each stage go + and - each parameter
            for (int i = 0; i < optimizer.getAmountOfShips() ; i ++) {
                // state for the first ship
                System.out.println("current i: " + i);

                assert state != null; ////////// delete afterwards

// ADD A CHANGE IN EVERY DIR
                double[][] temp1 = new double[][]{
                        {state[0][0] + state[0][1] + 1, state[0][0] + state[0][1] + 31, state[0][4] + random.nextDouble(), state[0][3] + random.nextDouble(), state[0][4] + random.nextDouble()},
                        {state[1][0] + state[0][1] + 1, state[1][0] + state[0][1] + 31, state[1][2] + random.nextDouble(), state[1][3] + random.nextDouble(), state[1][4] + random.nextDouble()}
                };

                optimizer.getShip(i).setPlan(temp1);

            }

            // run sim
            optimizer.updatePos(numbOfDays, 1.6, true);

            // get best plan made
            double cost = 0.0;
            for (int i = 0; i < optimizer.getAmountOfShips(); i++) {
                if (cost < optimizer.getShip(i).getCost()) {
                    cost = optimizer.getShip(i).getCost();
                    state = optimizer.getShip(i).getPlan(); // gets plan with highest cost
                }
            }

        }

        trajectory = state;

    }


    @Override
    public double[][] getTrajectory() {
        if (trajectory == null)
            makeTrajectory();

        return trajectory;
    }

}
