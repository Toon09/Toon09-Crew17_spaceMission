package BackEnd;

//to create model of solar system
public class CelestialBody {

    private String name = "";
    private double mass;

    private double[] pos;
    private double[] vel;
    private double[] acc;



    CelestialBody(double mass, double[] innitPos, double[] innitVel, double[] innitAcc){
        setMass(mass);
        setPos(innitPos);
        setVel(innitVel);
        setAcc(innitAcc);

    }

    CelestialBody(String name, double mass, double[] innitPos, double[] innitVel, double[] innitAcc){
        this.name = name;
        setMass(mass);
        setPos(innitPos);
        setVel(innitVel);
        setAcc(innitAcc);

    }


    //getters
    public double[] getPos(){ return pos; }
    public double[] getVel(){ return vel; }
    public double[] getAcc(){ return acc; }
    public double getMass(){ return mass; }
    public String getName(){ return name; }

    //setters
    public void setMass(double mass){ this.mass = mass; }
    public void setName(String name){ this.name = name; }
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

    @Override
    public boolean equals(Object obj) {
        if( !(obj instanceof CelestialBody) )
            return false;

        CelestialBody temp = (CelestialBody)obj;

        for(int i=0; i<3; i++){
            if( pos[i] != temp.getPos()[i] )
                return false;
        }

        for(int i=0; i<3; i++){
            if( vel[i] != temp.getVel()[i] )
                return false;
        }

        for(int i=0; i<3; i++){
            if( acc[i] != temp.getAcc()[i] )
                return false;
        }

        return true;
    }

}
