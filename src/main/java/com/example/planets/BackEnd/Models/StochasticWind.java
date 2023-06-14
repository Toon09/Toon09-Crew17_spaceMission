package com.example.planets.BackEnd.Models;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;

/*
only spaceships can be affected by wind conditions, not other planets because they are too big

- take mass into account for mass interaction
- from surface
 */
public class StochasticWind {

    private double maximunStrentgh = 12.0; // in km/s
    private double maxDistance = 300.0; // in km

    /**
     * @param distance its the distance from the ship to the ship
     * @return boolean that specifies if the wind should be applied to the ship
     */
    public boolean getDistance(double distance){
        // the range is from 15 meters from the surface up to the max distance from surface
        double titanRadius = 6200.0;
        return distance < titanRadius + maxDistance && distance > titanRadius + 15.0;
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
