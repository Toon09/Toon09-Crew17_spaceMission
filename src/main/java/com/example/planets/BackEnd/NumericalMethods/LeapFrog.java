package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

// https://en.wikipedia.org/wiki/Leapfrog_integration

public class LeapFrog implements NumSolver{

    Model3D v12;
    Model3D a1;

    @Override
    public void step(Model3D model, double dt) {
        //set up rk4 for position only
        RK4setUpVals(model, dt);

        //update position
        for(int i=0; i<model.size(); i++){

            model.setPos(i, new double[] {  model.getPos(i)[0] + dt * v12.getVel(i)[0],
                    model.getPos(i)[1] + dt * v12.getVel(i)[1],
                    model.getPos(i)[2] + dt * v12.getVel(i)[2]   } );

        }

        //update vel
        for(int i=0; i<model.size(); i++){
            model.setVel(i, new double[] {  v12.getVel(i)[0] + dt * a1.getAcc(i)[0],
                    v12.getVel(i)[1] + dt * a1.getAcc(i)[1],
                    v12.getVel(i)[2] + dt * a1.getAcc(i)[2]    } );

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

    private void RK4setUpVals(Model3D model, double dt){
        // calc a_+1 & v_+1/2
        v12 = model.clone(new Euler());
        v12.updatePos(dt/2.0, dt/2.0, false);

        a1 = model.clone(new Euler());
        a1.updatePos(dt, dt, false);


    }



    @Override
    public String getName() {
        return "LeapFrog";
    }

}
