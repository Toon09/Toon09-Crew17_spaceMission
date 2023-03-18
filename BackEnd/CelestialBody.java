package BackEnd;

//to create model of solar system
public class CelestialBody {

    private double mass;

    private double[] pos;
    private double[] vel;
    private double[] acc;

    //getters
    public double[] getPos(){ return pos; }
    public double[] getVel(){ return vel; }
    public double[] getAcc(){ return acc; }
    public double getMass(){ return mass; }

    //setters
    public void setMass(double mass){ this.mass = mass; }
    public void setPos(double[] pos){
        this.pos[0] = pos[0];
        this.pos[1] = pos[1];
        this.pos[2] = pos[2];
    }
    public void setVel(double[] vel){
        this.vel[0] = vel[0];
        this.vel[1] = vel[1];
        this.vel[2] = vel[2];
    }
    public void setAcc(double[] acc){
        this.acc[0] = acc[0];
        this.acc[1] = acc[1];
        this.acc[2] = acc[2];
    }

}
