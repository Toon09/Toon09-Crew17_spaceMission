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
    static Gravity0 model = new Gravity0(0, Math.PI / 2.0, new Euler());
    static double phaseTime = 1555200;
    public static void main(String[] args) {
        //create initial population
        Gravity0 initial = new Gravity0(0, Math.PI / 2.0, new RK4(), zeros());
        int popSize = 10;
        Gravity0[] population = new Gravity0[popSize];
        fillWith(initial,population);
        final int numberOfGenerations = 10;

        for (int i=0; i<numberOfGenerations; i++){
            //let a year pass for each model
            for (Gravity0 model:population){
                model.updatePos(364,120,true);
            }
            //sort the models
            notOptimalSort(population);
            //check the distance of the best model
            System.out.println("the best from generation "+i+" is: ");
            print(population[0]);
            System.out.println("------------------------------------------------------------");
            //get the best model, fill the new population with it ( with mutations )
            Gravity0 newInitial = new Gravity0(0, Math.PI / 2.0, new RK4(), population[0].getShip().getPlan());
            fillWith(newInitial, population);
       }
    }

    public static double distance(Gravity0 model) {
        return model.getShip().getDistance(getTitan(model));
    }
    public static void print( Gravity0 model){
        System.out.println("time: "+ model.getTime());
        System.out.println("titan at: "+ Arrays.toString(getTitan(model).getPos()));
        System.out.println("ship at: "+ Arrays.toString(model.getShip().getPos()) );
        System.out.println("distance: " + getTitan(model).getDistance(model.getShip()));
        System.out.println("planning: " + Arrays.deepToString(model.getShip().getPlan()));
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
        while (counter<models.length-1){
            if (distance(models[counter]) > distance(models[counter+1])){
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
    public static void fillWith(Gravity0 model, Gravity0[] population){
        for (int i =0; i<population.length; i++){
            population[i] = mutate(model);
        }
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
        double[][] plan = model.getShip().getPlan();
        for (int i = 0; i < plan.length; i++)
        {
            double[] arrayForPlan = plan[i];
            Random random = new Random();
            if (arrayForPlan.length >= 5)
                if (random.nextDouble(1) < 0.5)
                {
                    arrayForPlan[1] = arrayForPlan[1] + arrayForPlan[2] + random.nextDouble(50);
                    arrayForPlan[2] = arrayForPlan[2] + random.nextDouble(50);
                    arrayForPlan[3] = arrayForPlan[3] + random.nextDouble(50);
                    arrayForPlan[4] = arrayForPlan[4] + random.nextDouble(50);
                }
        }
        return new Gravity0(0, Math.PI / 2.0, new RK4(), plan);

    }

    public static double[][] zeros (){
        double[][] re = new double[20][5];
        re[0] = new double[]{0.0,phaseTime,0.0,0.0,0.0};
        for (int i =1; i<re.length ; i++){
            re[i] = new double[]{re[i-1][1], re[i-1][1]+phaseTime,0.0,0.0,0.0};
        }
        return re;
    }

}
