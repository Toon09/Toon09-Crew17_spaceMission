package com.example.planets;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.NumericalMethods.RK4;

import java.util.Arrays;
import java.util.Random;

public class Testing {
    static double phaseTime = 1555200/3;

    public static void main(String[] args) {
        double[][] itGotCloseOnX = new double[][] {{0.0, 1006099.2403889479, 13.478198938977393, 0.0, -11.574440038818498}, {1555200.0, 2232802.717894166, -8.234156003085303, -0.03866188065417672, 11.435245512192717}, {3110400.0, 4094530.9633089653, -11.593424886632755, 0.36225824128644546, -8.00371594337596}, {4665600.0, 5559968.467362431, 1.333487830589962, -0.028960730122266933, 1.2872081303495109}, {6220800.0, 6732535.969982143, 19.746015620307016, 0.0, 3.6360360087247767}, {7776000.0, 8576395.848272633, 16.916364021327016, 0.03191260847697667, 0.009075083388193891}, {9331200.0, 9984822.41158909, -7.311495018558531, 0.06764284289612625, 1.7899890121088204}, {1.08864E7, 1.1354442883958297E7, -1.1066797013371685, -0.11104043653322337, 5.174241109625327}, {1.24416E7, 1.2946646775464464E7, -7.569305250782059, -0.04300847385993879, 10.12338837673159}, {1.39968E7, 1.4941024658825539E7, -7.03623049402023, 0.0, -7.088231363212981}, {1.5552E7, 1.6347706421641855E7, -12.984034852499683, 0.0, -11.713721150338426}, {1.71072E7, 1.7732541112581246E7, -13.035653347402826, 0.021433181133065644, 1.9447250540208896}, {1.86624E7, 1.8956495086774006E7, 0.6088250223976773, 0.0, 0.5845878294268794}, {2.02176E7, 2.0728464128343135E7, -2.4429827523341414, 0.0, -2.9969411524918232}, {2.17728E7, 2.239092706040708E7, -3.7156578489697107, 0.0, 7.261100452960286}, {2.3328E7, 2.400670536396892E7, 5.003084803324452, 0.0, 9.548342427586869}, {2.48832E7, 2.523063876988673E7, 9.95893367429932, 0.15126753069022764, 8.217770629016789}, {2.64384E7, 2.6463881544144772E7, -5.745941945036617, -0.09855862527709733, 6.552032189861524}, {2.79936E7, 2.876128594873282E7, 13.315998647137498, 0.0, -8.766093673177066}, {2.95488E7, 3.0364198997304026E7, 3.5087608525975025, 0.0, -3.1342400696000503}};
        System.out.println(itGotCloseOnX.length);
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
                model.updatePos(364, 300, true);
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
                    arrayForPlan[2] = plan[i][2] + random.nextDouble(1) * negative;
                }else {
                    arrayForPlan[2] = plan[i][2];

                }
                if (random.nextDouble(1) < 0.5) {
                    if (random.nextDouble(1) < 0.5) {
                        negative = -1;
                    } else {
                        negative = 1;
                    }
                    arrayForPlan[3] = plan[i][3] + random.nextDouble(1) * negative;
                if (random.nextDouble(1) < 0.5) {
                        negative = -1;
                    } else {
                        negative = 1;
                    }
                    arrayForPlan[4] = plan[i][4] + random.nextDouble(1) * negative;
                } else {
                    arrayForPlan[4] = plan[i][4];

                }
            }
            plan[i] = arrayForPlan;
        }
        return new Gravity0(0, Math.PI / 2.0, new RK4(), plan);

    }

    public static double[][] zeros() {
        double[][] re = new double[5][5];
        re[0] = new double[]{0.0, phaseTime, 0.0, 0.0, 0.0};
        for (int i = 1; i < re.length; i++) {
            re[i] = new double[]{re[i - 1][1]+phaseTime, re[i - 1][1] + phaseTime, 0.0, 0.0, 0.0};
        }
        return re;
    }

}
