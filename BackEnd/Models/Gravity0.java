package BackEnd.Models;

import BackEnd.CelestialBody;

public class Gravity0 implements Model {
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

    //settings
    public void setPos(int index, double[] pos){ bodies[index].setPos(pos); }
    public void setVel(int index, double[] vel){ bodies[index].setPos(vel); }
    public void setAcc(int index, double[] acc){ bodies[index].setAcc(acc); }




    

    @Override
    public double _1Deriv() {

        //update all celestial bodies one by one with one another

        return 0; //doesnt return a val in specific bc its 3d, instead is saved in the bodies themselves
    }

    @Override
    public double _2Deriv() {


        return 0; //doesnt return a val in specific bc its 3d, instead is saved in the bodies themselves
    }



    
    // ########################### DO NOT IMPLEMENT

    @Override //////////////do not implement
    public void setX(double x) {
        throw new UnsupportedOperationException("Unimplemented method 'setX'");
    }
 
    @Override //////////////do not implement
    public void setY(double y) {
        throw new UnsupportedOperationException("Unimplemented method 'setY'");
    }

    @Override //////////////do not implement
    public double getX() {
        throw new UnsupportedOperationException("Unimplemented method 'getX'");
    }

    @Override //////////////do not implement
    public double getY() {
        throw new UnsupportedOperationException("Unimplemented method 'getY'");
    }

    @Override //////////////do not implement
    public void setYD(double yD) {
        throw new UnsupportedOperationException("Unimplemented method 'setYD'");
    }
}
