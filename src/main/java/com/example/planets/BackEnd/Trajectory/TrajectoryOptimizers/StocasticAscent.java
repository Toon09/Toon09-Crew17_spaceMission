package com.example.planets.BackEnd.Trajectory.TrajectoryOptimizers;

import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.NumericalMethods.RK4;

import java.util.Arrays;


/*
change cost function so that it takes closest dist and actual dist as input
 */
public class StocasticAscent implements TrajectoryPlanner {

    private final int numbOfSteps = 1;
    private final int numbOfStages;
    private final int numbOfDays;
    private final Model3D model;
    private String target;

    private double[][] trajectory;

    public StocasticAscent(Model3D model, int numbOfStages, String target, int numbOfDays){
        this.model = model.clone();
        this.numbOfStages = numbOfStages;
        this.numbOfDays = numbOfDays;
        this.target = target;


    }

    /*
    save best cost from prev iteration and compare to current
        if its waaay to similar (1 significant figure the same)
            make interval a bt more precise and increase amount of individuals
     dont do by a lot
     */
    private void makeTrajectory(){

        double prevBest = 0.0;

        // hyper parameters
        int individuals = 50;
        double rangeOfChange = Spaceship.getMaxSpeed(); // maximun range of change

        double[][] state = new double[numbOfStages][5];

        state[0][0] = 0.0;
        state[0][1] = 30*60.0;

        for(int i=1; i<numbOfStages; i++){
            state[i][0] = i*numbOfDays*24*60*60 / ((double) numbOfStages) - numbOfDays*24*60*60 * 0.15;
            state[i][1] = 60*60 +Math.random()*30*60;

        }

        System.out.println("numb of generations: " + numbOfSteps + ", numb of individuals: " + individuals + "\n");

        System.out.println("generation, used fuel, closest distance, cost val, time[ns]");

        double chrono = 0.0;

        for(int count=0; count<numbOfSteps; count++){

            double delta = System.nanoTime();

            // clone of initial condition
            Model3D optimizer = model.clone( new RK4() );

            // gives planning to ship
            optimizer.getShip().setPlan(state);

            // set target planet
            for(int i=0; i<model.size(); i++)
                if( optimizer.getBody(i).getName().equalsIgnoreCase(target) )
                    optimizer.getShip().setTarget( model.getBody(i) );

            optimizer.addShips( individuals ); // you are not using number of stages bruh

            // set states
            // in each stage go + and - each parameter
            for(int i=0; i < optimizer.getAmountOfShips()-1; i++){
                double[][] temp = new double[numbOfStages][5];
                for(int j=0; j<numbOfStages; j++){
                    for(int k=0; k<5; k++){
                        temp[j][k] = state[j][k];
                    }
                }

                // gets new random parameters based off best past parameters
                // with a velocity limited by the ships max velocity
                temp = generateRandParams(rangeOfChange, temp);

                optimizer.getShip(i).setPlan( temp );

            }

            // run sim
            optimizer.updatePos(numbOfDays, 500.0, true);

            Spaceship champion = optimizer.getShip();

            // get best plan made
            double cost = optimizer.getShip().getCost(); //
            //System.out.println("in loop");
            for(int i=0; i<optimizer.getAmountOfShips(); i++){ //

                if( cost < optimizer.getShip(i).getCost() ){ //
                    System.out.println("id: " + i);
                    System.out.println("plan: " + Arrays.deepToString(optimizer.getShip(i).getPlan()));
                    System.out.println("closest dist: " + optimizer.getShip(i).getClosestDistance());
                    System.out.println("cost: " + optimizer.getShip(i).getCost());

                    cost = optimizer.getShip(i).getCost(); //
                    state = optimizer.getShip(i).getPlan(); // gets plan with highest cost //
                    champion = optimizer.getShip(i); //
                }

            }

            delta = System.nanoTime() - delta;
            chrono += delta;

            // changes to hyper parameters
            if( count != 0 && champion.getCost()/prevBest > 0.85 ){ // the improvement is not big enough
                if( rangeOfChange > 5.0 )
                    rangeOfChange -= rangeOfChange*0.05; // reduced by 5%
                else
                    rangeOfChange -= rangeOfChange*0.01; // reduced by 1%

                //individuals += 2; //maybe not good
            }

            System.out.println( (count+1) + ", " + champion.getUsedFuel() + ", " + champion.getClosestDistance() + ", " + champion.getCost() + ", " + chrono);
            System.out.println("final dist to target: " + champion.getDistance( champion.getTarget() ) );
            System.out.println("range of change: " + rangeOfChange);
            System.out.println("\n\n\n");


            prevBest = champion.getCost(); // best cost

        } ///

        trajectory = state;

    }


    /**
     *
     * @param rangeOfChange maximun change that can be applied on every direction
     * @param init
     * @return a new set of parameters which all do not violate the maximun speed set static in the Spaceship class
     */
    private double[][] generateRandParams(double rangeOfChange, double[][] init){
        double[][] result = init.clone();

        double maxSpeed = 0.0;
        maxSpeed = Spaceship.getMaxSpeed(); // max magnitude

        for(int j = 0; j< result.length; j++){ // thru each dim
            for(int k=2; k<5; k++){
                // calculate range in which we can add a random val without violating maximun speed
                double range = maxSpeed*maxSpeed;

                for (int i=2; i<5; i++)
                    if(i != k)
                        range -= result[j][i]*result[j][i];

                range = Math.sqrt(range) - Math.abs(result[j][k]); // error where range < 0 thus sqrt(-1)

                if( range > rangeOfChange ) // if the change thats going to be done is smaller than the max admitted change
                    range = rangeOfChange;

                result[j][k] += range*Math.random() - range/2.0; // random + or - in range
            }

            // change thrust times
            if(j!=0) // always accelerates at start
                result[j][0] += 30*24*60.0*60.0*Math.random() - 30*24*60*60.0/2.0; // changes in initial thrust times

            result[j][1] += 15.0*60.0; // changes in thrust length by 15 mins // *Math.random()-15*60.0/2.0
            if( result[j][1] < 0.0 ) // avoids negative thrust
                result[j][1] = 0.0;
        }

        return result;
    }


    @Override
    public double[][] getTrajectory() {
        if( trajectory == null )
            makeTrajectory();

        return trajectory;
    }


}
