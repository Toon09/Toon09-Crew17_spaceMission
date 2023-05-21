package com.example.planets.BackEnd.CelestialEntities;

import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.Trajectory.Planning;

/*
add methods related to changing trajectory to another class that has an instance in this one
and will keep things simpler

could apply decorator for trying different types of engines and debug quick
 */

public class Spaceship extends CelestialBody {
    private double usedFuel;
    //has all the information of where to go and such
    Planning plan;
    private final double maxSpeed = 11000; //2,500 to 4,500 m/s (look up) according to falcon 9
    private final double accRate = 10;
    private  final double maxForce = 3 * Math.pow(10, 7); // Newtons
    private final double fuelConsumption = 1451.5; //kg  This fuel consumption is based on the falcon 9 maximum fuel consumption, so at max acceleration the consumption is this one.
    public double getUsedFuel(){ return usedFuel; }
    // public void setFuel(double fuel){ this.fuel = fuel; }


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
     *
     * @param body
     */
    private Spaceship(CelestialBody body){
        super( body.getName(), body.getMass(), body.getPos(), body.getVel());
        usedFuel = 0;

    }


    public void makePlan(Model3D model, String targetPlanet, int numberOfStages, int maxDays){
        plan = new Planning(model, targetPlanet, numberOfStages, maxDays);
    }

    public CelestialBody getTarget(){
        return plan.getTarget();
    }


    public boolean trajectoryChangeCondition(Model3D system) {
        if(plan == null){ return false; }
        // if this is true, then accelerate(double dt) happens
        // as soon as its false call the method plan.nextDirection()
        // this allows for the info of the next stage to be looked at
        // with the plan.getCurrent()

        // do plan.getCurrent() to get the info you need
        // plan.getCurrent()[0] gives an array with 2 things:
        // [0:start of time interval, 1:end of time interval]

        return false;

    }



    public void accelerate(Gravity0 model){
        if(plan == null){ return; }
        double time = model.getTime();
        if(time >= plan.getCurrent()[1]){
            plan.nextDirection();
        }
        if(time>= plan.getCurrent()[0]){
            double [] current = getAcc();
            for (int i =0; i<current.length; i++){
                current[i]+=plan.getCurrent()[i+2];
            }
            setAcc(current);
        }
        // from plan get current direction & accelerate in those
        //double[] acc1 = plan.getCurrent();
        //time = endPoint - startPoint
        //double[] goalAcc;
        // for(int = 0; i < time / dt; i++) {
            // currAccX, Y, Z = vel[0, 1, 2];
            // accRateX, Y, Z (goalAcc - currAcc) / time
            // addVel(new double[]{accRateX, Y, Z});
        // }

        // do plan.getCurrent() to get the info you need
        // plan.getCurrent()[1] gives an array with 3 things:
        // [0:acc in x, 1:acc in y, 2:acc in z]

        //useFuel(percentage);

    }

    public void useFuel(double percentage) { usedFuel += percentage*fuelConsumption; }

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

    private void addAcc(double[] acc){
        this.acc[0] += acc[0];
        this.acc[1] += acc[1];
        this.acc[2] += acc[2];
    }

    public Spaceship clone() {
        CelestialBody temp = super.clone();

        return new Spaceship(temp);
    }

}