package com.example.planets.BackEnd.Models;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.NumericalMethods.NumSolver;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.NumericalMethods.RK4;
import com.example.planets.BackEnd.Trajectory.Cost.CostFunction;
import com.example.planets.BackEnd.Trajectory.LandingModel;
import com.example.planets.Data.DataGetter;


public class Gravity0 implements Model3D {
    public static final double G = 6.6743 * Math.pow(10, -20);
    private CelestialBody[] bodies;
    private int spaceShipStart = 0;
    private int amountOfShips = 0;
    private NumSolver numSolver;
    private int amountOfModules = 0;
    //spaceShipStart
    //amountOfShips

    private double time = 0;



    /**
     * This constructor is mainly used in the copy function, it doesn't need
     *
     * @param bodies    1D array of CelestialBodies which is used to copy each one of them
     * @param numSolver type of numerical solver to be used
     */
    private Gravity0(CelestialBody[] bodies, NumSolver numSolver, int amountOfShips, int spaceShipStart, double time) {
        this.numSolver = numSolver;
        this.time = time;

        if (amountOfShips == 0)
            amountOfShips = 1;

        this.bodies = new CelestialBody[bodies.length]; // error with when there are ships and no ships
        for (int i = 0; i < bodies.length; i++) {
            if (bodies[i] instanceof Spaceship) {
                this.bodies[i] = ((Spaceship) bodies[i]).clone();
            } else {
                this.bodies[i] = new CelestialBody(bodies[i].getName(), bodies[i].getMass(), bodies[i].getPos(), bodies[i].getVel());

            }
            this.amountOfShips = amountOfShips;
            this.spaceShipStart = spaceShipStart;
        }

    }

    //for landing
    public Gravity0(CelestialBody titan, LandingModel module) {

        this.amountOfShips = 1;
        this.spaceShipStart = 1;
        this.bodies = new CelestialBody[2];
        this.bodies[0] = titan;
        this.bodies[1]= module;
        this.numSolver = new RK4();
    }


    /**
     * Constructor made to also have a singular spaceship in the model, starting at the surface
     * of the earth as specified by the parameters @param longitude and @param latitude
     *
     * @param longitude
     * @param latitude
     * @param numSolver type of numerical solver to be used
     */
    public Gravity0(double longitude, double latitude, NumSolver numSolver) {
        this.numSolver = numSolver;

        this.bodies = new CelestialBody[positions.length + 1];
        for (int i = 0; i < this.bodies.length - 1; i++) {
            this.bodies[i] = new CelestialBody(names[i], mass[i], positions[i], velocity[i]);
            spaceShipStart = i + 1;
        }
        this.bodies[this.bodies.length - 1] = new Spaceship(50000, positions[3], velocity[3], longitude, latitude);
        amountOfShips = 1;

    }


    /**
     * Constructor made to also have a singular spaceship in the model, starting at the surface
     * of the earth as specified by the parameters @param longitude and @param latitude
     *
     * @param longitude
     * @param latitude
     * @param numSolver  type of numerical solver to be used
     * @param folderName
     */
    public Gravity0(double longitude, double latitude, NumSolver numSolver, String folderName) {
        this.numSolver = numSolver;

        DataGetter dataGet = new DataGetter();
        //double[][] sun = dataGet.getTxtExpData(0, folderName + "/Sun.txt"); //data for the sun to center
        double[][] data;
        // save coors of the sun and do data-sun for all
        bodies = new CelestialBody[positions.length + 1];
        for (int i = 0; i < bodies.length - 1; i++) {
            // getting data for the planet
            data = dataGet.getTxtExpData(0, folderName + "/" + names[i] + ".txt"); //gets only first state

            // creating bodies with said data
            this.bodies[i] = new CelestialBody(names[i], mass[i], data[0], data[1]);
            spaceShipStart = i + 1;
        }

        data = dataGet.getTxtExpData(0, folderName + "/Earth.txt");

        this.bodies[this.bodies.length - 1] = new Spaceship(50000, data[0], data[1], longitude, latitude);
        amountOfShips = 1;

    }


