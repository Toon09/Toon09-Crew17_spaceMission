package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.Spaceship;

public class FeedBack implements IControler{
    private Spaceship ship;
    private LandingModel model;

    public FeedBack(Spaceship ship, LandingModel model) {
        this.ship = ship;
        this.model = model;
    }
    private void corrector(double[] error) {
        if(isVertical()) {
            ship.addAcc(error);
        }
    }

    private boolean isVertical() {
        return true; // for now the landing module is just vertical
    }

    private boolean isHorizontal() {
        return false;
    }

    private double[] getError() {
        double[] shipCoordinates = ship.getPos();
        double[] modelCoordinates = model.getPos();
        return new double[]{shipCoordinates[0] - modelCoordinates[0],
                            shipCoordinates[1] - modelCoordinates[1],
                            shipCoordinates[2] - modelCoordinates[2]};
    }
}
