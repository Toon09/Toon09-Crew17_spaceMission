package com.example.planets;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.NumericalMethods.Euler;

import java.util.Arrays;
import java.util.Random;

public class Testing {
    static Gravity0 model = new Gravity0(0, Math.PI / 2.0, new Euler());
    static double phaseTime =2;
    public static void main(String[] args) {
        //initialize the initial population
        Gravity0[] population = new Gravity0[20];
        //create the initial plan for the ships
        double[][] initialPlan = new double[20][5];
        initialPlan[0] = new double[]{0, 2, 11, 11, 11};
        Random random = new Random();
        for (int i = 1; i < 20; i++) {
            initialPlan[i] = new double[]{initialPlan[i - 1][0] + initialPlan[i - 1][1],initialPlan[i - 1][0] + initialPlan[i - 1][1]+phaseTime, random.nextDouble(3), random.nextDouble(3), random.nextDouble(3)};
        }
        //create a new population of Gravity 0
        for (int i = 0; i < 20; i++) {
            population[i] = new Gravity0(0, Math.PI / 2.0, new Euler(), changeABit(initialPlan));
        }
        while (true) {
            notOptimalSort(population);
            if (fit(population[0])< 1000){

            }

        }


    }

    public static double fit(Gravity0`model) {
        return
    }

    public static double[][] changeABit(double[][] oryginal) {
        double[][] changed = new double[oryginal.length][oryginal[0].length];
        changed[0] = oryginal[0];
        Random random = new Random();
        for (int i = 1; i < oryginal.length; i++){
            changed[i]= new double[]{oryginal[i][0],oryginal[i][1],oryginal[i][2]+random.nextDouble(2),oryginal[i][3]+random.nextDouble(2),oryginal[i][4]+random.nextDouble(3)};
        }
        return changed;
    }
    public static void notOptimalSort(Gravity0[] models){
        int counter =0;
        while (counter<models.length){
            if (models[counter].getShip().getDistance(getTitan(models[counter])) > models[counter+1].getShip().getDistance(getTitan(models[counter+1]))){
                Gravity0 tmp = models[counter];
                models[counter] = models[counter+1];
                models[counter+1] = tmp;
                if (counter!=0){
                    counter--;
                }
            }else{
                counter++;
            }
        }
    }
    public static CelestialBody getTitan(Gravity0 model){
        return model.getBody(8);
    }

}
