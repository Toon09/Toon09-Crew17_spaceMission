package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Planning;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Gravity0;

public class OpenLoop implements IControler {
    private double x; // Horizontal position
    private double y; // Vertical position
    private double theta; // Angle of rotation

    double initialX = 0.0;
    double initialY = 0.0;
    double initialTheta = Math.PI / 4; // 45 degrees
    double acceleration = 10.0; // m/s^2
    double torque = 2.0; // rad/s^2
    double g = 1.352 * Math.pow(10, -3); // km/s^2
    double dt = 0.01; // Time step in seconds
    int numSteps = 100; // Number of simulation steps
    private double u;
    private double v; // Total
    private Gravity0 gravity;
    public OpenLoop(Spaceship ship, CelestialBody titan, LandingModel module){
        titan.setPos(new double[]{0, -2574, 0});
        ship.setPos(new double[]{0, 300000, 0});
        module.setPos(new double[]{0, 300000, 0});
        this.gravity = new Gravity0(ship, titan, new LandingModel[]{module});
    }

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
    private double calculateForces() {
        double m = 4200;
        double g = 1.352;
        double F = g * m;
        double d = 300;
        double t = Math.sqrt((2 * d) / g);
        System.out.println("m = " + m);
        System.out.println("g = " + g);
        System.out.println("F = " + F);
        System.out.println("d = " + d);
        System.out.println("t = " + t);
        System.out.println("F*t = " + F * t);
        return F*t;
    }
}