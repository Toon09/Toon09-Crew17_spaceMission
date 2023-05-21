package com.example.planets.BackEnd.Trajectory.Cost;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;

public class Cost1 implements CostFunction{
    @Override
    public double calcCost(double fuel, CelestialBody target) {
        return 1;
    }
}
