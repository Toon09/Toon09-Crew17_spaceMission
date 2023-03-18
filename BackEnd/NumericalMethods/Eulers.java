package BackEnd.NumericalMethods;
import BackEnd.Models.Model;

public class Eulers {
    
    //calculates values of next step and returns then at the same time as saving them in model
    public static double _1DegStep(Model model, double dx){

        //getting values
        double deriv = model._1Deriv();
        double out = model.getY() + deriv * dx;

        //updating previous values
        model.setX( model.getX() +  dx );
        model.setY(out);

        return out;
    }

    //calculates all next steps with prev values
    public static double _2DegStep(Model model, double dx){
        //calc derivatives
        double deriv2 = model._2Deriv();
        double deriv1 = model._1Deriv() + deriv2 * dx;

        //do process ignoring new derivatives
        double out = _1DegStep(model, dx);

        //save new derivatives for next iteration
        model.setYD(deriv1);
        
        return out;
    }

}
