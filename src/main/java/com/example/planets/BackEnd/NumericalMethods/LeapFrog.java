package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

// https://en.wikipedia.org/wiki/Leapfrog_integration

public class LeapFrog implements NumSolver{

    //////////// vk1 & pk1 are just the model copied, use the model itself instead

    /// for position
    double[][][] v12;

    /// for velocity
    Model3D vk1;

    @Override
    public void step(Model3D model, double dt) {
        //set up rk4 for position only
        RK4setUpVals(model, dt);

        //update position
        for(int i=0; i<model.size(); i++){

            model.setPos(i, new double[] {  model.getPos(i)[0] + dt * ( v12[i][1][0] ),
                    model.getPos(i)[1] + dt * ( v12[i][1][1] ),
                    model.getPos(i)[2] + dt * ( v12[i][1][2] )   } );

        }

        //update vel
        for(int i=0; i<model.size(); i++){

            model.setVel(i, new double[] {  v12[i][1][0] + dt * ( vk1.getAcc(i)[0] ),
                    v12[i][1][1] + dt * ( vk1.getAcc(i)[1] ),
                    v12[i][1][2] + dt * ( vk1.getAcc(i)[2] )    } );

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

    private void RK4setUpVals(Model3D model, double dt){
        v12 = model.getState();

        //pk2 do half a step with vals of pk1
        for(int i=0; i<v12.length; i++){ //loops thru all planets
            for( int k=0; k<3; k++ ) {
                v12[i][1][k] = model.getBody(i).getVel()[k] + dt * model.getBody(i).getAcc()[k] / 2.0;
            }

        }

        //////// get values of the derivatives saved up
        RK4Vel(model, dt);

    }

    private void RK4Vel(Model3D model, double dt){
        vk1 = model.clone(new Euler());
        double[][][] temp = model.getState(); // to get +1 state for acc in vk1


        for(int i=0; i<temp.length; i++){
            for(int j=0; j<2; j++)
                for( int k=0; k<3; k++ ) // all dimensions
                    temp[i][j][k] = temp[i][j][k] + dt * temp[i][j+1][k];

        }

        vk1.setState(temp);
        vk1.hDeriv();

    }


    @Override
    public String getName() {
        return "LeapFrog";
    }

}
