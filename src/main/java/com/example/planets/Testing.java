package com.example.planets;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.NumericalMethods.Euler;
import com.example.planets.BackEnd.NumericalMethods.RK4;
import org.apache.poi.sl.draw.DrawNothing;

import java.util.Arrays;
import java.util.Random;

public class Testing {
    static double phaseTime = 1555200;

    public static void main(String[] args) {;
        //create initial population
        Gravity0 initial = new Gravity0(0, Math.PI / 2.0, new RK4(), zeros());
        int popSize = 15;
        Gravity0[] population = new Gravity0[popSize];
        population = fillWith(initial, population);


        final int numberOfGenerations = 999999;
        Gravity0 best = null;
        for (int i = 0; i < numberOfGenerations; i++) {

            //let a year pass for each model
            for (Gravity0 model : population) {
                model.updatePos(364, 120, true);
            }

            //sort the models
            notOptimalSort(population);
            //check the distance of the best model
            System.out.println("------------------------------------------------------------");
            System.out.println("the best from generation " + i + " is: ");
            print(population[0]);
            System.out.println("------------------------------------------------------------");

            if (i == 0 || distance(population[0]) < distance(best)) {
                best = population[0];
                System.out.println("------------------------------------------------------------");
                System.out.println("we have a new overall best");
                System.out.println("------------------------------------------------------------");
            }
//            System.out.println("The whole generation:");
//            for (int e=0; e<population.length; e++){
//                System.out.println("--------------------   " + e + "   -------------------------------");
//                print(population[e]);
//            }
//            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            //get the best model, fill the new population with it ( with mutations )
            Gravity0 modelOne = new Gravity0(0, Math.PI / 2.0, new RK4(), population[0].getShip().getPlan());
            Gravity0 modelTwo = new Gravity0(0, Math.PI / 2.0, new RK4(), population[1].getShip().getPlan());
            Gravity0 modelThree = new Gravity0(0, Math.PI / 2.0, new RK4(), population[2].getShip().getPlan());

            population = fillWith(modelOne, modelTwo, modelThree, population);
            System.out.println("--------------------------------------------------");

        }
        System.out.println("best we got after " + numberOfGenerations + " generations is :");
        print(best);

    }

    public static double distance(Gravity0 model) {
        return model.getShip().getDistance(getTitan(model));
    }

    public static void print(Gravity0 model) {
        System.out.println("time: " + model.getTime());
        System.out.println("titan at: " + Arrays.toString(getTitan(model).getPos()));
        System.out.println("ship at: " + Arrays.toString(model.getShip().getPos()));
        System.out.println("distance: " + getTitan(model).getDistance(model.getShip()));
        System.out.println("planning: " + Arrays.deepToString(model.getShip().getPlan()));
    }

    public static double[][] changeABit(double[][] oryginal) {
        double[][] changed = new double[oryginal.length][oryginal[0].length];
        changed[0] = oryginal[0];
        Random random = new Random();
        for (int i = 1; i < oryginal.length; i++) {
            changed[i] = new double[]{oryginal[i][0], oryginal[i][1], oryginal[i][2] + random.nextDouble(2), oryginal[i][3] + random.nextDouble(2), oryginal[i][4] + random.nextDouble(3)};
        }
        return changed;
    }

    public static void notOptimalSort(Gravity0[] models) {
        int counter = 0;
        while (counter < models.length - 1) {
            if (distance(models[counter]) > distance(models[counter + 1])) {
                Gravity0 tmp = models[counter];
                models[counter] = models[counter + 1];
                models[counter + 1] = tmp;
                if (counter != 0) {
                    counter--;
                }
            } else {
                counter++;
            }
        }
    }

    public static CelestialBody getTitan(Gravity0 model) {
        return model.getBody(8);
    }

    public static Gravity0[] fillWith(Gravity0 model, Gravity0[] population) {
        Gravity0[] newPop = new Gravity0[population.length];
        for (int i = 0; i < population.length; i++) {
            newPop[i] = mutate(model);
        }
        return newPop;
    }

    public static Gravity0[] fillWith(Gravity0 modelOne, Gravity0 modelTwo, Gravity0 modelThree, Gravity0[] population) {
        Gravity0[] newPop = new Gravity0[population.length];
        int stage = population.length / 5;
        for (int i = 0; i < population.length; i++) {
            if (i < stage * 2) {
                newPop[i] = mutate(modelOne);
            } else if (i < stage * 4) {
                newPop[i] = mutate(modelTwo);
            } else {
                newPop[i] = mutate(modelThree);
            }
        }
        return newPop;
    }

    /*
    public static Gravity0 mutate(Gravity0 model){
        double[][] plan = model.getShip().getPlan();
        for (int i =0; i<plan.length; i++)    {
            Random random = new Random();
            if (random.nextDouble(1)<0.5){
                plan[i]= new double[]{plan[i][0],plan[i][1]+plan[i][2]+random.nextDouble(50),plan[i][3]+random.nextDouble(50),plan[i][4]+random.nextDouble(50)};
            }
        }
        return new Gravity0(0, Math.PI / 2.0, new RK4(), plan);
    }
     */

    public static Gravity0 mutate(Gravity0 model) {
        double[][] plan = model.getShip().getPlan().clone();
        for (int i = 0; i < plan.length; i++) {
            double[] arrayForPlan = new double[plan[i].length];
            Random random = new Random();
            double negative = 1;
            if (arrayForPlan.length >= 5){
                if (random.nextDouble(1) < 0.7) {
                    arrayForPlan[0] = plan[i][0];
                    arrayForPlan[1] = plan[i][1]+ random.nextDouble(10000);
                    if (random.nextDouble(1) < 0.5) {
                        negative = -1;
                    } else {
                        negative = 1;
                    }
                    arrayForPlan[2] = arrayForPlan[2] + random.nextDouble(15) * negative;
                    if (random.nextDouble(1) < 0.5) {
                        negative = -1;
                    } else {
                        negative = 1;
                    }
                    arrayForPlan[3] = arrayForPlan[3] + random.nextDouble(15) * negative;
                    if (random.nextDouble(1) < 0.5) {
                        negative = -1;
                    } else {
                        negative = 1;
                    }
                    arrayForPlan[4] = arrayForPlan[4] + random.nextDouble(15) * negative;
                }else {
                    arrayForPlan = plan[i];
                }
            }
            plan[i] = arrayForPlan;
        }
        return new Gravity0(0, Math.PI / 2.0, new RK4(), plan);

    }

    public static double[][] zeros() {
        double[][] re = new double[20][5];
        re[0] = new double[]{0.0, phaseTime, 0.0, 0.0, 0.0};
        for (int i = 1; i < re.length; i++) {
            re[i] = new double[]{re[i - 1][1], re[i - 1][1] + phaseTime, 0.0, 0.0, 0.0};
        }
        return re;
    }

}
