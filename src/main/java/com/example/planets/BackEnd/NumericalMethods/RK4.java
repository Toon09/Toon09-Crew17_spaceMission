package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

public class RK4 implements NumSolver{

    //////////// vk1 & pk1 are just the model copied, use the model itself instead

    /// for position
    double[][][] pk1;
    double[][][] pk2;
    double[][][] pk3;
    double[][][] pk4;

    /// for velocity
    Model3D vk1;
    Model3D vk2;
    Model3D vk3;
    Model3D vk4;

    ////// https://lpsa.swarthmore.edu/NumInt/NumIntFourth.html
    @Override
    public void step(Model3D model, double dt) {
        //set up rk4 for position only
        RK4setUpVals(model, dt);

        //update position
        for(int i=0; i<model.size(); i++){

            model.setPos(i, new double[] {  model.getPos(i)[0] + dt * ( model.getVel(i)[0] + 2*pk2[i][1][0] + 2*pk3[i][1][0] + pk4[i][1][0] ) / 6.0,
                                            model.getPos(i)[1] + dt * ( model.getVel(i)[1] + 2*pk2[i][1][1] + 2*pk3[i][1][1] + pk4[i][1][1] ) / 6.0,
                                            model.getPos(i)[2] + dt * ( model.getVel(i)[2] + 2*pk2[i][1][2] + 2*pk3[i][1][2] + pk4[i][1][2] ) / 6.0   } );

        }

        //setting up values for acceleration


        //update vel
        for(int i=0; i<model.size(); i++){

            model.setVel(i, new double[] {  model.getVel(i)[0] + dt * ( model.getAcc(i)[0] + 2*vk2.getAcc(i)[0] + 2*vk3.getAcc(i)[0] + vk4.getAcc(i)[0] ) / 6.0,
                                            model.getVel(i)[1] + dt * ( model.getAcc(i)[1] + 2*vk2.getAcc(i)[1] + 2*vk3.getAcc(i)[1] + vk4.getAcc(i)[1] ) / 6.0,
                                            model.getVel(i)[2] + dt * ( model.getAcc(i)[2] + 2*vk2.getAcc(i)[2] + 2*vk3.getAcc(i)[2] + vk4.getAcc(i)[2] ) / 6.0    } );

        }

        //update acceleration
        model.hDeriv();

        //adds time
        model.addDt(dt);

        // update for trajectory changes of ship
        // trajectory changes, right after current acc is calculated
        if( model.getShip().trajectoryChangeCondition(model) ){
            //the magnitudes and such can be saved in another class and this call can be emptied out
            //the values can be saved in an array of sorts
            //model.getShip().accelerate(dt);
        }

    }

    private void RK4setUpVals(Model3D model, double dt){
        pk1 = model.getState(); /////////// dont copy, just use model info straigth up to avoid a copy
        pk2 = model.getState();
        pk3 = model.getState();
        pk4 = model.getState();

        //pk2 do half a step with vals of pk1
        for(int i=0; i<pk1.length; i++){ //loops thru all planets
            for(int j=0;j <2; j++)
                for( int k=0; k<3; k++ ) // all dimensions
                    pk2[i][j][k] = pk1[i][j][k] + dt * pk1[i][j+1][k] / 2; //pk1 = model, so we can use either

        }

        //pk3 is same but with pk1 + dt*pk2/2
        for(int i=0; i<pk1.length; i++){ //loops thru all planets
            for(int j=0;j <2; j++)
                for( int k=0; k<3; k++ ) // all dimensions
                    pk3[i][j][k] = pk1[i][j][k] + dt * pk2[i][j+1][k] / 2; //pk1 = model, so we can use either

        }

        //pk4 is almost the same, pk1 + pk3*dt
        for(int i=0; i<pk1.length; i++){ //loops thru all planets
            for(int j=0;j <2; j++)
                for( int k=0; k<3; k++ ) // all dimensions
                    pk4[i][j][k] = pk1[i][j][k] + dt * pk3[i][j+1][k]; //pk1 = model, so we can use either

        }

        //////// get values of the derivatives saved up
        RK4Vel(model, dt);

    }

    private void RK4Vel(Model3D model, double dt){
        vk1 = model.clone(new Euler());
        vk2 = model.clone(new Euler());
        vk3 = model.clone(new Euler());
        vk4 = model.clone(new Euler());

        // calc the values of vel in the prev setup & in this one just call to get hDeriv
        // make function in model3D (define in grav0) setState, takes in 3d array amd copies pos, vel & acc

        vk2.setState(pk2);
        vk2.hDeriv();

        vk3.setState(pk3);
        vk3.hDeriv();

        vk4.setState(pk4);
        vk4.hDeriv();

    }

    public void setState(double[][][] state) {

    }

    @Override
    public String getName() {
        return "RK4";
    }

}


// double[][] arr = { {X,Y,Z}, {Vx,Vy,Vz} };