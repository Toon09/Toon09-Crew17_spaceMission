package com.example.planets;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.NumericalMethods.RK4;

import java.util.Arrays;
import java.util.Random;

public class Testing {
    static double phaseTime = 1555200;

    public static void main(String[] args) {
        double[][] itGotCloseOnX = new double[][]{{0.0, 1006955.3451267978, 13.5898236845843, 0.0, -11.552170358395637}, {1555200.0, 2233412.517840434, -8.279807878842291, 0.14397578379963044, 11.179545664484314}, {3110400.0, 4096459.983588739, -11.687457951827694, 0.030060209338050717, -8.10027068411624}, {4665600.0, 5560225.248459364, 1.333487830589962, 0.0, 1.2159651196557828}, {6220800.0, 6732535.969982143, 19.796603060598063, -0.029941779857575714, 3.7678047656540286}, {7776000.0, 8576484.787354266, 16.83866097115771, 0.0, 0.05727792893621084}, {9331200.0, 9984822.41158909, -7.351768383774266, 0.0, 1.625347148268674}, {1.08864E7, 1.1354735430419931E7, -1.2496112277299638, 0.0, 5.252214674677525}, {1.24416E7, 1.2947116444481518E7, -7.569305250782059, -0.016453966308851986, 10.2234434101541}, {1.39968E7, 1.4942266317895986E7, -7.056572941697074, 0.01650984784425107, -7.045031985777224}, {1.5552E7, 1.6349126072262302E7, -13.00854011504879, 0.09516769612756114, -11.834043781926558}, {1.71072E7, 1.7733709274597418E7, -13.151248294709916, 0.019837612237089897, 1.8997114178383336}, {1.86624E7, 1.8957105770413455E7, 0.53764934350356, 0.0, 0.4464299593307638}, {2.02176E7, 2.0730790549235895E7, -2.451420464122587, 0.0, -2.9508456438288913}, {2.17728E7, 2.2392331210373204E7, -3.8097010456299474, 0.1417833999660631, 7.177280613319854}, {2.3328E7, 2.4008094418371215E7, 5.023263872050227, 0.050821089884041215, 9.693387914296753}, {2.48832E7, 2.5232806166049387E7, 10.099032604298205, 0.06514536760051813, 8.26950971082344}, {2.64384E7, 2.6464616404628478E7, -5.71559358898213, 0.023583946778446708, 6.572220218234816}, {2.79936E7, 2.8762879028058916E7, 13.291782240377382, 0.0, -8.804018690800117}, {2.95488E7, 3.036516114232909E7, 3.5167631955610905, 0.0, -3.1674865692522283}};
        //create initial population
        Gravity0 initial = new Gravity0(0, Math.PI / 2.0, new RK4(), itGotCloseOnX);
        int popSize = 15;
        Gravity0[] population = new Gravity0[popSize];
        population = fillWith(initial, population);


        final int numberOfGenerations = 999999;
        Gravity0 best = null;
        for (int i = 0; i < numberOfGenerations; i++) {

            //let a year pass for each model
            for (Gravity0 model : population) {
                model.updatePos(300, 120, true);
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

    public static Gravity0 mutate(Gravity0 model) {
        double[][] plan = model.getShip().getPlan().clone();
        for (int i = 0; i < plan.length; i++) {
            double[] arrayForPlan = new double[plan[i].length];
            Random random = new Random();
            double negative = 1;
            if (arrayForPlan.length >= 5) {
                if (random.nextDouble(1) < 0.2) {
                    arrayForPlan[0] = plan[i][0];
                    arrayForPlan[1] = plan[i][1] - random.nextDouble(1000);
                }else {
                    arrayForPlan[0] = plan[i][0];
                    arrayForPlan[1] = plan[i][1];
                }
                if (random.nextDouble(1) < 0.2) {
                    if (random.nextDouble(1) < 0.4) {
                        negative = -1;
                    } else {
                        negative = 1;
                    }
                    arrayForPlan[2] = plan[i][2] + random.nextDouble(0.1) * negative;
                }else {
                    arrayForPlan[2] = plan[i][2];

                }
                if (random.nextDouble(1) < 0.5) {
                    if (random.nextDouble(1) < 0.5) {
                        negative = -1;
                    } else {
                        negative = 1;
                    }
                    arrayForPlan[3] = plan[i][3] + random.nextDouble(0.1) * negative;
                if (random.nextDouble(1) < 0.5) {
                        negative = -1;
                    } else {
                        negative = 1;
                    }
                    arrayForPlan[4] = plan[i][4] + random.nextDouble(0.1) * negative;
                } else {
                    arrayForPlan[4] = plan[i][4];

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
