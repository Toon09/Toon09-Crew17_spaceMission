package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.*;

public class AB2 implements NumSolver {

    /*
     * save values of prev iteration in an array
     * use it for calculation of next step
     * in first iteration just use 10 or so euler steps backwards to ensure precision..
     * ..then test if less steps make a difference 
     */

    private Model3D prevStates;
    boolean first = true;

    public AB2(){
    }

    @Override
    public void step(Model3D model, double dt) {
        //saving this state for next iteration
        Model3D temp = model.clone( new RK2() );

        //bootstrapping if its first iteration
        if( first ){
            prevStates = model.clone( new RK2() );
            prevStates.updatePos(dt, -dt, false);
        }


        //update position
        for(int i=0; i<model.size(); i++){
            model.setPos(i, new double[] {  model.getPos(i)[0] + (dt/2.0) * ( 3*model.getVel(i)[0] - prevStates.getVel(i)[0] ),
                                            model.getPos(i)[1] + (dt/2.0) * ( 3*model.getVel(i)[1] - prevStates.getVel(i)[1] ),
                                            model.getPos(i)[2] + (dt/2.0) * ( 3*model.getVel(i)[2] - prevStates.getVel(i)[2] )   } );
        }


        //update vel
        for(int i=0; i<model.size(); i++){
            model.setVel(i, new double[] {  model.getVel(i)[0] + (dt/2.0) * ( 3*model.getAcc(i)[0] - prevStates.getAcc(i)[0] ),
                                            model.getVel(i)[1] + (dt/2.0) * ( 3*model.getAcc(i)[1] - prevStates.getAcc(i)[1] ),
                                            model.getVel(i)[2] + (dt/2.0) * ( 3*model.getAcc(i)[2] - prevStates.getAcc(i)[2] )   } );
        }

        //update acc
        model.hDeriv();

        //adds time
        model.addDt(dt);

        //updates last model as this one before the changes
        prevStates = temp;

    }

    @Override
    public void innitState(double[][][] state) {

    }

    public void setState(double[][][] state) {

    }

    @Override
    public String getName() {
        return "AB2";
    }
    
    
}