    /**
     * creates a model of the solar system along with a plan to get to a specified planet
     * using a specified number of stages
     * Uses the positions of the planets to be at april 1st of 2023
     *
     * @param longitude
     * @param latitude
     * @param numSolver
     * @param targetPlanet
     * @param numberOfStages
     */
    public Gravity0(double longitude, double latitude, NumSolver numSolver, String targetPlanet,
                    int numberOfStages, int maxDays, CostFunction cost) {
        this.numSolver = numSolver;

        this.bodies = new CelestialBody[positions.length + 1];
        for (int i = 0; i < this.bodies.length - 1; i++) {
            this.bodies[i] = new CelestialBody(names[i], mass[i], positions[i], velocity[i]);
            spaceShipStart = i + 1;
        }
        this.bodies[this.bodies.length - 1] = new Spaceship(50000, positions[3], velocity[3],
                longitude, latitude, cost);

        // make plan
        this.getShip().makePlan(this, targetPlanet, numberOfStages, maxDays);

        // get target planet and set it as it in ship
        for (CelestialBody bod : bodies) {
            if (bod.getName().equalsIgnoreCase(targetPlanet)) {
                this.getShip().setTarget(bod);
                break;
            }
        }

    }


    public Gravity0(double longitude, double latitude, NumSolver numSolver, double[][] plan) {
        this.numSolver = numSolver;

        this.bodies = new CelestialBody[positions.length + 1];
        for (int i = 0; i < this.bodies.length - 1; i++) {
            this.bodies[i] = new CelestialBody(names[i], mass[i], positions[i], velocity[i]);
            spaceShipStart = i + 1;
        }
        this.bodies[this.bodies.length - 1] = new Spaceship(50000, positions[3], velocity[3], longitude, latitude);
        this.getShip().setPlan(plan);
        amountOfShips = 1;
        this.plan = plan;

    }

    private double[][] plan;

    @Override
    public void addShips(int numShips) {
        // ships are always added at the end of array
        CelestialBody[] newBodies = new CelestialBody[bodies.length + numShips];

        for (int i = 0; i < bodies.length; i++)
            newBodies[i] = bodies[i];

        for (int i = bodies.length; i < bodies.length + numShips; i++)
            newBodies[i] = getShip().clone();

        amountOfShips += numShips;
        bodies = newBodies;
    }


    @Override
    public Spaceship getShip() {
        if (this.getBody(this.size() - 1) instanceof Spaceship)
            return (Spaceship) this.getBody(this.size() - 1);
        else
            return null;
    }

    @Override
    public Spaceship getShip(int index) {
        if (spaceShipStart == 0)
            return getShip();
        else
            return (Spaceship) bodies[spaceShipStart + index]; //spaceShipStart indicates start of all spaceship instances
    }

    @Override
    public int getAmountOfShips() {
        return amountOfShips;
    }

    @Override
    public void setSolver(NumSolver numSolver) {
        this.numSolver = numSolver;
    }

    @Override
    public void updatePos(double time, double dt, boolean days) {
        hDeriv(); //gets the acceleration at the starting point..
        //.. so that the velocity doesn't take the acc as 0 at the start

        //uses the unit of days to calculate how long to run the simulation for
        if (days) {
            for (int i = 0; i < CelestialBody.daysToSec(time) / Math.abs(dt); i++)
                numSolver.step(this, dt);
            //uses seconds to calculate how long to run
        } else {
            for (int i = 0; i < time / Math.abs(dt); i++)
                numSolver.step(this, dt);
        }


    }


    @Override
    public double[][][] getState() {
        double[][][] result = new double[this.size()][3/*0:pos, 1:vel, 2:acc*/][3/*x,y,z*/];

        //every body
        for (int i = 0; i < this.size(); i++) {
            for (int j = 0; j < 3; j++)
                result[i][0][j] = this.getPos(i)[j];
            for (int j = 0; j < 3; j++)
                result[i][1][j] = this.getVel(i)[j];
            for (int j = 0; j < 3; j++)
                result[i][2][j] = this.getAcc(i)[j];

        }

        return result;
    }

    @Override
    public void setState(double[][][] state) {
        for (int i = 0; i < bodies.length; i++)
            bodies[i].setState(state[i]);

    }


    //make function that returns the rocket
    @Override
    public CelestialBody getBody(int index) {
        return bodies[index];
    }

    @Override
    public int size() {
        return bodies.length;
    }


