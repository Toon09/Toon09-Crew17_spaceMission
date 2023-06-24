package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

// https://numerary.readthedocs.io/en/latest/dormand-prince-method.html
// https://en.wikipedia.org/wiki/Runge%E2%80%93Kutta%E2%80%93Fehlberg_method
public class DormandPrince implements AdaptiveMethod {

    private double step = 0.0;
    private double presicion = 0.01; // default
    private double length = 0.0;

    Model3D k2;
    Model3D k3;
    Model3D k4;
    Model3D k5;
    Model3D k6;
    Model3D k7;

    // coefficients for adding times
    static final double a2 = 1/5.0;
    static final double a3 = 3/10.0;
    static final double a4 = 4/5.0;
    static final double a5 = 8/9.0;


    // values for calculating k's
    static final double b31 = 3/10.0;
    static final double b32 = 9/10.0;

    static final double b41 = 44/45.0;
    static final double b42 = -56/15.0;
    static final double b43 = 23/9.0;

    static final double b51 = 19372/6561.0;
    static final double b52 = -25360/2187.0;
    static final double b53 = 64448/6561.0;
    static final double b54 = -212/729.0;

    static final double b61 = 9017/3168.0;
    static final double b62 = -355/33.0;
    static final double b63 =  46732/5247.0;
    static final double b64 = -49/176.0;
    static final double b65 = -5103/18656.0;

    static final double b71 = 35/384.0;
    static final double b73 =  500/1113.0;
    static final double b74 = 125/192.0;
    static final double b75 = -2187/6784.0;
    static final double b76 = 11/84.0;

    // values for rk7
    static final double g1 = 5179/57600.0;
    static final double g3 = 7571/16695.0;
    static final double g4 = 393/640.0;
    static final double g5 = 92097/339200.0;
    static final double g6 = 187/2100.0;
    static final double g7 = 1/40.0;

    // values for rk6

    public DormandPrince(double precision){this.presicion = precision;} //sets precision
    public DormandPrince(){}; // uses default precision


    ////// http://www.mymathlib.com/c_source/diffeq/runge_kutta/runge_kutta_ralston_4.c
    @Override
    public void step(Model3D model, double dt) {
        if(step == 0.0)
            step = dt;

        RKsetUpVals(model, dt);

        //update position
        for(int i=0; i<model.size(); i++){

            model.setPos(i, new double[] {  model.getPos(i)[0] + dt * ( g1*model.getVel(i)[0] + g3*k3.getVel(i)[0] + g4*k4.getVel(i)[0] + g5*k5.getVel(i)[0] + g6*k6.getVel(i)[0] + g7*k7.getVel(i)[0] ),
                    model.getPos(i)[1] + dt * ( g1*model.getVel(i)[1] + g3*k3.getVel(i)[1] + g4*k4.getVel(i)[1] + g5*k5.getVel(i)[1] + g6*k6.getVel(i)[1] + g7*k7.getVel(i)[1] ),
                    model.getPos(i)[2] + dt * ( g1*model.getVel(i)[2] + g3*k3.getVel(i)[2] + g4*k4.getVel(i)[2] + g5*k5.getVel(i)[2] + g6*k6.getVel(i)[2] + g7*k7.getVel(i)[2] )   } );

        }


        //update vel
        for(int i=0; i<model.size(); i++){

            model.setVel(i, new double[] {  model.getVel(i)[0] + dt * ( g1*model.getAcc(i)[0] + g3*k3.getAcc(i)[0] + g4*k4.getAcc(i)[0] + g5*k5.getAcc(i)[0] + g6*k6.getAcc(i)[0] + g7*k7.getAcc(i)[0] ),
                    model.getVel(i)[1] + dt * ( g1*model.getAcc(i)[1] + g3*k3.getAcc(i)[1] + g4*k4.getAcc(i)[1] + g5*k5.getAcc(i)[1] + g6*k6.getAcc(i)[1] + g7*k7.getAcc(i)[1] ),
                    model.getVel(i)[2] + dt * ( g1*model.getAcc(i)[2] + g3*k3.getAcc(i)[2] + g4*k4.getAcc(i)[2] + g5*k5.getAcc(i)[2] + g6*k6.getAcc(i)[2] + g7*k7.getAcc(i)[2] )   } );

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


        // k6: t+dt, y+ dt ( b61*k1 + b52*k2 + b53*k3 + b54*k4 )
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
        k6.addDt(dt);
        k6.hDeriv();


        // k7: t+dt, y+ dt ( b71*k1 + b73*k3 + b74*k4 + b75*k5 )
        k7 = model.clone(null);
        state = model.getState();

        for(int i=0; i<k7.size(); i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += dt*( b71*model.getVel(i)[k] + b73*k3.getVel(i)[k] + b74*k4.getVel(i)[k] + b75*k5.getVel(i)[k] + b76*k6.getVel(i)[k] );
            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += dt*( b71*model.getAcc(i)[k] + b73*k3.getAcc(i)[k] + b74*k4.getAcc(i)[k] + b75*k5.getAcc(i)[k] + b76*k6.getAcc(i)[k] );
        }

        k7.setState(state);
        k7.addDt(dt);
        k7.hDeriv();


    }

    @Override
    public String getName() {
        return "Dormant prince";
    }

    @Override
    public void inputLength(double length) {
        this.length = length;
    }
}
