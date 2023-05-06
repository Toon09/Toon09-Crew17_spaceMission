package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.*;

public interface NumSolver {

    /**
     * Takes a single step of length dt following the differential equation given as the model,
     * it then updates the values in the model itself
     * @param model the differential equation that is updated
     * @param dt the step size of the solver
     */
    void step(Model3D model, double dt);

    /**
     * Saves the model in memory for storing useful data in memory and speeding up the calculation process,
     * this needs to be done before you call the function step, but only once
     * @param state a 3D double array that contains:
     *                                      [ represents each element in the model in the same order ]
     *  arrays of length 3 that contain     [ 0:position, 1:velocity, 2:acceleration ]
     *  values of each coordinate in        [ 0:x, 1:y, 2:z ]
     */
    void setState(double[][][] state);

    /**
     * helps identify the specific numerical solver that is being used
     * @return a string that has the name of the numerical solver as well as any other parameters it might have
     */
    String getName();
    
}
