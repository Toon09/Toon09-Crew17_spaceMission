package com.example.planets.BackEnd.Trajectory.Cost;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;

public interface CostFunction {

    public double calcCost(double fuel, CelestialBody target);

}
