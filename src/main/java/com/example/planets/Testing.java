package com.example.planets;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.NumericalMethods.RK4;

import java.util.Arrays;
import java.util.Random;

public class Testing {
    static double phaseTime = 1555200;

    public static void main(String[] args) {
        double[][] itGotCloseOnX = new double[][]{{0.0, 1010462.9904649274, 13.563411555307415, 0.48506083467087024, -11.554940115801168}, {1555200.0, 2234860.8121268596, -8.355577673691904, 0.0, 11.01367346760598}, {3110400.0, 4098979.8673088267, -11.545462961073342, 0.0, -7.92598264083713}, {4665600.0, 5564441.630019719, 1.6164422643443008, -0.786332888526907, 0.8148260404574384}, {6220800.0, 6737512.637636197, 19.555460889550933, 0.0, 3.561269350212359}, {7776000.0, 8580917.974920297, 16.937868171089768, 0.0, 0.3754005166495944}, {9331200.0, 9988811.402657479, -7.208874782274462, 0.0, 1.2470275481504398}, {1.08864E7, 1.1357870660295697E7, -1.329674059180229, -0.4694537158749126, 5.090647068070802}, {1.24416E7, 1.2948187339020547E7, -7.673931677761051, 1.3918324435669713, 10.469866341933754}, {1.39968E7, 1.4944289165598998E7, -7.135254177281859, -0.6252189659647697, -7.061258232718764}, {1.5552E7, 1.6351800378067918E7, -12.958592823394385, 0.0, -11.899154941726007}, {1.71072E7, 1.773717635060074E7, -13.356698702208094, 0.3608685451483816, 1.6952373804089114}, {1.86624E7, 1.8959908120704126E7, 0.33298832177648086, 0.0, 0.8051908613761426}, {2.02176E7, 2.0733122506788097E7, -2.5919172163487216, 0.0, -3.14026408472472}, {2.17728E7, 2.2394278976513945E7, -4.017122376041968, -0.23403630490445881, 7.084385885171441}, {2.3328E7, 2.4011926058624703E7, 5.095032716742966, 0.0, 10.006602492347488}, {2.48832E7, 2.523786148284764E7, 10.208082505818131, 0.07626678749187388, 7.9269114738832345}, {2.64384E7, 2.6468847538030986E7, -5.807715254038964, 0.0, 6.061796125224062}, {2.79936E7, 2.876727403794861E7, 13.193262940498197, 0.0, -8.70925417740302}, {2.95488E7, 3.036790578423221E7, 3.778436026474903, 0.0, -2.74103070498108}};
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
