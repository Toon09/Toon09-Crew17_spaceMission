package com.example.planets;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.Models.*;
import com.example.planets.BackEnd.NumericalMethods.*;
import com.example.planets.BackEnd.Trajectory.Cost.PlanetaryRing;
import com.example.planets.Data.DataGetter;

import java.util.ArrayList;


class NumericalExperiments {

    /*
    ToDo
    + ENGINE
    + TRAJECTORY: https://www.sciencedirect.com/science/article/pii/S037604211830191X
        hill climb
        gradient descend
        genetic alg
        ###
        return interval of time velocity

    + make test class with 1 diff eq & 3 different starting positions (1 per dim)
        - only 1 body in this classes
        - have them to test out stuff
        - look up solved diff eq for this body to have

    + make list of famous space stations and allow people to launch from those sites
        - run simulations from each one of them and make it so that the result is saved and is
            accessed when you launch from that location
        - have a boolean so that it calculates the trajectory and saves it,
            or if the location is not even close to an already known one, then re-calculate it
    + write documentation
    + make a test folder and add folders inside with separated test cases for everything in here (pain)

    +++ in experiment setup change add appart from printing writing in a text file
        but instead make it a csv and write in the format, make the first line be the names of stuff


    ++++++++ MAKE TAYLOR RK'S TO CHECK PRECISION [ https://www.cfm.brown.edu/people/sg/AM35odes.pdf ]
            do one RK4 per deriv. that appears in expression, or think it out
    +++++ change model so that it uses polar coordinates (test, not garantee to fix personal life)


    change the way we chose initial conditions to one that matches earths coordinate system in angles

    //for adaptive method
    // https://youtu.be/6bHdFef1S60
    // https://en.m.wikipedia.org/wiki/Adaptive_step_size
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

        //experimentSetUp();

        //trajectoryTesting();

        testingAccuracyOfSolvers();

    }


    public static void engineTest() {
        /*
        +write a method to add points to the planning so the engine can be tested with these
         */

    }

    // ############################################################################## COMPARING SOLVER SET UP

    /*
    in RK4
    1.5 to 1.6 saves 7 seconds of real time execution on a whole year in sim (360 days)
    1.0 to 1.5 saves 180 seconds of real time execution on a whole year in sim (360 days)
     */
    public static void comparingToEachOther() {
        //experiment setup hyper parameters
        double time = 30;
        boolean isDay = true;
        int checkInterval = 1; //every how many days do you want it to print the values
        final int TARGET = 7;

        // benchmark model
        Model3D benchmark = new Gravity0( 0, Math.PI / 2.0, new RK4() );
        double benchmarkPrecision = 0.1;


        //  testing models
        ArrayList<Model3D> models = new ArrayList<Model3D>();
        ArrayList<Double> steps = new ArrayList<Double>();

        // add a model with the solver and next the step size its used with it
        //models.add( new Gravity0( 0, Math.PI / 2.0, new Euler() ) );
        //steps.add( 0.1 );

        models.add( new Gravity0( 0, 0, new RK4() ) );
        steps.add( 1.0 );

        models.add( new Gravity0( 0, 0, new RK4() ) );
        steps.add( 1.25 );

        models.add( new Gravity0( 0, 0, new RK4() ) );
        steps.add( 1.5 );

        models.add( new Gravity0( 0, 0, new RK4() ) );
        steps.add( 1.6 );

        //test details
        System.out.println("target planet: TARGET");
        System.out.println("benchmark precision: " + benchmarkPrecision + "s");
        System.out.println("benchmark model: " + benchmark.getSolverName() + "\n");

        double[][] errors = new double[models.size()][3];
        double[] chrono = new double[models.size()+1]; //last index is the benchmark

        // sim loop
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
                    System.out.println("Error= X: " + errors[j][0] + "; Y: " + errors[j][1] + "; Z: " + errors[j][2]);
                    double sum = 0.0;
                    for(int k=0; k<3; k++)
                        sum += errors[j][k]*errors[j][k];
                    sum = Math.sqrt(sum);
                    System.out.println("Error magnitude: " + sum + "km\n");

                }

                //print benchmark data here
                System.out.println("Benchmark time: " + chrono[ chrono.length-1 ] + "ms");
                System.out.println("In sim time: " + Math.round( benchmark.getTime() ) + "s");
                System.out.println("Benchmark step size: " + benchmarkPrecision + "s");
                double sum =0.0;
                for(int k=0; k<chrono.length; k++){
                    sum += chrono[k];
                }

                System.out.println("Total run time of this interval: " + sum + "ms");
                System.out.println("\n");
            }


        }

    }

    // ############################################################################## EXPERIMENT SET UP

    /*
    Sun
    Mercury
    Venus
    Earth
    Moon
    Mars
    Jupiter
    Saturn
    Titan
    Neptune
    Uranus
     */

    public static void experimentSetUp(){
        // experiment setup hyper parameters
        double time = 360;
        boolean isDay = true;
        int checkInterval = 1; //every how many days do you want it to print the values

        // target body
        final int TARGET = 5; //5=mars
        final String DATA_ORIGIN = "NASA_Horizons";
        final String TARGET_FILE =  "ExpData/ExpMars.txt"; // "ExpData/ExpMars.txt"
        final String SUN = "ExpData/ExpSun.txt"; // "ExpData/ExpSun.txt"


        //  testing models
        ArrayList<Model3D> models = new ArrayList<Model3D>();
        ArrayList<Double> steps = new ArrayList<Double>();

        // models
        models.add( new Gravity0( 0, 0, new AB2(), DATA_ORIGIN ) ); //, DATA_ORIGIN
        steps.add( 0.1 );

        models.add( new Gravity0( 0, 0, new RK4(), DATA_ORIGIN ) ); //, DATA_ORIGIN
        steps.add( 1.0 );

        models.add( new Gravity0( 0, 0, new RalstonsRK4(), DATA_ORIGIN ) ); //, DATA_ORIGIN
        steps.add( 1.0 );

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
            benchmark = dataGetter.getTxtExpData(i+2, TARGET_FILE);

            //centering benchmark data with sun
            for(int j=0; j<benchmark.length; j++)
                for (int k=0; k<benchmark[j].length; k++)
                        benchmark[j][k] = benchmark[j][k];// - sunData[j][k];

            // update all models positions
            for (int j=0; j<models.size(); j++) {
                //start count of how much each model took here
                chrono[j] = System.currentTimeMillis();
                models.get(j).updatePos(1, steps.get(j), isDay); ////////////////// NO RUN
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
                    System.out.print( models.get(j).getSolverName() );
                    System.out.println("\t\t ::: Execution time: " + chrono[j] + "ms");
                    //System.out.println("Sim time: " + models.get(j).getTime() + "s");
                    System.out.println("step size: " + steps.get(j) + "s");

                    System.out.println("Model pos= X: " + models.get(j).getBody(TARGET).getPos()[0] + //delete after
                            "; Y: " + models.get(j).getBody(TARGET).getPos()[1] +
                            "; Z: " + models.get(j).getBody(TARGET).getPos()[2]);

                    //System.out.println("Error= X: " + errors[j][0] + "; Y: " + errors[j][1] + "; Z: " + errors[j][2]);
                    System.out.println("Error magnitude= " +  Math.sqrt(errors[j][0]*errors[j][0]
                            + errors[j][1]*errors[j][1] + errors[j][2]*errors[j][2]) + " km\n");


                }

                System.out.println("Target pos= X:" + benchmark[0][0] + "; Y: " + benchmark[0][1] + "; Z: " + benchmark[0][2] + "\n");

            }


        }


    }


    // ############################################################################## TRAJECTORY MAKING SET UP
    public static void trajectoryTesting(){
        // set up hyper parameters
        int time = 7; // max number of days for a sim to reach goal
        String target = "moon"; // the moon
        int numberOfStages = 3;
        double updatePeriod = 0.5; // period on which it shows the positions (in unit of days)

        // models
        ArrayList<Model3D> models = new ArrayList<Model3D>();
        ArrayList<Double> steps = new ArrayList<Double>();

        // to print how long the planning takes
        double chrono = 0.0;

        //where the rock starts
        double latitude = 0.0;
        double longitude = 0.0;

        // make trajectory
        chrono = System.currentTimeMillis();
        models.add( new Gravity0( longitude, latitude, new RK4(), target, numberOfStages, time, new PlanetaryRing() ) );
        chrono = System.currentTimeMillis() - chrono;
        steps.add( 1.0 );

        System.out.println("Planning took: " + chrono + "ms\n\n\n");


        double[] error = new double[] {0.0, 0.0, 0.0};
        double errorMagnitude = 0.0;

        System.out.println("Showing travel");

        // trajectory is already done, run sim and check time step
        for(int i=0; i<time/updatePeriod; i++){
            models.get(0).updatePos( updatePeriod, 1.0, true ); // every half a day
            CelestialBody targetBody = models.get(0).getShip().getTarget();

            System.out.println("Target: " + target);
            System.out.println("Time intervals passed: " + time);
            System.out.println("Time interval of: " + updatePeriod + " * Days");

            System.out.println("Sim time: " + models.get(0).getTime() + "s");

            System.out.println("Target position= X:" + targetBody.getPos()[0] +
                                "; Y:" + targetBody.getPos()[1] + "; Z:" + targetBody.getPos()[2]);

            System.out.println("Ship position= X:" + models.get(0).getShip().getPos()[0] +
                                "; Y:" + models.get(0).getShip().getPos()[1] +
                                "; Z:" + models.get(0).getShip().getPos()[2]);

            for(int j=0; j<error.length; j++)
                error[j] = targetBody.getPos()[j] - models.get(0).getShip().getPos()[j];
            errorMagnitude = Math.sqrt( error[0]*error[0] + error[1]*error[1] + error[2]*error[2] );

            System.out.println("Error= X:" + error[0] + "; Y:" + error[1] + "; Z:" + error[2]);
            System.out.println("Error magnitude: " + errorMagnitude + "km\n\n");




        }


    }



    // ############################################################################## NUM SOLVER ACCURACY CHECK SET UP
    public static void testingAccuracyOfSolvers() {
        //experiment setup hyper parameters
        double time = 30;
        boolean isDay = true;
        int checkInterval = 1; //every how many days do you want it to print the values
        final int TARGET = 7;

        // benchmark model

        /////////////////// make benchmark a precise function eval
        /// make it a function inside of the class itself


        //  testing models
        ArrayList<Model3D> models = new ArrayList<Model3D>();
        ArrayList<Double> steps = new ArrayList<Double>();

        // add a model with the solver and next the step size its used with it
        //models.add( new Gravity0( 0, Math.PI / 2.0, new Euler() ) );
        //steps.add( 0.1 );

        models.add( new TestModel1( new RK4() ) );
        steps.add( 0.1 );

        models.add( new TestModel1( new RK3() ) );
        steps.add( 0.1);

        models.add( new TestModel1( new RK2() ) );
        steps.add( 0.1 );

        models.add( new TestModel1( new Euler() ) );
        steps.add( 0.1 );

        models.add( new TestModel2( new Euler() ) );
        steps.add( 0.1 );

        models.add( new TestModel2( new RK4() ) );
        steps.add( 0.1 );

        models.add( new TestModel2( new RK3() ) );
        steps.add( 0.1 );

        models.add( new TestModel2( new RK2() ) );
        steps.add( 0.1 );


        double[][] errors = new double[models.size()][3];
        double[] chrono = new double[models.size()]; //last index is the benchmark

        // sim loop
        for (int i = 0; i < time; i++) {
            //benchmark
            // make benchmark just a precise eval of the method thats going to be in each class

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
                    System.out.println("Error= X: " + errors[j][0] + "; Y: " + errors[j][1] + "; Z: " + errors[j][2]);
                    double sum = 0.0;
                    for(int k=0; k<3; k++)
                        sum += errors[j][k]*errors[j][k];
                    sum = Math.sqrt(sum);
                    System.out.println("Error magnitude: " + sum + "km\n");

                }

                double sum = 0.0;
                for(int k=0; k<chrono.length; k++){
                    sum += chrono[k];
                }

                System.out.println("Total run time of this interval: " + sum + "ms");
                System.out.println("\n");
            }


        }

    }

}
