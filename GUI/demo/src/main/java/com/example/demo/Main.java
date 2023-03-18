package com.example.demo;

import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.shape.Sphere;

public class Main extends Application {

    public static int SIZEFACTOR = 100;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group world = createEnvironment();

        Scene scene = new Scene(world);
        primaryStage.setScene(scene);
        primaryStage.setWidth(16 * SIZEFACTOR);
        primaryStage.setHeight(9 * SIZEFACTOR);

        Camera camera = new PerspectiveCamera();
        camera.setFarClip(2000);
        camera.setNearClip(1);

        scene.setCamera(camera);

        Rotate worldRotX = new Rotate(0, Rotate.X_AXIS);
        Rotate worldRotY = new Rotate(0, Rotate.Y_AXIS);

        Translate  worldTransX = new Translate();

        world.getTransforms().addAll(worldRotY, worldRotX);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch(event.getCode()){
                case LEFT:
                    worldRotY.setAngle(worldRotY.getAngle() + 10);
                    break;
                case RIGHT:
                    worldRotY.setAngle(worldRotY.getAngle() - 10);
                    break;
                case UP:
                    worldRotX.setAngle(worldRotX.getAngle() + 10);
                    break;
                case DOWN:
                    worldRotX.setAngle(worldRotX.getAngle() - 10);
                case SHIFT: //shift/Control is for z
                    world.setTranslateZ(world.getTranslateZ() + 10);
                    break;
                case CONTROL:
                    world.setTranslateZ(world.getTranslateZ() - 10);
                    break;
                case A:// a/d is x axis
                    world.setTranslateX(world.getTranslateX() + 10);
                    break;
                case D:
                    world.setTranslateX(world.getTranslateX() - 10);
                    break;
                case W:// w/s is for y axis
                    world.setTranslateY(world.getTranslateY() + 10);
                    break;
                case S:
                    world.setTranslateY(world.getTranslateY() - 10);
                    break;

            }
        });

        primaryStage.show();
    }

    private Group createEnvironment(){
        Group group = new Group();

        Sphere globe = new Sphere();
        globe.setRadius(100);


        globe.setTranslateX(-500);
        globe.setTranslateZ(-500);


        Sphere sun = new Sphere();
        sun.setTranslateY(0);
        sun.setTranslateX(0);


        group.getChildren().addAll(globe, sun);

        return group;
    }

    public static void main(String... args){
        launch(args);
    }
}