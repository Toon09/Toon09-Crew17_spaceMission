package com.example.planets.BackEnd.Trajectory.Cost;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;

public class PlanetaryRing implements CostFunction{

    private double radius = 150.0;
    private double degree = Math.log(radius)/Math.log( Math.log10(radius) + 3.0 );


    /*
    make it so that the function is more negative the closer to erath
    take as input the spaceship itself, the target planet and the origin planet
    then you can switch both around in the second phase
     */
    @Override
    public double calcCost(double fuel, double distance) {
        if(distance > radius)
            return 1000.0/( (fuel*fuel+1) * Math.log10(distance) );
        return Math.pow(distance, degree) / (fuel*fuel+1);

    }
}
