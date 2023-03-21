package com.example.planets.BackEnd.Models;

import com.example.planets.BackEnd.CelestialBody;
import com.example.planets.BackEnd.NumericalMethods.Eulers;

public class Gravity0 implements Model3D {
    public static final double G = 6.6743 * Math.pow(10, -20);
    private CelestialBody[] bodies;


    //days is how many days to compute at a time
    public void updatePos(double days, double dt){
        for(int i=0; i<CelestialBody.daysToSec(days)/dt ;i++ ){
            Eulers.step3D(this, dt);
        }
    }

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

    

    //setters
    @Override
    public void setPos(int index, double[] pos){ bodies[index].setPos(pos); }
    @Override
    public void setVel(int index, double[] vel){ bodies[index].setVel(vel); }
    @Override
    public void setAcc(int index, double[] acc){ bodies[index].setAcc(acc); }
    @Override
    public void addDt(double dt) { CelestialBody.addDt(dt); }
    


    //getters
    @Override
    public double[] getPos(int index) { return bodies[index].getPos(); }
    @Override
    public double[] getVel(int index) { return bodies[index].getVel(); }
    @Override
    public double[] getAcc(int index){ return bodies[index].getAcc(); }
    @Override
    public double getTime() { return CelestialBody.getTime(); }

    //upates all derivs
    @Override
    public void _2Deriv() {
        //must sum up all values of gravities with all other bodies

        double dist=0;
        for(int i=0; i<bodies.length; i++){
            bodies[i].setAcc(new double[] {0,0,0}); //reset to 0

            for(int j=0; j<bodies.length; j++){
                if(j==i)
                    continue;
                
                //calc distance between 2
                dist = CelestialBody.getDistance(bodies[i], bodies[j]);
                dist = dist*dist*dist;

                //adding in dims
                bodies[i].setAcc(new double[]{ bodies[i].getAcc()[0] + G*bodies[j].getMass()* (bodies[j].getPos()[0] - bodies[i].getPos()[0])/dist, 
                                               bodies[i].getAcc()[1] + G*bodies[j].getMass()* (bodies[j].getPos()[1] - bodies[i].getPos()[1])/dist,
                                               bodies[i].getAcc()[2] + G*bodies[j].getMass()* (bodies[j].getPos()[2] - bodies[i].getPos()[2])/dist  });

            }


        }

    }


    @Override
    public void _1Deriv() {
        //second order model so nothing to update
    }

}
