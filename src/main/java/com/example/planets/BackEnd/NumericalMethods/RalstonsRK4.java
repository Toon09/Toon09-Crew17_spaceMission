package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

public class RalstonsRK4 implements NumSolver{

    //////////// vk1 & pk1 are just the model copied, use the model itself instead

    /// for position
    double[][][] pk1;
    double[][][] pk2;
    double[][][] pk3;
    double[][][] pk4;

    /// for velocity
    Model3D vk2;
    Model3D vk3;
    Model3D vk4;

    // coefficients
    static final double b21 = 0.40;

    static final double b31 = (-2889.0 + 1428.0 * Math.sqrt(5.0)) / 1024.0;
    static final double b32 = (3785.0 - 1620.0 * Math.sqrt(5.0)) / 1024.0;
    static final double b41 = (-3365.0 + 2094.0 * Math.sqrt(5.0)) / 6040.0;
    static final double b42 = (-975.0 - 3046.0 * Math.sqrt(5.0)) / 2552.0;
    static final double b43 = (467040.0 + 203968.0 * Math.sqrt(5.0)) / 240845.0;

    static final double g1 = (263.0 + 24.0 * Math.sqrt(5.0)) / 1812.0;
    static final double g2 = (125.0 - 1000.0 * Math.sqrt(5.0)) / 3828.0;
    static final double g3 = 1024.0 * (3346.0 + 1623.0 * Math.sqrt(5.0)) / 5924787.0;
    static final double g4 = (30.0 - 4.0 * Math.sqrt(5.0)) / 123.0;


    ////// http://www.mymathlib.com/c_source/diffeq/runge_kutta/runge_kutta_ralston_4.c
    @Override
    public void step(Model3D model, double dt) {
        //set up rk4 for position only
        RK4setUpVals(model, dt);

        //update position
        for(int i=0; i<model.size(); i++){

            model.setPos(i, new double[] {  model.getPos(i)[0] + dt * ( g1*model.getVel(i)[0] + g2*pk2[i][1][0] + g3*pk3[i][1][0] + g4*pk4[i][1][0] ),
                                            model.getPos(i)[1] + dt * ( g1*model.getVel(i)[1] + g2*pk2[i][1][1] + g3*pk3[i][1][1] + g4*pk4[i][1][1] ),
                                            model.getPos(i)[2] + dt * ( g1*model.getVel(i)[2] + g2*pk2[i][1][2] + g3*pk3[i][1][2] + g4*pk4[i][1][2] )   } );

        }

        //setting up values for acceleration


        //update vel
        for(int i=0; i<model.size(); i++){

            model.setVel(i, new double[] {  model.getVel(i)[0] + dt * ( g1*model.getAcc(i)[0] + g2*vk2.getAcc(i)[0] + g3*vk3.getAcc(i)[0] + g4*vk4.getAcc(i)[0] ),
                                            model.getVel(i)[1] + dt * ( g1*model.getAcc(i)[1] + g2*vk2.getAcc(i)[1] + g3*vk3.getAcc(i)[1] + g4*vk4.getAcc(i)[1] ),
                                            model.getVel(i)[2] + dt * ( g1*model.getAcc(i)[2] + g2*vk2.getAcc(i)[2] + g3*vk3.getAcc(i)[2] + g4*vk4.getAcc(i)[2] )   } );

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
        pk1 = model.getState(); /////////// dont copy, just use model info straigth up to avoid a copy
        pk2 = model.getState();
        pk3 = model.getState();
        pk4 = model.getState();

        //pk2 do half a step with vals of pk1
        for(int i=0; i<pk1.length; i++){ //loops thru all planets
            for(int j=0;j <2; j++)
                for( int k=0; k<3; k++ ) // all dimensions
                    pk2[i][j][k] = pk1[i][j][k] + dt * b21*pk1[i][j+1][k]; //pk1 = model, so we can use either

        }

        //pk3 is same but with pk1 + dt*pk2/2
        for(int i=0; i<pk1.length; i++){ //loops thru all planets
            for(int j=0;j <2; j++)
                for( int k=0; k<3; k++ ) // all dimensions
                    pk3[i][j][k] = pk1[i][j][k] + dt * ( b31*pk1[i][j+1][k] + b32*pk2[i][j+1][k] );

        }

        //pk4 is almost the same, pk1 + pk3*dt
        for(int i=0; i<pk1.length; i++){ //loops thru all planets
            for(int j=0;j <2; j++)
                for( int k=0; k<3; k++ ) // all dimensions
                    pk4[i][j][k] = pk1[i][j][k] + dt * ( b41*pk1[i][j+1][k] + b42*pk2[i][j+1][k] + b43*pk3[i][j+1][k] );

        }

        //////// get values of the derivatives saved up
        RK4Vel(model, dt);

    }

    private void RK4Vel(Model3D model, double dt){
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
        return "Ralston's RK4";
    }

}