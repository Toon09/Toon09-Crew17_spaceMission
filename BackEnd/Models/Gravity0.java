package BackEnd.Models;

import BackEnd.CelestialBody;

public class Gravity0 implements Model3D {
    /*
     * 3D model
     * model is:
     * 
     * k'' = G*(k' - k)/r^3
     * 
     * k is 3d
     * r = distance from object i to another object j; i != j
     * G =  6.6743*10^(-20)
     */

    private static final double G = 6.6743 * Math.pow(10, -20);
    private CelestialBody[] bodies;

    public Gravity0(){
        bodies = new CelestialBody[0];
    }

    public CelestialBody getBody(int index){ return bodies[index]; }
    public int size(){ return bodies.length; }

    //only for 1 body
    public void addBody(CelestialBody body){
        CelestialBody[] temp = new CelestialBody[ bodies.length + 1 ];
        for(int i=0; i<bodies.length; i++){
            temp[i] = bodies[i];
        }
        temp[temp.length-1] = body;

        bodies = temp;
    }

    //for a set of bodies
    public void addBody(CelestialBody[] Nbody){
        CelestialBody[] temp = new CelestialBody[ bodies.length + Nbody.length ];

        for(int i=0; i<bodies.length; i++){
            temp[i] = bodies[i];
        }

        for(int i=bodies.length; i<temp.length; i++){
            temp[i] = Nbody[i-bodies.length];
        }

        bodies = temp;
    }

    public double getDistance(CelestialBody body1, CelestialBody body2){
        double a = ( body1.getPos()[0] - body2.getPos()[0] );
        double b = ( body1.getPos()[1] - body2.getPos()[1] );
        double c = ( body1.getPos()[2] - body2.getPos()[2] );

        return Math.sqrt( a*a + b*b + c*c );
    }

    //setters
    @Override
    public void setPos(int index, double[] pos){ bodies[index].setPos(pos); }
    @Override
    public void setVel(int index, double[] vel){ bodies[index].setPos(vel); }
    @Override
    public void setAcc(int index, double[] acc){ bodies[index].setAcc(acc); }


    //getters
    @Override
    public double[] getPos(int index) { return bodies[index].getPos(); }
    @Override
    public double[] getVel(int index) { return bodies[index].getVel(); }
    @Override
    public double[] getAcc(int index) { return bodies[index].getAcc(); }



    ///////////////////////////////// calc derivs derivs
    @Override
    public void _1Deriv(int index) {

    }

    @Override
    public void _2Deriv(int index) {

    }

    



}
