package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

// https://www.sciencepublishinggroup.com/journal/paperinfo?journalid=247&doi=10.11648/j.mcs.20190403.12
public class RK7 implements NumSolver{

    private Model3D k2;
    private Model3D k3;
    private Model3D k4;
    private Model3D k5;
    private Model3D k6;
    private Model3D k7;
    private Model3D k8;
    private Model3D k9;

    private static final double param = 77/1440.0;

    
    // coefficients for adding times
    private static final double a2 = 1/6.0; // also b1
    private static final double a3 = 1/3.0;
    private static final double a4 = 1/2.0;
    private static final double a5 = 2/11.0;
    private static final double a6 = 2/3.0;
    private static final double a7 = 6/7.0;
    private static final double a8 = 0.0;
    private static final double a9 = 1.0;


    // values for calculating k's
    private static final double b31 = 0.0;
    private static final double b32 = 1/3.0;

    private static final double b41 = 1/8.0;
    private static final double b42 = 0;
    private static final double b43 = 3/8.0;

    private static final double b51 = 148/1331.0;
    private static final double b52 = 0;
    private static final double b53 = 150/1331.0;
    private static final double b54 = -56/1331.0;

    private static final double b61 = -404/243.0;
    private static final double b62 = 0;
    private static final double b63 =  -170/27.0;
    private static final double b64 = 4024/1701.0;
    private static final double b65 = 10648/1701.0;

    private static final double b71 = 2466/2401.0;
    private static final double b72 =  0;
    private static final double b73 =  1242/343.0;
    private static final double b74 = -19176/16807.0;
    private static final double b75 = -51909/16807.0;
    private static final double b76 = 1053/2401.0;

    private static final double b81 = 1/(576.0*param);
    private static final double b82 =  0;
    private static final double b83 =  0;
    private static final double b84 = 1/(105.0*param);
    private static final double b85 = -1331/(279552.0*param);
    private static final double b86 = -9/(1024.0*param);
    private static final double b87 = 343/(149760.0*param);

    private static final double b91 = -71/32.0 - 270*param/11.0;
    private static final double b92 =  0;
    private static final double b93 =  -195/22.0;
    private static final double b94 = 32/7.0;
    private static final double b95 = 29403/3584.0;
    private static final double b96 = -729/512.0;
    private static final double b97 = 1029/1408.0;
    private static final double b98 = 270*param/11.0;

    // values for rk7
    private static final double g1 = 77/1440.0 - param;
    private static final double g2 = 0;
    private static final double g3 = 0;
    private static final double g4 = 32/105.0;
    private static final double g5 = 1771561/6289920.0;
    private static final double g6 = 243/2560.0;
    private static final double g7 = 16807/74880.0;
    private static final double g8 = param;
    private static final double g9 = 11/270.0;

    @Override
    public void step(Model3D model, double dt) {

        //set up rk4 for position only
        RKsetUpVals(model, dt);

        //update position
        for(int i=0; i<model.size(); i++){

            model.setPos(i, new double[] {  model.getPos(i)[0] + dt * ( g1*model.getVel(i)[0] + g2*k2.getVel(i)[0] + g3*k3.getVel(i)[0] + g4*k4.getVel(i)[0] + g5*k5.getVel(i)[0] + g6*k6.getVel(i)[0] + g7*k7.getVel(i)[0] + g8*k8.getVel(i)[0] + g9*k9.getVel(i)[0] ),
                    model.getPos(i)[1] + dt * ( g1*model.getVel(i)[1] + g2*k2.getVel(i)[1] + g3*k3.getVel(i)[1] + g4*k4.getVel(i)[1] + g5*k5.getVel(i)[1] + g6*k6.getVel(i)[1] + g7*k7.getVel(i)[1] + g8*k8.getVel(i)[1] + g9*k9.getVel(i)[1] ),
                    model.getPos(i)[2] + dt * ( g1*model.getVel(i)[2] + g2*k2.getVel(i)[2] + g3*k3.getVel(i)[2] + g4*k4.getVel(i)[2] + g5*k5.getVel(i)[2] + g6*k6.getVel(i)[2] + g7*k7.getVel(i)[2] + g8*k8.getVel(i)[2] + g9*k9.getVel(i)[2] )   } );

        }


        //update vel
        for(int i=0; i<model.size(); i++){

            model.setVel(i, new double[] {  model.getVel(i)[0] + dt * ( g1*model.getAcc(i)[0] + g2*k2.getAcc(i)[0] + g3*k3.getAcc(i)[0] + g4*k4.getAcc(i)[0] + g5*k5.getAcc(i)[0] + g6*k6.getAcc(i)[0] + g7*k7.getAcc(i)[0] + g8*k8.getAcc(i)[0] + g9*k9.getAcc(i)[0] ),
                    model.getVel(i)[1] + dt * ( g1*model.getAcc(i)[1] + g2*k2.getAcc(i)[1] + g3*k3.getAcc(i)[1] + g4*k4.getAcc(i)[1] + g5*k5.getAcc(i)[1] + g6*k6.getAcc(i)[1] + g7*k7.getAcc(i)[1] + g8*k8.getAcc(i)[1] + g9*k9.getAcc(i)[1] ),
                    model.getVel(i)[2] + dt * ( g1*model.getAcc(i)[2] + g2*k2.getAcc(i)[2] + g3*k3.getAcc(i)[2] + g4*k4.getAcc(i)[2] + g5*k5.getAcc(i)[2] + g6*k6.getAcc(i)[2] + g7*k7.getAcc(i)[2] + g8*k8.getAcc(i)[2] + g9*k9.getAcc(i)[2] )   } );

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


        // k7: t+dt, y+ dt ( b71*k1 + b73*k3 + b74*k4 + b75*k5 )
        k8 = model.clone(null);
        state = model.getState();

        for(int i=0; i<k8.size(); i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += dt*( b81*model.getVel(i)[k] + b82*k2.getVel(i)[k] + b83*k3.getVel(i)[k] + b84*k4.getVel(i)[k] + b85*k5.getVel(i)[k] + b86*k6.getVel(i)[k] + b87*k7.getVel(i)[k] );
            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += dt*( b81*model.getAcc(i)[k] + b82*k2.getAcc(i)[k] + b83*k3.getAcc(i)[k] + b84*k4.getAcc(i)[k] + b85*k5.getAcc(i)[k] + b86*k6.getAcc(i)[k] + b87*k7.getAcc(i)[k] );
        }

        k8.setState(state);
        k8.addDt(dt*a8);
        k8.hDeriv();


        // k7: t+dt, y+ dt ( b71*k1 + b73*k3 + b74*k4 + b75*k5 )
        k9 = model.clone(null);
        state = model.getState();

        for(int i=0; i<k9.size(); i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += dt*( b91*model.getVel(i)[k] + b92*k2.getVel(i)[k] + b93*k3.getVel(i)[k] + b94*k4.getVel(i)[k] + b95*k5.getVel(i)[k] + b96*k6.getVel(i)[k] + b97*k7.getVel(i)[k] + b98*k8.getVel(i)[k] );
            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += dt*( b91*model.getAcc(i)[k] + b92*k2.getAcc(i)[k] + b93*k3.getAcc(i)[k] + b94*k4.getAcc(i)[k] + b95*k5.getAcc(i)[k] + b96*k6.getAcc(i)[k] + b97*k7.getAcc(i)[k] + b98*k8.getAcc(i)[k] );
        }

        k9.setState(state);
        k9.addDt(dt*a9);
        k9.hDeriv();

    }

    @Override
    public String getName() {
        return "RK7";
    }

}
