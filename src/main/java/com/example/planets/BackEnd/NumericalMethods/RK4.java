package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

// https://lpsa.swarthmore.edu/NumInt/NumIntFourth.html
public class RK4 implements NumSolver{

    Model3D pk2;
    Model3D pk3;
    Model3D pk4;

    @Override
    public void step(Model3D model, double dt) {
        //set up rk4 for position only
        RKsetUpVals(model, dt);
        //update position
        for(int i=0; i<model.size(); i++){
            model.setPos(i, new double[] {  model.getPos(i)[0] + dt * ( model.getVel(i)[0] + 2*pk2.getVel(i)[0] + 2*pk3.getVel(i)[0] + pk4.getVel(i)[0] ) / 6.0,
                                            model.getPos(i)[1] + dt * ( model.getVel(i)[1] + 2*pk2.getVel(i)[1] + 2*pk3.getVel(i)[1] + pk4.getVel(i)[1] ) / 6.0,
                                            model.getPos(i)[2] + dt * ( model.getVel(i)[2] + 2*pk2.getVel(i)[2] + 2*pk3.getVel(i)[2] + pk4.getVel(i)[2] ) / 6.0   } );

        }
        //update vel
        for(int i=0; i<model.size(); i++){

            model.setVel(i, new double[] {  model.getVel(i)[0] + dt * ( model.getAcc(i)[0] + 2*pk2.getAcc(i)[0] + 2*pk3.getAcc(i)[0] + pk4.getAcc(i)[0] ) / 6.0,
                                            model.getVel(i)[1] + dt * ( model.getAcc(i)[1] + 2*pk2.getAcc(i)[1] + 2*pk3.getAcc(i)[1] + pk4.getAcc(i)[1] ) / 6.0,
                                            model.getVel(i)[2] + dt * ( model.getAcc(i)[2] + 2*pk2.getAcc(i)[2] + 2*pk3.getAcc(i)[2] + pk4.getAcc(i)[2] ) / 6.0    } );

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
        // pk2: just an euler step from og function
        pk2 = model.clone(new Euler()); ////
        pk2.updatePos(dt/2.0, dt/2.0, false);

        // pk3: t+dt/2, y+dt/2 k2
        pk3 = model.clone(null);
        double[][][] state = model.getState();
        for(int i=0; i<state.length; i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += dt/2.0 * pk2.getVel(i)[k];

            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += dt/2.0 * pk2.getAcc(i)[k];
        }

        pk3.setState(state);
        pk3.addDt(dt/2.0);
        pk3.hDeriv();

        // pk4: t+dt, y+dt*pk3
        pk4 = model.clone(null);
        state = model.getState();
        //System.out.println("state: "+ state.length);
        for(int i=0; i<state.length; i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += dt * pk3.getVel(i)[k];

            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += dt * pk3.getAcc(i)[k];
        }
        pk4.setState(state);
        pk4.addDt(dt);
        pk4.hDeriv();
    }

    @Override
    public String getName() {
        return "Classical RK4";
    }

}
