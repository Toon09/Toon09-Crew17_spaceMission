package BackEnd.Models;

public interface Model3D {

    //setters
    public void setPos(int index, double[] pos);
    public void setVel(int index, double[] vel);
    public void setAcc(int index, double[] acc);

    //getters
    public double[] getPos(int index);
    public double[] getVel(int index);
    public double[] getAcc(int index);


    //calculates first derivative
    public void _1Deriv(int index);

    //calculates second deriv if there is
    public void _2Deriv(int index);
    
}
