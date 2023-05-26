package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.*;

public class RK2 implements NumSolver {

    //for different variants of RK2
    private double alpha = 2.0/3.0; //Ralston's method by default

    public RK2(){}
    public RK2(double alph){
        this.alpha = alph;
    }

    double[][][] pk1;

    Model3D vk1;

    public void step(Model3D model, double dt) {

        RKsetUpVals(model, dt);
        

        //update position
        for(int i=0; i<model.size(); i++){

            model.setPos(i, new double[] {  model.getPos(i)[0] + dt * ( (1.0-1.0/(2.0*alpha)) * model.getVel(i)[0] + (1.0/(2.0*alpha)) * pk1[i][1][0] ),
                                            model.getPos(i)[1] + dt * ( (1.0-1.0/(2.0*alpha)) * model.getVel(i)[1] + (1.0/(2.0*alpha)) * pk1[i][1][1] ),
                                            model.getPos(i)[2] + dt * ( (1.0-1.0/(2.0*alpha)) * model.getVel(i)[2] + (1.0/(2.0*alpha)) * pk1[i][1][2] )   } );

        }


        //update vel
        for(int i=0; i<model.size(); i++){

            model.setVel(i, new double[] {  model.getVel(i)[0] + dt * ( (1.0-1.0/(2.0*alpha)) * model.getAcc(i)[0] + (1.0/(2.0*alpha)) * vk1.getAcc(i)[0] ),
                                            model.getVel(i)[1] + dt * ( (1.0-1.0/(2.0*alpha)) * model.getAcc(i)[1] + (1.0/(2.0*alpha)) * vk1.getAcc(i)[1] ),
                                            model.getVel(i)[2] + dt * ( (1.0-1.0/(2.0*alpha)) * model.getAcc(i)[2] + (1.0/(2.0*alpha)) * vk1.getAcc(i)[2] )    } );
                                            
        }

        //update acceleration
        model.hDeriv();

        //adds time
        model.addDt(dt);

        // letting the ship do its plans (works for many ships)
        for(int i=0; i< model.getAmountOfShips(); i++){
            model.getShip(i).executePlans(model.getTime(), dt);
        }


    }

    private void RKsetUpVals(Model3D model, double dt){
        pk1 = model.getState();

        //pk2 do half a step with vals of pk1
        for(int i=0; i<pk1.length; i++){ //loops thru all planets
            for(int j=0;j <2; j++) // loops through position and vel
                for( int k=0; k<3; k++ ) // all dimensions
                    pk1[i][j][k] = pk1[i][j][k] + alpha * dt * pk1[i][j+1][k]; //pk1 = model, so we can use either

        }

        //////// get values of the derivatives saved up
        RKVel(model, dt);

    }

    private void RKVel(Model3D model, double dt){
        vk1 = model.clone(new Euler());

        vk1.setState(pk1);
        vk1 .hDeriv();

    }


    @Override
    public String getName() {
        return "RK2: alpha = " + alpha;
    }

}
