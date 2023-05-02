package com.example.planets.BackEnd.Models;

import com.example.planets.BackEnd.CelestialBody;
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
    public int size();

    //derivs
    public void _2Deriv();
    public double[] _2DerivInd(int index); //gets the values of the second derivative per individual celestial body
    public Model3D clone(NumSolver numSolver);
    public double[][][] getState(); //returns all values of velicity and acceleration in an ordered format

    //updating
    public void updatePos(double time, double dt, boolean inDays);

    
}
