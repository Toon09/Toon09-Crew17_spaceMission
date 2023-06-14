package com.example.planets.BackEnd.Trajectory.Cost;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;

/*
same function, but once it reaches a certain distToTarget once, it switches from closest dist
to the other kind of distToTarget
 */

public class PlanetaryRing implements CostFunction{

    private double radius = 150.0;
    private double degree = Math.log(radius)/Math.log( Math.log10(radius) + 3.0 );


    @Override
    public double calcCost(double fuel, double distToTarget) {
        if(distToTarget > radius)
            return 0.0; // 1000.0/( (fuel*fuel+1) * Math.log10(distToTarget) ); //
        return Math.pow(distToTarget, degree) / (fuel*fuel+1);

    }

}
