package com.example.planets;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.Models.*;
import com.example.planets.BackEnd.NumericalMethods.*;
import com.example.planets.BackEnd.Trajectory.Cost.PlanetaryRing;
import com.example.planets.Data.DataGetter;

import java.util.ArrayList;


class NumericalExperiments {
    /*
    - do trajectory
    - do test of engine
    - do experiment set up
     */

    /*
    ToDo
    + change the trajectory from ArrayList<double[]> to double[][]\
    + ENGINE
    + TRAJECTORY: https://www.sciencedirect.com/science/article/pii/S037604211830191X
        hill climbXX
        gradient descend
        genetic alg

    + make setup to test swarm step

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
        //engineTest();

        //comparingToEachOther();

        //comparingToNasaData();

        trajectoryTesting();

        //testingAccuracyOfSolvers();

    }

    public Model3D model;


    public static void engineTest() {

        // write and launch 2 ships
        // 1 with engine 1 without & check difference
        double time = 30;
        boolean isDay = true;
        int checkInterval = 1; //every how many days do you want it to print the values
        final int TARGET = 7;

        // benchmark model
        Model3D model = new Gravity0(0, 0, new RK4());
        double dt = 0.5;

        model.addShips(3); //ship that will be unchanged

        double[][] plan = new double[][] {{0,10*60,
                    11,0,0},
                {3*24*60*60, 3*24*60*60 + 30*60,
                        2,2,2}};

        model.getShip().setPlan( plan );

        plan[0][2] = 35.0;
        model.getShip(1).setPlan( plan );

        System.out.println("initializing test: \nIn days?: " + isDay + "\nTime interval: "+checkInterval+"s or day\n\n");

        double[][] pos = new double[model.getAmountOfShips()][3];

        for(int i=0; i<time; i++){
            model.updatePos(1.0, dt, isDay );

            if( (i+1)%checkInterval == 0 ){
                System.out.println("time stamp: " + (i+1));

                for(int j=0; j<model.getAmountOfShips(); j++){
                    pos[j] = model.getShip(j).getPos();
                }

                System.out.println("Sips: ");
                for(int k=0; k<model.getAmountOfShips(); k++){
                    System.out.println("Ship: " + k);
                    System.out.println("X: " + pos[k][0] + "; Y: " + pos[k][1] + "; Z: " + pos[k][2]);
                    System.out.println("acc x: " + model.getShip(k).getAcc()[0]+"\n");
                }


                System.out.println("\nfuel's:");
                for(int j=0; j<model.getAmountOfShips(); j++){
                    System.out.println("fuel " + j + ": " + model.getShip(j).getUsedFuel() );
                }

                System.out.println("");
            }


        }

    }

    // ############################################################################## COMPARING SOLVER SET UP

    /*
    in RK4
    1.5 to 1.6 saves 7 seconds of real time execution on a whole year in sim (360 days)
    1.0 to 1.5 saves 180 seconds of real time execution on a whole year in sim (360 days)
     */
    public static void comparingToEachOther() {
        //experiment setup hyper parameters
        double time = 2*8;
        boolean isDay = true;
        int checkInterval = 1; //every how many days do you want it to print the values
        final int TARGET = 7;

        // benchmark model
        Model3D benchmark = new Gravity0( 0, Math.PI / 2.0, new AB2() );
        double benchmarkPrecision = 1.6;


        //  testing models
        ArrayList<Model3D> models = new ArrayList<Model3D>();
        ArrayList<Double> steps = new ArrayList<Double>();

        // add a model with the solver and next the step size its used with it
        //models.add( new Gravity0( 0, Math.PI / 2.0, new Euler() ) );
        //steps.add( 0.1 );

        models.add( new Gravity0( 0, 0, new LeapFrog() ) );
        steps.add( 0.1 );

        models.add( new Gravity0( 0, 0, new Euler() ) );
        steps.add( 0.1 );

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

    // ############################################################################## NASA DATA COMPARISON

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

    public static void comparingToNasaData(){
        // experiment setup hyper parameters
        double time = 1464.0;
        boolean isDay = false;
        int checkInterval = 1; //every how many days do you want it to print the values

        // target body
        final int TARGET = 5; //5=mars
        final String DATA_ORIGIN = "NASA_Horizons";
        final String TARGET_FILE =  "ExpData/ExpMars.txt"; // "ExpData/ExpMars.txt"
        final String SUN = "ExpData/ExpSun.txt"; // "ExpData/ExpSun.txt"

        double dt = 100.0;

        //  testing models
        ArrayList<Model3D> models = new ArrayList<Model3D>();
        ArrayList<Double> steps = new ArrayList<Double>();

        // models
        models.add( new Gravity0( 0, 0, new Euler(), DATA_ORIGIN ) ); //, DATA_ORIGIN
        steps.add( dt );

        models.add( new Gravity0( 0, 0, new AB2(), DATA_ORIGIN ) ); //, DATA_ORIGIN
        steps.add( dt );

        models.add( new Gravity0( 0, 0, new LeapFrog(), DATA_ORIGIN ) ); //, DATA_ORIGIN
        steps.add( dt );

        models.add( new Gravity0( 0, 0, new RK4(), DATA_ORIGIN ) ); //, DATA_ORIGIN
        steps.add( dt );

        //benchmark
        DataGetter dataGetter = new DataGetter();
        double[][] benchmark = new double[2][3];
        double[][] sunData = new double[2][3];

        // model data
        double[][] errors = new double[models.size()][3];
        double[] chrono = new double[models.size()+1]; //last index is the benchmark

        // innit position:
        System.out.println(); //////// comments

        System.out.println("sim time[s], time step[s], Euler abs, Euler relative, Euler time[ns], AB2 abs, AB2 relative, AB2 time[ns], LeapFrog abs, LeapFrog relative, LeapFrog time[ns], RK4 abs, RK4 relative, RK4 time[ns]");

        // main loop
        for (int i = 0; i < time; i++) {
            // getting benchmark data
            benchmark = dataGetter.getTxtExpData(i+1, TARGET_FILE);

            //centering benchmark data with sun
            for(int j=0; j<benchmark.length; j++)
                for (int k=0; k<benchmark[j].length; k++)
                        benchmark[j][k] = benchmark[j][k];// - sunData[j][k];

            // update all models positions
            for (int j=0; j<models.size(); j++) {
                //start count of how much each model took here
                double delta = System.nanoTime();
                models.get(j).updatePos(6*60*60, steps.get(j), isDay); ////////////////// NO RUN
                //end count of how long each model here
                delta = System.nanoTime() - delta;
                chrono[j] += delta;
            }


            //calculate errors
            for (int j = 0; j < models.size(); j++) {
                errors[j][0] = benchmark[0][0] - models.get(j).getBody(TARGET).getPos()[0];
                errors[j][1] = benchmark[0][1] - models.get(j).getBody(TARGET).getPos()[1];
                errors[j][2] = benchmark[0][2] - models.get(j).getBody(TARGET).getPos()[2];
            }


            //prints
            if ((i + 1) % checkInterval == 0) {

                System.out.print( Math.round( models.get(0).getTime() ) + ", " + dt );

                for (int j = 0; j < models.size(); j++) {
                    //System.out.print( models.get(j).getSolverName() );
                    //System.out.println("Sim time: " + models.get(j).getTime() + "s");

                    //System.out.println("Model pos= X: " + models.get(j).getBody(TARGET).getPos()[0] + "; Y: " + models.get(j).getBody(TARGET).getPos()[1] + "; Z: " + models.get(j).getBody(TARGET).getPos()[2]);

                    double errorMag = Math.sqrt(errors[j][0]*errors[j][0]
                            + errors[j][1]*errors[j][1] + errors[j][2]*errors[j][2]);

                    double presMag = Math.sqrt(benchmark[0][0]*benchmark[0][0] +
                            + benchmark[0][1]*benchmark[0][1] + benchmark[0][2]*benchmark[0][2]);

                    //System.out.println("Error= X: " + errors[j][0] + "; Y: " + errors[j][1] + "; Z: " + errors[j][2]);
                    //System.out.println("Error magnitude= " +  errorMag + " km");
                    //System.out.println("relative error: " + (errorMag)/presMag + "\n");

                    System.out.print(", " + errorMag + ", " + (errorMag/presMag) + ", " + chrono[j]);

                }

                //System.out.println("Target pos= X:" + benchmark[0][0] + "; Y: " + benchmark[0][1] + "; Z: " + benchmark[0][2] + "\n");
                System.out.println();
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
        double time = 30000; //30000
        boolean isDay = false;
        int checkInterval = 30; // 30

        /* time steps
        2.5
        1.5
        1.0
        0.5
        0.1
        0.01
        0.005
        0.001
         */
        double dt = 1.0;


        //  testing models
        ArrayList<Model3D> models = new ArrayList<Model3D>();
        ArrayList<Double> steps = new ArrayList<Double>();

        //////// test models
        models.add( new TestModel1( new Euler() ) );
        steps.add( dt );

        models.add( new TestModel1( new LeapFrog() ) );
        steps.add( dt );

        models.add( new TestModel1( new AB2() ) );
        steps.add( dt );

        models.add( new TestModel1( new RK2() ) );
        steps.add( dt );

        models.add( new TestModel1( new HeunsRK3() ) );
        steps.add( dt );

        models.add( new TestModel1( new RK4() ) );
        steps.add( dt );

        models.add( new TestModel1( new RalstonsRK4() ) );
        steps.add( dt );


        double[][] errors = new double[models.size()][2]; // 0 is absolute and 1 is relative error
        double sol = 0.0;
        double[] chrono = new double[models.size()]; //last index is the benchmark

        System.out.println("sim time[s], time step[s], Euler abs, Euler relative, Euler time[ns], LeapFrog abs, LeapFrog relative, LeapFrog time[ns], AB2 abs, AB2 relative, AB2 time[ns] , Ralston's RK2 abs, Ralston's RK2 relative, Ralston's RK2 time[ns], Rk3 abs, Rk3 relative, RK3 time[ns], Classical RK4 abs, Classical RK4 relative, Classical RK4 time[ns], Ralston's Rk4 abs, Ralston's Rk4 realtive, Ralston's Rk4 time[ns]");

        // sim loop
        for (int i = 0; i < time; i++) {
            //benchmark
            // make benchmark just a precise eval of the method thats going to be in each class

            // update all models positions
            for (int j=0; j<models.size(); j++) {
                //start count of how much each model took here
                double delta = System.nanoTime();
                models.get(j).updatePos(Math.max(1.0, dt), steps.get(j), false);
                //end count of how long each model here
                delta = System.nanoTime() - delta;

                chrono[j] += delta;
            }


            //calculate errors
            for (int j = 0; j < models.size(); j++) {
                if( models.get(j) instanceof TestModel1 ){
                    sol = ( (TestModel1)models.get(j) ).getActualValue(models.get(j).getTime());
                    errors[j][0] = ( sol - models.get(j).getBody(0).getPos()[0] );
                    errors[j][0] = Math.abs(errors[j][0]);
                    errors[j][1] = errors[j][0] / Math.abs(sol);
                    //errors[j] = models.get(j).getBody(0).getPos()[0];

                }
                if( models.get(j) instanceof TestModel2 ){
                    sol = ( (TestModel2)models.get(j) ).getActualValue(models.get(j).getTime());
                    errors[j][0] = ( sol - models.get(j).getBody(0).getPos()[0] );
                    errors[j][0] = Math.abs(errors[j][0]);
                    errors[j][1] = errors[j][0] / Math.abs(sol);
                    //errors[j] = models.get(j).getBody(0).getPos()[0];

                }

                if( models.get(j) instanceof TestModel3 ){
                    sol = ( (TestModel3)models.get(j) ).getActualValue(models.get(j).getTime());
                    errors[j][0] = ( sol - models.get(j).getBody(0).getPos()[0] );
                    errors[j][0] = Math.abs(errors[j][0]);
                    errors[j][1] = errors[j][0] / Math.abs(sol);
                    //errors[j] = models.get(j).getBody(0).getPos()[0];
                }
            }


            //prints
            if ((i + 1) % checkInterval == 0) {

                System.out.print((i+1) + ", " + dt);
                for (int j = 0; j < models.size(); j++) {
                    System.out.print(", " + errors[j][0] + ", " + errors[j][1] +  ", " + chrono[j]);

                }
                System.out.println();


            }


        }

    }

}
