package com.example.planets.BackEnd.Trajectory.SteepestAscent;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.Trajectory.TrajectoryPlanner;

import java.util.ArrayList;

public class SteepestAscent implements TrajectoryPlanner {

    Model3D model;
    CelestialBody target;
    Spaceship ship;

    public SteepestAscent(Model3D model, CelestialBody target){
        this.model = model.clone();
        target = target.clone();
    }

    /*

     */
    @Override
    public ArrayList<double[]> getTrajectory() {
        return null;
    }

}
