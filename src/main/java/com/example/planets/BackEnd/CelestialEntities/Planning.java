package com.example.planets.BackEnd.CelestialEntities;

import java.util.ArrayList;

/*
this class is where all the information about where the ship will go and all is saved
 */

// use single ton for this class
public class Planning {
    //stages in which rocket has had to accelerate and such
    static private int countOfStages = 0;

    //array of directiosn and magnitutes in which the rockets has had to go
    private static ArrayList<double[]> directions;

    public double[] getNextDirection(){
        countOfStages++;

        if(countOfStages >= directions.size())
            return null;

        return directions.get(countOfStages-1);
    }


    /// write the values of where the rocket will go, and they will be initialized somewhere, we will see
    public Planning(){

        // initialize all values
        if(directions == null){
            directions = new ArrayList<double[]>();
        }
    }

}
