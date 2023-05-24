package com.example.planets.BackEnd.Trajectory.SteepestAscent;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.NumericalMethods.RK4;

import java.util.ArrayList;

public class SteepestAscent implements TrajectoryPlanner {

    private final int numbOfSteps = 100;
    private final int numbOfStages;
    private int numbOfDays;
    private Model3D model;
    private CelestialBody target;

    private ArrayList<double[]> trajectory;

    public SteepestAscent(Model3D model, int numbOfStages, String target, int numbOfDays){
        this.model = model.clone();
        this.numbOfStages = numbOfStages;
        this.numbOfDays = numbOfDays;

        for(int i=0; i<this.model.size(); i++)
            if( target.equalsIgnoreCase(this.model.getBody(i).getName()) )
                target = this.model.getBody(i).getName();


    }

    private void makeTrajectory(){

        //make copy of copy lol
        Model3D optimizer = model.clone();

        ArrayList<double[]> state = new ArrayList<double[]>(numbOfStages);

        state.add( new double[]{0,0,  0,0,0} );
        state.add( new double[]{0,0,  0,0,0} );

        optimizer.getShip().setPlan( state );

        for(int count=0; count<numbOfSteps; count++){
            // clone of initial condition
            optimizer = model.clone( new RK4() );

            optimizer.getShip().setPlan( state );

            // "gradientOrder" parameters per stage and in each you calculate a derivative around the last ship
            // a set of spaceShips per stage of launch, since each has its own set of parameters
            // 5 per number of stages since thats the number of variables
            // 2 bc we go in both directions for each parameters
            optimizer.addShips( 2*5*numbOfStages );

            // set states
            // in each stage go + and - each parameter
            for(int i=0; i < optimizer.getAmountOfShips()-1; i+=2){
                // state for the first ship
                ArrayList<double[]> temp1 = new ArrayList<double[]>(numbOfStages);

                temp1.add( new double[]{0,0,  0,0,0} );
                temp1.add( new double[]{0,0,  0,0,0} );

                optimizer.getShip(i).setPlan( temp1 );

                // state for the second ship
                ArrayList<double[]> temp2 = new ArrayList<double[]>(numbOfStages);

                temp2.add( new double[]{0,0,  0,0,0} );
                temp2.add( new double[]{0,0,  0,0,0} );

                optimizer.getShip(i+1).setPlan( temp2 );

            }

            // run sim
            optimizer.updatePos(numbOfDays, 1.6, true);

            // get best spaceShip and set its state as the next
            state = null; //set this equal to best state

        }

    }


    @Override
    public ArrayList<double[]> getTrajectory() {
        if( trajectory == null )
            makeTrajectory();

        return trajectory;
    }

}
