package com.example.planets;

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
            //population[i] = new Gravity0(0, Math.PI / 2.0, new Euler(), changeABit(initialPlan));
        }
        while (true) {


        }


    }
    public double fit(double[] ship, double[] titan){

        double distance = Math.sqrt(Math.pow(titan[0]-ship[0],2) + Math.pow(titan[1]-ship[1],2) + Math.pow(titan[2]-ship[2],2)) ;
        return distance;
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
}
