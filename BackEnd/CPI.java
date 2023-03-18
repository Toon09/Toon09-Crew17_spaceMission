package BackEnd;

public interface CPI {
    // interface for CelestialBody and Model1D



        
    


        

    

 

  

    //getters
    public double[] getPos();
    public double[] getVel();
    public double[] getAcc();
    public static double getTime();
    public double getMass();
    public double getradius();
    public String getName();
    public void addDt(double dt);
    //setters
    public void setMass(double mass)
    public void setradius(double radius);
    public void setName(String name);
    public void setPos(double[] pos);
       
    
    public void setVel(double[] vel);
      
    
    public void setAcc(double[] acc);
        
    


    @Override
    public boolean equals(Object obj);

}

}
