package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.Models.Model3D;

import java.util.ArrayList;


/*
    Here we save the output from the classes that generate the trajectories
    they take the model as an input and start from a specified planet, this is so we can reuse code
    for the way back.

    THe output of the trajectory must be a ArrayList<double[][]> in the same format as "maneuverPoints"
 */
public class Planning {
    //count of how many stages located in "maneuverPoints" have been executed so far
    private int countOfStages = 0;

    // this arrayList contains 2D arrays of:
    // first dimension  [ 0:start of time interval, 1:end of interval ] //times to start and stop accelerating
    // second dimension [ 0:acc. in x, 1:acc. in y, 2:acc. in z ]
    private ArrayList<double[][]> maneuverPoints;

    /**
     * increases the count to access the next maneuverPoint that needs to be executed
     */
    public void getNextDirection(){

        if(countOfStages < maneuverPoints.size())
            countOfStages++;

    }

    /**
     * Gets the current maneuver that needs to be executed in the following format:
     * first dimension  [ 0:start of time interval, 1:end of interval ] //times to start and stop accelerating
     * second dimension [ 0:acc. in x, 1:acc. in y, 2:acc. in z ]
     * @return a 2D array in the format described above, if all maneuvers have been executed, then it returns null
     */
    public double[][] getCurrent(){
        if( countOfStages >= maneuverPoints.size() )
            return null;
        return maneuverPoints.get(countOfStages);
    }


    /**
     * As soon as its made it generates the trajectory using Hohmann transfers, in the future more methods
     */
    public Planning(Model3D model){
        this.maneuverPoints = new ArrayList<double[][]>();

        // have hohmann transfer shenanigans here

    }



    /**
     * this constructor is for the copy function
     * @param maneuverPoints the 2D array containing all information calculated for the trajectory
     * @param countOfStages the current stage of the planning the spaceship is at
     */
    private Planning(ArrayList<double[][]> maneuverPoints, int countOfStages){
        this.maneuverPoints = new ArrayList<double[][]>( maneuverPoints.size() );
        this.maneuverPoints.addAll( maneuverPoints );

        this.countOfStages = countOfStages;
    }


    @Override
    public Planning clone() {
        return new Planning(maneuverPoints, countOfStages);
    }
}
