package com.example.planets;

import com.example.planets.BackEnd.CelestialBody;
import com.example.planets.BackEnd.Models.Gravity0;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

//ToDo
//add orbits
//update the camera movement (figure out how to rotate with something else than 0/0/0 in the middle) -Jakub
//set the planets sizes
//better scale

// extra stuff if we have time
//add button for axis
//make the sun shine
//some glow around other planets (tried for the sun but it doesnt work)

public class Merged extends Application {
    static Gravity0 model = new Gravity0(0, Math.PI / 2, new double[]{11, 11, 0});
    private static int scale = 50;
    private static int counter = 0;
    private int planetSize = 6371 / 2;
    private static boolean lookAtEarth = false;
    private static boolean lookAtTitan = false;
    private static boolean lookAtSun = false;
    private static boolean lookatEverything = false;

    @Override
    public void start(Stage stage) throws Exception {
        //create a new group
        Group world = createEnvironment();
        Scene scene = new Scene(world, 1920, 1080, true);
        //Group axis = buildAxes();
        //world.getChildren().addAll(axis);
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
        camera.getTransforms().addAll(worldRotY, worldRotX);
        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case LEFT:
                    worldRotY.setAngle(worldRotY.getAngle() - 20);
                    break;
                case RIGHT:
                    worldRotY.setAngle(worldRotY.getAngle() + 20);
                    break;
                case UP:
                    worldRotX.setAngle(worldRotX.getAngle() - 20);
                    break;
                case DOWN:
                    worldRotX.setAngle(worldRotX.getAngle() + 20);
                    break;
                case X: //shift/Control is for z
                    camera.setTranslateZ(camera.getTranslateZ() - 3500);
                    break;
                case Z:
                    camera.setTranslateZ(camera.getTranslateZ() + 3500);
                    break;
                case A:// a/d is x axis
                    camera.setTranslateX(camera.getTranslateX() - 1350);
                    break;
                case D:
                    camera.setTranslateX(camera.getTranslateX() + 1350);
                    break;
                case W:// w/s is for y axis
                    camera.setTranslateY(camera.getTranslateY() - 1350);
                    break;
                case S:
                    camera.setTranslateY(camera.getTranslateY() + 1350);
                    break;
                case O:
                    System.out.println("camera at:");
                    System.out.println("X: " + camera.getTranslateX());
                    System.out.println("Y: " + camera.getTranslateY());
                    System.out.println("Z: " + camera.getTranslateZ());
                    System.out.println("titan at: ");
                    for (int i = 0; i < 3; i++) {
                        System.out.println(model.getBody(8).getPos()[i] / scale);
                    }
                    System.out.println("rotation is");
                    System.out.println("Y: " + worldRotY.getAngle());
                    System.out.println("X: " + worldRotX.getAngle());

                    break;
                case P:
                    System.exit(0);
                    break;
                case DIGIT1:
                    if (lookAtEarth) {
                        lookAtEarth = false;
                    } else {
                        lookAtEarth = true;
                        lookAtTitan = false;
                        lookAtSun = false;
                    }
                    break;
                case DIGIT2:
                    if (lookAtSun) {
                        lookAtSun = false;
                    } else {
                        lookAtSun = true;
                        lookAtTitan = false;
                        lookAtEarth = false;
                    }
                    break;
                case DIGIT3:
                    if (lookAtTitan) {
                        lookAtTitan = false;
                    } else {
                        lookAtTitan = true;
                        lookAtSun = false;
                        lookAtEarth = false;
                    }
                    break;
                case DIGIT4:
                    if (lookatEverything) {
                        lookatEverything = false;
                    } else {
                        lookatEverything = true;
                        lookAtSun = false;
                        lookAtEarth = false;
                        lookAtTitan = false;
                    }


            }
        });

        world.setTranslateZ(world.getTranslateZ() + 100000);
        worldRotX.setAngle(worldRotX.getAngle());
        System.out.println(camera.getRotationAxis());
        world.setRotationAxis(new Point3D(model.getBody(3).getPos()[0], model.getBody(3).getPos()[1], model.getBody(3).getPos()[2]));

        stage.show();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.updatePos(0.1, 0.1);
                for (int i = 0; i < 12; i++) {
                    setPosition(world.getChildren().get(i), model.getBody(i));
                }
                if (lookAtEarth) {
                    camera.setTranslateX(model.getBody(3).getPos()[0] / scale + 1000);
                    camera.setTranslateY(model.getBody(3).getPos()[1] / scale + 2000);
                    camera.setTranslateZ(model.getBody(3).getPos()[2] / scale - 24000);
                }
                if (lookAtSun) {
                    camera.setTranslateX(-model.getBody(0).getPos()[0] / scale);
                    camera.setTranslateY(-model.getBody(0).getPos()[1] / scale);
                    camera.setTranslateZ(model.getBody(0).getPos()[2] / scale - 360500);
                }
                if (lookAtTitan) {
                    camera.setTranslateX(model.getBody(8).getPos()[0] / scale + 1000);
                    camera.setTranslateY(model.getBody(8).getPos()[1] / scale + 2000);
                    camera.setTranslateZ(model.getBody(8).getPos()[2] / scale - 24000);
                }
                if (lookatEverything){
                    camera.setTranslateX(model.getBody(8).getPos()[0] / scale - 237755);
                    camera.setTranslateY(model.getBody(8).getPos()[1] / scale + 115144);
                    camera.setTranslateZ(model.getBody(8).getPos()[2] / scale -471948);
                }

            }
        }, 1, 1);
    }

    public static void main(String... args) {
        launch(args);
    }

    public static void setPosition(Node sphere, CelestialBody body) {
        sphere.setTranslateX(body.getPos()[0] / scale);
        sphere.setTranslateY(body.getPos()[1] / scale);
        sphere.setTranslateZ(body.getPos()[2] / scale);
    }

    public static void setPosition(Box box, CelestialBody body) {
        box.setTranslateX(body.getPos()[0] / scale);
        box.setTranslateY(body.getPos()[1] / scale);
        box.setTranslateZ(body.getPos()[2] / scale);
    }

    private void setPosition(Sphere sphere, int index) {
        sphere.setTranslateX(positions[index][0] / scale);
        sphere.setTranslateY(positions[index][1] / scale);
        sphere.setTranslateZ(positions[index][2] / scale);
    }

    private void setPosition(Box box, int index) {
        box.setTranslateX(positions[index][0] / scale);
        box.setTranslateY(positions[index][1] / scale);
        box.setTranslateZ(positions[index][2] / scale);
    }

    private void setPosition(Cylinder cylinder, int index) {
        cylinder.setTranslateX(positions[index][0] / scale);
        cylinder.setTranslateY(positions[index][1] / scale);
        cylinder.setTranslateZ(positions[index][2] / scale);
    }

    private static double[][] positions = {{0, 0, 0}, {7.83e6, 4.49e7, 2.87e6}, {-2.82e7, 1.04e8, 3.01e6},
            {-1.48e8, -2.78e7, 3.37e4}, {-1.48e8, -2.75e7, 7.02e4}, {-1.59e8, 1.89e8, 7.87e6},
            {6.93e8, 2.59e8, -1.66e7}, {1.25e9, -7.60e8, -3.67e7}, {1.25e9, 7.61e8, -3.63e7},
            {4.45e9, -3.98e8, -9.45e7}, {1.96e9, 2.19e9, -1.72e7}};

    public Group createEnvironment() {
        Group group = new Group();

        //create the sun
        Sphere sun = new Sphere();
        setPosition(sun, 0);
        sun.setRadius(planetSize);
        //glow
        Glow sunGlow = new Glow();
        sunGlow.setLevel(1000);
        //material for the sun
        sun.setEffect(sunGlow);
        PhongMaterial sunMaterial = new PhongMaterial();
        sunMaterial.setDiffuseMap(new Image("sunTexture.jpg"));
        sun.setMaterial(sunMaterial);
        Label sunLabel = new Label("SUN");
        sunLabel.setTranslateX(0);
        sunLabel.setTranslateY(planetSize + sun.getRadius());
        sunLabel.setTranslateZ(0);

        //create mercury
        Sphere mercury = new Sphere();
        setPosition(mercury, 1);
        mercury.setRadius(planetSize);
        //material for the mercury
        PhongMaterial mercuryMaterial = new PhongMaterial();
        mercuryMaterial.setDiffuseMap(new Image("mercuryTexture.jpg"));
        mercury.setMaterial(mercuryMaterial);

        // rotation for mercury

        //rotation(mercury);

        //create venus
        Sphere venus = new Sphere();
        setPosition(venus, 2);
        venus.setRadius(planetSize);
        //material for the venus
        PhongMaterial venusMaterial = new PhongMaterial();
        venusMaterial.setDiffuseMap(new Image("venusTexture.jpg"));
        venus.setMaterial(venusMaterial);

        // rotation for venus

        //rotation(venus);

        //create earth
        Sphere earth = new Sphere();
        earth.setRadius(planetSize);
        setPosition(earth, 3);
        //material for the earth
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image("earthTexture.jpg"));
        earth.setMaterial(earthMaterial);

        // rotation for earth

        //rotation(earth);

        //create moon
        Sphere moon = new Sphere();
        setPosition(moon, 4);
        moon.setRadius(500);
        //material for the moon
        PhongMaterial moonMaterial = new PhongMaterial();
        moonMaterial.setDiffuseMap(new Image("moonTexture.jpg"));
        moon.setMaterial(moonMaterial);

        // rotation for moon

        //rotation(moon);

        //create mars
        Sphere mars = new Sphere();
        setPosition(mars, 5);
        mars.setRadius(planetSize);
        //material for the mars
        PhongMaterial marsMaterial = new PhongMaterial();
        marsMaterial.setDiffuseMap(new Image("marsTexture.png"));
        mars.setMaterial(marsMaterial);

        // rotation for mars

        //rotation(mars);

        //create jupiter
        Sphere jupiter = new Sphere();
        setPosition(jupiter, 6);
        jupiter.setRadius(planetSize);
        //material for the jupiter
        PhongMaterial jupiterMaterial = new PhongMaterial();
        jupiterMaterial.setDiffuseMap(new Image("jupiterTexture.jpg"));
        jupiter.setMaterial(jupiterMaterial);

        // rotation for jupiter

        //rotation(jupiter) ;

        //create saturn
        Sphere saturn = new Sphere();
        setPosition(saturn, 7);
        saturn.setRadius(planetSize);
        //material for the saturn
        PhongMaterial saturnMaterial = new PhongMaterial();
        saturnMaterial.setDiffuseMap(new Image("saturnTexture.jpg"));
        saturn.setMaterial(saturnMaterial);

        // rotation for saturn

        //rotation(saturn) ;

        //create titan
        Sphere titan = new Sphere();
        setPosition(titan, 8);
        titan.setRadius(planetSize);
        //material for the titan
        PhongMaterial titanMaterial = new PhongMaterial();
        titanMaterial.setDiffuseMap(new Image("titanTexture.jpg"));
        titan.setMaterial(titanMaterial);

        // rotation for titan

