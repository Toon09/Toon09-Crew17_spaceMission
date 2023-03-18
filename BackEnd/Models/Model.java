package BackEnd.Models;

/*
 * interface that makes using models and coding them into numerical methods faster
 */
public interface Model {

    //each model must be initialized with initial values on them

    //setters
    public void setX(double x);
    public void setY(double y);
    public void setYD(double yD);

    //getters
    public double getX();
    public double getY();

    //calculates first derivative
    public double _1Deriv();

    //calculates second deriv if there is
    public double _2Deriv();

}
