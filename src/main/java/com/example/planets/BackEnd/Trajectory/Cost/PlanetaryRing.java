package com.example.planets.BackEnd.Trajectory.Cost;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;

/*
same function, but once it reaches a certain distance once, it switches from closest dist
to the other kind of distance
 */

public class PlanetaryRing implements CostFunction{

    private double radius = 150.0;
    private double alpha = 1.0;
    private double degree = Math.log(radius)/Math.log( Math.log10(radius) + Math.log10(alpha) );


    @Override
    public double calcCost(double fuel, double distToTarget) {
        /*
        if(distToTarget > radius)
            return alpha / ( (Math.log10(fuel)/2.0+1.0) * Math.log10(distToTarget) );
        return Math.pow(distToTarget, degree) / (fuel*fuel+1);
         */
        return -(distToTarget*distToTarget);
    }

}
