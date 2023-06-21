package com.example.planets.BackEnd.Trajectory.TrajectoryOptimizers;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.Models.Model3D;

import java.util.Arrays;

public class LazyPlanner implements TrajectoryPlanner{

    private CelestialBody target;
    private CelestialBody home;
    private Model3D model; // have this copy be same as the one being used and for calculations use a clone
    private boolean toTarget = true;
    private double[] curretVel = new double[4];
    private double phaseTime = 10000; // checks every 3 days
    private double slowPhaseTime = 20000; //check every 6 days
    private double targetDistance = 500000.0;
    private double lastAcc = 0;

    public LazyPlanner(Model3D model, String target, String home){
        this.model = model;

        for(int i=0; i< model.size(); i++){
            if(target.equalsIgnoreCase(model.getBody(i).getName()))
                this.target = model.getBody(i);

            if(home.equalsIgnoreCase(model.getBody(i).getName()))
                this.home = model.getBody(i);
        }
    }


    @Override
    public void makeTrajectory() { }

    // gets what has been done so far or just the last step ?
    @Override
    public double[][] getTrajectory() {
        return new double[][] {curretVel};
    }

    @Override
    public void next() {
        CelestialBody targetPlanet = home;
        if (toTarget) {
            targetPlanet = target;
        }
        double x = targetPlanet.getPos()[0] - model.getShip().getPos()[0];
        double y = targetPlanet.getPos()[1] - model.getShip().getPos()[1];
        double z = targetPlanet.getPos()[2] - model.getShip().getPos()[2];
        double[] newAcc = new double[3];

        double distance = model.getShip().getDistance(targetPlanet);
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
        }

        curretVel[0] = model.getTime();
        for(int i=1; i<curretVel.length; i++)
            curretVel[i] = newAcc[i-1];

        System.out.println(Arrays.toString(curretVel));
        System.out.println(lastAcc + phaseTime);

        if (distance < targetDistance && distance != 0) {
            toTarget = false;
        }
    }

    @Override
    public double[] getCurrent() {
        return curretVel;
    }

    @Override
    public void setTrajectory(double[][] trajectory) {
        curretVel = trajectory[0];
    }
}
