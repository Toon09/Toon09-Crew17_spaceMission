package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.Trajectory.SteepestAscent.SteepestAscent;
import com.example.planets.BackEnd.Trajectory.SteepestAscent.TrajectoryPlanner;

import java.util.ArrayList;


/*
    Here we save the output from the classes that generate the trajectories
    they take the model as an input and start from a specified planet, this is so we can reuse code
    for the way back.

    The output of the trajectory must be a ArrayList<double[][]> in the same format as "maneuverPoints"
 */
public class Planning {
    //count of how many stages located in "maneuverPoints" have been executed so far
    private int countOfStages = 0;

    // this arrayList contains 2D arrays of:
    //[ 0:start of time interval, 1:end of interval, 2:acc. in x, 3:acc. in y, 4:acc. in z ]
    // a different maneuver of these on each diemnsion
    private double[][] maneuverPoints;
    private CelestialBody target;

    private TrajectoryPlanner planner;

    /**
     * increases the count to access the next maneuverPoint that needs to be checked and executed
     */
    public void nextDirection(){
        countOfStages++;

    }

    public int getStageVal(){
        return countOfStages;
    }

    public int getManeuverLength(){
        return maneuverPoints.length;
    }

    /**
     * Gets the current maneuver that needs to be executed in the following format:
     *      first dimension  [0:start of time interval, 1:end of interval ] //times to start and stop accelerating
     *      second dimension [ 0:acc. in x, 1:acc. in y, 2:acc. in z ]
     * @return a 2D array in the format described above, if all maneuvers have been executed, then it returns null
     */
    public double[] getCurrent(){
        return maneuverPoints[countOfStages];
    }

    /**
     *
     * @return
     */
    public double[][] getAll(){
        return maneuverPoints;
    }


    /**
     *
     * @param model
     * @param targetPlanet
     * @param numberOfStages
     * @param maxDays
     */
    public Planning(Model3D model, String targetPlanet, int numberOfStages, int maxDays){
        this.maneuverPoints = new double[numberOfStages][5];

        for(int i=0; i<model.size(); i++)
            if( targetPlanet.equalsIgnoreCase(model.getBody(i).getName()) )
                target = model.getBody(i);

        //creates planner and gets trajectory
        planner = new SteepestAscent(model, numberOfStages, targetPlanet, maxDays);

        maneuverPoints = planner.getTrajectory();

    }

    public Planning(){ }


    /**
     * this constructor is for the copy function
     * @param maneuverPoints the 1D array containing all information calculated for the trajectory
     */
    private Planning(double[][] maneuverPoints, int countOfStages){
        this.maneuverPoints = maneuverPoints;

        this.countOfStages = countOfStages;
    }

    public CelestialBody getTarget(){
        return target;
    }

    public void setState(double[][] state){
        maneuverPoints = state;
    }


    @Override
    public Planning clone() {
        return new Planning(maneuverPoints, countOfStages);
    }

}
