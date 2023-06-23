package com.example.planets.BackEnd.CelestialEntities;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;

import java.util.Arrays;

/*
only spaceships can be affected by wind conditions, not other planets because they are too big

- take mass into account for mass interaction
- from surface
 */
public class StochasticWind {
    private final double maxDistance = 600.0; // in km

    // parts of the atmosphere // https://www.nasa.gov/mission_pages/cassini/whycassini/cassinif-20070601-05.html
    private double[] v1; private final double mag1 = 0.12/10.0; // 120 to up
    private double[] v2; private final double mag2 = (0.12/2.0)/10.0; // 60 to 120
    private double[] v3; private final double mag3 = 0.001/10.0; // 6 to 60
    private double[] v4; private final double mag4 = 0.0001/10.0; // 0.7 to 6


    /**
     * @param distance its the distance from the ship to the ship
     * @return boolean that specifies if the wind should be applied to the ship
     */
    private boolean inRange(double distance){
        // the range is from 15 meters from the surface up to the max distance from surface
        double titanRadius = 2574.7;
        return distance < titanRadius + maxDistance && distance > titanRadius;
    }

    /**
     * adds a random amount of velocity according to how extreme you want the simulation to be
     * @param ship entity to which the gravity affects
     */
    public void stochasticWind(Spaceship ship, CelestialBody planet, double dt){
        // ends the process if its not inside the range required
        //System.out.println(ship.getDistance(planet));

        if( !inRange( ship.getDistance(planet) ) )
            return;

        System.out.println("fdghjkhgfghjkhgfchjihgfhjkuhygtfrhuji    "+Arrays.toString(v1));

        updateWind(ship, planet, dt);

        double[] wind = this.getRange( ship.getDistance(planet) );


        // corresponding wind is added
        ship.addVel( new double[] { wind[0]*dt,
                                    wind[1]*dt,
                                    wind[2]*dt } );

    }

    /*
    initialize with the direction and all fo the ships
     */
    private void updateWind(Spaceship ship, CelestialBody planet, double dt){
        // vector from ship to planet
        // use spaceship velocity as direction of wind
        // project vector onto x,y axis

        // only in x direction since y is up

        // generates wind vectors if they arent already
        if(v1 == null)
            v1 = new double[] { 1*mag1, 0, 0 };

        if(v2 == null)
            v2 = new double[] { 1*mag2, 0, 0 };

        if(v3 == null)
            v3 = new double[] { -1*mag3, 0, 0 };

        if(v4 == null)
            v4 = new double[] { 1*mag4, 0, 0 };


        // add some randomness to the magnitude
        v1 = randomVectorMaxMagnitude(v1, mag1, dt);
        v2 = randomVectorMaxMagnitude(v2, mag2, dt);
        v3 = randomVectorMaxMagnitude(v3, mag3, dt);
        v4 = randomVectorMaxMagnitude(v4, mag4, dt);


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


    /**
     * Generates a vector with a small change for the x, y direction while staying under the max magnitude,
     * the wind can only go up to 20 degrees upwards to avoid it being completely vertical (y is always up)
     * @param vector vector which is going to be have a small change given to, the z direction must be 0 already
     * @param maxMag maximun magnitude of the output vector
     * @param dt
     * @return
     */
    private double[] randomVectorMaxMagnitude(double[] vector, double maxMag, double dt){
        double[] result = {0,0,0};

        // maximun degree that the wind can go in the upwards direction (20 degrees in radians)
        final double DEG = 20.0 *3.141592654/180.0;

        final double maxAngleChange = 0.5 *3.141592654/180.0; // half a degree up or down is max whta it cna change a second
        final double maxMagChange = 0.00005; // max per second is half a meter per second for the magnitude change

        double deg = Math.atan( vector[1]/vector[0] ); // gets actual value of the degree that the vector currently has

        double magnitude = 0.0;
        for(int i=0; i<vector.length; i++)
            magnitude += vector[i]*vector[i];
        magnitude = Math.sqrt(magnitude);


        double newDeg = deg + ( 2*maxAngleChange*Math.random() - maxAngleChange )*dt;

        if( Math.abs(newDeg) > DEG ){ //exceeding the max value inmidiatly makes it go down
            if(newDeg>0)
                newDeg = DEG - DEG*0.1;
            else
                newDeg = -(DEG - DEG*0.1);
        }


        double newMagnitude = magnitude + ( 2*maxMagChange*Math.random() - maxMagChange )*dt;

        // magnitude should always be positives
        if( newMagnitude > maxMag ){ // it also goes down inmidiatly if it exceeds this maximun
            if(newMagnitude > 0)
                newMagnitude = maxMag - maxMag*0.1;
            else
                newMagnitude = maxMagChange; // if its lower than 0, we reset it back to a small number
        }

        result[0] = Math.cos(newDeg)*newMagnitude;
        result[1] = Math.sin(newDeg)*newMagnitude;
        result[2] = 0; // make sure its always 0

        return  result;
    }


}
