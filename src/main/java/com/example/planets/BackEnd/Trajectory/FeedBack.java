package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.StochasticWind;
import com.example.planets.BackEnd.Models.Gravity0;

import java.util.Arrays;

public class FeedBack implements IControler {
    private final LandingModel landingModule;
    private final CelestialBody titan;
    private final Gravity0 model;
    private boolean lastPhase;
    private boolean finished;
    private final StochasticWind wind;
    private int countX;

    public double getWindScaling(){ return wind.getMagScaling(); }
    public void setWindScaling(double scale){ wind.setwindScalling(scale); }
    public double getRotation(){ return landingModule.getRotation(); }

    /**
     * default constructor that places the landing module at X = 0, Y = 300km and Y = 0 is the surface of titan
     */
    public FeedBack() {
        titan = new CelestialBody(1.35e23, new double[]{0, -2574, 0}, new double[]{0, 0, 0});
        landingModule = new LandingModel(4200, new double[]{0, 300, 0}, new double[]{0, 0, 0});
        model = new Gravity0(titan, landingModule);
        lastPhase = false;
        finished = false;
        wind = new StochasticWind();
        countX = 0;
    }

    /**
     * @param initialPosition an array specifying the landing module position where index 0 is X position, index 1 is Y position
     * @param initialVelocity one value of Y axis initial velocity of the module
     */
    @SuppressWarnings("unused")
    public FeedBack(double[] initialPosition, double initialVelocity) {
        titan = new CelestialBody(1.35e23, new double[]{0, -2574, 0}, new double[]{0, 0, 0});
        landingModule = new LandingModel(4200, new double[]{initialPosition[0], initialPosition[1], 0}, new double[]{initialVelocity, 0, 0});
        model = new Gravity0(titan, landingModule);
        lastPhase = false;
        finished = false;
        wind = new StochasticWind();
        countX = 0;
    }

    /**
     * function to monitor Y velocity of the module that tries to achieve:
     * 1km/s at 200km
     * 0.5km/s at 100km
     * 0.1km/s at 20km
     * 0.01km/s at 10km
     * 0.001km/s at 5km
     * 0.0001km/s at 0.5km ( and no rotation anymore )
     *
     * @param time how much time should pass ( in seconds )
     */
    public void update(double time) {
        model.updatePos(time, 0.1, false);
        boolean isThereWind = true;
        //noinspection ConstantValue
        if (isThereWind) {
            wind.stochasticWind(landingModule, titan, 0.1);
        }
        double XTolerance = 0.01;
        if (landingModule.getPos()[1] <= 0.00001) {
            finished = true;
            //printLanding();
        } else if (landingModule.getPos()[1] > 200) {
            if (Math.abs(landingModule.getPos()[0]) > XTolerance && countX <= 0) {
                correctX();
            } else {
                correctY(0.75, false);
            }
        } else if (landingModule.getPos()[1] > 100) {
            if (Math.abs(landingModule.getPos()[0]) > XTolerance && countX <= 0) {
                correctX();
            } else {
                correctY(0.5, false);
            }
        } else if (landingModule.getPos()[1] > 20) {
            if (Math.abs(landingModule.getPos()[0]) > XTolerance && countX <= 0) {
                correctX();
            } else {
                correctY(0.1, false);
            }
        } else if (landingModule.getPos()[1] > 10) {
            if (Math.abs(landingModule.getPos()[0]) > XTolerance && countX <= 0) {
                correctX();
            } else {
                correctY(0.01, false);
            }
        } else if (landingModule.getPos()[1] > 5) {
            correctY(0.001, false);
        } else if (landingModule.getPos()[1] > 0.01) {
            correctY(0.0001, false);
            lastPhase = true;
        } else {
            lastPhase = true;
            correctY(0.00001, false);
        }
    }

    public void update(){
        if (lastPhase){
            update(1);
        }else{
            update(1);
        }
    }

    private void printLanding(){
        System.out.println("WE LANDED AT: ");
        System.out.println(Arrays.toString(landingModule.getPos()));
        System.out.println("WITH VELOCITY OF:");
        System.out.println(Arrays.toString(landingModule.getVel()));
        System.out.println("AFTER " + model.getTime() + " SECONDS");
    }

