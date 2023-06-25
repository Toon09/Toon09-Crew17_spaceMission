package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Gravity0;
import java.util.Arrays;

public class LandingTest {
    private static Gravity0 model;
    private static FeedBack feedBack;
    public static void main(String[] args) {
        //feedBack = new FeedBack();
       feedBack = new FeedBack(new double[]{0,300},0) ;
        model = feedBack.getModel();
        System.out.println("start");
        print();
        System.out.println("let 30 seconds pass");
        //feedBack.update(30);
        print();
        boolean stop = false;
        while (!stop){
            if (feedBack.isLastPhase()){
                //feedBack.update(0.1);
            }else {
                //feedBack.update(5);
            }
            stop = feedBack.isFinished();
        }
    }
    private static void print(){
        System.out.println("Module at: "+ Arrays.toString(feedBack.getLandingModule().getPos()));
        System.out.println("Module velocity is: "+ Arrays.toString(feedBack.getLandingModule().getVel()));
    }

}
