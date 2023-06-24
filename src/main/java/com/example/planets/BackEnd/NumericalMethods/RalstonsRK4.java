package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

// http://www.mymathlib.com/c_source/diffeq/runge_kutta/runge_kutta_ralston_4.c
public class RalstonsRK4 implements NumSolver{

    Model3D pk2;
    Model3D pk3;
    Model3D pk4;

    
    // coefficients
    static final double a2 = 0.40;
    static final double a3 = (14.0 - 3.0 * Math.sqrt(5.0)) / 16.0;
    static final double b31 = (-2889.0 + 1428.0 * Math.sqrt(5.0)) / 1024.0;
    static final double b32 = (3785.0 - 1620.0 * Math.sqrt(5.0)) / 1024.0;
    static final double b41 = (-3365.0 + 2094.0 * Math.sqrt(5.0)) / 6040.0;
    static final double b42 = (-975.0 - 3046.0 * Math.sqrt(5.0)) / 2552.0;
    static final double b43 = (467040.0 + 203968.0 * Math.sqrt(5.0)) / 240845.0;

    static final double g1 = (263.0 + 24.0 * Math.sqrt(5.0)) / 1812.0;
    static final double g2 = (125.0 - 1000.0 * Math.sqrt(5.0)) / 3828.0;
    static final double g3 = 1024.0 * (3346.0 + 1623.0 * Math.sqrt(5.0)) / 5924787.0;
    static final double g4 = (30.0 - 4.0 * Math.sqrt(5.0)) / 123.0;


    @Override
    public void step(Model3D model, double dt) {
        //set up rk4 for position only
        RKsetUpVals(model, dt);

        //update position
        for(int i=0; i<model.size(); i++){

            model.setPos(i, new double[] {  model.getPos(i)[0] + dt * ( g1*model.getVel(i)[0] + g2*pk2.getVel(i)[0] + g3*pk3.getVel(i)[0] + g4*pk4.getVel(i)[0] ),
                                            model.getPos(i)[1] + dt * ( g1*model.getVel(i)[1] + g2*pk2.getVel(i)[1] + g3*pk3.getVel(i)[1] + g4*pk4.getVel(i)[1] ),
                                            model.getPos(i)[2] + dt * ( g1*model.getVel(i)[2] + g2*pk2.getVel(i)[2] + g3*pk3.getVel(i)[2] + g4*pk4.getVel(i)[2] )   } );

        }

        //setting up values for acceleration


        //update vel
        for(int i=0; i<model.size(); i++){

            model.setVel(i, new double[] {  model.getVel(i)[0] + dt * ( g1*model.getAcc(i)[0] + g2*pk2.getAcc(i)[0] + g3*pk3.getAcc(i)[0] + g4*pk4.getAcc(i)[0] ),
                                            model.getVel(i)[1] + dt * ( g1*model.getAcc(i)[1] + g2*pk2.getAcc(i)[1] + g3*pk3.getAcc(i)[1] + g4*pk4.getAcc(i)[1] ),
                                            model.getVel(i)[2] + dt * ( g1*model.getAcc(i)[2] + g2*pk2.getAcc(i)[2] + g3*pk3.getAcc(i)[2] + g4*pk4.getAcc(i)[2] )   } );

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
        // pk1 is normal euler step to b21*dt
        pk2 = model.clone(new Euler());
        pk2.updatePos(dt*a2, dt*a2, false);

        // pk3: t+dt, y + dt ( b31*k1 + b32*k2 )
        pk3 = model.clone(null);
        double[][][] state = model.getState();

        for(int i=0; i<pk3.size(); i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += dt*( b31*model.getVel(i)[k] + b32*pk2.getVel(i)[k] );
            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += dt*( b31*model.getAcc(i)[k] + b32*pk2.getAcc(i)[k] );
        }

        pk3.setState(state);
        pk3.addDt(dt*a3);
        pk3.hDeriv();


        // pk4: t+dt, y+ dt ( b41*k1 + b42*k2 + b43*k3 )
        pk4 = model.clone(null);
        state = model.getState();

        for(int i=0; i<pk3.size(); i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += dt*( b41*model.getVel(i)[k] + b42*pk2.getVel(i)[k] + b43*pk3.getVel(i)[k] );
            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += dt*( b41*model.getAcc(i)[k] + b42*pk2.getAcc(i)[k] + b43*pk3.getAcc(i)[k] );
        }

        pk4.setState(state);
        pk4.addDt(dt);
        pk4.hDeriv();


    }


    @Override
    public String getName() {
        return "Ralston's RK4";
    }

}