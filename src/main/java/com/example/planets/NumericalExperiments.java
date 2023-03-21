package com.example.planets;

import com.example.planets.BackEnd.CelestialBody;
import com.example.planets.BackEnd.Models.*;
import com.example.planets.BackEnd.NumericalMethods.*;

class Main {

    /*
     * a simple experiment set up to check how things work so far, before improvements
     */
    public static void main(String[] args) {
        testing3D();
        //
    }

    /*
     * hohmann for going from planet to planet
     */

    public static void testing3D(){
        Gravity0 grav = new Gravity0();

        //to save space
        {
            CelestialBody[] bodies = new CelestialBody[ positions.length ];
            for(int i=0; i<bodies.length; i++)
                bodies[i] = new CelestialBody(mass[i][0], positions[i], velocity[i]);
            
            grav.addBody(bodies);
        }

        ////////////////////////////// parameters
        double dt = 0.1;
        long days = 5;
        int planet = 3;
        //earth is index 3

        //with any dt days/execution time ratio remains mostly constant
        //time to execution time ratio is approx 1.6666 for dt=0.1
        
        System.out.println("\nstart");
        System.out.println("x: " + grav.getBody(planet).getPos()[0] + ", y: " + grav.getBody(planet).getPos()[1] + ", z: " + grav.getBody(planet).getPos()[2]);
        System.out.println( "theta: " + (180/3.1415)*Math.atan( grav.getBody(planet).getPos()[1]/grav.getBody(planet).getPos()[0] ) );
        System.out.println("r: " + CelestialBody.getDistance( grav.getBody(0), grav.getBody(planet) ));

        
        long start = System.currentTimeMillis();
        
        System.out.println("\ndays: " + days + "; dt: " + dt);
        for(long i=0; i<CelestialBody.daysToSec(days)/dt; i++){
            Eulers.step3D(grav, dt);
            //System.out.println("time: " +  CelestialBody.secToDays( (long)(i*dt) ));
        }

        System.out.println( "\ntime taken[s]: " + (System.currentTimeMillis()-start)/1000 );

        System.out.println("x: " + grav.getBody(planet).getPos()[0] + ", y: " + grav.getBody(planet).getPos()[1] + ", z: " + grav.getBody(planet).getPos()[2]);
        System.out.println( "theta: " + (180/3.1415)*Math.atan( grav.getBody(planet).getPos()[1]/grav.getBody(planet).getPos()[0] ) );
        System.out.println("r: " + CelestialBody.getDistance( grav.getBody(0), grav.getBody(planet) ));

        System.out.println();
        
        
    }



    public static void testing1D(){
        Model1D simple = new simple2(0, 8, 1);

        int size = 1000;
        double dx = 0.001;
        
        double[][] errors = new double[2][size];

        //test
        System.out.println("approx");
        for(int i=0; i<size; i++){
            errors[0][i] = Eulers._2DegStep1D(simple, dx);
            System.out.println(simple.getX() + ", " + errors[0][i]);
        }
        System.out.println("\n");


        //solution for test
        System.out.println("solution: y(x) = 3*e^t-2"); 
        //found with walfram alpha
        // https://www.wolframalpha.com/input?i=y%27+%3D+2+%2B+y%2C+y%280%29+%3D+1

        double x = 0;
        for(int i=0; i<size; i++){
            x+=dx;
            errors[1][i] = 7 * Math.exp(x) - 6;
            //System.out.println( x + ", " + errors[1][i] );
        }
        System.out.println("\n");


        //error finding
        x = 0;
        System.out.println("Errors");
        for(int i=0; i<size; i++){
            //System.out.println( x + ", " + (errors[0][i] - errors[1][i])/errors[1][i] );
            x+=dx;
        }
    }


    /*
     * write names of planets
     * write their radiuses as well
     */

    private static double[][] positions = { { 0, 0, 0 }, { 7.83e6, 4.49e7, 2.87e6 }, { -2.82e7, 1.04e8, 3.01e6 },
			{ -1.48e8, -2.78e7, 3.37e4 }, { -1.48e8, -2.75e7, 7.02e4 }, { -1.59e8, 1.89e8, 7.87e6 },
			{ 6.93e8, 2.59e8, -1.66e7 }, { 1.25e9, -7.60e8, -3.67e7 }, { 1.25e9, 7.61e8, -3.63e7 },
			{ 4.45e9, -3.98e8, -9.45e7 }, { 1.96e9, 2.19e9, -1.72e7 } };
            
	private static double[][] velocity = { { 0, 0, 0 }, { -5.75e1, 1.15e1, 6.22e0 }, { -3.40e1, -8.97e0, 1.84e0 },
			{ 5.05e0, -2.94e1, 1.71e-3 }, { 4.34e0, -3.00e1, -1.16e-2 }, { -1.77e1, -1.35e1, 1.52e-1 },
			{ -4.71e0, 1.29e1, 5.22e-2 }, { 4.47e0, 8.24e0, -3.21e-1 }, { 9.00e0, 1.11e1, -2.25e0 },
			{ 4.48e-1, 5.45e0, -1.23e-1 }, { -5.13e0, 4.22e0, 8.21e-2 } };

	private static double[][] mass = { { 1.99e30 }, { 3.30e23 }, { 4.87e24 }, { 5.97e24 }, { 7.35e22 }, { 6.42e23 },
			{ 1.90e27 }, { 5.68e26 }, { 1.35e23 }, { 1.02e26 }, { 8.68e25 } };

    private static double[][] radius = null;

}
