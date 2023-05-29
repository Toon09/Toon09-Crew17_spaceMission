package com.example.planets.JUnitTesting;

import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.Models.Model3D;
import com.example.planets.BackEnd.Models.TestModel1;
import com.example.planets.BackEnd.NumericalMethods.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class numSolversTesting {

    @Test
    public void testRK2() {
        double[] expectedValues = {0.0000, 0.4277, 0.6803, 0.7066, 0.5093};
        TestModel1 model = new TestModel1(new RK2());
        int j = 0;

        for (double i = 0; i < 2.5; i+=0.5) {
            double result  = model.getBody(0).getPos()[0];
            Assert.assertEquals(expectedValues[j], result, 0.1);
            j += 1;
            model.updatePos(0.5, 0.5, false);
        }
    }

    @Test
    public void testHeunsRK3() {
        double[] expectedValues = {0.0000, 0.4409, 0.7048, 0.7376, 0.5403};
        TestModel1 model = new TestModel1(new HeunsRK3());
        int j = 0;

        for (double i = 0; i < 2.5; i+=0.5) {
            double result  = model.getBody(0).getPos()[0];
            Assert.assertEquals(expectedValues[j], result, 0.01);
            j += 1;
            model.updatePos(0.5, 0.5, false);
        }
    }

    @Test
    public void testRK4() {

        double[] expectedValues = {0.000000, 0.440815, 0.704461, 0.737016};
        TestModel1 model = new TestModel1(new RK4());
        int j = 0;

        for (double i = 0; i < 2; i+=0.5) {
            double result  = model.getBody(0).getPos()[0];
            Assert.assertEquals(expectedValues[j], result, 0.0001);
            j += 1;
            model.updatePos(0.5, 0.5, false);
        }
    }

    @Test
    public void testRalstonsRK4() {

        double[] expectedValues = {0.000000, 0.440815, 0.704461, 0.737016};
        TestModel1 model = new TestModel1(new RalstonsRK4());
        int j = 0;

        for (double i = 0; i < 2; i+=0.5) {
            double result  = model.getBody(0).getPos()[0];
            Assert.assertEquals(expectedValues[j], result, 0.0001);
            j += 1;
            model.updatePos(0.5, 0.5, false);
        }
    }

    @Test
    public void testLeapFrog() {

        double[] expectedValues = {0.000000, 0.440815, 0.704461, 0.737016};
        TestModel1 model = new TestModel1(new LeapFrog());
        int j = 0;

        for (double i = 0; i < 2; i+=0.5) {
            double result  = model.getBody(0).getPos()[0];
            Assert.assertEquals(expectedValues[j], result, 0.01);
            j += 1;
            model.updatePos(0.5, 0.5, false);
        }
    }

    @Test
    public void testAdamBashForth2() {

        double[] expectedValues = {0.000000, 0.440815, 0.704461, 0.737016};
        TestModel1 model = new TestModel1(new AB2());
        int j = 0;

        for (double i = 0; i < 2; i+=0.5) {
            double result  = model.getBody(0).getPos()[0];
            Assert.assertEquals(expectedValues[j], result, 0.1);
            j += 1;
            model.updatePos(0.5, 0.5, false);
        }
    }

    @Test
    public void testEuler() {

        double[] expectedValues = {0.000000, 0.440815, 0.704461, 0.737016};
        TestModel1 model = new TestModel1(new Euler());
        int j = 0;

        for (double i = 0; i < 2; i+=0.5) {
            double result  = model.getBody(0).getPos()[0];
            Assert.assertEquals(expectedValues[j], result, 0.3);
            j += 1;
            model.updatePos(0.5, 0.5, false);
        }
    }

    @Test
    public void testPlanNoPlanShips() {
        try{
            Model3D model = new Gravity0(0, 0, new RK4());
            model.addShips(3);

            double[][] plan = new double[][] {{0,10*60,
                    11,0,0},
                    {3*24*60*60, 3*24*60*60 + 30*60,
                            2,2,2}};

            plan[0][2] = 35.0;
            model.getShip().setPlan( plan );

            model.updatePos(1.0, 100.0, true );

        }catch(Exception e){
            assertTrue(false);
        }
        assertTrue(true);
    }







}
