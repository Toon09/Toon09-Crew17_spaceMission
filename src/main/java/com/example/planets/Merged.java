package com.example.planets;

import com.example.planets.BackEnd.CelestialBody;
import com.example.planets.BackEnd.Models.Gravity0;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

//ToDo
//update the camera movement (figure out how to rotate with something else than 0/0/0 in the middle) -Jakub
//set the planets sizes
//better scale
//make the sun shine
//some glow around other planets (tried for the sun but it doesnt work)
//make the button to stop the program

public class Merged extends Application {
    static Gravity0 model = new Gravity0(0,Math.PI/2, new double[]{11,11,0});
    private static int scale = 3000;
    private static int counter = 0;

    @Override
    public void start(Stage stage) throws Exception {
        //create a new group
        GUI gui = new GUI();
        Group world = gui.createEnvironment();
        Scene scene = new Scene(world, 1920, 1080, true);
        Group axis = buildAxes();
        world.getChildren().addAll(axis);
        //background
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        Camera camera = new PerspectiveCamera();
        camera.setFarClip(4000);
        camera.setNearClip(1);
        //initial camera setting
        scene.setCamera(camera);
        Rotate worldRotX = new Rotate(0, Rotate.X_AXIS);
        Rotate worldRotY = new Rotate(0, Rotate.Y_AXIS);
        Translate worldTransX = new Translate();
        world.getTransforms().addAll(worldRotY, worldRotX);
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

            }
        });
        world.setTranslateZ(world.getTranslateZ() + 100000);
        worldRotX.setAngle(worldRotX.getAngle());
        System.out.println(camera.getRotationAxis());
        world.setRotationAxis(new Point3D(model.getBody(3).getPos()[0],model.getBody(3).getPos()[1],model.getBody(3).getPos()[2]));

        stage.show();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.updatePos(0.1,0.1);
                for (int i=0; i<12; i++){
                    setPosition(world.getChildren().get(i),model.getBody(i));

                }
                //System.out.println("earth at: "+Arrays.toString(model.getBody(3).getPos()));
                //System.out.println("ship at: "+Arrays.toString(model.getBody(11).getPos()));
            }
        }, 0, 1);
    }

    public static void main(String... args) {
        launch(args);
    }

    public static void setPosition(Node sphere, CelestialBody body) {
        sphere.setTranslateX(body.getPos()[0] / scale);
        sphere.setTranslateY(body.getPos()[1] / scale);
        sphere.setTranslateZ(body.getPos()[2] / scale);
    }
    private Group buildAxes() {
        //green - y
        //blue -z
        //red - X

        Box xAxis = new Box(1200000, 100, 100);
        Box yAxis = new Box(100, 1200000, 100);
        Box zAxis = new Box(100, 100, 1200000);

        xAxis.setMaterial(new PhongMaterial(Color.RED));
        yAxis.setMaterial(new PhongMaterial(Color.GREEN));
        zAxis.setMaterial(new PhongMaterial(Color.BLUE));

        Group axisGroup = new Group();
        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        return axisGroup;
    }
}
