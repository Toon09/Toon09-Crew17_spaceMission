package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

// https://www.ams.org/mcom/1968-22-102/S0025-5718-68-99876-1/S0025-5718-68-99876-1.pdf
public class RK6 implements NumSolver {


    Model3D k2;
    Model3D k3;
    Model3D k4;
    Model3D k5;
    Model3D k6;
    Model3D k7;



    // coefficients for adding times
    static final double a2 = 1; // also b1
    static final double a3 = 1/2.0;
    static final double a4 = 3/2.0;
    static final double a5 = (7-Math.sqrt(21.0))/14.0;
    static final double a6 = (7+Math.sqrt(21.0))/14.0;
    static final double a7 = 1;

    // values for calculating k's
    static final double b31 = 3/8.0;
    static final double b32 = 1/8.0;

    static final double b41 = 8/27.0;
    static final double b42 = 2/27.0;
    static final double b43 = 8/27.0;

    static final double b51 = (3*(3*Math.sqrt(21.0)-7))/392.0;
    static final double b52 = -8*(7-Math.sqrt(21.0))/392.0;
    static final double b53 = 48*(7-Math.sqrt(21.0))/392.0;
    static final double b54 = -3*(21-Math.sqrt(21.0))/392.0;

    static final double b61 = -5*(231 + 51*Math.sqrt(21.0))/1960.0;
    static final double b62 =  -40*(7 + Math.sqrt(21.0))/1960.0;
    static final double b63 =  -320*Math.sqrt(21.0)/1960.0;
    static final double b64 =  3*(21 + 121*Math.sqrt(21.0))/1960.0;
    static final double b65 =  392*(6 + Math.sqrt(21.0))/1960.0;

    static final double b71 =  15*(22 + 7*Math.sqrt(21.0))/180.0;
    static final double b72 =  120/180.0;
    static final double b73 =  40*(7*Math.sqrt(21.0) - 5)/180.0;
    static final double b74 = -63*(3*Math.sqrt(21.0) - 2)/180.0;
    static final double b75 = -14*(49 + 9*Math.sqrt(21.0))/180.0;
    static final double b76 =  70*(7 - Math.sqrt(21.0))/180.0;


    // values for rk7
    static final double g1 = 9/180.0;
    static final double g2 = 0;
    static final double g3 = 64/180.0;
    static final double g4 = 0;
    static final double g5 = 49/180.0;
    static final double g6 = 49/180.0;
    static final double g7 = 9/180.0;

