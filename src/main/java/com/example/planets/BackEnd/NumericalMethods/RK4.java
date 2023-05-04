package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

public class RK4 implements NumSolver{

    //////
    @Override
    public void step(Model3D model, double dt) {
        double alpha = 1.0;

        //set up rk2
        Model3D states = model.clone( new Euler() ); //doesn't copy spaceship
        states.updatePos(alpha * dt, alpha * dt, false);


        //update position
        for(int i=0; i<model.size(); i++){

            model.setPos(i, new double[] {  model.getPos(i)[0] + dt * ( (1-1/(2*alpha)) * model.getVel(i)[0] + (1/(2*alpha)) * states.getVel(i)[0] ),
                    model.getPos(i)[1] + dt * ( (1-1/(2*alpha)) * model.getVel(i)[1] + (1/(2*alpha)) * states.getVel(i)[1] ),
                    model.getPos(i)[2] + dt * ( (1-1/(2*alpha)) * model.getVel(i)[2] + (1/(2*alpha)) * states.getVel(i)[2] )   } );

        }


        //update vel
        for(int i=0; i<model.size(); i++){

            model.setVel(i, new double[] {  model.getVel(i)[0] + dt * ( (1-1/(2*alpha)) * model.getAcc(i)[0] + (1/(2*alpha)) * states.getAcc(i)[0] ),
                    model.getVel(i)[1] + dt * ( (1-1/(2*alpha)) * model.getAcc(i)[1] + (1/(2*alpha)) * states.getAcc(i)[1] ),
                    model.getVel(i)[2] + dt * ( (1-1/(2*alpha)) * model.getAcc(i)[2] + (1/(2*alpha)) * states.getAcc(i)[2] )    } );

        }

        //update acceleration
        model.hDeriv();

        //adds time
        model.addDt(dt);

        //update for trajectory changes of ship
        // trajectory changes, right after current acc is calculated
        if( model.getShip().trajectoryChangeCondition(model) ){
            //the magnitutes and such can be saved in another class and this call can be emptied out
            //the values can be saved in an array of sorts
            model.getShip().accelerate(dt);
        }


    }

    @Override
    public String getName() {
        return "RK4";
    }

}


// double[][] arr = { {X,Y,Z}, {Vx,Vy,Vz} };