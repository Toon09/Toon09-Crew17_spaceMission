package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

// in our courses cheat sheet
public class HeunsRK3 implements NumSolver{

    Model3D pk2;
    Model3D pk3;

    @Override
    public void step(Model3D model, double dt) {
        //set up rk4 for position only
        RKsetUpVals(model, dt);

        //update position
        for(int i=0; i<model.size(); i++){

            model.setPos(i, new double[] {  model.getPos(i)[0] + dt * ( model.getVel(i)[0] + 3*pk3.getVel(i)[0] ) / 4.0,
                    model.getPos(i)[1] + dt * ( model.getVel(i)[1] + 3*pk3.getVel(i)[1] ) / 4.0,
                    model.getPos(i)[2] + dt * ( model.getVel(i)[2] + 3*pk3.getVel(i)[2] ) / 4.0   } );

        }

        //update vel
        for(int i=0; i<model.size(); i++){

            model.setVel(i, new double[] {  model.getVel(i)[0] + dt * ( model.getAcc(i)[0] + 3*pk3.getAcc(i)[0] ) / 4.0,
                    model.getVel(i)[1] + dt * ( model.getAcc(i)[1] + 3*pk3.getAcc(i)[1] ) / 4.0,
                    model.getVel(i)[2] + dt * ( model.getAcc(i)[2] + 3*pk3.getAcc(i)[2] ) / 4.0    } );

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
        pk2 = model.clone(new Euler());

        pk2.updatePos(dt/3.0, dt/3.0, false);

        //pk2 do half a step with vals of pk1
        //for(int i=0; i<pk1.length; i++) //loops thru all planets
        //    for(int j=0;j <2; j++)
        //        for( int k=0; k<3; k++ ) // all dimensions
        //            pk2[i][j][k] = pk1[i][j][k] + dt * pk1[i][j+1][k] / 3.0; //pk1 = model, so we can use either

        pk3 = model.clone(null);

        // k3 = deriv at t=t_i + 2/3 h , y_i+2/3= y_i + 2/3 h pk2
        // addDt(2.0*dt/3.0)
        // create state where you add the vals of 2/3 pk2 (add to get vel to pos and acc to vel)
        // calc deriv

        double[][][] state = model.getState();

        for(int i=0; i<state.length; i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += 2.0*dt/3.0 * pk2.getVel(i)[k];

            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += 2.0*dt/3.0 * pk2.getAcc(i)[k];
        }

        pk3.setState(state);
        pk3.addDt(dt*2.0/3.0);
        pk3.hDeriv();


    }



    @Override
    public String getName() {
        return "Heun's RK3";
    }

}
