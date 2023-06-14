package com.example.planets.BackEnd.Trajectory.Cost;

public class MinDistAndFuel implements CostFunction {
    @Override
    public double calcCost(double fuel, double distToTarget) {
        return - distToTarget*distToTarget / ( (Math.log10(fuel+10.0)/100.0 + 1.0) );
    }
}
