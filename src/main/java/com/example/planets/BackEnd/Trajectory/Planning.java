package com.example.planets.BackEnd.Trajectory;

import java.util.ArrayList;

/*
this class is where all the information about where the ship will go and all is saved
 */


public class Planning {
    //stages in which rocket has had to accelerate and such
    static private int countOfStages = 0;

    // this arrayList contains 2D arrays of:
    // first dimension  [ 0:start of time interval, 1:end of interval ] //times to start and stop accelerating
    // second dimension [ 0:acc. in x, 1:acc. in y, 2:acc. in z ]
    private static ArrayList<double[][]> maneuverPoints;

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
    public Planning(){

        // have hohmann transfer shenanigans here

    }

}
