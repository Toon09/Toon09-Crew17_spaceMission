package com.example.planets.BackEnd.Trajectory.Cost;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;

public class PlanetaryRing implements CostFunction{

    private double radius = 150.0;
    private double degree = Math.log(radius)/Math.log( Math.log10(radius) + 3.0 );

    /*
    yet to be implemented, ill write down the idea later im beat lol
     */
    @Override
    public double calcCost(double fuel, double distance) {
        if(distance > radius)
            return 1000.0/( fuel * Math.log10(distance) );
        return Math.pow(distance, degree) / fuel;

    }
}
