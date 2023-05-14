package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

public class FastRK2 implements NumSolver {

    // Ralston's method by default
    private double alpha = 2.0/3.0;
    private double[][][] k1;
    private Model3D vk1;

    public FastRK2(){}
    public FastRK2(double alpha){ this.alpha = alpha; }

    @Override
    public void step(Model3D model, double dt) {

        // setting up
        RK2setUpVals(model, dt);

        //update position
        for(int i=0; i<model.size(); i++){

            model.setPos(i, new double[] {  model.getPos(i)[0] + dt * ( (1-1/(2*alpha)) * model.getVel(i)[0] + (1/(2*alpha)) * k1[i][1][0] ),
                                            model.getPos(i)[1] + dt * ( (1-1/(2*alpha)) * model.getVel(i)[1] + (1/(2*alpha)) * k1[i][1][1] ),
                                            model.getPos(i)[2] + dt * ( (1-1/(2*alpha)) * model.getVel(i)[2] + (1/(2*alpha)) * k1[i][1][2] )   } );

        }


        //update vel
        for(int i=0; i<model.size(); i++){

            model.setVel(i, new double[] {  model.getVel(i)[0] + dt * ( (1-1/(2*alpha)) * model.getAcc(i)[0] + (1/(2*alpha)) * vk1.getAcc(i)[0] ),
                                            model.getVel(i)[1] + dt * ( (1-1/(2*alpha)) * model.getAcc(i)[1] + (1/(2*alpha)) * vk1.getAcc(i)[1] ),
                                            model.getVel(i)[2] + dt * ( (1-1/(2*alpha)) * model.getAcc(i)[2] + (1/(2*alpha)) * vk1.getAcc(i)[2] )    } );

        }

        //update acceleration
        model.hDeriv();

        //adds time
        model.addDt(dt);

        //update for trajectory changes of ship
        // trajectory changes, right after current acc is calculated
        if( model.getShip().trajectoryChangeCondition(model) ){
            //the magnitudes and such can be saved in another class and this call can be emptied out
            //the values can be saved in an array of sorts
            //model.getShip().accelerate(dt);
        }
    }


    private void RK2setUpVals(Model3D model, double dt){
        k1 = model.getState();

        //gets all new velocities
        for(int i=0; i<k1.length; i++) // in every planet
            for(int j=0; j<3; j++)
                k1[i][1][j] = k1[i][1][j] + dt*alpha * k1[i][2][j];

        //with velocities it calculates accelerations
        vk1 = model.clone(new Euler());
        vk1.setState(k1);

        vk1.hDeriv();
    }

    @Override
    public String getName() {
        return "Fast RK2";
    }



}
