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
     * helps identify the specific numerical solver that is being used
     * @return a string that has the name of the numerical solver as well as any other parameters it might have
     */
    String getName();
    
}
