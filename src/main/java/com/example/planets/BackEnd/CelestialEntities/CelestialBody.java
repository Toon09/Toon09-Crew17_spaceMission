package com.example.planets.BackEnd.CelestialEntities;


//to create model of solar system
public class CelestialBody {

    protected String name = "Ship";
    protected double mass = 0;
    protected double radius = 0;

    protected double[] pos = new double[3];
    protected double[] vel = new double[3];
    protected double[] acc = new double[]{0,0,0};


    /**
     * converts a number of days to seconds
     * @param days number of days thats wants to be known in seconds, can be fractional
     * @return amount of seconds in those days
     */
    public static double daysToSec(double days){
        return days*60.0*60.0*24.0;
    }

    /**
     * converts a number of seconds to an amount in days
     * @param sec the number of seconds that wants to be not known in days
     * @return number representing the amount in days, its fractional in case
     *      the seconds dont add up exactly to a precise number of days
     */
    public static double secToDays(double sec){
        return sec/(60.0*60.0*24.0);
    }

    /**
     * Uses pythagorean theorem to calculate the distance between this body
     *      and another which is specified as @param body2
     * @param body2 the other CelestialBody whose distance you want to be calculated too
     * @return double representing the Euclidean distance
     */
    public double getDistance(CelestialBody body2){
        double a = ( this.getPos()[0] - body2.getPos()[0] );
        double b = ( this.getPos()[1] - body2.getPos()[1] );
        double c = ( this.getPos()[2] - body2.getPos()[2] );

        return Math.sqrt( a*a + b*b + c*c );
    }


    /**
     * creates an entity of this class
     * @param mass how much mass the object has in Kg
     * @param innitPos in Km and in the following format:
     *                 [0:x, 1:y, 2:z]
     * @param innitVel in Km and in the same format as the position
     */
    public CelestialBody(double mass, double[] innitPos, double[] innitVel){
        setMass(mass);
        setPos(innitPos);
        setVel(innitVel);
        acc = new double[] {0,0,0};

    }

    /**
     *
     * @param name String name that is attached to this object
     * @param mass how much mass the object has in Kg
     * @param innitPos in Km and in the following format:
     *      *                 [0:x, 1:y, 2:z]
     * @param innitVel in Km and in the same format as the position
     */
    public CelestialBody(String name, double mass, double[] innitPos, double[] innitVel){
        this.name = name;
        setMass(mass);
        setPos(innitPos);
        setVel(innitVel);

    }


    /**
     * takes 2D array and sets the velocity, positions anda ccelerations given in it as the models own,
     * must be in the following format:
     *  arrays of length 3 that contain     [ 0:position, 1:velocity, 2:acceleration ]
     *  values of each coordinate in        [ 0:x, 1:y, 2:z ]
     *
     * @param state array containing positions, velocities and accelerations to be set
     *              in the format given above
     */
    public void setState(double[][] state){
        setPos( state[0] );
        setVel( state[1] );
        setAcc( state[2] );
    }


    //getters

    /**
     * @return a 1D array of doubles that contain the position in Km in the following format:
     *      [ 0:x, 1:y, 2:z ]
     */
    public double[] getPos(){ return pos; }

    /**
     * @return a 1D array of doubles that contain the velocity in Km/s in the following format:
     *      [ 0:x, 1:y, 2:z ]
     */
    public double[] getVel(){ return vel; }

    /**
     * @return a 1D array of doubles that contain the acceleration in Km/s^2 in the following format:
     *      [ 0:x, 1:y, 2:z ]
     */
    public double[] getAcc(){ return acc; }


    /**
     * @return mass in Kg
     */
    public double getMass(){ return mass; }

    // public double getRadius(){ return radius; }

    /**
     * @return name assigned to planet, if any was assigned to it
     */
    public String getName(){ return name; }

    //setters

    /**
     * @param mass mass that is going to replace the one that entity currently has
     */
    private void setMass(double mass){ this.mass = mass; }
    // public void setRadius(double radius){ this.radius = radius; }

    /**
     * Changes the current position to one that is specified as a 1D array of doubles
     *  in this format: [0:x, 1:y, 2:z]
     * @param pos 1D array in the following format
     */
    public void setPos(double[] pos){
        this.pos[0] = pos[0];
        this.pos[1] = pos[1];
        this.pos[2] = pos[2];
    }

    /**
     * Changes the current velocity to one that is specified as a 1D array of doubles
     *  in this format: [0:x, 1:y, 2:z]
     * @param vel 1D array in the following format
     */
    public void setVel(double[] vel){
        this.vel[0] = vel[0];
        this.vel[1] = vel[1];
        this.vel[2] = vel[2];
    }

    /**
     * Changes the current acceleration to one that is specified as a 1D array of doubles
     *  in this format: [0:x, 1:y, 2:z]
     * @param acc 1D array in the following format
     */
    public void setAcc(double[] acc){
        this.acc[0] = acc[0];
        this.acc[1] = acc[1];
        this.acc[2] = acc[2];
    }

    public void addVel(double[] vel){
        this.vel[0] += vel[0];
        this.vel[1] += vel[1];
        this.vel[2] += vel[2];
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if( !(obj instanceof CelestialBody) )
            return false;

        CelestialBody temp = (CelestialBody)obj;

        for(int i=0; i<3; i++){
            if( pos[i] != temp.getPos()[i] )
                return false;
        }

        for(int i=0; i<3; i++){
            if( vel[i] != temp.getVel()[i] )
                return false;
        }

        for(int i=0; i<3; i++){
            if( acc[i] != temp.getAcc()[i] )
                return false;
        }

        return true;
    }

    public CelestialBody clone() {
        return new CelestialBody(this.getName(), this.getMass(), this.getPos(), this.getVel());
    }


}