    @Override
    public void step(Model3D model, double dt) {

        //set up rk4 for position only
        RKsetUpVals(model, dt);

        //update position
        for(int i=0; i<model.size(); i++){

            model.setPos(i, new double[] {  model.getPos(i)[0] + dt * ( g1*model.getVel(i)[0] + g2*k2.getVel(i)[0] + g3*k3.getVel(i)[0] + g4*k4.getVel(i)[0] + g5*k5.getVel(i)[0] + g6*k6.getVel(i)[0] + g7*k7.getVel(i)[0] ),
                    model.getPos(i)[1] + dt * ( g1*model.getVel(i)[1] + g2*k2.getVel(i)[1] + g3*k3.getVel(i)[1] + g4*k4.getVel(i)[1] + g5*k5.getVel(i)[1] + g6*k6.getVel(i)[1] + g7*k7.getVel(i)[1] ),
                    model.getPos(i)[2] + dt * ( g1*model.getVel(i)[2] + g2*k2.getVel(i)[2] + g3*k3.getVel(i)[2] + g4*k4.getVel(i)[2] + g5*k5.getVel(i)[2] + g6*k6.getVel(i)[2] + g7*k7.getVel(i)[2] )   } );

        }


        //update vel
        for(int i=0; i<model.size(); i++){

            model.setVel(i, new double[] {  model.getVel(i)[0] + dt * ( g1*model.getAcc(i)[0] + g2*k2.getAcc(i)[0] + g3*k3.getAcc(i)[0] + g4*k4.getAcc(i)[0] + g5*k5.getAcc(i)[0] + g6*k6.getAcc(i)[0] + g7*k7.getAcc(i)[0] ),
                    model.getVel(i)[1] + dt * ( g1*model.getAcc(i)[1] + g2*k2.getAcc(i)[1] + g3*k3.getAcc(i)[1] + g4*k4.getAcc(i)[1] + g5*k5.getAcc(i)[1] + g6*k6.getAcc(i)[1] + g7*k7.getAcc(i)[1] ),
                    model.getVel(i)[2] + dt * ( g1*model.getAcc(i)[2] + g2*k2.getAcc(i)[2] + g3*k3.getAcc(i)[2] + g4*k4.getAcc(i)[2] + g5*k5.getAcc(i)[2] + g6*k6.getAcc(i)[2] + g7*k7.getAcc(i)[2] )   } );

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
        // k1 is normal euler step to b21*dt
        k2 = model.clone(new Euler());
        k2.updatePos(dt*a2, dt*a2, false);

        // k3: t+dt, y + dt ( b31*k1 + b32*k2 )
        k3 = model.clone(null);
        double[][][] state = model.getState();

        for(int i=0; i<k3.size(); i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += dt*( b31*model.getVel(i)[k] + b32*k2.getVel(i)[k] );
            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += dt*( b31*model.getAcc(i)[k] + b32*k2.getAcc(i)[k] );
        }

        k3.setState(state);
        k3.addDt(dt*a3);
        k3.hDeriv();


        // k4: t+dt, y+ dt ( b41*k1 + b42*k2 + b43*k3 )
        k4 = model.clone(null);
        state = model.getState();

        for(int i=0; i<k4.size(); i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += dt*( b41*model.getVel(i)[k] + b42*k2.getVel(i)[k] + b43*k3.getVel(i)[k] );
            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += dt*( b41*model.getAcc(i)[k] + b42*k2.getAcc(i)[k] + b43*k3.getAcc(i)[k] );
        }

        k4.setState(state);
        k4.addDt(dt*a4);
        k4.hDeriv();


        // k5: t+dt, y+ dt ( b51*k1 + b52*k2 + b53*k3 + b54*k4 )
        k5 = model.clone(null);
        state = model.getState();

        for(int i=0; i<k5.size(); i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += dt*( b51*model.getVel(i)[k] + b52*k2.getVel(i)[k] + b53*k3.getVel(i)[k] + b54*k4.getVel(i)[k] );
            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += dt*( b51*model.getAcc(i)[k] + b52*k2.getAcc(i)[k] + b53*k3.getAcc(i)[k] + b54*k4.getAcc(i)[k] );
        }

        k5.setState(state);
        k5.addDt(dt*a5);
        k5.hDeriv();


        // k6: t+dt, y+ dt ( b61*k1 + b62*k2 + b63*k3 + b64*k4 )
        k6 = model.clone(null);
        state = model.getState();

        for(int i=0; i<k6.size(); i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += dt*( b61*model.getVel(i)[k] + b62*k2.getVel(i)[k] + b63*k3.getVel(i)[k] + b64*k4.getVel(i)[k] + b65*k5.getVel(i)[k] );
            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += dt*( b61*model.getAcc(i)[k] + b62*k2.getAcc(i)[k] + b63*k3.getAcc(i)[k] + b64*k4.getAcc(i)[k] + b65*k5.getAcc(i)[k] );
        }

        k6.setState(state);
        k6.addDt(dt*a6);
        k6.hDeriv();


        // k7: t+dt, y+ dt ( b71*k1 + b73*k3 + b74*k4 + b75*k5 )
        k7 = model.clone(null);
        state = model.getState();

        for(int i=0; i<k7.size(); i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += dt*( b71*model.getVel(i)[k] + b72*k2.getVel(i)[k] + b73*k3.getVel(i)[k] + b74*k4.getVel(i)[k] + b75*k5.getVel(i)[k] + b76*k6.getVel(i)[k] );
            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += dt*( b71*model.getAcc(i)[k] + b72*k2.getAcc(i)[k] + b73*k3.getAcc(i)[k] + b74*k4.getAcc(i)[k] + b75*k5.getAcc(i)[k] + b76*k6.getAcc(i)[k] );
        }

        k7.setState(state);
        k7.addDt(dt*a7);
        k7.hDeriv();

    }

    @Override
    public String getName() {
        return "RK6";
    }

}
