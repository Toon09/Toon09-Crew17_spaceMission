package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.*;

public class Euler implements NumSolver {

    public void step(Model3D model, double dt){

        //update position
        for(int i=0; i<model.size(); i++){
            model.setPos(i, new double[] {  model.getPos(i)[0] + model.getVel(i)[0] * dt,
                    model.getPos(i)[1] + model.getVel(i)[1] * dt,
                    model.getPos(i)[2] + model.getVel(i)[2] * dt   } );
        }


        //update vel
        for(int i=0; i<model.size(); i++){
            model.setVel(i, new double[] {  model.getVel(i)[0] + model.getAcc(i)[0] * dt,
                    model.getVel(i)[1] + model.getAcc(i)[1] * dt,
                    model.getVel(i)[2] + model.getAcc(i)[2] * dt   } );
        }

        //adds time
        model.addDt(dt);

        //update acc
        model.hDeriv();

        // letting the ship do its plans (works for many ships)
        for(int i=0; i<model.getAmountOfShips(); i++){
            model.getShip(i).executePlans(model.getTime(), dt);
        }

    }

    @Override
    public String getName() {
        return "Euler";
    }


}