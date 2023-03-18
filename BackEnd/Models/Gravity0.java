package BackEnd.Models;

import BackEnd.CelestialBody;

public class Gravity0 implements Model {
    /*
     * 3D model
     */

    private CelestialBody[] bodies;

    public Gravity0(){
        bodies = new CelestialBody[0];
    }

    public void addBody(CelestialBody body){
        CelestialBody[] temp = new CelestialBody[ bodies.length + 1 ];
        for(int i=0; i<bodies.length; i++){
            temp[i] = bodies[i];
        }
        temp[temp.length-1] = body;

        bodies = temp;
    }

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

    @Override
    public void setX(double x) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setX'");
    }

    @Override
    public void setY(double y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setY'");
    }

    @Override
    public void setYD(double yD) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setYD'");
    }

    @Override
    public double getX() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getX'");
    }

    @Override
    public double getY() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getY'");
    }

    @Override
    public double _1Deriv() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method '_1Deriv'");
    }

    @Override
    public double _2Deriv() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method '_2Deriv'");
    }

}
