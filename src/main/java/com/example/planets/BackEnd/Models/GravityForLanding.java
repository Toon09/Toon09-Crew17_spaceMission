package com.example.planets.BackEnd.Models;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Trajectory.LandingModule;

public class GravityForLanding {
    private CelestialBody titan;
    private static Spaceship ship;
    private LandingModule module;
    double g = 1.352 * Math.pow(10, -3); // km/s^2
    private Gravity0 gravity0;


    public GravityForLanding(CelestialBody titan, Spaceship ship, LandingModule module) {
        this.titan = titan;
        this.ship = ship;
        this.module = module;
        this.gravity0 = new Gravity0(ship, titan, new LandingModule[]{module});
        titan.setPos(new double[]{0, -2574, 0});
        ship.setPos(new double[]{0, 300000, 0});
        module.setPos(new double[]{0, 300000, 0});
    }

    private void applyGravity() {
        module.addAcc(new double[]{0, g,0});
    }

    //force of gravity = m*g
    //g = 1.352 m/s^2
    //m = 42000 kg
    //F = 5678,4 N
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
