package BackEnd.Models;

//example for second degree diff. eq. but couldnt think of anything
public class simple2 implements Model {

    private double x;
    private double y;
    private double yD;

    public simple2(double xInnit, double yInnit, double yDInnit){
        x = xInnit;
        y = yInnit;
        yD = yDInnit;
    }

    //setters
    @Override
    public void setX(double x) { this.x = x; }
    @Override
    public void setY(double y) { this.y = y; }
    @Override
    public void setYD(double yD) { this. yD = yD; }

    //getters
    @Override
    public double getX() { return x; }
    @Override
    public double getY() { return y; }

    //calculating derivs
    @Override
    public double _1Deriv() {
        return yD;
    }

    @Override
    public double _2Deriv() {
        return 0;
    }
    
}
