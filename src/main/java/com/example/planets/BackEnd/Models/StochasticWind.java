package com.example.planets.BackEnd.Models;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;

/*
only spaceships can be affected by wind conditions, not other planets because they are too big

- take mass into account for mass interaction
- from surface
 */
public class StochasticWind {
    private final double maxDistance = 600.0; // in km

    // parts of the atmosphere // https://www.nasa.gov/mission_pages/cassini/whycassini/cassinif-20070601-05.html
    private double[] v1 = {0, 0, 0}; private final double mag1 = 0.12; // 120 to up
    private double[] v2 = {0, 0, 0}; private final double mag2 = 0.12/2.0; // 60 to 120
    private double[] v3 = {0, 0, 0}; private final double mag3 = 0.001; // 6 to 60
    private double[] v4 = {0, 0, 0}; private final double mag4 = 0.0001; // 0.7 to 6


    /**
     * @param distance its the distance from the ship to the ship
     * @return boolean that specifies if the wind should be applied to the ship
     */
    public boolean inRange(double distance){
        // the range is from 15 meters from the surface up to the max distance from surface
        double titanRadius = 6200.0;
        return distance < titanRadius + maxDistance && distance > titanRadius;
    }

    /**
     * adds a random amount of velocity according to how extreme you want the simulation to be
     * @param ship entity to which the gravity affects
     */
    public void stochasticWind(Spaceship ship, CelestialBody planet, double dt){
        updateWind(ship, planet, dt);

        double[] wind = this.getRange( ship.getDistance(planet) );

        // corresponding wind is added
        ship.setVel( new double[] { ship.getVel()[0] + wind[0]*dt,
                                    ship.getVel()[1] + wind[1]*dt,
                                    ship.getVel()[2] + wind[2]*dt } );

    }

    /*
    get the velocities and all in relation to the magnitudes and the vectors

    everything goes in same direction, but from 6km to 700m it goes the other
     */
    private double[] getRange(double distance){
        if( distance < maxDistance && distance > 120.0 ){
            return v1;
        } else if ( distance <= 120.0 && distance > 60.0 ) {
            return v2;
        } else if ( distance <= 60.0 && distance > 6.0 ) {
            return v3;
        } else {
            return v4;
        }

    }

    /*
    initialize with the direction and all fo the ships
     */
    private void updateWind(Spaceship ship, CelestialBody planet, double dt){
        // vector from ship to planet
        // use spaceship velocity as direction of wind
    }


}



/*
direction of wind is just in x, y with the direction of the spaceship as the dir

Hohmann you set the z velocity to be the same as the target, then you do hohman in x and y dir normally
    hohmann is simplified
    fuel goes up

 */