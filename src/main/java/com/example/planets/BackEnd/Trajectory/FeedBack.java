package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Gravity0;

public class FeedBack implements IControler {
    private final LandingModel landingModule;
    private final double[] zeroPosition;
    private final double[] directionsToZero = new double[2];
    private final CelestialBody titan;

    public FeedBack( Gravity0 model) {
        Spaceship ship = model.getShip();
        landingModule = new LandingModel(4200, new double[]{ship.getPos()[0], ship.getPos()[1], 0}, new double[]{ship.getVel()[0], ship.getVel()[1], 0});
        titan = model.getBody(8);
        zeroPosition = getPoint(titan.getPos(), ship.getPos(), 2574.7);
        directionsToZero[0] = zeroPosition[0] - titan.getPos()[0];
        directionsToZero[1] = zeroPosition[1] - titan.getPos()[1];
    }

    public void update() {
        setZero();
        double difference = getDistance2D(landingModule.getPos(), zeroPosition);
        double speed = 0.001;
        if (difference > 100){
            speed = 1;
        }else if (difference > 50){
            speed = 0.5;
        }else if (difference > 1){
            speed = 0.2;
        }
        double[] xAndY = getDirection(landingModule.getPos(), zeroPosition, speed);
        landingModule.setVel(new double[]{xAndY[0], xAndY[1], 0});
    }
    private void setZero() {
        zeroPosition[0] = titan.getPos()[0] + directionsToZero[0];
        zeroPosition[1] = titan.getPos()[1] + directionsToZero[1];
    }

    private double[] getPoint(double[] A, double[] B, double distance) {
        double X = B[0] - A[0];
        double Y = B[1] - A[1];
        double length = Math.sqrt(X * X + Y * Y);
        double normX = X / length;
        double normY = Y / length;
        return new double[]{A[0] + normX * distance, A[1] + normY * distance};
    }

    public double getDistance2D(double[] A, double[] B) {
        double X = A[0] - B[0];
        double Y = A[1] - B[1];
        return Math.sqrt(X * X + Y * Y);
    }

    private double[] getDirection(double[] start, double[] end, double velocity) {
        double deltaX = end[0] - start[0];
        double deltaY = end[1] - start[1];

        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        double directionX = velocity * (deltaX / distance);
        double directionY = velocity * (deltaY / distance);

        return new double[]{directionX, directionY};
    }
    public LandingModel getLandingModule() {
        return landingModule;
    }
}