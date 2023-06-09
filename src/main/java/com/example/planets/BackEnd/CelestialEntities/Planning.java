package com.example.planets.BackEnd.CelestialEntities;

import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.Trajectory.TrajectoryOptimizers.StocasticAscent;
import com.example.planets.BackEnd.Trajectory.TrajectoryOptimizers.TrajectoryHolder;
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
    private CelestialBody target;
    private TrajectoryPlanner planner;

    public Planning(){ }

    /**
     *
     * @param model
     * @param targetPlanet
     * @param numberOfStages
     * @param maxDays
     */
    public Planning(Model3D model, String targetPlanet, String homePlanet, int numberOfStages, int maxDays){

        for(int i=0; i<model.size(); i++)
            if( targetPlanet.equalsIgnoreCase(model.getBody(i).getName()) )
                target = model.getBody(i);


        //creates planner and gets trajectory
        //planner = new StocasticAscent(model, numberOfStages, targetPlanet, maxDays);
        planner = new TrajectoryHolder();

        //

        // do the calculation of the orbit from the planet
        planner.makeTrajectory();

    }


    public double[] getCurrent(){
        return planner.getCurrent();
    }


    /**
     * increases the count to access the next maneuverPoint that needs to be checked and executed
     */
    public void nextDirection(){
        planner.next();
    }

    /**
     * this constructor is for the copy function
     */
    private Planning(TrajectoryPlanner plan, int countOfStages, CelestialBody target){
        planner = plan;
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
        if(planner == null)
            planner = new TrajectoryHolder();
        planner.setTrajectory(state);

    }

    public void setTrajectory(TrajectoryPlanner trajectory){
        this.planner = trajectory;
    }


    public double[][] getAll(){
        return planner.getTrajectory();
    }


    @Override
    public Planning clone() {
        return new Planning(planner, countOfStages, target);
    }

}
