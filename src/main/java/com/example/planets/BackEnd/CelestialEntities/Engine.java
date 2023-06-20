package com.example.planets.BackEnd.CelestialEntities;

public class Engine {
    private static final double maxSpeed = 11.0;
    private static final double maxForce = 3.0 * Math.pow(10, 7); // Newtons
    private static final double fuelConsumption = 5292.94;


    public double[] thrust(Planning plan, double time){
        double[] result = new double[] {0,0,0};

        if (time >= plan.getCurrent()[0]){
            for (int i =0; i<3; i++)
                result[i] = plan.getCurrent()[i+1];

            plan.nextDirection();
        }

        return result;
    }

    public double useFuel(Planning plan, double mass) {
        double magnitude = calcMagnitude(plan.getCurrent());
        return (magnitude*mass/maxForce)*fuelConsumption;
    }

    // calcs magnitude
    private double calcMagnitude(double[] velocity) {
        double magnitude = 0.0;
        for (int i = 1; i < 4; i++) {
            magnitude += Math.pow(velocity[i], 2);
        }
        return Math.sqrt(magnitude);
    }
    public static double getMaxSpeed(){ return maxSpeed; }



}
