package BackEnd.Models;

/*
 * interface that makes using models and coding them into numerical methods faster
 */
public interface Model1D {

    //each model must be initialized with initial values on them

    //setters
    public void setX(double x);
    public void setY(double y);
    public void setYD(double yd);

    //getters
    public double getX();
    public double getY();

    //calc derivs
    public double _1Deriv();
    public double _2Deriv();

}
