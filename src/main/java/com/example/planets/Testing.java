package com.example.planets;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.NumericalMethods.RK4;

import java.util.Arrays;
import java.util.Random;

public class Testing {
    static double phaseTime = 1555200;

    public static void main(String[] args) {
        double[][] itGotCloseOnX = new double[][]{{0.0, 1557661.2595868367, 12.724566786262924, -11.56975416416912, -13.187099568807334}, {1555200.0, 3120173.170069419, -14.687366077781249, 8.071414177147657, 11.947022571750011}, {3110400.0, 4674821.408970904, -13.982154185665102, -12.637000381807535, -6.938132452559761}, {4665600.0, 6223820.696330711, -4.595572189608356, -5.002822099809511, 3.4676896255994407}, {6220800.0, 7785891.473174564, 14.460050404975293, 3.201727835632133, 4.645898374592764}, {7776000.0, 9334344.779238155, 8.843438094484204, 2.4502912124587573, 4.498943606999527}, {9331200.0, 1.0893375802886382E7, -4.94662556338006, 11.398100961890428, 0.032852257407151586}, {1.08864E7, 1.2441728381711507E7, -1.6733580617929595, 8.680513797649244, 7.362706208645842}, {1.24416E7, 1.4012288679905305E7, -7.068513330679969, -6.335025792409857, 12.038473529165833}, {1.39968E7, 1.5563802726786422E7, -12.82108143322914, 4.572486041533924, -12.027611800244943}, {1.5552E7, 1.7112199578083906E7, -13.01959314700848, -11.351716649485855, -11.559937945592953}, {1.71072E7, 1.8664028833072945E7, -14.346286060483347, -12.12732696477152, 3.458394994208322}, {1.86624E7, 2.02176E7, 0.0, 0.0, 0.0}, {2.02176E7, 2.178043939844495E7, -5.764021252153141, -1.6808565689323203, -2.7444184142960015}, {2.17728E7, 2.333493293937622E7, -4.817830434912766, 8.173090860248228, 7.520930263133849}, {2.3328E7, 2.4894468311031472E7, 0.3117794010125202, 3.6512444369537604, 12.982174292845718}, {2.48832E7, 2.6452055648960017E7, 7.731226888610791, -10.65305698923278, 14.54684785158655}, {2.64384E7, 2.8001347771612175E7, -5.412386475965186, -11.216589237671855, 6.33253687119324}, {2.79936E7, 2.955340386780767E7, 10.05475656997354, 9.93017972714041, -7.988046855178425}, {2.95488E7, 3.1104E7, 0.0, 0.0, 0.0}};
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
                if (random.nextDouble(1) < 0.5) {
                    arrayForPlan[0] = plan[i][0];
                    arrayForPlan[1] = plan[i][1] - random.nextDouble(100000);
                }else {
                    arrayForPlan[0] = plan[i][0];
                    arrayForPlan[1] = plan[i][1];
                }
                if (random.nextDouble(1) < 0.5) {
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
        double[][] re = new double[20][5];
        re[0] = new double[]{0.0, phaseTime, 0.0, 0.0, 0.0};
        for (int i = 1; i < re.length; i++) {
            re[i] = new double[]{re[i - 1][1], re[i - 1][1] + phaseTime, 0.0, 0.0, 0.0};
        }
        return re;
    }

}
