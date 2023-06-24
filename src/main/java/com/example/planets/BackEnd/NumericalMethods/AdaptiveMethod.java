package com.example.planets.BackEnd.NumericalMethods;
/*
this interface serves the purpouse of changing the call in the updatePos methods inside the Model3D methods
this way the other methods dont need to be changed in order to implement adaptive methods
 */
public interface AdaptiveMethod extends NumSolver {

    // sets for how long the method should run (in simulation time)
    public void inputLength(double length);
}
