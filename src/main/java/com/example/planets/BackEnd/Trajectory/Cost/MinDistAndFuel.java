package com.example.planets.BackEnd.Trajectory.Cost;

public class MinDistAndFuel implements CostFunction {

    /*
    at a time input and max time input and add a porcentage sort of thing where the more tie it speeds near the end of interval
        the more it earns
     */
    @Override
    public double calcCost(double fuel, double distToTarget) {
        // segment it in more rigions, so that the really high vals dont make it hard to detect the smaller ones
        return - distToTarget*distToTarget*distToTarget*distToTarget * Math.log10( fuel + 10.0) ;

        // - distToTarget*distToTarget*distToTarget * ( (Math.log10( Math.log10(fuel+10.0) + 10.0)) );

    }
}
