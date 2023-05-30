package com.example.planets.BackEnd.Trajectory.Cost;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;

/*
same function, but once it reaches a certain distance once, it switches from closest dist
to the other kind of distance
 */

public class PlanetaryRing implements CostFunction{


    @Override
    public double calcCost(double fuel, double distToTarget) {
        return - distToTarget*distToTarget / ( (2*Math.log10(fuel+10.0) + 1.0) );
        //return -(distToTarget*distToTarget*distToTarget)*(fuel/1000.0+1.0);
    }

}
