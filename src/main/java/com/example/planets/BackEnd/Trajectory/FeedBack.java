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
    private StochasticWind wind;
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
    }

    /**
     * @param initialPosition an array specifying the landing module position where index 0 is X position, index 1 is Y position
     * @param initialVelocity one value of Y axis initial velocity of the module
     */
    public FeedBack(double[] initialPosition, double initialVelocity) {
        titan = new CelestialBody(1.35e23, new double[]{0, -2574, 0}, new double[]{0, 0, 0});
        landingModule = new LandingModel(4200, new double[]{initialPosition[0], initialPosition[1], 0}, new double[]{initialVelocity, 0, 0});
        model = new Gravity0(titan, landingModule);
        lastPhase = false;
        finished = false;
        wind = new StochasticWind();
    }

    /**
     * @param time how much time should pass
     *             function to monitor Y velocity of the module that tries to achieve:
     *             1km/s at 200km
     *             0.5km/s at 100km
     *             0.1km/s at 20km
     *             0.01km/s at 10km
     *             0.001km/s at 5km
     *             0.0001km/s at 0.5km ( and no rotation anymore )
     */
    public void update(double time) {
        model.updatePos(time, 0.1, false);
        if (landingModule.getPos()[1] <= 0.00001) {
            finished = true;
            System.out.println("WE LANDED AT: ");
            System.out.println(Arrays.toString(landingModule.getPos()));
            System.out.println("WITH VELOCITY OF:");
            System.out.println(Arrays.toString(landingModule.getVel()));
            System.out.println("AFTER " + model.getTime() + " SECONDS");
        } else if (landingModule.getPos()[1] > 200) {
            if(Math.abs(landingModule.getPos()[0]) > 0.01){
                correctX();
            }else {
                correctY(1, false);
            }
        } else if (landingModule.getPos()[1] > 100) {
            correctY(0.5, false);
        } else if (landingModule.getPos()[1] > 20) {
            correctY(0.1, false);
        } else if (landingModule.getPos()[1] > 10) {
            correctY(0.01, false);
        } else if (landingModule.getPos()[1] > 5) {
            correctY(0.001, false);
        } else if (landingModule.getPos()[1] > 0.05) {
            correctY(0.0001, false);
        } else {
            System.out.println("else");
            lastPhase = true;
            correctY(0.000001, true);
        }
    }

    private void correctY(double target, boolean boost) {
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

    private void calculateVelocityY(double target) {
        calculateVelocity(target, 1);
    }

    private void calculateVelocity(double target, int axis) {
        if (landingModule.getVel()[axis] == target) {
            return;
        }
        double difference = target - landingModule.getVel()[axis];
    }

    private void correctX() {
        if (landingModule.getPos()[0] == 0) {
            //if it's above the point and has no velocity, leave it be, else fix the velocity by rotating and applying its opposite
            if (landingModule.getVel()[0] == 0) {
                return;
            } else if (landingModule.getVel()[0] > 0) {
                if (landingModule.getRotation() != 270) {
                    double rotation = landingModule.getRotation();
                    //rotate to -90 degrees ( so 270 degrees )
                    landingModule.rotate(-(rotation + 90));
                } else {
                    activateEngine(landingModule.getVel()[0] / 2);
                }
                //if its going towards negative x rotate it or make it go towards positive x
            } else if (landingModule.getVel()[0] < 0) {
                if (landingModule.getRotation() != 90) {
                    double rotation = landingModule.getRotation();
                    //rotate to 90 degrees
                    landingModule.rotate(-rotation + 90);
                } else {
                    activateEngine(landingModule.getVel()[0] / 2);
                }
            }
        } else {
            if (landingModule.getPos()[0] > 0) {
                if (landingModule.getRotation() != 270) {
                    double rotation = landingModule.getRotation();
                    //rotate to -90 degrees ( so 270 degrees )
                    landingModule.rotate(-(rotation + 90));
                } else {
                    activateEngine(landingModule.getPos()[0] / 5);
                }
            } else if (landingModule.getPos()[0] < 0) {
                if (landingModule.getRotation() != 90) {
                    double rotation = landingModule.getRotation();
                    //rotate to 90 degrees
                    landingModule.rotate(-rotation + 90);
                } else {
                    activateEngine(landingModule.getPos()[0] / 5);
                }
            }
        }

    }

    public void activateEngine(double velocity) {
        double[] velocityInDirection = calculateVelocityInDirection(velocity);
        wind.stochasticWind(landingModule,titan,1);
        landingModule.addVel2D(velocityInDirection);
    }

    public double[] calculateVelocityInDirection(double velocity) {
        // Convert rotation to radians
        double rotationInRadians = Math.toRadians(landingModule.getRotation());
        // Calculate the velocity components in the x and y directions
        double velocityX = velocity * Math.sin(rotationInRadians);
        double velocityY = velocity * Math.cos(rotationInRadians);

        return new double[]{velocityX, velocityY};
    }

    public LandingModel getLandingModule() {
        return landingModule;
    }

    public Gravity0 getModel() {
        return model;
    }

    //functions that are useful if we are working on the same system as before, not a separate one
    private double[] getPoint(double[] A, double[] B, double distance) {
        double X = B[0] - A[0];
        double Y = B[1] - A[1];
        double length = Math.sqrt(X * X + Y * Y);
        double normX = X / length;
        double normY = Y / length;
        return new double[]{A[0] + normX * distance, A[1] + normY * distance};
    }

    public double getDistance2D(double[] A, double[] B) {
        double X = A[0] - B[0];
        double Y = A[1] - B[1];
        return Math.sqrt(X * X + Y * Y);
    }

    private double[] getDirection(double[] start, double[] end, double velocity) {
        double deltaX = end[0] - start[0];
        double deltaY = end[1] - start[1];
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double directionX = velocity * (deltaX / distance);
        double directionY = velocity * (deltaY / distance);

        return new double[]{directionX, directionY};
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isLastPhase() {
        return lastPhase;
    }
}