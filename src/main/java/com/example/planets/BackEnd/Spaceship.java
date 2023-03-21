package com.example.planets.BackEnd;

public class Spaceship extends CelestialBody {
    private double fuel;
    //speed limit is 60km/s (starting)

    public Spaceship(){
        fuel = 100; //from 100 usage to 0, should be changed to litters later for more precision tho
    }

    //getter
    public double getFuel(){ return fuel; }

    /**
     * this method
     * @param vel the velocity desired tanglential to the rocket to reach the planet
     */
    public void addAcc(double vel){
        //
    }

    //setter
    public void setFuel(double fuel){ this.fuel = fuel; }


}

/*
 * calc where titan will be and shoot there, repeat to account for gravity changrs with a larger dt
 */