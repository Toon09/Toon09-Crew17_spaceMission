package com.example.planets.BackEnd.CelestialEntities;

/*
only spaceships can be affected by wind conditions, not other planets because they are too big

- take mass into account for mass interaction
- from surface
 */
public class StochasticWind {
    private final double maxDistance = 600.0; // in km
    private double titanRadius = 2574.7;

    // parts of the atmosphere // https://www.nasa.gov/mission_pages/cassini/whycassini/cassinif-20070601-05.html

    private double windScalling = 0.0005;
    private double[] v1; private double mag1 = 0.12 *windScalling; // 120 to up
    private double[] v2; private double mag2 = (0.12/2.0) *windScalling; // 60 to 120
    private double[] v3; private double mag3 = 0.005 *windScalling; // 6 to 60
    private double[] v4; private double mag4 = 0.001 *windScalling; // 0.7 to 6


    public double getMagScaling(){
        return windScalling;
    }

    public void setwindScalling(double scale){
        windScalling = scale;
        mag1 *= windScalling;
        mag2 *= windScalling;
        mag3 *= windScalling;
        mag4 *= windScalling;

    }

    /**
     * @param distance it's the distance from the ship to the ship
     * @return boolean that specifies if the wind should be applied to the ship
     */
    private boolean inRange(double distance){
        // the range is from 15 meters from the surface up to the max distance from surface
        return distance < titanRadius + maxDistance && distance > titanRadius + 0.7;
    }

    /**
     * adds a random amount of velocity according to how extreme you want the simulation to be
     * @param ship entity to which the gravity affects
     */
    public void stochasticWind(Spaceship ship, CelestialBody planet, double dt){
        // ends the process if it's not inside the range required
        //System.out.println(ship.getDistance(planet));

        if( !inRange( ship.getDistance(planet) ) )
            return;

        updateWind(ship, planet, dt);

        double[] wind = this.getRange( ship.getDistance(planet) );


        // corresponding wind is added
        ship.addVel( new double[] { wind[0]*dt / ship.getMass(),
                                    wind[1]*dt / ship.getMass(),
                                    wind[2]*dt / ship.getMass() } );

    }

    /*
    initialize with the direction and all fo the ships
     */
    private void updateWind(Spaceship ship, CelestialBody planet, double dt){
        // generates wind vectors if they aren't already
        if(v1 == null)
            v1 = new double[] { 1.0*mag1, 0, 0 };

        if(v2 == null)
            v2 = new double[] { 1.0*mag2, 0, 0 };

        if(v3 == null)
            v3 = new double[] { -1.0*mag3, 0, 0 };

        if(v4 == null)
            v4 = new double[] { 1.0*mag4, 0, 0 };


        // add some randomness to the magnitude
        v1 = randomVectorMaxMagnitude(v1, mag1, dt);
        v2 = randomVectorMaxMagnitude(v2, mag2, dt);
        v3 = randomVectorMaxMagnitude(v3, mag3, dt);
        v4 = randomVectorMaxMagnitude(v4, mag4, dt);

        int a=0;

    }


    /*
   get the velocities and all in relation to the magnitudes and the vectors

   everything goes in same direction, but from 6km to 700m it goes the other
    */
    private double[] getRange(double distance){
        if( distance < maxDistance + titanRadius && distance > 120.0 + titanRadius ){
            return v1;
        } else if ( distance <= 120.0 + titanRadius && distance > 60.0 + titanRadius ) {
            return v2;
        } else if ( distance <= 60.0 + titanRadius && distance > 6.0 + titanRadius) {
            return v3;
        } else {
            return v4;
        }

    }


    /**
     * Generates a vector with a small change for the x, y direction while staying under the max magnitude,
     * the wind can only go up to 20 degrees upwards to avoid it being completely vertical (y is always up)
     * @param vector vector which is going to be have a small change given to, the z direction must be 0 already
     * @param maxMag maximum magnitude of the output vector
     * @param dt
     * @return
     */
    private double[] randomVectorMaxMagnitude(double[] vector, double maxMag, double dt){
        double[] result = {0,0,0};

        if(maxMag <= 0.0)
            return new double[] {0,0,0};

        final double maxMagChange = 0.00005; // max per second is half a meter per second for the magnitude change

        double magnitude = 0.0;
        for(int i=0; i<vector.length; i++)
            magnitude += vector[i]*vector[i];
        magnitude = Math.sqrt(magnitude);


        double newMagnitude = magnitude + ( 2*maxMagChange*Math.random() - maxMagChange )*dt;

        // magnitude should always be positives
        if( Math.abs(newMagnitude) > maxMag ){ // it also goes down intimidatingly if it exceeds this maximum
            if(newMagnitude > 0)
                newMagnitude = maxMag - maxMag*0.1;
            else
                newMagnitude = maxMagChange; // if its lower than 0, we reset it back to a small number
        }

        result[0] = newMagnitude;
        result[1] = 0.0;
        result[2] = 0.0; // make sure its always 0

        return  result;

    }


}
