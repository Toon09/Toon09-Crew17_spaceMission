package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Planning;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.NumericalMethods.NumSolver;
import com.example.planets.BackEnd.NumericalMethods.RK4;

public class OpenLoop implements Model3D {
    private double x; // Horizontal position
    private double y; // Vertical position
    private double theta; // Angle of rotation

    double initialX = 0.0;
    double initialY = 0.0;
    double initialTheta = Math.PI / 4;
    double acceleration = 10.0; // m/s^2
    double torque = 2.0; // rad/s^2
    double g = 1.352 * Math.pow(10, -3); // km/s^2
    double dt = 0.01; // Time step in seconds
    int numSteps = 100; // Number of simulation steps
    private double u;
    private double v; // Total
    private Gravity0 model;
    private double time;
    private Spaceship ship;
    private CelestialBody titan;
    private LandingModel module;
    private CelestialBody[] bodies;
    private NumSolver numSolver;

    public OpenLoop(Spaceship ship, CelestialBody titan, LandingModel module) {
        this.ship = ship;
        this.titan = titan;
        this.module = module;
        CelestialBody[] bodies = {ship, titan, module}; // Adjusted array size




        titan.setPos(new double[] { 0, -2574, 0 });
        ship.setPos(new double[] { 0, 300000, 0 });
        module.setPos(new double[] { 0, 300000, 0 });
        Gravity0 gravity0 = new Gravity0(titan, module);
        System.out.println("Length of bodies: " + gravity0.getBodies().length);

        for (int i = 0; i < 100; i++) {
            System.out.println(gravity0.getBody(0).toString());
            System.out.println(gravity0.getBody(1).toString());
            System.out.println(gravity0.getBody(2).toString());
            gravity0.updatePos(20, 3, false);
        }
    }

    public static void main(String[] args) {
        Spaceship ship = new Spaceship(4200, new double[] { 0, 300, 0 }, new double[] { 0, 0, 0 });
        CelestialBody titan = new CelestialBody("titan", 1.35e23, new double[] { 0, -2574, 0 }, new double[] { 0, 0, 0 });
        LandingModel module = new LandingModel(4200, new double[] { 0, 300, 0 }, new double[] { 0, 0, 0 });
        OpenLoop openLoop = new OpenLoop(ship, titan, module);

        // Apply the update method numSteps times
        for (int i = 0; i < openLoop.numSteps; i++) {
            System.out.println("x = " + openLoop.x + ", y = " + openLoop.y + ", theta = " + openLoop.theta);
            openLoop.updatePos(100, 0.1, false);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
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
        return F * t;
    }

    @Override
    public void setPos(int index, double[] pos) {

    }

    @Override
    public void setVel(int index, double[] vel) {

    }

    @Override
    public void setAcc(int index, double[] acc) {

    }

    @Override
    public void setSolver(NumSolver numSolver) {
        numSolver = new RK4();
    }

    @Override
    public void addDt(double dt) {

    }

    @Override
    public void addShips(int numShips) {

    }

    @Override
    public int getAmountOfShips() {
        return 0;
    }

    @Override
    public double[] getPos(int index) {
        return new double[0];
    }

    @Override
    public double[] getVel(int index) {
        return new double[0];
    }

    @Override
    public double[] getAcc(int index) {
        return new double[0];
    }

    @Override
    public void setState(double[][][] state) {

    }

    @Override
    public double getTime() {
        return 0;
    }

    @Override
    public String getSolverName() {
        return null;
    }

    @Override
    public CelestialBody getBody(int index) {
        return null;
    }

    @Override
    public Spaceship getShip() {
        return ship;
    }

    @Override
    public Spaceship getShip(int index) {
        return ship;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public double[][][] getState() {
        return new double[0][][];
    }

    @Override
    public void hDeriv() {
        model.getBody(2).setAcc(new double[] { (u * Math.toRadians(Math.sin(theta))),
                (u * Math.toRadians(Math.cos(theta)) - g), v });
    }

    @Override
    public Model3D clone(NumSolver numSolver) {
        return null;
    }

    @Override
    public Model3D clone() {
        return null;
    }

    @Override
    public void updatePos(double time, double dt, boolean inDays) {
        model.getBody(2).setPos(new double[] { model.getBody(2).getPos()[0] + model.getBody(2).getVel()[0] * dt,
                model.getBody(2).getPos()[1] + model.getBody(2).getVel()[1] * dt,
                model.getBody(2).getPos()[2] + model.getBody(2).getVel()[2] * dt });
    }
}
