package com.example.planets.BackEnd.Models;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.NumericalMethods.NumSolver;

/*
this method tests the accuracy of different solvers by using 3 1 dimensional
differential equations which solutions we know.

it uses 1 CelestialBody to store all the data and calculate all values
 */
public class TestModel1 implements Model3D{

    private CelestialBody[] bodies;
    private NumSolver numSolver;
    private double time = 0.0;

    /*
    create 2 con
     */

    public TestModel1(NumSolver numsolver){
        this.numSolver = numsolver;

        bodies = new CelestialBody[1];

        double[] innitPos = {0.0, 0.0, 0.0};
        double[] innitVel = {0.0, 0.0, 0.0};

        // add each dim with the initial values of the corresponding eq.
        bodies[0] = new CelestialBody("particle", 0.0, innitPos, innitVel);

    }

    // setters
    @Override
    public void setPos(int index, double[] pos) { bodies[index].setPos(pos); }
    @Override
    public void setVel(int index, double[] vel) { bodies[index].setVel(vel); }
    @Override
    public void setAcc(int index, double[] acc) { bodies[index].setAcc(acc); }
    @Override
    public void setSolver(NumSolver numSolver) { this.numSolver = numSolver; }

    // getters
    @Override
    public double[] getPos(int index) { return bodies[index].getPos(); }
    @Override
    public double[] getVel(int index) { return bodies[index].getVel(); }
    @Override
    public double[] getAcc(int index) { return bodies[index].getAcc(); }
    @Override
    public void setState(double[][][] state) { bodies[0].setState(state[0]); }
    @Override
    public double getTime() { return time; }
    @Override
    public String getSolverName() { return numSolver.getName(); }
    @Override
    public double[][][] getState() {
        return new double[][][] { {bodies[0].getPos()}, {bodies[0].getVel()}, {bodies[0].getAcc()} };
    }
    @Override
    public CelestialBody getBody(int index) { return bodies[0]; }

    @Override
    public Spaceship getShip() { return null; }

    @Override
    public Spaceship getShip(int index) { return null; }

    /// functionality

    @Override
    public int size() { return 1; }

    @Override
    public void addDt(double dt) { time += dt; }

    @Override
    public void addShips(int numShips) {}

    @Override
    public int getAmountOfShips() {
        return 0;
    }

    @Override
    public void hDeriv() {
        bodies[0].setAcc(new double[]{ Math.cos(getTime()) - getAcc(0)[0]/3,
                                    Math.cos(getTime()) - getAcc(0)[1]/3,
                                    Math.cos(getTime()) - getAcc(0)[2]/3 });
    }

    @Override
    public Model3D clone(NumSolver numSolver) {
        return null;
    }

    @Override
    public Model3D clone() {
        return null;
    }

    @Override
    public void updatePos(double time, double dt, boolean inDays) {
        hDeriv();
        if( inDays ){
            for(int i=0; i<CelestialBody.daysToSec(time)/Math.abs(dt); i++ )
                numSolver.step(this, dt);
        //uses seconds to calculate how long to run
        }else{
            for(int i=0; i<time/Math.abs(dt); i++ )
                numSolver.step(this, dt);
            
        }
    }

    public double actualValue(double t) {
        return 9/10*Math.sin(t) + 3/10*(Math.cos(t) - Math.exp(-t/3));
    }

}
