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


    public CelestialBody(double mass, double[] innitPos, double[] innitVel){
        setMass(mass);
        setPos(innitPos);
        setVel(innitVel);
        acc = new double[] {0,0,0};

    }

    public CelestialBody(String name, double mass, double[] innitPos, double[] innitVel){
        this.name = name;
        setMass(mass);
        setPos(innitPos);
        setVel(innitVel);

    }

    // constructor for csv file
    public CelestialBody( String name,String x1,String x2,String x3,String v1,String v2,String v3,String m){
        this.name = name;
        setMass(Double.parseDouble(m));
        setRadius(0);
        this.pos[0] = Double.parseDouble(x1);
        this.pos[1] = Double.parseDouble(x2);
        this.pos[2] = Double.parseDouble(x3);

        this.vel[0] = Double.parseDouble(v1);
        this.vel[1] = Double.parseDouble(v2);
        this.vel[2] = Double.parseDouble(v3);

        switch (name) {
            case "Sun" -> this.radius = 696340;
            case "Mercury" -> this.radius = 2439.7;
            case "Venus" -> this.radius = 6051.8;
            case "Earth" -> this.radius = 6371;
            case "Mars" -> this.radius = 3389.5;
            case "Jupiter" -> this.radius = 69911;
            case "Saturn" -> this.radius = 58232;
            case "Uranus" -> this.radius = 25362;
            case "Neptune" -> this.radius = 24622;
            case "Pluto" -> this.radius = 1188.3;
            default -> this.radius = 0;
        }


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
    public double[] getPos(){ return pos; }
    public double[] getVel(){ return vel; }
    public double[] getAcc(){ return acc; }
    public double getForce() { return Math.sqrt(acc[0]*acc[0] + acc[1]*acc[1] + acc[2]*acc[2])*mass;
    }
    public double getMass(){ return mass; }
    public double getRadius(){ return radius; }
    public String getName(){ return name; }

    //setters
    public void setMass(double mass){ this.mass = mass; }
    public void setRadius(double radius){ this.radius = radius; }
    public void setName(String name){ this.name = name; }
    public void setPos(double[] pos){
        this.pos[0] = pos[0];
        this.pos[1] = pos[1];
        this.pos[2] = pos[2];
    }
    public void setVel(double[] vel){
        this.vel[0] = vel[0];
        this.vel[1] = vel[1];
        this.vel[2] = vel[2];
    }
    public void setAcc(double[] acc){
        this.acc[0] = acc[0];
        this.acc[1] = acc[1];
        this.acc[2] = acc[2];
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


    /////////////////////////////// IMPLEMENT
    public CelestialBody clone() {
        return new CelestialBody(this.getName(), this.getMass(), this.getPos(), this.getVel());
    }
}
