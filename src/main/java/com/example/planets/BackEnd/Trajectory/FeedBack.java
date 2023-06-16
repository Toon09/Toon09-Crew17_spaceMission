package com.example.planets.BackEnd.Trajectory;

import com.example.planets.BackEnd.CelestialEntities.Spaceship;
import com.example.planets.BackEnd.Models.Gravity0;

public class FeedBack implements IControler{
    private Spaceship ship;
    private LandingModule landingModule;
    private Gravity0 model;
    private double[] zeroPosition;
    public FeedBack(Spaceship ship, LandingModule landingModule, Gravity0 model) {
        this.ship = ship;
        this.landingModule = landingModule;
        this.model = model;
    }
    private double[] getZeroPoint(){
        double [] point = new double[2];
        point[0] =model.getBody(8).getPos()[0];
        point[1] =model.getBody(8).getPos()[1];
        return point;
    }

    private void correct(double[] error) {
        correctX();

    }

    private boolean isVertical() {
        return true; // for now the landing module is just vertical
    }

    private boolean isHorizontal() {
        return false;
    }

    private double[] getError() {
        double[] shipCoordinates = ship.getPos();
        double[] modelCoordinates = landingModule.getPos();
        return new double[]{shipCoordinates[0] - modelCoordinates[0],
                            shipCoordinates[1] - modelCoordinates[1],
                            shipCoordinates[2] - modelCoordinates[2]};
    }
    private void correctX(){
        double difference = Math.abs(landingModule.getPos()[0]-getZeroPoint()[0]);
        moveShipX(difference/4);
    }

    private void correctY(){
        double difference = Math.abs(landingModule.getPos()[1]-getZeroPoint()[1]);
        moveShipY(difference/4);
    }

    //method to check if the landing module is above the drop point
    private boolean isAboveDropPoint(){
        return false;
    }

    //created as for what I understand now just adding the acceleration wouldn't work, so we need to find out
    // if we should add acc or velocity or how should that be handled, for now empty
    private void moveShip(double x, double y){

    }
    private void moveShipX(double x){
        moveShip(x, 0);
    }

    private void moveShipY(double y){
        moveShip(0, y);
    }

}
