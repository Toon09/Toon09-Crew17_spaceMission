package com.example.planets.BackEnd.Models;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.NumericalMethods.NumSolver;

public interface Model3D {

    // ####################################################################### setters

    /**
     * changes position of every object to the specified one, it requires a certain a format:
     *      These are the positions of each dimension [0:x, 1:y, 2:z]
     *
     * @param index index that refers to the celestial body that wants the position to be changed
     * @param pos 3D array of doubles that specifies the position in the x, y and z direction in the format given
     *            above
     */
    public void setPos(int index, double[] pos);

    /**
     * changes velocity of every object to the specified one, it requires a certain a format:
     *  These are the positions of each dimension [0:x, 1:y, 2:z]
     *
     * @param index index that refers to the celestial body that wants the velocity to be changed
     * @param vel 3D array of doubles that specifies the velocity in the x, y and z direction in the format given
     *            above
     */
    public void setVel(int index, double[] vel);

    /**
     * changes acceleration of every object to the specified one, it requires a certain a format:
     *      These are the positions of each dimension [0:x, 1:y, 2:z]
     *
     * @param index index that refers to the celestial body that wants the acceleration to be changed
     * @param acc 3D array of doubles that specifies the acceleration in the x, y and z direction in the format given
     *            above
     */
    public void setAcc(int index, double[] acc);

    /**
     * Changes the solver from the previous one to the new one, the solver must extend
     *      NumSolver for it to be used as one
     * @param numSolver numerical solver that wants to be used to run the simulation
     */
    public void setSolver(NumSolver numSolver);

    /**
     * Adds the time passed to the current count of the time
     * @param dt amount of time elapsed that wants to be added to count
     */
    public void addDt(double dt);

    // ####################################################################### getters

    /**
     * Returns a 3D array of positions from an object identified by an index, the format is the following:
     *      [0:x, 1:y, 2:z]
     * @param index index that refers to the celestial body whose positions needs to be accessed
     * @return a 3D array of positions of the object specified in the format shown above
     */
    public double[] getPos(int index);

    /**
     * Returns a 3D array of velocities from an object identified by an index, the format is the following:
     *      [0:x, 1:y, 2:z]
     * @param index index that refers to the celestial body whose velocities needs to be accessed
     * @return a 3D array of velocities of the object specified in the format shown above
     */
    public double[] getVel(int index);

    /**
     * Returns a 3D array of accelerations from an object identified by an index, the format is the following:
     *      [0:x, 1:y, 2:z]
     * @param index index that refers to the celestial body whose accelerations needs to be accessed
     * @return a 3D array of accelerations of the object specified in the format shown above
     */
    public double[] getAcc(int index);

    /**
     * takes in 3D array of values and takes makes the model match the state given,
     * input array @states must be in the following format:
     *                                      [ represents each element in the model in the same order ]
     *  arrays of length 3 that contain     [ 0:position, 1:velocity, 2:acceleration ]
     *  values of each coordinate in        [ 0:x, 1:y, 2:z ]
     *
     * @param state a 3D array of values in the same format as described above
     */
    void setState(double[][][] state);

    /**
     * Gets in simulation time, meaning how many time steps have been taken on the model so far
     * @return double representing the amount of time that has passed i nthe simulation
     */
    public double getTime();

    /**
     * Way to identify the solver that is being used without having to check types
     * @return string representation of the name of the solver thats being used
     */
    public String getSolverName();

    /**
     * Returns a specified celestial object
     * @param index index that identifies a specific celestial object that wants to be accessed
     * @return the specified celestial object
     */
    public CelestialBody getBody(int index);

    /**
     * Gets a single spaceship from the model
     * @return returns 1 spaceship from the model
     */
    public Spaceship getShip();

    /**
     * @return amount of Celestial bodies (including spaceShips)
     */
    public int size();

    /**
     * returns a 3D array of doubles that contains the position, velocity and acceleration of every
     * object in the model, in this format:
     *                                      [ represents each element in the model in the same order ]
     *  arrays of length 3 that contain     [ 0:position, 1:velocity, 2:acceleration ]
     *  values of each coordinate in        [ 0:x, 1:y, 2:z ]
     *
     * @return a 3D arrays with the characteristics described above
     */
    public double[][][] getState();

    /**
     * @return returns the model currently being used by the model
     */
    public NumSolver getSolver();

    // ####################################################################### operations

    /**
     * Calculates highest derivative and saves the changes in-place to the value of it
     */
    public void hDeriv();

    /**
     * Makes a copy (no references, only values) of the object, it also lets you
     *      decide which numerical solver should this copy have
     * @param numSolver the numerical solver the copy of the model should have
     * @return perfect copy of another model with a specified numerical solver
     */
    public Model3D clone(NumSolver numSolver);

    /**
     *
     * @param time amount of units of time (in seconds or days depending on @days) that it wants to be run for
     * @param dt time step (always in seconds) which will be using
     * @param inDays true if you are imputting the amount of days to run for, false if in seconds
     */
    public void updatePos(double time, double dt, boolean inDays);

}
