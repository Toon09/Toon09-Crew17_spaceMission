package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Planning;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Gravity0;

import java.util.ArrayList;
import java.util.Arrays;

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
    private Gravity0 model;
    public OpenLoop(Spaceship ship, CelestialBody titan, LandingModel module){
        titan.setPos(new double[]{0, -2574, 0});
        ship.setPos(new double[]{0, 300000, 0});
        module.setPos(new double[]{0, 300000, 0});
        this.model = new Gravity0(ship, titan, module);
        System.out.println("lenght of bodies: "+ model.getBodies().length);
        System.out.println(model.getBody(0).toString());
        System.out.println(model.getBody(1).toString());
        for(int i = 0; i<100; i++){
            model.updatePos(20,3,false);
            print();
        }
    }

    public static void main(String[] args) {
        Spaceship ship = new Spaceship(4200,new double[]{0,300,0}, new double[]{0,0,0});
        CelestialBody titan = new CelestialBody("titan",1.35e23,new double[]{0, -2574, 0},new double[]{0, 0, 0});
        LandingModel module = new LandingModel(4200,new double[]{0, 300000, 0},new double[]{0, 0, 0});
        OpenLoop openLoop = new OpenLoop(ship,titan,module);
    }

    private void print(){
        System.out.println("titan at: " + Arrays.toString(model.getBody(0).getPos()));
        System.out.println("module at: " + Arrays.toString(model.getBody(1).getPos()));
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
    private void IDK (){
        model.updatePos(666/2,4,false);
        double againIDK = 3782798.963730428/4200/2;
        double x = model.getBody(0).getVel()[0];
        double y = model.getBody(1).getVel()[1] + againIDK;
        double z = model.getBody(1).getVel()[2];
        model.getBody(1).setVel(new double[] {x,y,z});
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