package com.example.planets.BackEnd.CelestialEntities;

import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.Models.Model3D;

/*
add methods related to changing trajectory to another class that has an instance in this one
and will keep things simpler

could apply decorator for trying different types of engines and debug quick
 */

public class Spaceship extends CelestialBody {
    private double usedFuel;
    //has all the information of where to go and such
    Planning plan = new Planning();
    private final double maxSpeed = 2_500; //2,500 to 4,500 m/s (look up)
    private  final double maxForce = 3 * Math.pow(10, 7); // Newtons
    public double getUSedFuel(){ return usedFuel; }
    // public void setFuel(double fuel){ this.fuel = fuel; }



    public Spaceship(double mass, double[] pos, double[] vel, double theta, double phi, double[] addVel){
        super(mass, pos, vel);
        usedFuel = 0;

        //positions
        double x = Gravity0.radiuses[3] * Math.cos(theta) * Math.cos(phi);
        double y = Gravity0.radiuses[3] * Math.sin(theta) * Math.cos(phi);
        double z = Gravity0.radiuses[3] * Math.sin(phi);
        //add to its position
        this.addPos( new double[] { x, y, z } );
        this.addVel(addVel);
    }


    public Spaceship(double mass, double[] pos, double[] vel){
        super(mass, pos, vel);
        usedFuel = 0;
    }


    //will check for the points where the trajectory will be changed
    public boolean trajectoryChangeCondition(Model3D system) {
        return false;

    }



    //give the magnitute of the velocity and tbe direction (normalized) in which the aircraft needs to travel
    //if there's some way to use something other than acceleration and magnitute to calculate it
    //give Raul a call and he can add it

    /*
    calc velocity for each with v[i] + 1/m * Isp

    Isp is the instantaneous impulse, integral can be calced with RK4 or so
    make it so the integrator can be changed easily

    Isp is integral of force over time (bery small) with max force being 3*10^7 N

    Amount of fuel used is proportional to force ||F|| * 1m
    so 1 second exerting force ||F|| its ||F|| amount of fuel
     */

    //magnitute and thso evalue are saved inside the Planning class
    public void accelerate(double dt){
        double[] direction = plan.getNextDirection(); //gets all info of where it needs to go
        // change so it doesnt change order per time step
        // add a var thats called order than in the condition make it so that
        // it saves it and then its used here

        //calc the fuel loss (magnitute is necesary)
        double magnitute = direction[3];
        usedFuel += dt* Math.abs(magnitute);

        //calc the velocity change if the force can still be applied
        // can apply other methods like RK2 and such here for more precision /////////////////////
        if( magnitute*getMass() <= maxForce ){
            addVel( new double[] {  direction[0]*magnitute*dt,
                                    direction[1]*magnitute*dt,
                                    direction[2]*magnitute*dt   });
        }

    }


    private void addPos(double[] pos){
        this.pos[0] += pos[0];
        this.pos[1] += pos[1];
        this.pos[2] += pos[2];
    }

    private void addVel(double[] vel){
        this.vel[0] += vel[0];
        this.vel[1] += vel[1];
        this.vel[2] += vel[2];
    }

}