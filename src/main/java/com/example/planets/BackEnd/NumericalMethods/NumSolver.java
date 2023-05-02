package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.*;

public interface NumSolver {

    void step(Model3D model, double dt);
    String getName();
    
}
