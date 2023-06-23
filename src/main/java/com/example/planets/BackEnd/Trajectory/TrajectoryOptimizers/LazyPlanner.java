package com.example.planets.BackEnd.Trajectory.TrajectoryOptimizers;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.NumericalMethods.Euler;

import java.util.Arrays;

public class LazyPlanner implements TrajectoryPlanner{

    private String target;
    private String home;
    private Model3D model; // have this copy be same as the one being used and for calculations use a clone
    private boolean toTarget = true;
    private double phaseTime = 10000; // checks every 3 days
    private double slowPhaseTime = 20000; //check every 6 days
    private double targetDistance = 500000.0;
    private double lastAcc = 0;

    // both velocities that will be kept
    private double[] curretVel;
    private double[] nextVel;

    public LazyPlanner(Model3D model, String target, String home){
        this.model = model;

        this.target = target;
        this.home = home;
    }


    @Override
    public void makeTrajectory() { }

    // gets what has been done so far or just the last step ?
    @Override
    public double[][] getTrajectory() {
        return new double[][] {curretVel};
    }

    private boolean first = true;

    @Override
    public void next() { // always makes sure current and next is calcualted
        if(curretVel == null) // first possible time step
            curretVel = getToDo(model);

        // to get next value
        Model3D temp = model.clone();
        // important to give it an empty trajectory since it avoids coming back to this function call and entering a recursion
        // it also avoids unnecessary calculations since we are only looking at the targets position
        temp.getShip().setTrajectory(new TrajectoryHolder()); //////// find way to remove the ship from it

        // gets a model on the next value where it must be calculated
        if(model.getShip().getDistance(model.getBody(target)) > 500_000_000)
            temp.updatePos(slowPhaseTime, 150, false); ///////////// causes infinite loop ################ FIX
        else
            temp.updatePos(phaseTime, 150, false);

        nextVel = getToDo(temp);

    }

    /**
     * gets what should be done for the ship to reach the target planet with the model currently given,
     * so you must be
     * @param model state of the model when you want to plan the next move
     * @return
     */
    private double[] getToDo(Model3D model){
        CelestialBody targetPlanet;
        if (toTarget)
            targetPlanet = model.getBody(target);
        else
            targetPlanet = model.getBody(home);

        double x = targetPlanet.getPos()[0] - model.getShip().getPos()[0];
        double y = targetPlanet.getPos()[1] - model.getShip().getPos()[1];
        double z = targetPlanet.getPos()[2] - model.getShip().getPos()[2];
        double[] newAcc = new double[3];

        double distance = model.getShip().getDistance( targetPlanet ); /////
        if ((distance > 500000000 && model.getTime() > lastAcc + slowPhaseTime) || distance == 0) {
            newAcc[0] = model.getShip().getVel()[0] + x / 900000;
            newAcc[1] = model.getShip().getVel()[1] + y / 900000;
            newAcc[2] = model.getShip().getVel()[2] + z / 900000;
            lastAcc = model.getTime();
        } else if (model.getTime() > lastAcc + phaseTime && distance < 90000000) {
            newAcc[0] = model.getShip().getVel()[0] + x / 40000;
            newAcc[1] = model.getShip().getVel()[1] + y / 40000;
            newAcc[2] = model.getShip().getVel()[2] + z / 40000;
            lastAcc = model.getTime();

        } else if (model.getTime() > lastAcc + phaseTime && distance < 500000000) {
            newAcc[0] = model.getShip().getVel()[0] + x / 550000;
            newAcc[1] = model.getShip().getVel()[1] + y / 550000;
            newAcc[2] = model.getShip().getVel()[2] + z / 550000;
            lastAcc = model.getTime();
        }else if(model.getTime() > lastAcc + phaseTime || first){
            newAcc[0] = model.getShip().getVel()[0] + x / 550000;
            newAcc[1] = model.getShip().getVel()[1] + y / 550000;
            newAcc[2] = model.getShip().getVel()[2] + z / 550000;
            lastAcc = model.getTime();

            first = false;
        }

        double[] vel = new double[4];

        vel[0] = model.getTime();
        for(int i=1; i<vel.length; i++)
            vel[i] = newAcc[i-1];

        //System.out.println(Arrays.toString(vel));
        //System.out.println(lastAcc + phaseTime);

        if (distance < targetDistance && distance != 0) {
            toTarget = false;
        }

        return vel;
    }


    @Override
    public double[] getCurrent() { // returns current and sets next as new current
        if(curretVel == null){
            next();
        }

        // returns the current one and makes sure the next one is set as the new current one
        double[] result = curretVel;
        if(nextVel != null)
            curretVel = nextVel;

        // once the next is set as the current one, its set as null to prevent it being set again
        nextVel = null;

        return result;
    }

    @Override
    public void setTrajectory(double[][] trajectory) {
        curretVel = trajectory[0];
    }
}
