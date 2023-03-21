package com.example.planets.BackEnd;

public class Spaceship extends CelestialBody {
    //speed limit 60 km/s
    private double fuel;
    public Spaceship(){
        fuel = 100; //from 100 usage to 0, should be changed to litters later for more precision tho
    }

    //getter
    public double getFuel(){ return fuel; }
    public void addAcc(double[] acc){
        this.acc[0] += acc[0];
        this.acc[1] += acc[1];
        this.acc[2] += acc[2];
    }

    //setter
    public void setFuel(double fuel){ this.fuel = fuel; }


}