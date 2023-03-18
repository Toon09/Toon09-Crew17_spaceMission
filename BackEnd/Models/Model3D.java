package BackEnd.Models;
import BackEnd.CelestialBody;

public interface Model3D {

    //setters
    public void setPos(int index, double[] pos);
    public void setVel(int index, double[] vel);
    public void setAcc(int index, double[] acc);
    public void addDt(double dt);

    //getters
    public double[] getPos(int index);
    public double[] getVel(int index);
    public double[] getAcc(int index);
    public double getTime();

    //elements
    public CelestialBody getBody(int index);
    public int size();

    //derivs
    public void _1Deriv();
    public void _2Deriv();

    
}
