package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.Planning;

public class OpenLoop implements IControler {
    private double x; // Horizontal position
    private double y; // Vertical position
    private double theta; // Angle of rotation

    double initialX = 0.0;
    double initialY = 0.0;
    double initialTheta = Math.PI / 4; // 45 degrees
    double acceleration = 10.0; // m/s^2
    double torque = 2.0; // rad/s^2
    double gravity = 1.352 * Math.pow(10, -3); // km/s^2

    double dt = 0.01; // Time step in seconds
    int numSteps = 100; // Number of simulation steps
    private double u;
    private double v; // Total
    private double g;
    @Override public Planning claclateThrust(double initialX, double initialY, double initialTheta,
                                             double acceleration, double torque, double gravity) {
        x = initialX;
        y = initialY;
        theta = initialTheta;
        u = acceleration;
        v = torque;
        g = gravity;
        return null;
    }

    // Method to update the spaceship's position and orientation over time
    public void update(double dt) {
        // Calculate the second derivatives (accelerations)
        double xAcceleration = u * Math.sin(theta);
        double yAcceleration = u * Math.cos(theta) - g;
        double thetaAcceleration = v;

        // Update the position and orientation using Euler's method
        x += xAcceleration * dt * dt / 2;
        y += yAcceleration * dt * dt / 2;
        theta += thetaAcceleration * dt * dt / 2;
    }

    // Getters for position and orientation
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getTheta() {
        return theta;
    }

    @Override
    public Planning creatPlan() {
        return IControler.super.creatPlan();
    }
}