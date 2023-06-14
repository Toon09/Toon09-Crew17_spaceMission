package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.Trajectory.LandingModule;
import com.example.planets.BackEnd.CelestialEntities.Planning;
import com.example.planets.BackEnd.Models.Model3D;
 // this is what you need


public interface IControler {
    Model3D model = null;
    Planning plan = null;
    LandingModule landingModule = null;
    double maxX = 0.0001; //(0.1m or 10^-4km)
    double maxXVelocity = 0.0001; //(0.1m or 10^-4km)
    double maxYVelocity = 0.0001; //(0.1m or 10^-4km)

    public default Planning calculateTrajectory(double initialX, double initialY, double initialTheta,
                                                double acceleration, double torque, double gravity) {
        Planning plan = null;

        return plan;
    }



    public default Planning claclateThrust(double initialX, double initialY, double initialTheta,
                                           double acceleration, double torque, double gravity) {
    Planning plan= null;

    return plan;
    }
    public default  Planning creatPlan(){

        Planning plan=null ;
        return plan ;

    }
}
