package com.example.planets.BackEnd.Trajectory.Cost;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;

public class PlanetaryRing implements CostFunction{

    private double radius = 150.0;
    private double degree = Math.log(radius)/Math.log( Math.log10(radius) + 3.0 );


    @Override
    public double calcCost(double fuel, double distance) {
        if(distance > radius)
            return 1.0/( (fuel*fuel+1) * Math.log10(distance) );
        return Math.pow(distance, degree) / (fuel*fuel+1);

    }
}
