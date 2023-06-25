package com.example.planets.JUnitTesting;

import com.example.planets.BackEnd.Trajectory.FeedBack;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class FeedbackControllerTest {
    private FeedBack feedBack;

    @Before
    public void setUp(){
        feedBack = new FeedBack();
    }

    @Test
    public void testUpdate_LandingAtSurface() {
        feedBack.update(728.55918);
        Assert.assertTrue(feedBack.isFinished());
        Assert.assertTrue(0.1 > Math.abs(feedBack.getLandingModule().getPos()[1]));
    }
    @Test
    public void testUpdate_CorrectY() {
        double initialY = feedBack.getLandingModule().getPos()[1];
        feedBack.update(4.0);
        double newY = feedBack.getLandingModule().getPos()[1];
        Assert.assertTrue(newY < initialY); // Y position should decrease
    }
    @Test
    public void testActivateEngine() {
        double initialVelocity = feedBack.getLandingModule().getVel()[1];
        feedBack.activateEngine(10.0);
        double newVelocity = feedBack.getLandingModule().getVel()[1];
        Assert.assertTrue(newVelocity > initialVelocity); // Y velocity should increase
    }
    @Test
    public void testCalculateVelocityInDirection() {
        double velocity = 10;
        double[] velocityInDirection = feedBack.calculateVelocityInDirection(velocity);
        double rotationInRadians = Math.toRadians(feedBack.getLandingModule().getRotation());
        double expectedVelocityX = velocity * Math.sin(rotationInRadians);
        double expectedVelocityY = velocity * Math.cos(rotationInRadians);
        Assert.assertTrue(expectedVelocityX == velocityInDirection[0]);
        Assert.assertTrue(expectedVelocityY == velocityInDirection[1]);
    }

}
