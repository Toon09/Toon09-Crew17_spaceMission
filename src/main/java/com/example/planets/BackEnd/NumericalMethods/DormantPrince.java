package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

// https://numerary.readthedocs.io/en/latest/dormand-prince-method.html
public class DormantPrince implements AdaptiveMethod {

    private double precision = 50.0;
    private double length = 0.0;
    private double step = 0.0;

    Model3D k2;
    Model3D k3;
    Model3D k4;
    Model3D k5;
    Model3D k6;
    Model3D k7;


    // coefficients for adding times
    static final double a2 = 1/5.0; // also b1
    static final double a3 = 3/10.0;
    static final double a4 = 4/5.0;
    static final double a5 = 8/9.0;
    static final double a6 = 1;
    static final double a7 = 1;

    // values for calculating k's
    static final double b31 = 3/40.0;
    static final double b32 = 9/40.0;

    static final double b41 = 44/45.0;
    static final double b42 = -56/15.0;
    static final double b43 = 32/9.0;

    static final double b51 = 19372/6561.0;
    static final double b52 = -25360/2187.0;
    static final double b53 = 64448/6561.0;
    static final double b54 = -212/729.0;

    static final double b61 =  9017/3168.0;
    static final double b62 =  -355/33.0;
    static final double b63 =  -46732/5247.0;
    static final double b64 =  49/176.0;
    static final double b65 =  -5103/18656.0;

    static final double b71 =  35/384.0;
    static final double b72 =  0;
    static final double b73 =  500/1113.0;
    static final double b74 = 125/192.0;
    static final double b75 = -2187/6784.0;
    static final double b76 =  11/84.0;


    // values for rk7
    private static final double g1 = 35/384.0;
    static final double g2 = 0;
    static final double g3 = 500/1113.0;
    static final double g4 = 125/192.0;
    static final double g5 = -2187/6784.0;
    static final double g6 = 11/84.0;


    // error calculation terms
    private static final double e1 = 71/57600.0;
    private static final double e2 = 0;
    private static final double e3 = -71/16695.0;
    private static final double e4 = 71/1920.0;
    private static final double e5 = -17253/339200.0;
    private static final double e6 = 22/525.0;
    private static final double e7 = -1/40.0;


    public DormantPrince(double precision){ this.precision = precision; }
    public DormantPrince(){} // uses default precision of 0.01


    @Override
    public void step(Model3D model, double dt) {
        step = dt;
        double added = step;

        for(double k=0.0 ;k<length; k+=added){
            Model3D temp = model.clone();

            added = step;

            //set up rk4 for position only
            RKsetUpVals(model, step);

            //update position
            for(int i=0; i<temp.size(); i++){

                temp.setPos(i, new double[] {  temp.getPos(i)[0] + step * ( g1*model.getVel(i)[0] + g2*k2.getVel(i)[0] + g3*k3.getVel(i)[0] + g4*k4.getVel(i)[0] + g5*k5.getVel(i)[0] + g6*k6.getVel(i)[0] ),
                        temp.getPos(i)[1] + step * ( g1*model.getVel(i)[1] + g2*k2.getVel(i)[1] + g3*k3.getVel(i)[1] + g4*k4.getVel(i)[1] + g5*k5.getVel(i)[1] + g6*k6.getVel(i)[1] ),
                        temp.getPos(i)[2] + step * ( g1*model.getVel(i)[2] + g2*k2.getVel(i)[2] + g3*k3.getVel(i)[2] + g4*k4.getVel(i)[2] + g5*k5.getVel(i)[2] + g6*k6.getVel(i)[2] )   } );

            }


            //update vel
            for(int i=0; i<temp.size(); i++){

                temp.setVel(i, new double[] {  temp.getVel(i)[0] + step * ( g1*model.getAcc(i)[0] + g2*k2.getAcc(i)[0] + g3*k3.getAcc(i)[0] + g4*k4.getAcc(i)[0] + g5*k5.getAcc(i)[0] + g6*k6.getAcc(i)[0] ),
                        temp.getVel(i)[1] + step * ( g1*model.getAcc(i)[1] + g2*k2.getAcc(i)[1] + g3*k3.getAcc(i)[1] + g4*k4.getAcc(i)[1] + g5*k5.getAcc(i)[1] + g6*k6.getAcc(i)[1] ),
                        temp.getVel(i)[2] + step * ( g1*model.getAcc(i)[2] + g2*k2.getAcc(i)[2] + g3*k3.getAcc(i)[2] + g4*k4.getAcc(i)[2] + g5*k5.getAcc(i)[2] + g6*k6.getAcc(i)[2] )   } );

            }

            // letting the ship do its plans (works for many ships)
            for(int i=0; i< temp.getAmountOfShips(); i++){
                temp.getShip(i).executePlans(temp.getTime(), step);
            }

            // since we can have several planets, we get the maximun error and act according to that one
            // and since its 3 dimensions we also consider the max of the 3 directions
            double error = 0.0;
            for(int i=0; i< temp.size(); i++){
                for(int j=0; j<3; j++)
                    error = Math.max( error, Math.abs( e1*temp.getPos(i)[j] + e2*k2.getPos(i)[j] + e3*k3.getPos(i)[j] + e4*k4.getPos(i)[j] + e5*k5.getPos(i)[j] + e6*k6.getPos(i)[j] + e7*k7.getPos(i)[j] ) );
            }

            double optimalMultiplier = Math.pow( (precision*step)/(2*error) , 1/5.0);


            // check error
            if(precision >= error){ // step is correct, thus
                model.setState( temp.getState() );
                model.addDt(step);
                model.hDeriv();

            }else { // step doesnt meet requirement, thus adds 1 more step to be done and doesnt save process done
                k -= added;
                step = step*optimalMultiplier*0.5;

            }

            //model.setState( temp.getState() );
            //model.addDt(step);
            //model.hDeriv();

        }


    }

