package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

public class FastRK2 implements NumSolver {

    // Ralston's method by default
    private double alpha = 2.0/3.0;
    private double[][][] state = null;

    public FastRK2(){}
    public FastRK2(double alpha){ this.alpha = alpha; }

    @Override
    public void step(Model3D model, double dt) {

        // use state instead to run step, can use instance of euler or by hand
        setState( model.getState() );

        // loop through all values and do a step of euler with dt*alpha from vel to pos
        for(int i=0; i< model.size(); i++){
            //loop through all objects
            for (int j=0; j<3; j++){
                // update position of each of them
                setPos(i,   this.getPos(i)[0] + this.getVel(i)[0] * dt*alpha,
                            this.getPos(i)[1] + this.getVel(i)[1] * dt*alpha,
                            this.getPos(i)[2] + this.getVel(i)[2] * dt*alpha);
            }
        }


        //update position
        for(int i=0; i<model.size(); i++){

            model.setPos(i, new double[] {  model.getPos(i)[0] + dt * ( (1-1/(2*alpha)) * model.getVel(i)[0] + (1/(2*alpha)) * this.getVel(i)[0] ),
                                            model.getPos(i)[1] + dt * ( (1-1/(2*alpha)) * model.getVel(i)[1] + (1/(2*alpha)) * this.getVel(i)[1] ),
                                            model.getPos(i)[2] + dt * ( (1-1/(2*alpha)) * model.getVel(i)[2] + (1/(2*alpha)) * this.getVel(i)[2] )   } );

        }


        // loop thru all values and do a step of euler with dt*alpha from acc to vel
        for(int i=0; i< model.size(); i++){
            //loop thru all objects
            for (int j=0; j<3; j++){
                // update position of each of them
                setVel(i,   this.getVel(i)[0] + model.getAcc(i)[0] * dt*alpha,
                            this.getVel(i)[1] + model.getAcc(i)[1] * dt*alpha,
                            this.getVel(i)[2] + model.getAcc(i)[2] * dt*alpha);
            }
        }


        //update vel
        for(int i=0; i<model.size(); i++){

            model.setVel(i, new double[] {  model.getVel(i)[0] + dt * ( (1-1/(2*alpha)) * model.getAcc(i)[0] + (1/(2*alpha)) * this.getAcc(i)[0] ),
                                            model.getVel(i)[1] + dt * ( (1-1/(2*alpha)) * model.getAcc(i)[1] + (1/(2*alpha)) * this.getAcc(i)[1] ),
                                            model.getVel(i)[2] + dt * ( (1-1/(2*alpha)) * model.getAcc(i)[2] + (1/(2*alpha)) * this.getAcc(i)[2] )    } );

        }

        //update acceleration
        model.hDeriv();

        //adds time
        model.addDt(dt);

        //update for trajectory changes of ship
        // trajectory changes, right after current acc is calculated
        if( model.getShip().trajectoryChangeCondition(model) ){
            //the magnitudes and such can be saved in another class and this call can be emptied out
            //the values can be saved in an array of sorts
            model.getShip().accelerate(dt);
        }
    }

    @Override
    public void innitState(double[][][] state) {
        this.state = new double[ state.length ][ 3 ][ 3 ];
    }


    public void setState(double[][][] state) {
        for(int i=0; i<state.length; i++){
            for(int j=0; j<3; j++){
                System.arraycopy(state[i][j], 0, this.state[i][j], 0, 3);
            }
        }

    }

    @Override
    public String getName() {
        return "Fast RK2";
    }

    private double[] getVel(int index){
        return state[index][1];
    }

    private void setVel(int index, double x, double y, double z){
        state[index][1][0] = x;
        state[index][1][1] = y;
        state[index][1][2] = z;
    }

    private double[] getPos(int index){
        return state[index][0];
    }

    private void setPos(int index, double x, double y, double z){
        state[index][0][0] = x;
        state[index][0][1] = y;
        state[index][0][2] = z;
    }

    private double[] getAcc(int index){
        return state[index][2];
    }

}
