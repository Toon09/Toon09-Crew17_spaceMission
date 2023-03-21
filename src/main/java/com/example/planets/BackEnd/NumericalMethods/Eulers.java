package com.example.planets.BackEnd.NumericalMethods;

import com.example.planets.BackEnd.Models.*;

public class Eulers {

    //3D numerical methods

    //updates positions in 3d space
    public static void step3D(Model3D model, double dt){

        //update position
        for(int i=0; i<model.size(); i++){
            model.setPos(i, new double[] {  model.getPos(i)[0] + model.getVel(i)[0] * dt,
                                            model.getPos(i)[1] + model.getVel(i)[1] * dt,
                                            model.getPos(i)[2] + model.getVel(i)[2] * dt   } );
        }

        //update acc
        model._2Deriv();

        //update vel
        for(int i=0; i<model.size(); i++){
            model.setVel(i, new double[] {  model.getVel(i)[0] + model.getAcc(i)[0] * dt,
                                            model.getVel(i)[1] + model.getAcc(i)[1] * dt,
                                            model.getVel(i)[2] + model.getAcc(i)[2] * dt   } );
        }

        //adds time
        model.addDt(dt);

    }




    //////// 1 dimensional for testing of methods, 3d is above
    
    //calculates values of next step and returns then at the same time as saving them in model
    public static double _1DegStep1D(Model1D model, double dx){

        //getting values
        double deriv = model._1Deriv();
        double out = model.getY() + deriv * dx;

        //updating previous values
        model.setX( model.getX() +  dx );
        model.setY(out);

        return out;
    }

    //calculates all next steps with prev values
    public static double _2DegStep1D(Model1D model, double dx){
        //calc derivatives
        double deriv2 = model._2Deriv();
        double deriv1 = model._1Deriv() + deriv2 * dx;

        //do process ignoring new derivatives
        double out = _1DegStep1D(model, dx);

        //save new derivatives for next iteration
        model.setYD(deriv1);
        
        return out;
    }

}
