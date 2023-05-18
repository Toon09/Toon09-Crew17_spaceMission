package com.example.planets;

import com.example.planets.BackEnd.Models.*;
import com.example.planets.BackEnd.NumericalMethods.*;
import com.example.planets.Data.DataGetter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


class NumericalExperiments {

    /*
    ToDo
    + finish setting up the engine for easy use and implementation
    + write documentation
    + make a test folder and add folders inside with separated test cases for everything in here (pain)
    + make lightweight versions of the classes that are extended by heavier ones we use normally

    + make gravity constructor that gets data from all planets inside NASA_Horizons folder
    + must take in name of folder from which the data comes


    + check cosntructor in grav for folders & fix comment
    + you must always download the data for the sun, since its centered around the sun

    change time to run instead of in amount of days, to be in hours ?

    change the way we chose initial conditions to one that matches earths coordinate system in angles

    //for adaptive method
    // https://youtu.be/6bHdFef1S60
    // https://en.m.wikipedia.org/wiki/Adaptive_step_size

    Runge Kutta Fehlberg (famous adaptive rk)
    Cash - Karp rk fehlberg -> rk4&5

    // RK6: https://youtu.be/soEj7YHrKyE

    // https://youtube.com/playlist?list=PLYdroRCLMg5PhZqzEJJlyLo55-1Vdd4Bd [numerical methods]
    // https://youtube.com/playlist?list=PLOIRBaljOV8je0oxFAyj2o6YLXcBX1rTZ [rocket trajectory]

    change for loop to be inside the step func in NumSolver so the step ize change of of dormant prince doesnt destroy anything
    have the length of execution be a parameter

    -implement dormant prince with for loop inside & just change the dt you already have
     */

    // https://ssd.jpl.nasa.gov/horizons/app.html#/ [ experiment data ]
    public static void main(String[] args) {
        //engineTest()

        //comparingToEachOther();

        experimentSetUp();

    }


    public static void engineTest() {
        /*
        +write a method to add points to the planning so the engine can be tested with these
         */

    }

    // change so that each solver can have its own dt and display it as well
    public static void comparingToEachOther() {
        //experiment setup hyper parameters
        double time = 30;
        boolean isDay = true;
        int checkInterval = 1; //every how many days do you want it to print the values
        final int TARGET = 4;

        // benchmark model
        Model3D benchmark = new Gravity0( 0, Math.PI / 2.0, new RK4() );
        double benchmarkPrecision = 0.1;


        //  testing models
        ArrayList<Model3D> models = new ArrayList<Model3D>();
        ArrayList<Double> steps = new ArrayList<Double>();

        // add a model with the solver and next the step size its used with it
        //models.add( new Gravity0( 0, Math.PI / 2.0, new Euler() ) );
        //steps.add( 0.1 );

        models.add( new Gravity0( 0, 0, new RK3() ) );
        steps.add( 1.0 );


        //test details
        System.out.println("target planet: TARGET");
        System.out.println("benchmark precision: " + benchmarkPrecision + "s");
        System.out.println("benchmark model: " + benchmark.getSolverName() + "\n");

        double[][] errors = new double[models.size()][3];
        double[] chrono = new double[models.size()+1]; //last index is the benchmark

        //using mars
        for (int i = 0; i < time; i++) {
            //benchmark precision
            chrono[ chrono.length-1 ] = System.currentTimeMillis();
            benchmark.updatePos(1, benchmarkPrecision, isDay);
            chrono[ chrono.length-1 ] = System.currentTimeMillis() - chrono[ chrono.length-1 ];

            // update all models positions
            for (int j=0; j<models.size(); j++) {
                //start count of how much each model took here
                chrono[j] = System.currentTimeMillis();
                models.get(j).updatePos(1, steps.get(j), isDay);
                //end count of how long each model here
                chrono[j] = System.currentTimeMillis() - chrono[j];
            }


            //calculate errors
            for (int j = 0; j < models.size(); j++) {
                errors[j][0] = benchmark.getBody(TARGET).getPos()[0] - models.get(j).getBody(TARGET).getPos()[0];
                errors[j][1] = benchmark.getBody(TARGET).getPos()[1] - models.get(j).getBody(TARGET).getPos()[1];
                errors[j][2] = benchmark.getBody(TARGET).getPos()[2] - models.get(j).getBody(TARGET).getPos()[2];
            }


            //prints
            if ((i + 1) % checkInterval == 0) {
                System.out.println("Day: " + (i + 1) + "\n");

                for (int j = 0; j < models.size(); j++) {
                    System.out.println( models.get(j).getSolverName() );
                    System.out.println("Execution time: " + chrono[j] + "ms");
                    System.out.println("Sim time: " + models.get(j).getTime() + "s");
                    System.out.println("step size: " + steps.get(j) + "s");
                    System.out.println("Error= X: " + errors[j][0] + "; Y: " + errors[j][1] + "; Z: " + errors[j][2] + "\n");

                }

                //print benchmark data here
                System.out.println("Benchmark time: " + chrono[ chrono.length-1 ] + "ms");
                System.out.println("In sim time: " + Math.round( benchmark.getTime() ) + "s");
                System.out.println("Benchmark step size: " + benchmarkPrecision + "s");
                System.out.println("Benchmark data= x:" + benchmark.getBody(TARGET).getPos()[0] +
                                                  "; y:" + benchmark.getBody(TARGET).getPos()[1] +
                                                  "; z:" + benchmark.getBody(TARGET).getPos()[2]);
                System.out.println("\n");
            }


        }

    }

