package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.*;

public class AB2 implements NumSolver {

    /*
     * save values of prev iteration in an array
     * use it for calculation of next step
     * in first iteration just use 10 or so euler steps backwards to ensure precision..
     * ..then test if less steps make a difference 
     */


    private Model3D prev;
    private Model3D temp;

    public AB2(){}

    @Override
    public void step(Model3D model, double dt) {

        AB2Vals(model, dt);

        //update position
        for(int i=0; i<model.size(); i++){
            model.setPos(i, new double[] {  model.getPos(i)[0] + (dt/2.0) * ( 3.0*model.getVel(i)[0] - prev.getVel(i)[0] ),
                                            model.getPos(i)[1] + (dt/2.0) * ( 3.0*model.getVel(i)[1] - prev.getVel(i)[1] ),
                                            model.getPos(i)[2] + (dt/2.0) * ( 3.0*model.getVel(i)[2] - prev.getVel(i)[2] )   } );
        }


        //update vel
        for(int i=0; i<model.size(); i++){
            model.setVel(i, new double[] {  model.getVel(i)[0] + (dt/2.0) * ( 3.0*model.getAcc(i)[0] - prev.getAcc(i)[0] ),
                                            model.getVel(i)[1] + (dt/2.0) * ( 3.0*model.getAcc(i)[1] - prev.getAcc(i)[1] ),
                                            model.getVel(i)[2] + (dt/2.0) * ( 3.0*model.getAcc(i)[2] - prev.getAcc(i)[2] )   } );
        }

        //update acc
        model.hDeriv();

        //adds time
        model.addDt(dt);

        // letting the ship do its plans (works for many ships)
        for(int i=0; i< model.getAmountOfShips(); i++){
            model.getShip(i).executePlans(model.getTime(), dt);
        }


    }

    /*
    - bootstrap RK2
    - calc value of t0
    - use t0 to t1
    - calc value t1+1

    must use 2 behind, the bootstrap is 2 behind and the current model is 1 behind

    so bootstrap

     */

    private void AB2Vals(Model3D model, double dt){

        if( prev == null ){ //bootstrapping with RK2
            prev = model.clone( new RK4() );

            //prev.updatePos(dt, -dt, false); // a step backwards
            temp = model.clone(null); //copy current model

        } else { // using last model
            prev = temp.clone(null); //copies previous model
            temp = model.clone(null); //copies current model

        }

    }

    private void AB2acc(Model3D model, double dt){
        //

    }

    @Override
    public String getName() {
        return "AB2";
    }
    
    
}
