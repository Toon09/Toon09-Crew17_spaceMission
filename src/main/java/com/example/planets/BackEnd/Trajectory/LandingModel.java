package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.Spaceship;

public class LandingModel extends Spaceship {
    private double[] zeroZero;
    public LandingModel(double mass, double[] pos, double[] vel) {
        super(mass, pos, vel);
        zeroZero = pos;
    }
    public double[] getPos() {
        return pos;
    }



}


