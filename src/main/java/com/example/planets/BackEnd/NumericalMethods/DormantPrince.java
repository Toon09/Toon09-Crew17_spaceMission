package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.Model3D;

// https://numerary.readthedocs.io/en/latest/dormand-prince-method.html
public class DormantPrince implements AdaptiveMethod {

    private double precision = 1.0;
    private double length = 0.0;
    private double step = 0.0;

    public DormantPrince(double precision){ this.precision = precision; }
    public DormantPrince(){} // uses default precision of 0.01

    @Override
    public void step(Model3D model, double dt) {
        step = dt;

        for(double k=0.0 ;k<length; k+=step){
            Model3D higher = model.clone( new ButchersRK5() );
            Model3D lower = model.clone( new RK4() );

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
            if(precision >= optimalMultiplier){ // step is correct, thus
                model.setState( higher.getState() );
                model.addDt(step);
                model.hDeriv();
                // update step size after the last state is set
                if(step <= 10.0)
                    step = step*optimalMultiplier;

            }else { // step doesnt meet requirement, thus adds 1 more step to be done and doesnt save process done
                // update stepsize before adding the extra step you need to do
                step = step*optimalMultiplier;

                // negates the step that was just taken in the count of steps (the for loop)
                // so its as if nothing happened
                length -= step;

            }


        }


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
