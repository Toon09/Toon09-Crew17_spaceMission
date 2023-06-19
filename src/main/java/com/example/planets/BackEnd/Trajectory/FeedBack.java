package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Gravity0;

import java.util.Arrays;

public class FeedBack implements IControler {
    private Spaceship ship;
    private LandingModel landingModule;
    private Gravity0 model;
    private double[] zeroPosition;
    private double radiusOfTitan = 2574.7;
    private double[] directionsToZero = new double[2];
    private CelestialBody titan;
    private boolean isBelow = false;

    public FeedBack(Spaceship ship, Gravity0 model) {
        this.ship = ship;
        landingModule = new LandingModel(4200, new double[]{ship.getPos()[0], ship.getPos()[1], 0}, new double[]{ship.getVel()[0], ship.getVel()[1], 0});
        this.model = model;
        titan = model.getBody(8);
        zeroPosition = getPoint(titan.getPos(), ship.getPos(), radiusOfTitan);
        directionsToZero[0] = zeroPosition[0] - titan.getPos()[0];
        directionsToZero[1] = zeroPosition[1] - titan.getPos()[1];
        this.model = model;
        if (ship.getPos()[1] < titan.getPos()[1]) {
            isBelow = true;
        }
    }

    public void update() {
        setZero();
        controlX();
        controlY();
    }

    private void setZero() {
        zeroPosition[0] = titan.getPos()[0] + directionsToZero[0];
        zeroPosition[1] = titan.getPos()[1] + directionsToZero[1];
    }

    private void controlY() {
        double diference = Math.abs(zeroPosition[1] - landingModule.getPos()[1]);
        if (isBelow) {
            if (diference >= 100) {
                landingModule.setVel(new double[]{landingModule.getVel()[0], 1, 0});
            } else if (diference > 50) {
                landingModule.setVel(new double[]{landingModule.getVel()[0], 1, 0});
            }
        }
    }

    
    private void controlX() {
        double difference = zeroPosition[0] - landingModule.getPos()[0];
        if (difference < -1 && landingModule.getVel()[0] < 0.3) {
            landingModule.addVel(new double[]{0.3, 0, 0});
        }
    }


    private double[] getPoint(double[] A, double[] B, double distance) {
        double X = B[0] - A[0];
        double Y = B[1] - A[1];
        double lenght = Math.sqrt(X * X + Y * Y);
        //normalize the vector
        double normX = X / lenght;
        double normY = Y / lenght;
        return new double[]{A[0] + normX * distance, A[1] + normY * distance};
    }

    public double getDistance2D(double[] A, double[] B) {
        double X = A[0] - B[0];
        double Y = A[1] - B[1];
        double distance = Math.sqrt(X * X + Y * Y);
        return distance;
    }

    public LandingModel getLandingModule() {
        return landingModule;
    }
}
