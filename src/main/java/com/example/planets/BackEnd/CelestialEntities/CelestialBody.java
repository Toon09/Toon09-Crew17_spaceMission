package com.example.planets.BackEnd.CelestialEntities;


//to create model of solar system
public class CelestialBody {

    protected String name = "Ship";
    protected double mass = 0;
    protected double radius = 0;

    protected double[] pos = new double[3];
    protected double[] vel = new double[3];
    protected double[] acc = new double[]{0,0,0};


    public static double daysToSec(double days){
        return days*60*60*24;
    }

    public static long secToDays(long sec){
        return sec/(60*60*24);
    }

    public static double getDistance(CelestialBody body1, CelestialBody body2){
        double a = ( body1.getPos()[0] - body2.getPos()[0] );
        double b = ( body1.getPos()[1] - body2.getPos()[1] );
        double c = ( body1.getPos()[2] - body2.getPos()[2] );

        return Math.sqrt( a*a + b*b + c*c );
    }

    public CelestialBody(){}


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
        
        switch(name){
            case "Sun":
                this.radius = 696340;
                break;
            case "Mercury":
                this.radius = 2439.7;
                break;
            case "Venus":
                this.radius = 6051.8;
                break;
            case "Earth":
                this.radius = 6371;
                break;
            case "Mars":
                this.radius = 3389.5;
                break;
            case "Jupiter":
                this.radius = 69911;
                break;
            case "Saturn":
                this.radius = 58232;
                break;
            case "Uranus":
                this.radius = 25362;
                break;
            case "Neptune":
                this.radius = 24622;
                break;
            case "Pluto":
                this.radius = 1188.3;
                break;
            default:
                this.radius = 0;
                break;
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
