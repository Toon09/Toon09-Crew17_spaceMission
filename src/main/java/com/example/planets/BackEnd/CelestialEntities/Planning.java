package com.example.planets.BackEnd.CelestialEntities;

import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.Trajectory.LazyTrajectories.LazyTargetting;
import com.example.planets.BackEnd.Trajectory.LazyTrajectories.LazyTrajectory;
import com.example.planets.BackEnd.Trajectory.OrbitEnters.Hohmann;
import com.example.planets.BackEnd.Trajectory.OrbitEnters.OrbitEnterer;
import com.example.planets.BackEnd.Trajectory.TrajectoryOptimizers.StocasticAscent;
import com.example.planets.BackEnd.Trajectory.TrajectoryOptimizers.TrajectoryPlanner;


/*
    Here we save the output from the classes that generate the trajectories
    they take the model as an input and start from a specified planet, this is so we can reuse code
    for the way back.

    The output of the trajectory must be a ArrayList<double[][]> in the same format as "maneuverPoints"




    //////////////////////
 */
public class Planning {
    //count of how many stages located in "maneuverPoints" have been executed so far
    private int countOfStages = 0;

    // this arrayList contains 2D arrays of:
    //[ 0:start of time interval, 1:end of interval, 2:acc. in x, 3:acc. in y, 4:acc. in z ]
    // a different maneuver of these on each dimension
    private double[][] maneuverPoints;
    private CelestialBody target;
    private TrajectoryPlanner planner; // optimizer to get close to the planet
    private LazyTrajectory lazyPlanner; // just brute forcing closer //////////////////
    private OrbitEnterer orbiter;

    private double orbitalVelocity;
    private boolean notInLazyStage = true;


    /**
     *
     * @param model
     * @param targetPlanet
     * @param numberOfStages
     * @param maxDays
     */
    public Planning(Model3D model, String targetPlanet, String homePlanet, int numberOfStages, int maxDays){
        this.maneuverPoints = new double[numberOfStages][5];

        for(int i=0; i<model.size(); i++)
            if( targetPlanet.equalsIgnoreCase(model.getBody(i).getName()) ) {
                target = model.getBody(i);
            }

        //creates planner and gets trajectory
        planner = new StocasticAscent(model, numberOfStages, targetPlanet, maxDays);

        // lazy planner
        lazyPlanner = new LazyTargetting(model);

        // class for hohmann and the following things
        orbiter = new Hohmann(model);

        // do the calculation of the orbit from the planet
        maneuverPoints = planner.getTrajectory();

    }


    public Planning(){ }

    public int getStageVal(){
        return countOfStages;
    }

    public int getManeuverLength(){
        return maneuverPoints.length;
    }

    /**
     * Gets the current maneuver that needs to be executed in the following format:
     *      first dimension  [0:start of time interval, 1:length of interval ] //times to start and stop accelerating
     *      second dimension [ 0:vel. in x, 1:vel. in y, 2:vel. in z ]
     * @return a 2D array in the format described above, if all maneuvers have been executed, then it returns null
     */
    public double[] getCurrent(){ //////////////////
        // if its in the first stage of the planning
        if(notInLazyStage)
            return maneuverPoints[countOfStages];

        if(lazyPlanner.finished()) // hohmann goes here
            return orbiter.getOrbitEntryVel();

        return lazyPlanner.getCurrent();
    }


    /**
     * increases the count to access the next maneuverPoint that needs to be checked and executed
     */
    public void nextDirection(){ ////////////////
        countOfStages++;
        if(countOfStages >= maneuverPoints.length)
            notInLazyStage = false;
    }

    /**
     * this constructor is for the copy function
     * @param maneuverPoints the 1D array containing all information calculated for the trajectory
     */
    private Planning(double[][] maneuverPoints, int countOfStages, CelestialBody target){
        this.maneuverPoints = maneuverPoints;
        this.target = target;

        this.countOfStages = countOfStages;
    }

    public CelestialBody getTarget(){
        return target;
    } //


    public void setTarget(CelestialBody target){
        this.target = target;
    } //


    public void setState(double[][] state){ //
        maneuverPoints = new double[state.length][5];

        for(int i=0; i<state.length; i++)
            for(int j=0; j<5; j++)
                maneuverPoints[i][j] = state[i][j];

    }

    /**
     *
     * @return
     */
    public double[][] getAll(){
        return maneuverPoints;
    }


    @Override
    public Planning clone() {
        return new Planning(maneuverPoints, countOfStages, target);
    }

}
