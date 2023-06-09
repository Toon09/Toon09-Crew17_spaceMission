package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.Spaceship;

public class LandingModel extends Spaceship {
    private double rotation;

    public LandingModel(double mass, double[] pos, double[] vel) {
        super(mass, pos, vel);
        rotation = 0;
    }

    public void rotate(double rotationChange) {
        boolean negative = rotationChange < 0;
        rotationChange = Math.abs(rotationChange) % 360;
        if (negative && rotation < rotationChange) {
            rotation = 360 - rotationChange + rotation;
        } else if (negative) {
            rotation = (rotationChange - rotation) % 360;
        } else if (!negative) {
            rotation = (rotationChange + rotation) % 360;
        }
    }

    @Override
    public void addVel(double[] vel) {
        super.addVel(new double[]{vel[0], vel[1], 0});
    }

    public double getRotation() {
        return rotation;
    }
}


