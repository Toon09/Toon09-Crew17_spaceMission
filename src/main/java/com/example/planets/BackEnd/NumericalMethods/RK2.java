package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.*;

// in our courses cheat sheet
public class RK2 implements NumSolver {

    //for different variants of RK2
    private double alpha = 2.0/3.0; //Ralston's method by default

    public RK2(){}
    public RK2(double alph){
        this.alpha = alph;
    }

    Model3D pk1;

    Model3D vk1;

    public void step(Model3D model, double dt) {

        RKsetUpVals(model, dt);
        

        //update position
        for(int i=0; i<model.size(); i++){

            model.setPos(i, new double[] {  model.getPos(i)[0] + dt * ( (1.0-1.0/(2.0*alpha)) * model.getVel(i)[0] + (1.0/(2.0*alpha)) * pk1.getVel(i)[0] ),
                                            model.getPos(i)[1] + dt * ( (1.0-1.0/(2.0*alpha)) * model.getVel(i)[1] + (1.0/(2.0*alpha)) * pk1.getVel(i)[1] ),
                                            model.getPos(i)[2] + dt * ( (1.0-1.0/(2.0*alpha)) * model.getVel(i)[2] + (1.0/(2.0*alpha)) * pk1.getVel(i)[2] )   } );

        }


        //update vel
        for(int i=0; i<model.size(); i++){

            model.setVel(i, new double[] {  model.getVel(i)[0] + dt * ( (1.0-1.0/(2.0*alpha)) * model.getAcc(i)[0] + (1.0/(2.0*alpha)) * pk1.getAcc(i)[0] ),
                                            model.getVel(i)[1] + dt * ( (1.0-1.0/(2.0*alpha)) * model.getAcc(i)[1] + (1.0/(2.0*alpha)) * pk1.getAcc(i)[1] ),
                                            model.getVel(i)[2] + dt * ( (1.0-1.0/(2.0*alpha)) * model.getAcc(i)[2] + (1.0/(2.0*alpha)) * pk1.getAcc(i)[2] )    } );
                                            
        }

        //adds time
        model.addDt(dt);

        //update acceleration
        model.hDeriv();

        // letting the ship do its plans (works for many ships)
        for(int i=0; i< model.getAmountOfShips(); i++){
            model.getShip(i).executePlans(model.getTime(), dt);
        }


    }


    private void RKsetUpVals(Model3D model, double dt){
        pk1 = model.clone( new Euler()) ;

        pk1.updatePos(alpha*dt, dt*alpha, false );
    }



    @Override
    public String getName() {
        if(alpha == 2/3.0)
            return "Ralston's RK2";
        return "RK2: alpha = " + alpha;
    }

}
