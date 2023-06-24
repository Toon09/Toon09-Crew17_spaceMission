package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

public class ode78 implements AdaptiveMethod{

    private double precision = 1.0;
    private double length = 0.0;
    private double step = 0.0;

    public ode78(double precision){ this.precision = precision; }
    public ode78(){} // uses default precision of 0.01

    @Override
    public void step(Model3D model, double dt) {
        step = dt;

        for(double k=0.0 ;k<length; k+=step){
            Model3D higher = model.clone( new RK8() );
            Model3D lower = model.clone( new RK7() );

            higher.updatePos(step, step, false);
            lower.updatePos(step, step, false);

            // getting maximun error of all planets and all directions
            double error = 0.0;
            for(int i=0; i< model.size(); i++){
                for(int j=0; j<3; j++){
                    error = Math.max( error, Math.abs( higher.getPos(i)[j] - lower.getPos(i)[j] ) );
                }
            }

            double optimalMultiplier = Math.pow( (precision*step)/(2*error) , 1/5.0);


            // check error
            if(precision <= optimalMultiplier){ // step is correct, thus
                model.setState( higher.getState() );
                model.addDt(step);
                model.hDeriv();

                // update step size after the last state is set
                step = step*optimalMultiplier;

            }else { // step doesnt meet requirement, thus adds 1 more step to be done and doesnt save process done
                // negates the step that was just taken in the count of steps (the for loop)
                // so its as if nothing happened
                k -= step;

                step = step*optimalMultiplier;

            }


        }


    }

    @Override
    public String getName() {
        return "ode78";
    } //
    @Override
    public void inputLength(double length) {
        this.length = length;
    }
}
