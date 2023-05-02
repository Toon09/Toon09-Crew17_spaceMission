package com.example.planets.BackEnd;

import com.example.planets.BackEnd.Models.Gravity0;


public class Spaceship extends CelestialBody {
    private double fuel;
    //speed limit is 60km/s (starting)

    public Spaceship(double mass, double[] pos, double[] vel, double theta, double phi, double[] addVel){
        super(mass, pos, vel);
        fuel = 100; //from 100 usage to 0, should be changed to litters later for more precision tho

        //positions
        double x = Gravity0.radiuses[3] * Math.cos(theta) * Math.cos(phi);
        double y = Gravity0.radiuses[3] * Math.sin(theta) * Math.cos(phi);
        double z = Gravity0.radiuses[3] * Math.sin(phi);
        //add to its position
        this.addPos( new double[] { x, y, z } );
        this.addVel(addVel);
    }


    public Spaceship(double mass, double[] pos, double[] vel){
        super(mass, pos, vel);
        fuel = 100; //from 100 usage to 0, should be changed to litters later for more precision tho
    }

    //getter
    public double getFuel(){ return fuel; }


    private void addPos(double[] pos){
        this.pos[0] += pos[0];
        this.pos[1] += pos[1];
        this.pos[2] += pos[2];
    }

    private void addVel(double[] vel){
        this.vel[0] += vel[0];
        this.vel[1] += vel[1];
        this.vel[2] += vel[2];
    }


    //setter
    public void setFuel(double fuel){ this.fuel = fuel; }


}

/*
 * calc where titan will be and shoot there, repeat to account for gravity changrs with a larger dt
 */