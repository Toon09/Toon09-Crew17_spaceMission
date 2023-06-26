package com.example.planets;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.*;
import com.example.planets.BackEnd.NumericalMethods.*;
import com.example.planets.BackEnd.Trajectory.Cost.MinDistAndFuel;
import com.example.planets.Data.DataGetter;

import java.util.ArrayList;


class NumericalExperiments {

    /*
    fix trajectory
        - actual values dont match with simualted ones
     */

    // https://ssd.jpl.nasa.gov/horizons/app.html#/ [ experiment data ]
    public static void main(String[] args) {
        //engineTest();

        //comparingToEachOther();

        comparingToNasaData();

        //trajectoryTesting();

        //testingAccuracyOfSolvers();

    }


    public static void engineTest() {

        // write and launch 2 ships
        // 1 with engine 1 without & check difference
        double time = 30;
        boolean isDay = true;
        int checkInterval = 1; //every how many days do you want it to print the values

        // benchmark model
        Model3D model = new Gravity0(0, 0, new RK4());
        double dt = 1.5;

        model.addShips(1); //ship that will be unchanged

        double[][] plan = new double[][] {{0,11,0,0},
                {3*24*60*60, 2,2,2}};

        model.getShip().setPlan( plan );

        System.out.println("initializing test: \nIn days?: " + isDay + "\nTime interval: "+checkInterval+"s or day\n\n");

        double[] pos1 = new double[3];
        double[] pos2 = new double[3];

        for(int i=0; i<time; i++){
            Spaceship a = model.getShip(0);
            Spaceship b = model.getShip(1);

            model.updatePos(1.0, dt, isDay );

            if( (i+1)%checkInterval == 0 ){
                System.out.println("time stamp: " + (i+1));

                pos1 = model.getShip(1).getPos();
                pos2 = model.getShip(0).getPos(); // check if

                System.out.println("Ship with engine plan:");
                System.out.println("X: " + pos1[0] + "; Y: " + pos1[1] + "; Z: " + pos1[2]);
                System.out.println("Ship with NO engine plan:");
                System.out.println("X: " + pos2[0] + "; Y: " + pos2[1] + "; Z: " + pos2[2] + "\n");

                System.out.println("Difference of both: ");
                System.out.println("X: " + (pos1[0]-pos2[0]) + "; Y: " + (pos1[1]-pos2[1]) + "; Z: " + (pos1[2]-pos2[2]));

                System.out.println("fuel 0: " + model.getShip(0).getUsedFuel());
                System.out.println("fuel 1: " + model.getShip(1).getUsedFuel());

                System.out.println("\n\n");
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

    /* this is the order in which the planets are stored inside our simulation
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
        double time = 20;
        boolean isDay = false;
        int checkInterval = 1; //every how many days do you want it to print the values

        // target body
        final int TARGET = 5; //5=mars
        final String DATA_ORIGIN = "NASA_Horizons";
        final String TARGET_FILE =  "ExpData/ExpMars.txt"; // "ExpData/ExpMars.txt"
        final String SUN = "ExpData/ExpSun.txt"; // "ExpData/ExpSun.txt"

        double dt = 0.1;

        //  testing models
        ArrayList<Model3D> models = new ArrayList<Model3D>();

        // models
        models.add( new Gravity0( 0, 0, new Euler(), DATA_ORIGIN ) );
        models.add( new Gravity0( 0, 0, new LeapFrog(), DATA_ORIGIN ) );
        models.add( new Gravity0( 0, 0, new AB2(), DATA_ORIGIN ) );
        models.add( new Gravity0( 0, 0, new RK2(), DATA_ORIGIN ) );
        models.add( new Gravity0( 0, 0, new HeunsRK3(), DATA_ORIGIN ) );
        models.add( new Gravity0( 0, 0, new RK4(), DATA_ORIGIN ) );
        models.add( new Gravity0( 0, 0, new RK8(), DATA_ORIGIN ) );
        models.add( new Gravity0( 0, 0, new ode45(), DATA_ORIGIN ) );


        //benchmark
        DataGetter dataGetter = new DataGetter();
        double[][] benchmark = new double[2][3];
        double[][] sunData = new double[2][3];

        // model data
        double[][] errors = new double[models.size()][3];
        double[] chrono = new double[models.size()+1]; //last index is the benchmark

        // innit position:
        System.out.println();

        //System.out.println("time step[s], Euler abs, Euler relative, Euler time[ns], AB2 abs, AB2 relative, AB2 time[ns], Ralston's RK4 abs, Ralston's RK4 relative, Ralston's RK4 time[ns]");

        errors = new double[models.size()][3]; // 0 is absolute and 1 is relative error
        //sol = 0.0;
        chrono = new double[models.size()]; //last index is the benchmark

        //System.out.println("sim time[s], " + time + "\nadpad. precision., " + precision);

        System.out.print("sim time[s], time step[s]");

        for(int i=0; i<models.size(); i++){
            System.out.print(", " + models.get(i).getSolverName() + " log(relative), "  + models.get(i).getSolverName() + " log(time[ns])");
        }
        System.out.print("\n");

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
                models.get(j).updatePos(6*60*60, dt, isDay); ////////////////// NO RUN
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

                    System.out.print(", " + Math.log10(errorMag/presMag) + ", " + Math.log10(chrono[j]));

                }

                //System.out.println("Target pos= X:" + benchmark[0][0] + "; Y: " + benchmark[0][1] + "; Z: " + benchmark[0][2] + "\n");
                System.out.println();
            }


        }


    }


    // ############################################################################## TRAJECTORY MAKING SET UP
    public static void trajectoryTesting(){
        // set up hyper parameters
        int time = 364; // max number of days for a sim to reach goal
        String target = "titan";
        int numberOfStages = 4;
        double updatePeriod = 1.0; // period on which it shows the positions (in unit of days)

        //where the rock starts
        double latitude = 0.0;
        double longitude = 0.0;

        // make trajectory
        double chrono = System.currentTimeMillis();
        Model3D model = new Gravity0( longitude, latitude, new RK4(), target, numberOfStages, time, new MinDistAndFuel() );
        chrono = System.currentTimeMillis() - chrono;

        System.out.println("Planning took: " + chrono + "ms\n\n\n");

        double[] error = new double[] {0.0, 0.0, 0.0};
        double errorMagnitude = 0.0;


        System.out.println("Showing travel");

        // trajectory is already done, run sim and check time step
        for(int i=0; i<time/updatePeriod; i++){
            model.updatePos( updatePeriod, 1.5, true ); // every half a day
            CelestialBody targetBody = model.getShip().getTarget();

            //System.out.println("Target: " + target);
            System.out.println("Time passed: " + (i+1)*updatePeriod + " Days");

            System.out.println("Sim time: " + model.getTime() + "s");

            //System.out.println("Target position= X:" + targetBody.getPos()[0] +
            //        "; Y:" + targetBody.getPos()[1] + "; Z:" + targetBody.getPos()[2]);

            System.out.println("Ship position= X:" + model.getShip().getPos()[0] +
                    "; Y:" + model.getShip().getPos()[1] + "; Z:" + model.getShip().getPos()[2]);

            for(int j=0; j<error.length; j++)
                error[j] = targetBody.getPos()[j] - model.getShip().getPos()[j];
            errorMagnitude = Math.sqrt( error[0]*error[0] + error[1]*error[1] + error[2]*error[2] );

            // System.out.println("Error= X:" + error[0] + "; Y:" + error[1] + "; Z:" + error[2]);
            System.out.println("Error: " + errorMagnitude + "km\n\n");
            //System.out.println("Closest dist: " + model.getShip().getClosestDistance() + "km");
            //System.out.println("trget from ship: " + model.getShip().getTarget() + "\n\n");

        }


    }


    /// https://www.osti.gov/servlets/purl/6111421

    // http://faculty.olin.edu/bstorey/Notes/DiffEq.pdf more promising

    // ############################################################################## NUM SOLVER ACCURACY CHECK SET UP
    public static void testingAccuracyOfSolvers() {
        //experiment setup hyper parameters
        double time = 30000; //30000
        boolean isDay = false;

        ArrayList<Double> steps = new ArrayList<Double>();

        // all the step sizes that are going to be tested
        steps.add(5.0);
        steps.add(4.0);
        steps.add(3.0);
        steps.add(2.0);
        steps.add(1.5);
        steps.add(1.0);
        steps.add(0.75);
        steps.add(0.5);
        steps.add(0.25);
        steps.add(0.1);
        steps.add(0.05);
        steps.add(0.025);
        steps.add(0.01);
        steps.add(0.005);
        steps.add(0.001);

        double[][] errors = new double[0][0];
        double sol;
        double[] chrono = new double[0];


        for(int k=0; k< steps.size(); k++){

            //  testing models
            ArrayList<Model3D> models = new ArrayList<Model3D>();

            models.add( new TestModel3( new Euler() ) );
            models.add( new TestModel3( new LeapFrog() ) );

            models.add( new TestModel3( new AB2() ) );
            models.add( new TestModel3( new AB3() ) );
            models.add( new TestModel3( new AB4() ) );
            models.add( new TestModel3( new AB5() ) );

            models.add( new TestModel3( new RK2() ) );
            models.add( new TestModel3( new HeunsRK3() ) );
            models.add( new TestModel3( new RK4() ) );
            models.add( new TestModel3( new RalstonsRK4() ) );
            models.add( new TestModel3( new ButchersRK5() ) );
            models.add( new TestModel3( new RK6() ) );
            models.add( new TestModel3( new RK7() ) );
            models.add( new TestModel3( new RK8() ) );

            double precision = 0.01;
            models.add( new TestModel3( new ode23(precision) ) );
            models.add( new TestModel3( new ode45(precision) ) );
            models.add( new TestModel3( new ode78(precision) ) );
            //models.add( new TestModel3( new DormantPrince(precision) ) );

            if(k==0){
                errors = new double[models.size()][2]; // 0 is absolute and 1 is relative error
                sol = 0.0;
                chrono = new double[models.size()]; //last index is the benchmark

                System.out.println("sim time[s], " + time + "\nadpad. precision., " + precision);

                System.out.print("\ntime step[s]");

                for(int i=0; i<models.size(); i++){
                    System.out.print(", " + models.get(i).getSolverName() + " log(relative), "  + models.get(i).getSolverName() + " log(time[ns])");
                }
                System.out.print("\n");
            }


            // update all models positions
            for (int j=0; j<models.size(); j++) {
                //start count of how much each model took here
                double delta = System.nanoTime();
                models.get(j).updatePos(time, steps.get(k), isDay);
                //end count of how long each model here
                delta = System.nanoTime() - delta;

                chrono[j] += delta;

            }

            for (int j = 0; j < models.size(); j++) {
                if( models.get(j) instanceof TestModel1 ){
                    sol = ( (TestModel1)models.get(j) ).getActualValue(models.get(j).getTime());
                    errors[j][0] = Math.abs( sol - models.get(j).getBody(0).getPos()[0] );
                    errors[j][1] = errors[j][0] / Math.abs(sol);
                    //errors[j] = models.get(j).getBody(0).getPos()[0];

                }
                if( models.get(j) instanceof TestModel2 ){
                    sol = ( (TestModel2)models.get(j) ).getActualValue(models.get(j).getTime());
                    errors[j][0] = Math.abs( sol - models.get(j).getBody(0).getPos()[0] );
                    errors[j][1] = errors[j][0] / Math.abs(sol);
                    //errors[j] = models.get(j).getBody(0).getPos()[0];

                }

                if( models.get(j) instanceof TestModel3 ){
                    sol = ( (TestModel3)models.get(j) ).getActualValue(models.get(j).getTime());
                    errors[j][0] = Math.abs( sol - models.get(j).getBody(0).getPos()[0] );
                    errors[j][1] = errors[j][0] / Math.abs(sol);
                    //errors[j] = models.get(j).getBody(0).getPos()[0];
                }
            }

            System.out.print(Math.log10(steps.get(k)));
            for (int j = 0; j < models.size(); j++) {
                System.out.print(", " + Math.log10(errors[j][1]) + ", " + Math.log10(chrono[j])); //  + ", " + errors[j][1] +  ", " + chrono[j]


            }
            System.out.println();//


        }

    }




}