    /**
     * a function that corrects the Y position of the ship, it either rotates the ship to face
     * the correct direction
     *
     * @param target a target velocity of a landing module
     * @param boost  should the velocity be positive ( normally the target velocity of 0.5
     *               would be -0.5 as it's going downwards )
     */
    private void correctY(double target, @SuppressWarnings("SameParameterValue") boolean boost) {
        countX -= 1;
        //System.out.println("Y");
        if (landingModule.getRotation() != 0) {
            landingModule.rotate(landingModule.getRotation() * -1);
            return;
        }
        if (boost) {
            double difference = target - landingModule.getVel()[1];
            activateEngine(difference * 1.7);
        } else {
            target = target * -1; // that's because we are going down, so we want negative velocity
            double difference = target - landingModule.getVel()[1];
            activateEngine(difference);
        }
    }

    /**
     * A function to correct the X position of the ship, it either rotates the ship
     * to face the correct direction or accelerates
     */
    private void correctX() {
        //System.out.println("X");
        if (landingModule.getPos()[0] > 0) {
            if (landingModule.getRotation() != 270.0) {
                //System.out.println("rotation is not 270.0, its " + landingModule.getRotation());
                double rotation = landingModule.getRotation();
                //rotate to -90 degrees ( so 270 degrees )
                landingModule.rotate(-(rotation + 90));
                //System.out.println("Rotation is: " + landingModule.getRotation());
            } else {
                countX = 10;
                //System.out.println("Before, vel: " + Arrays.toString(landingModule.getVel()) +", pos: " + Arrays.toString(landingModule.getPos()));
                //System.out.println("Rotation is: " + landingModule.getRotation());
                //System.out.println("added " + -1 * (landingModule.getVel()[0]) + " velocity");
                //System.out.println("added " + -0.01 + " velocity");
                activateEngine( (landingModule.getVel()[0]));
                activateEngine(0.00001);
                //System.out.println("After, vel: " + Arrays.toString(landingModule.getVel()) +", pos: " + Arrays.toString(landingModule.getPos()));

            }
        } else if (landingModule.getPos()[0] < 0) {
            if (landingModule.getRotation() != 90.0) {
                //System.out.println("rotation is not 90.0, its " + landingModule.getRotation());
                double rotation = landingModule.getRotation();
                //rotate to 90 degrees
                landingModule.rotate(90 - rotation);
                //System.out.println("Rotation is: " + landingModule.getRotation());
            } else {
                //System.out.println("Before, vel: " + Arrays.toString(landingModule.getVel()) +", pos: " + Arrays.toString(landingModule.getPos()));
                //System.out.println("Rotation is: " + landingModule.getRotation());
                //System.out.println("added " + -1 * (landingModule.getVel()[0]) + " velocity");
                //System.out.println("added " + 0.01 + " velocity");

                activateEngine((landingModule.getVel()[0]));
                activateEngine(0.00001);
                //System.out.println("After, vel: " + Arrays.toString(landingModule.getVel()) +", pos: " + Arrays.toString(landingModule.getPos()));


            }
        }
    }

    /**
     * Function to add @velocity in the direction that the spaceship is currently facing, so it only goes forward
     *
     * @param velocity velocity to add to the current velocity of the landing module
     */
    public void activateEngine(double velocity) {
        double[] velocityInDirection = calculateVelocityInDirection(velocity);
        landingModule.addVel(velocityInDirection);
    }

    /**
     * A function to calculate a double velocity into an array representing
     * the same velocity but in the direction its facing
     *
     * @param velocity a target velocity in the direction the ship is facing
     * @return a double array where index 0 is the velocity on X axis and index 1 is velocity on Y axis
     */
    public double[] calculateVelocityInDirection(double velocity) {
        // Convert rotation to radians
        double rotationInRadians = Math.toRadians(landingModule.getRotation());
        // Calculate the velocity components in the x and y directions
        double velocityX = velocity * Math.sin(rotationInRadians);
        double velocityY = velocity * Math.cos(rotationInRadians);

        return new double[]{velocityX, velocityY};
    }

    //getters for the model, landing module and isLastPhase and finished boolean
    public LandingModel getLandingModule() {
        return landingModule;
    }

    public Gravity0 getModel() {
        return model;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isLastPhase() {
        return lastPhase;
    }
}