    private void RKsetUpVals(Model3D model, double step){
        // k1 is normal euler step to b21*step
        k2 = model.clone(new Euler());
        k2.updatePos(step*a2, step*a2, false);

        // k3: t+step, y + step ( b31*k1 + b32*k2 )
        k3 = model.clone(null);
        double[][][] state = model.getState();

        for(int i=0; i<k3.size(); i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += step*( b31*model.getVel(i)[k] + b32*k2.getVel(i)[k] );
            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += step*( b31*model.getAcc(i)[k] + b32*k2.getAcc(i)[k] );
        }

        k3.setState(state);
        k3.addDt(step*a3);
        k3.hDeriv();


        // k4: t+step, y+ step ( b41*k1 + b42*k2 + b43*k3 )
        k4 = model.clone(null);
        state = model.getState();

        for(int i=0; i<k4.size(); i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += step*( b41*model.getVel(i)[k] + b42*k2.getVel(i)[k] + b43*k3.getVel(i)[k] );
            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += step*( b41*model.getAcc(i)[k] + b42*k2.getAcc(i)[k] + b43*k3.getAcc(i)[k] );
        }

        k4.setState(state);
        k4.addDt(step*a4);
        k4.hDeriv();


        // k5: t+step, y+ step ( b51*k1 + b52*k2 + b53*k3 + b54*k4 )
        k5 = model.clone(null);
        state = model.getState();

        for(int i=0; i<k5.size(); i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += step*( b51*model.getVel(i)[k] + b52*k2.getVel(i)[k] + b53*k3.getVel(i)[k] + b54*k4.getVel(i)[k] );
            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += step*( b51*model.getAcc(i)[k] + b52*k2.getAcc(i)[k] + b53*k3.getAcc(i)[k] + b54*k4.getAcc(i)[k] );
        }

        k5.setState(state);
        k5.addDt(step*a5);
        k5.hDeriv();


        // k6: t+step, y+ step ( b61*k1 + b62*k2 + b63*k3 + b64*k4 )
        k6 = model.clone(null);
        state = model.getState();

        for(int i=0; i<k6.size(); i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += step*( b61*model.getVel(i)[k] + b62*k2.getVel(i)[k] + b63*k3.getVel(i)[k] + b64*k4.getVel(i)[k] + b65*k5.getVel(i)[k] );
            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += step*( b61*model.getAcc(i)[k] + b62*k2.getAcc(i)[k] + b63*k3.getAcc(i)[k] + b64*k4.getAcc(i)[k] + b65*k5.getAcc(i)[k] );
        }

        k6.setState(state);
        k6.addDt(step*a6);
        k6.hDeriv();


        // k7: t+step, y+ step ( b71*k1 + b73*k3 + b74*k4 + b75*k5 )
        k7 = model.clone(null);
        state = model.getState();

        for(int i=0; i<k7.size(); i++){
            // position
            for(int k=0; k<3; k++)
                state[i][0][k] += step*( b71*model.getVel(i)[k] + b72*k2.getVel(i)[k] + b73*k3.getVel(i)[k] + b74*k4.getVel(i)[k] + b75*k5.getVel(i)[k] + b76*k6.getVel(i)[k] );
            // velocity
            for(int k=0; k<3; k++)
                state[i][1][k] += step*( b71*model.getAcc(i)[k] + b72*k2.getAcc(i)[k] + b73*k3.getAcc(i)[k] + b74*k4.getAcc(i)[k] + b75*k5.getAcc(i)[k] + b76*k6.getAcc(i)[k] );
        }

        k7.setState(state);
        k7.addDt(step*a7);
        k7.hDeriv();

    }


    @Override
    public String getName() {
        return "Dormant Prince";
    } //
    @Override
    public void inputLength(double length) {
        this.length = length;
    }

}