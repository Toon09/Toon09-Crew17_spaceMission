package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Gravity0;
import java.util.Arrays;

public class LandingTest {
    private static FeedBack feedBack;
    public static void main(String[] args) {
        //double[] scales = new double[] {1/100_000_000.0, 2/100_000.0, 1/1_000.0, 2/1_000.0, 1/100.0, 2/100.0, 1/10.0, 2/10.0, 1.0, 0.0, 10.0};

        double[] scales = new double[] {1/1000.0, 1/100.0, 2/100.0, 1/10.0, 2/10.0, 0.0, 1.0}; //


        System.out.println("max x error: " + 0.1/1000.0);
        System.out.println("max Vx error: " + 0.1/1000.0);
        System.out.println("max Vy error: " + 0.1/1000.0);
        System.out.println("max angle error: " + 0.02);
        System.out.println();

        System.out.println("wind scaling, x error, y error, Velocity x, Velocity y, angle");

        for(int i=0; i<scales.length; i++){
            experimentLoop(scales[i]);
        }

    }

    private static void experimentLoop(double windScale){
        feedBack = new FeedBack(new double[]{0,300},0);
        Gravity0 model = feedBack.getModel();

        feedBack.setWindScaling(windScale);
        feedBack.update(30);

        boolean stop = false;
        while (!stop){
            feedBack.update();

            stop = feedBack.isFinished();
        }
        print(feedBack);

    }


    private static void print(FeedBack feedback){

        System.out.print(feedback.getWindScaling() + ", " + feedBack.getLandingModule().getPos()[0]);
        System.out.print(", " + feedBack.getLandingModule().getPos()[1] + ", " + feedBack.getLandingModule().getVel()[0]);
        System.out.print(", " + feedBack.getLandingModule().getVel()[1]);
        System.out.print(", " + feedback.getRotation()); // the ship is set to have 0 rotation and roation speed in final stages

        System.out.println();
    }

}
