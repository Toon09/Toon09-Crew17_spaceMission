package com.example.planets.BackEnd.Models;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.NumericalMethods.NumSolver;

public interface Model3D {

    //setters
    public void setPos(int index, double[] pos);
    public void setVel(int index, double[] vel);
    public void setAcc(int index, double[] acc);
    public void addDt(double dt);

    public void setSolver(NumSolver numSolver);

    //getters
    public double[] getPos(int index);
    public double[] getVel(int index);
    public double[] getAcc(int index);
    public double getTime();
    public String getSolverName();

    //elements
    public CelestialBody getBody(int index);
    public Spaceship getShip();
    public int size();

    //derivs
    public void hDeriv();
    public Model3D clone(NumSolver numSolver);
    public double[][][] getState(); //returns all values of velicity and acceleration in an ordered format

    //updating
    public void updatePos(double time, double dt, boolean inDays);

    
}
