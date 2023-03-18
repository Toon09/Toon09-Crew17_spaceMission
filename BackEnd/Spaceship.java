package BackEnd;

public class Spaceship extends CelestialBody {

    public void addAcc(double[] acc){
        this.acc[0] += acc[0];
        this.acc[1] += acc[1];
        this.acc[2] += acc[2];
    }

}