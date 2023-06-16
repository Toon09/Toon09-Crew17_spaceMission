package com.example.planets.BackEnd.Models;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Trajectory.LandingModel;
import org.apache.poi.ss.formula.ptg.AttrPtg;

public class GravityForLanding {
    private CelestialBody titan;
    private Spaceship ship;
    private LandingModel module;
    double gravity = 1.352 * Math.pow(10, -3); // km/s^2


    public GravityForLanding(CelestialBody titan, Spaceship ship, LandingModel module) {
        this.titan = titan;
        this.ship = ship;
        this.module = module;
        titan.setPos(new double[]{0, -2574, 0});
        ship.setPos(new double[]{0, 300000, 0});
        module.setPos(new double[]{0, 300000, 0});
    }

    private void applyGravity() {
        double distance = 0;
        //d = mg^2
    }

    //force of gravity = m*g
    //g = 1.352 m/s^2
    //m = 42000 kg
    //F = 5678,4 N
    private void calculteForces() {
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
    }
}
