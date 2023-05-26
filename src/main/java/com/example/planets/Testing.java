package com.example.planets;

import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.NumericalMethods.Euler;

import java.util.Arrays;

public class Testing {
    static Gravity0 model = new Gravity0(0, Math.PI / 2.0, new Euler());
    public static void main(String[] args) {
        //creating the initial population
        Gravity0[] population = new Gravity0[20];
        double[][] initialPlan = new double[20][5];
        initialPlan[0] ={}
        while (true){
            for (int i=0; i<20; i++){
                population[i] = new Gravity0(0, Math.PI / 2.0, new Euler());
            }
        }


    }
    public double fit(double[] ship, double[] titan){
        double distance = 0;
        for (int i =0; i<3; i++){
            distance+= titan[i]-ship[i];
        }
        return distance;
    }
}