    // ############################################################################## EXPERIMENT SET UP

    public static void experimentSetUp(){
        // experiment setup hyper parameters
        double time = 30;
        boolean isDay = true;
        int checkInterval = 1; //every how many days do you want it to print the values

        // target body
        final int TARGET = 0; //4=mars
        final String DATA_ORIGIN = "NASA_Horizons";
        final String TARGET_FILE = "ExpData/ExpMars.txt";
        final String SUN = "ExpData/ExpSun.txt";


        //  testing models
        ArrayList<Model3D> models = new ArrayList<Model3D>();
        ArrayList<Double> steps = new ArrayList<Double>();

        // models
        models.add( new Gravity0( 0, 0, new Euler() ) ); //, DATA_ORIGIN
        steps.add( 0.1 );

        //benchmark
        DataGetter dataGetter = new DataGetter();
        double[][] benchmark = new double[2][3];
        double[][] sunData = new double[2][3];

        // model data
        double[][] errors = new double[models.size()][3];
        double[] chrono = new double[models.size()+1]; //last index is the benchmark

        // innit position:
        System.out.println(); //////// comments

        // main loop
        for (int i = 0; i < time; i++) {
            // getting benchmark data
            benchmark = dataGetter.getTxtExpData(i+1, TARGET_FILE);
            sunData = dataGetter.getTxtExpData(i+1, SUN);

            //centering benchmark data with sun
            for(int j=0; j<benchmark.length; j++)
                for (int k=0; k<benchmark[j].length; k++)
                        benchmark[j][k] = benchmark[j][k];// - sunData[j][k];

            // update all models positions
            for (int j=0; j<models.size(); j++) {
                //start count of how much each model took here
                chrono[j] = System.currentTimeMillis();
                models.get(j).updatePos(1, steps.get(j), isDay);
                //end count of how long each model here
                chrono[j] = System.currentTimeMillis() - chrono[j];
            }


            //calculate errors
            for (int j = 0; j < models.size(); j++) {
                errors[j][0] = benchmark[0][0] - models.get(j).getBody(TARGET).getPos()[0];
                errors[j][1] = benchmark[0][1] - models.get(j).getBody(TARGET).getPos()[1];
                errors[j][2] = benchmark[0][2] - models.get(j).getBody(TARGET).getPos()[2];
            }


            //prints
            if ((i + 1) % checkInterval == 0) {
                System.out.println("Day: " + (i + 1) + "\n");

                for (int j = 0; j < models.size(); j++) {
                    System.out.println( models.get(j).getSolverName() );
                    System.out.println("Execution time: " + chrono[j] + "ms");
                    System.out.println("Sim time: " + models.get(j).getTime() + "s");
                    System.out.println("step size: " + steps.get(j) + "s");

                    System.out.println("Model pos= X: " + models.get(j).getBody(TARGET).getPos()[0] + //delete after
                            "; Y: " + models.get(j).getBody(TARGET).getPos()[1] +
                            "; Z: " + models.get(j).getBody(TARGET).getPos()[2]);

                    System.out.println("Error= X: " + errors[j][0] + "; Y: " + errors[j][1] + "; Z: " + errors[j][2]);
                    System.out.println("Error magnitude= " +  Math.sqrt(errors[j][0]*errors[j][0]
                            + errors[j][1]*errors[j][1] + errors[j][2]*errors[j][2]) + " km\n");


                }

                System.out.println("Sun pos= X:" + sunData[0][0] + "; Y: " + sunData[0][1] + "; Z: " + sunData[0][2]); // delete
                System.out.println("Target pos= X:" + (benchmark[0][0]+sunData[0][0]) + "; Y: " + (benchmark[0][1]+sunData[0][1]) + "; Z: " + (benchmark[0][2]+sunData[0][2])); // delete
                System.out.println("Benchmark pos= X:" + benchmark[0][0] + "; Y: " + benchmark[0][1] + "; Z: " + benchmark[0][2] + "\n");

            }


        }


    }







}
