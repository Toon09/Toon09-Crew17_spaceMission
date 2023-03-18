package BackEnd.Models;

/**
 * Spaceship
 */
public class Spaceship {

    int id;
    String name;
    double mass;
    double[] pos = new double[3];
    double[] vel = new double[3];
    double[] acc = new double[3];

    

// make a constructor
public Spaceship(int id, String name, double mass, double[] pos, double[] vel, double[] acc) {
    this.id = id;
    this.name = name;
    this.mass = mass;
    this.pos = pos;
    this.vel = vel;
    this.acc = acc;
}
// make getters and setters
 public int getId() {
     return id;
    }
    public void setId(int id) {
        this.id = id;
    }   
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getMass() {
        return mass;
    }
    public void setMass(double mass) {
        this.mass = mass;
    }
    public double[] getPos() {
        return pos;
    }
    public void setPos(double[] pos) {
        this.pos = pos;
    }
    public double[] getVel() {
        return vel;
    }
    public void setVel(double[] vel) {
        this.vel = vel;
    }
    public double[] getAcc() {
        return acc;
    }
    public void setAcc(double[] acc) {
        this.acc = acc;
    }
    public Spaceship() {
        this.id = 1;
        this.name = "Crew17_spaceMissionTrip_1";
        this.mass = 5000;
        this.pos = new double[3];
        this.vel = new double[3];
        this.acc = new double[3];

    }
}