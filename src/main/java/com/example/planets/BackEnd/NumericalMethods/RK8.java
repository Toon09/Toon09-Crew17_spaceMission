package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

// http://www.mymathlib.com/diffeq/runge-kutta/runge_kutta_verner.html#:~:text=Verner's%20eighth%20order%20method%20is,)%2C%20eleven%20times%20per%20step.
public class RK8 implements NumSolver{

    /////////////////// change params to one in link

    Model3D k2;
    Model3D k3;
    Model3D k4;
    Model3D k5;
    Model3D k6;
    Model3D k7;
    Model3D k8;
    Model3D k9;
    Model3D k10;
    Model3D k11;


    // coefficients for adding times
    static final double a2 = 1/2.0; // also b11
    static final double a3 = 1/2.0;
    static final double a4 = (7+Math.sqrt(21.0))/14.0;
    static final double a5 = (7+Math.sqrt(21.0))/14.0;
    static final double a6 = 1/2.0;
    static final double a7 = (7-Math.sqrt(21.0))/14.0;
    static final double a8 = (7-Math.sqrt(21.0))/14.0;
    static final double a9 = 1/2.0;
    static final double a10 = (7+Math.sqrt(21.0))/14.0;
    static final double a11 = 1.0;


    // values for calculating k's
    static final double b31 =  1.0 / 4.0;
    static final double b32 =  1.0 / 4.0;

    static final double b41 =  1.0 / 7.0;
    static final double b42 = -(7.0 + 3.0 * Math.sqrt(21.0)) / 98.0;
    static final double b43 =  (21.0 + 5.0 * Math.sqrt(21.0)) / 49.0;

    static final double b51 =  (11.0 + Math.sqrt(21.0)) / 84.0;
    static final double b52 =  0;
    static final double b53 =  (18.0 + 4.0 * Math.sqrt(21.0)) / 63.0;
    static final double b54 =  (21.0 - Math.sqrt(21.0)) / 252.0;

    static final double b61 =  (5.0 + Math.sqrt(21.0)) / 48.0;
    static final double b62 =  0;
    static final double b63 =  (9.0 + Math.sqrt(21.0)) / 36.0;
    static final double b64 =  (-231.0 + 14.0 * Math.sqrt(21.0)) / 360.0;
    static final double b65 =  (63.0 - 7.0 * Math.sqrt(21.0)) / 80.0;

    static final double b71 =  (10.0 - Math.sqrt(21.0)) / 42.0;
    static final double b72 =  0;
    static final double b73 =  (-432.0 + 92.0 * Math.sqrt(21.0)) / 315.0;
    static final double b74 =  (633.0 - 145.0 * Math.sqrt(21.0)) / 90.0;
    static final double b75 =  (-504.0 + 115.0 * Math.sqrt(21.0)) / 70.0;
    static final double b76 =  (63.0 - 13.0 * Math.sqrt(21.0)) / 35.0;

    static final double b81 =  1.0 / 14.0;
    static final double b82 =  0;
    static final double b83 =  0;
    static final double b84 =  0;
    static final double b85 =  (14.0 - 3.0 * Math.sqrt(21.0)) / 126.0;
    static final double b86 =  (13.0 - 3.0 * Math.sqrt(21.0)) / 63.0;
    static final double b87 =  1.0 / 9.0;

    static final double b91 =  1.0 / 32.0;
    static final double b92 =  0;
    static final double b93 =  0;
    static final double b94 =  0;
    static final double b95 =  (91.0 - 21.0 * Math.sqrt(21.0)) / 576.0;
    static final double b96 =  11.0 / 72.0;
    static final double b97 = -(385.0 + 75.0 * Math.sqrt(21.0)) / 1152.0;
    static final double b98 =  (63.0 + 13.0 * Math.sqrt(21.0)) / 128.0;

    static final double b101 =  1.0 / 14.0;
    static final double b102 =  0;
    static final double b103 =  0;
    static final double b104 =  0;
    static final double b105 =  1.0 / 9.0;
    static final double b106 = -(733.0 + 147.0 * Math.sqrt(21.0)) / 2205.0;
    static final double b107 =  (515.0 + 111.0 * Math.sqrt(21.0)) / 504.0;
    static final double b108 = -(51.0 + 11.0 * Math.sqrt(21.0)) / 56.0;
    static final double b109 =  (132.0 + 28.0 * Math.sqrt(21.0)) / 245.0;

    static final double b111 = 0;
    static final double b112 = 0;
    static final double b113 = 0;
    static final double b114 = 0;
    static final double b115 = (-42.0 + 7.0 * Math.sqrt(21.0)) / 18.0;
    static final double b116 = (-18.0 + 28.0 * Math.sqrt(21.0)) / 45.0;
    static final double b117 = -(273.0 + 53.0 * Math.sqrt(21.0)) / 72.0;
    static final double b118 =  (301.0 + 53.0 * Math.sqrt(21.0)) / 72.0;
    static final double b119 =  (28.0 - 28.0 * Math.sqrt(21.0)) / 45.0;
    static final double b1110 = (49.0 - 7.0 * Math.sqrt(21.0)) / 18.0;

    // values for rk7
    static final double g1 = 9/180.0;
    static final double g2 = 0;
    static final double g3 = 0;
    static final double g4 = 0;
    static final double g5 = 0;
    static final double g6 = 0;
    static final double g7 = 0;
    static final double g8 = 49/180.0;
    static final double g9 = 64/180.0;
    static final double g10 = g8;
    static final double g11 = g1;