//        rotation(titan) ;

        //create neptune
        Sphere neptune = new Sphere();
        setPosition(neptune, 9);
        neptune.setRadius(planetSize);
        //material for the neptune
        PhongMaterial neptuneMaterial = new PhongMaterial();
        neptuneMaterial.setDiffuseMap(new Image("neptuneTexture.jpg"));
        neptune.setMaterial(neptuneMaterial);

        // rotation for neptune

        //rotation(neptune);

        //create uranus
        Sphere uranus = new Sphere();
        setPosition(uranus, 10);
        uranus.setRadius(planetSize);
        //material for the uranus
        PhongMaterial uranusMaterial = new PhongMaterial();
        uranusMaterial.setDiffuseMap(new Image("uranusTexture.jpg"));
        uranus.setMaterial(uranusMaterial);

        // rotation for uranus

        //rotation(uranus);

        // create the rocket

        Box rocketBase = new Box(1000, 500, 1000);
        setPosition(rocketBase, 3);
        rocketBase.setTranslateX(rocketBase.getTranslateX() + earth.getRadius() + 100);
        PhongMaterial rocketBaseMaterial = new PhongMaterial();
        rocketBaseMaterial.setDiffuseColor(Color.DARKVIOLET);
        rocketBase.setMaterial(rocketBaseMaterial);

        // create the path of the rocket

        Cylinder rocketPath = new Cylinder(100, 1000);
        setPosition(rocketPath, 3);
        rocketPath.setRotate(90);
        rocketPath.setTranslateX(rocketPath.getTranslateX() + earth.getRadius() + 100);
        PhongMaterial rocketPathMaterial = new PhongMaterial();
        rocketPathMaterial.setDiffuseColor(Color.GOLD);
        rocketPath.setMaterial(rocketPathMaterial);


        group.getChildren().addAll(sun, mercury, venus, earth, moon, mars, jupiter, saturn, titan, neptune, uranus, rocketBase, rocketPath);

        return group;
    }

    private static void rotation(Sphere sphere) {
        RotateTransition rotate = new RotateTransition(Duration.seconds(10), sphere);
        rotate.setByAngle(360);
        rotate.setAxis(Rotate.Y_AXIS);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setCycleCount(Animation.INDEFINITE);
        rotate.play();
    }

}