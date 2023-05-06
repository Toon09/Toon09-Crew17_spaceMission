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

    /**
     * returns a 3D array of doubles that contains the position, velocity and acceleration of every
     * object in the model, in this format:
     *                                      [ represents each element in the model in the same order ]
     *  arrays of length 3 that contain     [ 0:position, 1:velocity, 2:acceleration ]
     *  values of each coordinate in        [ 0:x, 1:y, 2:z ]
     *
     * @return a 3D arrays with the characteristics described above
     */
    public double[][][] getState(); //returns all values of velicity and acceleration in an ordered format

    //updating
    public void updatePos(double time, double dt, boolean inDays);

    
}
