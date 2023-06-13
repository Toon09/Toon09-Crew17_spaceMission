package com.example.planets.BackEnd.CelestialEntities;

public class LandingModule extends Spaceship{
    private Controler controler;
    public LandingModule(double mass, double[] pos, double[] vel, Controler controler) {
        super(mass, pos, vel);
        this.controler = controler;
    }
}
