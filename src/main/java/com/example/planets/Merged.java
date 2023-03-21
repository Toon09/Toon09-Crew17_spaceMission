package com.example.planets;

import com.example.planets.BackEnd.CelestialBody;
import com.example.planets.BackEnd.Models.Gravity0;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;

//ToDo
//set the planets sizes
//better scale
//make the sun shine
//some glow around other planets (tried for the sun but it doesnt work)
//make the button to stop the program

public class Merged extends Application {
    static Gravity0 model = new Gravity0();
    private static int scale = 5000;
    private static int counter = 0;

    @Override
    public void start(Stage stage) throws Exception {
        //create a new group
        GUI gui = new GUI();
        Group world = gui.createEnvironment();
        Scene scene = new Scene(world, 1920, 1080, true);
        //background
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        Camera camera = new PerspectiveCamera();
        camera.setFarClip(2000);
        camera.setNearClip(1);
        //initial camera settings
        scene.setCamera(camera);
        Rotate worldRotX = new Rotate(0, Rotate.X_AXIS);
        Rotate worldRotY = new Rotate(0, Rotate.Y_AXIS);
        Translate worldTransX = new Translate();
        camera.getTransforms().addAll(worldRotY, worldRotX);
        //world.getTransforms().addAll(worldRotY, worldRotX);
        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case LEFT:
                    worldRotY.setAngle(worldRotY.getAngle() + 20);
                    break;
                case RIGHT:
                    worldRotY.setAngle(worldRotY.getAngle() - 20);
                    break;
                case UP:
                    worldRotX.setAngle(worldRotX.getAngle() + 20);
                    break;
                case DOWN:
                    worldRotX.setAngle(worldRotX.getAngle() - 20);
                    break;
                case X: //shift/Control is for z
                    world.setTranslateZ(world.getTranslateZ() + 3500);
                    break;
                case Z:
                    world.setTranslateZ(world.getTranslateZ() - 3500);
                    break;
                case A:// a/d is x axis
                    world.setTranslateX(world.getTranslateX() + 1350);
                    break;
                case D:
                    world.setTranslateX(world.getTranslateX() - 1350);
                    break;
                case W:// w/s is for y axis
                    world.setTranslateY(world.getTranslateY() + 1350);
                    break;
                case S:
                    world.setTranslateY(world.getTranslateY() - 1350);
                    break;
                case U:
                    camera.setTranslateX(camera.getTranslateX() + 1350);
                    break;
            }
        });
        world.setTranslateZ(world.getTranslateZ() + 100000);
        worldRotX.setAngle(worldRotX.getAngle());
        stage.show();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                model.updatePos(0.1,0.1);
                for (int i=0; i<world.getChildren().size(); i++){
                    setPosition(world.getChildren().get(i),model.getBody(i));
                }
            }
        }, 0, 1);


    }

    public static void main(String... args) {
        launch(args);
    }

    public Group createEnvironment() {
        Group group = new Group();
        Sphere sun = new Sphere();
        setPosition(sun, model.getBody(0));
        sun.setRadius(120);
        group.getChildren().add(sun);
        return group;
    }

    public static void setPosition(Node sphere, CelestialBody body) {
        sphere.setTranslateX(body.getPos()[0] / scale);
        sphere.setTranslateY(body.getPos()[1] / scale);
        sphere.setTranslateZ(body.getPos()[2] / scale);
    }
}
