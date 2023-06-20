package com.example.planets.BackEnd.CelestialEntities;

public class Engine {
    private double usedFuel = 0;
    private static final double maxSpeed = 11;
    private final double maxForce = 3 * Math.pow(10, 7); // Newtons
    private final double fuelConsumption = 5292.94;

    public double useFuel(double mass, double magnitude, double dt) {
        return usedFuel += (magnitude*mass/maxForce)*fuelConsumption*dt;
    }

    public double calcMagnitude(double[] velocity) {
        double magnitude = 0.0;
        for (int i = 0; i < 3; i++) {
            magnitude += Math.pow(velocity[i], 2);
        }
        return Math.sqrt(magnitude);
    }
    public static double getMaxSpeed(){ return maxSpeed; }
    public double getUsedFuel(){ return usedFuel; }


}
