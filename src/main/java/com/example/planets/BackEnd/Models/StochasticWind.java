package com.example.planets.BackEnd.Models;

import com.example.planets.BackEnd.CelestialEntities.Spaceship;

/*
only spaceships can be affected by wind conditions, not other planets because they are too big
 */
public class StochasticWind {

    private double maximunStrentgh = 12.0; // in km/s
    private double maxDistance = 300.0; // in km

    /**
     * @return maximum distance from the target on which the wind will take effect
     */
    public double getDistance(){
        return maxDistance;
    }

    /**
     * adds a random amount of velocity according to how extreme you want the simulation to be
     * @param ship entity to which the gravity affects
     */
    public void stochasticWind(Spaceship ship){
        ship.setVel( new double[] { ship.getVel()[0] + Math.random()*maximunStrentgh - maximunStrentgh/2.0,
                                    ship.getVel()[1] + Math.random()*maximunStrentgh - maximunStrentgh/2.0,
                                    ship.getVel()[2] + Math.random()*maximunStrentgh - maximunStrentgh/2.0, } );

    }

    /*
    make it so that the ship gets added velocity in a random direction
    random direction should gradually change with time, not all of a sudden
     */


}
