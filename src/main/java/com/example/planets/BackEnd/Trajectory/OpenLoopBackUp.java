package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.NumericalMethods.NumSolver;
import com.example.planets.BackEnd.NumericalMethods.RK4;
public  class OpenLoopBackUp {

    private static final double MAX_SPEED = 1000;
    private static final double TORQUE = 0.1; ;
    ;
    double x;
    double y;
    double theta;
    double omega;
    static double THRUST;
    double vx;
    double vy;
    double thrust, torque;
  static  double  SIMULATION_TIME;

    double EPSILON_X;

    double TIME_STEP;
    double DELTA_X;
    static double MAX_ANGLE;
    public OpenLoopBackUp(  double x, double y, double theta, double omega, double vx, double vy, double thrust, double torque, double EPSILON_X, double SIMULATION_TIME, double TIME_STEP, double DELTA_X) {
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.omega = omega;
        this.vx = vx;
        this.vy = vy;
        this.thrust = thrust;
        this.torque = torque;
        this.EPSILON_X = EPSILON_X;
        this.SIMULATION_TIME = 10000000;
        this.TIME_STEP = TIME_STEP;
        this.DELTA_X = DELTA_X;

    }

    public static double[] calculateThrust(double x, double y, double vx, double vy, double theta, double omega) {
        double[] thrust = new double[2];

        // Calculate the thrust force
        thrust[0] = Math.sqrt(vx * vx + vy * vy) < MAX_SPEED ? THRUST : 0;
        System.out.println("thrust[0]"+thrust[0]);
        // Calculate the torque
        thrust[1] = Math.abs(theta % (2 * Math.PI)) < MAX_ANGLE ? TORQUE : 0;
        System.out.println("thrust[1]"+thrust[1]);
        return thrust;
    }

    public static void main(String[] args) {
        // Initial conditions
        double x = 0; // Horizontal position (m)
        double y = 300000; // Vertical position (m)
        double theta = Math.PI / 4; // Angle of rotation (radians)
        double vx = 0; // Horizontal velocity (m/s)
        double vy = 0; // Vertical velocity (m/s)
        double omega = 0; // Angular velocity (rad/s)
        double GRAVITY= 1.352 * Math.pow(10, -3);
        // Landing conditions
        double DELTA_X = 0.1; // m
        double EPSILON_X = 0.1; // m/s
         // s
        double TIME_STEP = 0.01; // s
        double EPSILON_Y = 0.1; // m/s
        double DELTA_THETA = 0.1; // radians
double time = 0; // Current simulation time (seconds)

        // Simulate the landing process using numerical integration
        while ( true ){

        double[] thrust = calculateThrust(x, y, vx, vy, theta, omega);

        // Update the state using Euler's method
        x += vx * TIME_STEP;
        System.out.println("x"+x);
        y += vy * TIME_STEP;
        System.out.println("y"+y);
        vx += thrust[0] * Math.sin(theta) * TIME_STEP;
        vy += thrust[0] * Math.cos(theta) * TIME_STEP - GRAVITY * TIME_STEP;
        theta += omega * TIME_STEP;
        omega += thrust[1] * TIME_STEP;
        // Check if the landing conditions are satisfied
        if (Math.abs(x) <= DELTA_X && Math.abs(y) <= DELTA_X && Math.abs(vx) <= EPSILON_X
        && Math.abs(vy) <= EPSILON_Y && Math.abs(theta % (2 * Math.PI)) <= DELTA_THETA) {
        System.out.println("Safe landing achieved!");

        break;
        }

        time += TIME_STEP;
        }
    };
        }

