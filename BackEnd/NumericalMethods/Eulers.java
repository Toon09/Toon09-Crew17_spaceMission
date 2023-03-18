package BackEnd.NumericalMethods;
import BackEnd.Models.*;

public class Eulers {

    //3D numerical methods

    //updates positions in 3d space
    public static void _1DegStep3D(Model3D model, double dt){

        //need to:
        //get all x, y, z and add with dt * deriv of x, y, z (temp[i] = getVel[i] * dt + pos[i])
        //update x, y, z positions ( pos[i] = temp[i] )
        //add dt to the time (addDt)

        

    }

    //updates velocities and acceleration
    public static void _2DegStep3D(Model3D model, double dt){

        //need to:
        //get all x', y', z' and add with dt * deriv of x', y', z' (temp[i] = getAcc[i] * dt + vel[i])
        //update x, y, z positions ( vel[i] = temp[i] )
        //call _1DegStep
        
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
