package com.example.planets.BackEnd.CelestialEntities;


import java.util.Arrays;

public class Engine {
    private static final double maxSpeed = 11.0;
    private static final double maxForce = 3.0 * Math.pow(10, 7); // Newtons
    private static final double fuelConsumption = 5292.94;
    private double usedFuel = 0.0;

    public Engine(){}


    public double[] thrust(Planning plan, double mass, double time, double dt){
        double[] result = new double[] {0,0,0};

        if (time >= plan.getCurrent()[0] && time <= plan.getCurrent()[0] + dt){
            for (int i =0; i<3; i++)
                result[i] = plan.getCurrent()[i+1];

            usedFuel += useFuel(plan, mass); // consumes required fuel

            plan.nextDirection();
            System.out.println("time: " + time); // always same????
            System.out.println("plan: " + Arrays.toString(result) + "\n\n");
        }

        return result;
    }

    private double useFuel(Planning plan, double mass) {
        double magnitude = calcMagnitude(plan.getCurrent());

        return (magnitude*mass/maxForce)*fuelConsumption;
    }

    public double getUsedFuel(){
        return usedFuel;
    }

    // calcs magnitude
    private double calcMagnitude(double[] velocity) {
        double magnitude = 0.0;
        for (int i = 1; i < 4; i++) {
            magnitude += velocity[i]*velocity[i];
        }
        return Math.sqrt(magnitude);
    }

    public Engine clone(){
        return new Engine(usedFuel);
    }

    private Engine(double fuel){usedFuel = fuel;}

    public static double getMaxSpeed(){ return maxSpeed; }


}