    //setters
    @Override
    public void setPos(int index, double[] pos) {
        bodies[index].setPos(pos);
    }

    @Override
    public void setVel(int index, double[] vel) {
        bodies[index].setVel(vel);
    }

    @Override
    public void setAcc(int index, double[] acc) {
        bodies[index].setAcc(acc);
    }

    @Override
    public void addDt(double dt) {
        time += dt;
    }


    //getters
    @Override
    public double[] getPos(int index) {
        return bodies[index].getPos();
    }

    @Override
    public double[] getVel(int index) {
        return bodies[index].getVel();
    }

    @Override
    public double[] getAcc(int index) {
        return bodies[index].getAcc();
    }

    @Override
    public double getTime() {
        return time;
    }


    //updates all derivs
    @Override
    public void hDeriv() {
        //must sum up all values of gravities with all other bodies

        double dist = 0;
        for (int i = 0; i < bodies.length; i++) {
            bodies[i].setAcc(new double[]{0, 0, 0}); //reset to 0

            for (int j = 0; j < bodies.length; j++) {
                if (j == i || bodies[j] instanceof Spaceship) {
                    continue;
                }
                //calc distance between 2
                dist = bodies[i].getDistance(bodies[j]);

                dist = dist * dist * dist;

                //adding in dims
                bodies[i].setAcc(new double[]{bodies[i].getAcc()[0] + G * bodies[j].getMass() * (bodies[j].getPos()[0] - bodies[i].getPos()[0]) / dist,
                        bodies[i].getAcc()[1] + G * bodies[j].getMass() * (bodies[j].getPos()[1] - bodies[i].getPos()[1]) / dist,
                        bodies[i].getAcc()[2] + G * bodies[j].getMass() * (bodies[j].getPos()[2] - bodies[i].getPos()[2]) / dist});

            }

        }

    }

    public CelestialBody[] getBodies(){
        return bodies;
    }

    @Override
    public String getSolverName() {
        return numSolver.getName();
    }


    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < bodies.length; i++) {
            result += bodies[i].toString() + "; ";
        }

        return result;
    }

    //spaceShipStart
    //amountOfShips

    @Override
    public Model3D clone(NumSolver numSolver) {
        return new Gravity0(this.bodies, numSolver, amountOfShips, spaceShipStart, time);
    }

    @Override
    public Model3D clone() {
        return new Gravity0(this.bodies, this.numSolver, amountOfShips, spaceShipStart, time);
    }

    public final static double[][] positions = {{0, 0, 0}, {7.83e6, 4.49e7, 2.87e6}, {-2.82e7, 1.04e8, 3.01e6},
            {-1.48e8, -2.78e7, 3.37e4}, {-1.48e8, -2.75e7, 7.02e4}, {-1.59e8, 1.89e8, 7.87e6},
            {6.93e8, 2.59e8, -1.66e7}, {1.25e9, -7.60e8, -3.67e7}, {1.25e9, -7.61e8, -3.63e7},
            {4.45e9, -3.98e8, -9.45e7}, {1.96e9, 2.19e9, -1.72e7}};

    public final static double[][] velocity = {{0, 0, 0}, {-5.75e1, 1.15e1, 6.22e0}, {-3.40e1, -8.97e0, 1.84e0},
            {5.05e0, -2.94e1, 1.71e-3}, {4.34e0, -3.00e1, -1.16e-2}, {-1.77e1, -1.35e1, 1.52e-1},
            {-4.71e0, 1.29e1, 5.22e-2}, {4.47e0, 8.24e0, -3.21e-1}, {9.00e0, 1.11e1, -2.25e0},
            {4.48e-1, 5.45e0, -1.23e-1}, {-5.13e0, 4.22e0, 8.21e-2}};

    public final static double[] mass = {1.99e30, 3.30e23, 4.87e24, 5.97e24, 7.35e22, 6.42e23,
            1.90e27, 5.68e26, 1.35e23, 1.02e26, 8.68e25};

    public final static double[] radiuses = {696340, 2439.7, 6051.8, 6371, 1737.4, 3390, 69911,
            58232, 2574.7, 24622, 25362};

    public final static String[] names = {"Sun", "Mercury", "Venus", "Earth", "Moon", "Mars", "Jupiter",
            "Saturn", "Titan", "Neptune", "Uranus"};


}
