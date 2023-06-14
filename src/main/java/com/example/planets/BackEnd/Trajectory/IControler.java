package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.Planning;
import com.example.planets.BackEnd.Models.Model3D;

public interface IControler {
    Model3D model = null;
    landingModel moter = null;
    Planning plan= null;

    public default Planning claclateThrust(double initialX, double initialY, double initialTheta,
                                           double acceleration, double torque, double gravity) {
    Planning plan= null;

    return plan;
    }
    public default <moter, model> Planning creatPlan(model, moter){

        Planning plan=null ;
        return plan ;

    }
}