    @Override
    public void step(Model3D model, double dt) {

        //set up rk4 for position only
        RKsetUpVals(model, dt);

        //update position
        for(int i=0; i<model.size(); i++){

            model.setPos(i, new double[] {  model.getPos(i)[0] + dt * ( g1*model.getVel(i)[0] + g2*k2.getVel(i)[0] + g3*k3.getVel(i)[0] + g4*k4.getVel(i)[0] + g5*k5.getVel(i)[0] + g6*k6.getVel(i)[0] + g7*k7.getVel(i)[0] + g8*k8.getVel(i)[0] + g9*k9.getVel(i)[0] + g10*k10.getVel(i)[0] + g11*k11.getVel(i)[0] ),
                    model.getPos(i)[1] + dt * ( g1*model.getVel(i)[1] + g2*k2.getVel(i)[1] + g3*k3.getVel(i)[1] + g4*k4.getVel(i)[1] + g5*k5.getVel(i)[1] + g6*k6.getVel(i)[1] + g7*k7.getVel(i)[1] + g8*k8.getVel(i)[1] + g9*k9.getVel(i)[1] + g10*k10.getVel(i)[1] + g11*k11.getVel(i)[1] ),
                    model.getPos(i)[2] + dt * ( g1*model.getVel(i)[2] + g2*k2.getVel(i)[2] + g3*k3.getVel(i)[2] + g4*k4.getVel(i)[2] + g5*k5.getVel(i)[2] + g6*k6.getVel(i)[2] + g7*k7.getVel(i)[2] + g8*k8.getVel(i)[2] + g9*k9.getVel(i)[2] + g10*k10.getVel(i)[2] + g11*k11.getVel(i)[2] )   } );

        }


        //update vel
        for(int i=0; i<model.size(); i++){

            model.setVel(i, new double[] {  model.getVel(i)[0] + dt * ( g1*model.getAcc(i)[0] + g2*k2.getAcc(i)[0] + g3*k3.getAcc(i)[0] + g4*k4.getAcc(i)[0] + g5*k5.getAcc(i)[0] + g6*k6.getAcc(i)[0] + g7*k7.getAcc(i)[0] + g8*k8.getAcc(i)[0] + g9*k9.getAcc(i)[0] + g10*k10.getAcc(i)[0] + g11*k11.getAcc(i)[0] ),
                    model.getVel(i)[1] + dt * ( g1*model.getAcc(i)[1] + g2*k2.getAcc(i)[1] + g3*k3.getAcc(i)[1] + g4*k4.getAcc(i)[1] + g5*k5.getAcc(i)[1] + g6*k6.getAcc(i)[1] + g7*k7.getAcc(i)[1] + g8*k8.getAcc(i)[1] + g9*k9.getAcc(i)[1] + g10*k10.getAcc(i)[1] + g11*k11.getAcc(i)[1] ),
                    model.getVel(i)[2] + dt * ( g1*model.getAcc(i)[2] + g2*k2.getAcc(i)[2] + g3*k3.getAcc(i)[2] + g4*k4.getAcc(i)[2] + g5*k5.getAcc(i)[2] + g6*k6.getAcc(i)[2] + g7*k7.getAcc(i)[2] + g8*k8.getAcc(i)[2] + g9*k9.getAcc(i)[2] + g10*k10.getAcc(i)[2] + g11*k11.getAcc(i)[2] )   } );

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


        // k7: t+dt, y+ dt ( b71*k1 + b73*k3 + b74*k4 + b75*k5 )
        k10 = model.clone(null);
        state = model.getState();

        for(int i=0; i<k10.size(); i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += dt*( b101*model.getVel(i)[k] + b102*k2.getVel(i)[k] + b103*k3.getVel(i)[k] + b104*k4.getVel(i)[k] + b105*k5.getVel(i)[k] + b106*k6.getVel(i)[k] + b107*k7.getVel(i)[k] + b108*k8.getVel(i)[k] + b109*k9.getVel(i)[k] );
            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += dt*( b101*model.getAcc(i)[k] + b102*k2.getAcc(i)[k] + b103*k3.getAcc(i)[k] + b104*k4.getAcc(i)[k] + b105*k5.getAcc(i)[k] + b106*k6.getAcc(i)[k] + b107*k7.getAcc(i)[k] + b108*k8.getAcc(i)[k] + b109*k9.getAcc(i)[k] );
        }

        k10.setState(state);
        k10.addDt(dt*a10);
        k10.hDeriv();


        // k7: t+dt, y+ dt ( b71*k1 + b73*k3 + b74*k4 + b75*k5 )
        k11 = model.clone(null);
        state = model.getState();

        for(int i=0; i<k11.size(); i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += dt*( b111*model.getVel(i)[k] + b112*k2.getVel(i)[k] + b113*k3.getVel(i)[k] + b114*k4.getVel(i)[k] + b115*k5.getVel(i)[k] + b116*k6.getVel(i)[k] + b117*k7.getVel(i)[k] + b118*k8.getVel(i)[k] + b119*k9.getVel(i)[k] + b1110*k10.getVel(i)[k] );
            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += dt*( b111*model.getAcc(i)[k] + b112*k2.getAcc(i)[k] + b113*k3.getAcc(i)[k] + b114*k4.getAcc(i)[k] + b115*k5.getAcc(i)[k] + b116*k6.getAcc(i)[k] + b117*k7.getAcc(i)[k] + b118*k8.getAcc(i)[k] + b119*k9.getAcc(i)[k] + b1110*k10.getAcc(i)[k] );
        }

        k11.setState(state);
        k11.addDt(dt*a11);
        k11.hDeriv();

    }

    @Override
    public String getName() {
        return "RK8";
    }

}
