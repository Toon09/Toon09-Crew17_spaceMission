package com.example.planets.BackEnd.CelestialEntities;

import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.Trajectory.Cost.CostFunction;
import com.example.planets.BackEnd.Trajectory.Cost.PlanetaryRing;
import com.example.planets.BackEnd.Trajectory.Planning;

import java.util.ArrayList;

/*
add methods related to changing trajectory to another class that has an instance in this one
and will keep things simpler

could apply decorator for trying different types of engines and debug quick
 */

public class Spaceship extends CelestialBody {
    private double usedFuel;
    //has all the information of where to go and such
    Planning plan;
    CostFunction costFunc = new PlanetaryRing();
    double cost=0.0;
    private final double maxSpeed = 11000; //2,500 to 4,500 m/s (look up) according to falcon 9
    private final double accRate = 10;
    private  final double maxForce = 3 * Math.pow(10, 7); // Newtons
    private final double fuelConsumption = 1451.5; //kg this fuel consumption is based on the falcon 9 maximum fuel consumption, so at max acceleration the consumption is this one.
    public double getUsedFuel(){ return usedFuel; }
    // public void setFuel(double fuel){ this.fuel = fuel; }

    /**
     *
     * @param mass
     * @param pos
     * @param vel
     * @param longitude
     * @param latitude
     * @param costFunc
     */
    public Spaceship(double mass, double[] pos, double[] vel, double longitude, double latitude, CostFunction costFunc){
        super(mass, pos, vel);
        usedFuel = 0;
        this.costFunc = costFunc;
        //positions
        double x = Gravity0.radiuses[3] * Math.cos(longitude) * Math.cos(latitude);
        double y = Gravity0.radiuses[3] * Math.sin(longitude) * Math.cos(latitude);
        double z = Gravity0.radiuses[3] * Math.sin(latitude);
        //add to its position
        this.addPos( new double[] { x, y, z } );
    }


    /**
     *
     * @param mass
     * @param pos
     * @param vel
     * @param longitude
     * @param latitude
     */
    public Spaceship(double mass, double[] pos, double[] vel, double longitude, double latitude){
        super(mass, pos, vel);
        usedFuel = 0;
        //positions
        double x = Gravity0.radiuses[3] * Math.cos(longitude) * Math.cos(latitude);
        double y = Gravity0.radiuses[3] * Math.sin(longitude) * Math.cos(latitude);
        double z = Gravity0.radiuses[3] * Math.sin(latitude);
        //add to its position
        this.addPos( new double[] { x, y, z } );
    }


    /**
     *
     * @param mass
     * @param pos
     * @param vel
     */
    public Spaceship(double mass, double[] pos, double[] vel){
        super(mass, pos, vel);
        usedFuel = 0;
    }


    /**
     * constructor used in the clone method
     * @param body CelestialBody that contains all the physical information the previous ship had
     * @param costFunc specifies how the body will calculate its cost for the cost function used in the
     *                 trajectory calculation
     */
    private Spaceship(CelestialBody body, CostFunction costFunc){
        super( body.getName(), body.getMass(), body.getPos(), body.getVel());
        this.costFunc = costFunc;
        usedFuel = 0;

    }


    /**
     * Generates the plans that the spaceship will execute to be able to get to its traget and back
     * @param model entity that extends Model3D
     * @param targetPlanet name that was assigned to the entity of type CelestialBody
     * @param numberOfStages the amount of times the rocket will thrust
     * @param maxDays the maximum amount of days that the travel can go on for (on the way back and forward)
     */
    public void makePlan(Model3D model, String targetPlanet, int numberOfStages, int maxDays){
        plan = new Planning(model, targetPlanet, numberOfStages, maxDays);
    }


    /**
     * @return entity of CelestialBody that the spaceShip wants to go to, if there is any.
     */
    public CelestialBody getTarget(){
        return plan.getTarget();
    }


    /**
     * @param state takes in an ArrayList of double arrays that makes it the new
     *              set of maneuvers that the SpaceShip will execute
     */
    public void setPlan(double[][] state){
        if( plan == null )
            plan = new Planning();

        plan.setState(state);
    }

    public double getCost(){
        return cost;
    }

    public double[][] getPlan(){
        return plan.getAll();
    }


    /**
     * executes all functions that need to be executed during the ships flight
     * @param time the time that has passed until now from the start from the simulation in seconds
     * @param dt the time step that is being used to calculate the changes in the model with the numerical solvers
     */
    public void executePlans(double time, double dt){

        if( plan != null ){
            calcCost(getTarget(), dt, getUsedFuel());
            accelerate(time);
        }

    }


    /**
     * @param time the time that has passed until now from the start from the simulation in seconds
     */
    public void accelerate(double time){
        if( time >= plan.getCurrent()[1]  ){
            // in getForce get value of the acc given in plan
            //make plan just give acc & fuel consumption be 1 (in requirements)
            //plans are being set equal and copied by reference, not value
            double force = 0.0;
            for (int i=0; i<3; i++)
                force += plan.getCurrent()[i+2]*plan.getCurrent()[i+2];

            force = Math.sqrt(force);

            usedFuel += (force/maxForce)*fuelConsumption*(plan.getCurrent()[1]-plan.getCurrent()[0]);
            plan.nextDirection();

            System.out.println("the fuel added: " + (force/maxForce)*fuelConsumption*(plan.getCurrent()[1]-plan.getCurrent()[0]));
        }
        if(time >= plan.getCurrent()[0] && time <= plan.getCurrent()[1]){
            double [] current = getAcc();
            for (int i =0; i<current.length; i++){
                current[i]+= getAcc()[i] + plan.getCurrent()[i+2];
            }
            setAcc(current);
        }

    }


    /**
     * adds the cost given by the cost function to the one that we already have
     * @param target the target CelestialBody that wants to be reached
     * @param dt the time step that is being used to calculate the changes in the model with the numerical solvers
     * @param fuel the fuel that has been consumed so far
     */
    private void calcCost(CelestialBody target, double dt, double fuel){
        cost += costFunc.calcCost(fuel, target) * dt;
    }


    /**
     * @param pos adds the specified array of doubles to the position, must be in the following format:
     *            [0:x, 1:y, 2:z]
     */
    private void addPos(double[] pos){
        this.pos[0] += pos[0];
        this.pos[1] += pos[1];
        this.pos[2] += pos[2];
    }


    /**
     * @return an identical copy of this entity
     */
    public Spaceship clone() {
        CelestialBody temp = super.clone();

        return new Spaceship(temp, costFunc);
    }


}