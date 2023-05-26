package com.example.planets.BackEnd.Models;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.NumericalMethods.NumSolver;

/*
this method tests the accuracy of different solvers by using 3 1 dimensional
differential equations which solutions we know.

it uses 1 CelestialBody to store all the data and calculate all values
 */
public class TestModel2 implements Model3D{

    private CelestialBody[] bodies;
    private NumSolver numSolver;
    private double time = 1.0;

    /*
    create 2 con
     */

    public TestModel2(NumSolver numsolver){
        this.numSolver = numsolver;

        bodies = new CelestialBody[1];

        double[] innitPos = {3.0, 3.0, 3.0};
        double[] innitVel = {0.0, 0.0, 0.0};

        // add each dim with the initial values of the corresponding eq.
        bodies[0] = new CelestialBody("particle", 0.0, innitPos, innitVel);

    }

    private TestModel2(CelestialBody[] bodies, NumSolver numSolver, double time){
        this.bodies = new CelestialBody[1];
        this.bodies[0] = bodies[0].clone();

        this.numSolver = numSolver;
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
        double[][][] result = new double[this.size()][3/*0:pos, 1:vel, 2:acc*/][3/*x,y,z*/];

        //every body
        for(int i=0; i<this.size(); i++){
            for(int j=0; j<3; j++)
                result[i][0][j] = this.getPos(i)[j];
            for(int j=0; j<3; j++)
                result[i][1][j] = this.getVel(i)[j];
            for(int j=0; j<3; j++)
                result[i][2][j] = this.getAcc(i)[j];

        }

        return result;
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
        bodies[0].setVel(new double[]{getPos(0)[0]/getTime() - 2.0,
                                    getPos(0)[1]/getTime() - 2.0,
                                    getPos(0)[2]/getTime() - 2.0});
    }

    @Override
    public Model3D clone(NumSolver numSolver) {
        return new TestModel2(this.bodies, numSolver, time);
    }

    @Override
    public Model3D clone() {
        return new TestModel2(this.bodies, numSolver, time);
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

    public double getActualValue(double t) {
        return t*(3.0 - 2.0*Math.log(t));
    }

}