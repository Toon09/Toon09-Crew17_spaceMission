package com.example.planets.BackEnd.Trajectory.OrbitEnters;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.Models.Model3D;



// https://www.toppr.com/guides/physics-formulas/orbital-velocity-formula/
public class Hohmann implements OrbitEnterer{

    private Model3D model;

    public Hohmann(Model3D model){
        this.model = model;

    }

    public double calculateOrbitalVelocity(Spaceship ship) {
        CelestialBody target = model.getShip().getTarget();
        double distance = ship.getDistance(target);
        return Math.sqrt((Gravity0.G*target.getMass())/distance);
    }
    //m/s
    //When we reach tita

    @Override
    public double[] getOrbitEntryVel() {
        return new double[0];
    }

}
