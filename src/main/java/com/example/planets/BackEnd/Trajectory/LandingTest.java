package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Gravity0;
import java.util.Arrays;

public class LandingTest {
    public static void main(String[] args) {
        FeedBack feedBack = new FeedBack();
        Gravity0 model = feedBack.getModel();
        System.out.println("Module at: "+ Arrays.toString(model.getBodies()[1].getPos()));
        System.out.println("Module velocity is: "+ Arrays.toString(model.getBody(1).getVel()));
        System.out.println("let some time pass");
        model.updatePos(30,2,false);
        System.out.println("Module at: "+ Arrays.toString(model.getBodies()[1].getPos()));
        System.out.println("Module velocity is: "+ Arrays.toString(model.getBody(1).getVel()));
        System.out.println("Add velocity ( hopefully in the right direction so up ) of 1 km/s");
        feedBack.activateEngine(1);
        System.out.println("Module velocity is: "+ Arrays.toString(model.getBody(1).getVel()));
        System.out.println("Rotate that bad boy 90 degrees");
        feedBack.getLandingModule().rotate(90);
        System.out.println("Add velocity ( hopefully in the right direction so right ) of 1 km/s");
        feedBack.activateEngine(1);
        System.out.println("Module velocity is: "+ Arrays.toString(model.getBody(1).getVel()));

    }

}
