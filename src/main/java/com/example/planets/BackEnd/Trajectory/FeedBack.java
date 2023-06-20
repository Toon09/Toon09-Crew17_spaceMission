package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.Models.Gravity0;

public class FeedBack implements IControler {
    private final LandingModel landingModule;
    private final CelestialBody titan;
    private final Gravity0 model;

    /**
     * default constructor that places the landing module at X = 0, Y = 300km and Y = 0 is the surface of titan
     */
    public FeedBack() {
        titan = new CelestialBody(1.35e23, new double[]{0, -2574, 0}, new double[]{0, 0, 0});
        landingModule = new LandingModel(4200, new double[]{0, 300, 0}, new double[]{0, 0, 0});
        model = new Gravity0(titan, landingModule);
    }

    /**
     * @param initialPosition an array specifying the landing module position where index 0 is X position, index 1 is Y position
     * @param initialVelocity one value of Y axis initial velocity of the module
     */
    public FeedBack(double[] initialPosition, double initialVelocity) {
        titan = new CelestialBody(1.35e23, new double[]{0, -2574, 0}, new double[]{0, 0, 0});
        landingModule = new LandingModel(4200, new double[]{initialPosition[0], initialPosition[1], 0}, new double[]{initialVelocity, 0, 0});
        model = new Gravity0(titan, landingModule);
    }

    public void update(double time, boolean days) {
        model.updatePos(time, 1, days);
        if (landingModule.getPos()[1] > 100) {
            correctX();
        }
    }

    private void correctX() {
        if (landingModule.getPos()[0] == 0) {
            //if its above the point and has no velocity, leave it be, else fix the velocity by rotating and applying its opposite
            if (landingModule.getVel()[0] == 0) {
                return;
            } else if (landingModule.getVel()[0] > 0) {
                double rotation = landingModule.getRotation();
                //rotate to -90 degrees ( so 270 degrees )
                landingModule.rotate(-(rotation + 90));
                activateEngine(landingModule.getVel()[0]);
            }else if (landingModule.getVel()[0] < 0){
                double rotation = landingModule.getRotation();
                //rotate to -90 degrees ( so 270 degrees )
                landingModule.rotate(-(rotation + 90));
                activateEngine(landingModule.getVel()[0]);
            }
        }

    }

    public void activateEngine(double velocity) {
        double[] velocityInDirection = calculateVelocityInDirection(velocity);
        landingModule.addVel2D(velocityInDirection);
    }

    public double[] calculateVelocityInDirection(double velocity) {
        // Convert rotation to radians
        double rotationInRadians = Math.toRadians(landingModule.getRotation());
        // Calculate the velocity components in the x and y directions
        double velocityX = velocity * Math.sin(rotationInRadians);
        double velocityY = velocity * Math.cos(rotationInRadians);

        return new double[]{velocityX, velocityY};
    }

    public LandingModel getLandingModule() {
        return landingModule;
    }

    public Gravity0 getModel() {
        return model;
    }

    //functions that are useful if we are working on the same system as before, not a separate one
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

}