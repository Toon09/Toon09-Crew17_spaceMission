package com.example.planets.BackEnd.Models;

//this model is for the eq: y' = 2 + y for demonstration

//y(x) = c_1 e^x - 2

public class simple1 implements Model1D {

    private double x;
    private double y;

    public simple1(double xInnit, double yInnit){
        x = xInnit;
        y = yInnit;
    }

    //setters
    @Override
    public void setX(double x){ this.x = x; }
    @Override
    public void setY(double y){ this.y = y; }
    @Override
    public void setYD(double yd) {}

    //getters
    @Override
    public double getX(){ return x; }
    @Override
    public double getY(){ return y; }

    
    //calculate derivatives
    @Override
    public double _1Deriv() {
        return 6 + y;
    }

    @Override
    public double _2Deriv() {
        return _1Deriv(); //if want actual value its the same as first deriv, but not necesary
    }

    
}
