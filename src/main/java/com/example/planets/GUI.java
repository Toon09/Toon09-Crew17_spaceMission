package com.example.planets;

import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.shape.Sphere;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.effect.Glow;

//ToDo
//Textures - for now there are just colors in place
//Planets moving
//Add the rest of the planets/moons - done
//Putting the planets in the initial positions - kinda done
//set the planets sizes
//fix the camera
//planets placement - done i think
//better scale
//make the sun shine
//some glow around other planets (tried for the sun but it doesnt work)
public class GUI extends Application {

    public static int SIZEFACTOR = 100;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group world = createEnvironment();

        Scene scene = new Scene(world, 1920, 1080, true);
        Group axis = buildAxes();
        world.getChildren().addAll(axis);
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setWidth(16 * SIZEFACTOR);
        primaryStage.setHeight(9 * SIZEFACTOR);
        Camera camera = new PerspectiveCamera();
        camera.setFarClip(2000);
        camera.setNearClip(1);

        //initial camera setting
        scene.setCamera(camera);
        Rotate worldRotX = new Rotate(0, Rotate.X_AXIS);
        Rotate worldRotY = new Rotate(0, Rotate.Y_AXIS);
        Translate worldTransX = new Translate();
        world.getTransforms().addAll(worldRotY, worldRotX);
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
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



        worldRotX.setAngle(worldRotX.getAngle());
        primaryStage.show();
    }

    private Group createEnvironment() {
        Group group = new Group();
        //create the sun
        Sphere sun = new Sphere();
        setPosition(sun, 0);
        sun.setRadius(10000);
        //glow
        Glow sunGlow = new Glow();
        sunGlow.setLevel(10000);
        //material for the sun
        sun.setEffect(sunGlow);
        PhongMaterial sunMaterial = new PhongMaterial();
        sunMaterial.setDiffuseMap(new Image("sunTexture.jpg"));
        sun.setMaterial(sunMaterial);
        Label sunLabel = new Label("SUN");
        sunLabel.setTranslateX(0);
        sunLabel.setTranslateY(60000+sun.getRadius());
        sunLabel.setTranslateZ(0);

        //create mercury
        Sphere mercury = new Sphere();
        setPosition(mercury, 1);
        mercury.setRadius(2440);
        //material for the mercury
        PhongMaterial mercuryMaterial = new PhongMaterial();
        mercuryMaterial.setDiffuseMap(new Image("mercuryTexture.jpg"));
        mercury.setMaterial(mercuryMaterial);

        //create venus
        Sphere venus = new Sphere();
        setPosition(venus, 2);
        venus.setRadius(6050);
        //material for the venus
        PhongMaterial venusMaterial = new PhongMaterial();
        venusMaterial.setDiffuseMap(new Image("venusTexture.jpg"));
        venus.setMaterial(venusMaterial);

        //create earth
        Sphere earth = new Sphere();
        earth.setRadius(6370);
        setPosition(earth, 3);
        //material for the earth
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image("earthTexture.jpg"));
        earth.setMaterial(earthMaterial);

        //create moon
        Sphere moon = new Sphere();
        setPosition(moon, 4);
        moon.setRadius(1740);
        //material for the moon
        PhongMaterial moonMaterial = new PhongMaterial();
        moonMaterial.setDiffuseMap(new Image("moonTexture.jpg"));
        moon.setMaterial(moonMaterial);

        //create mars
        Sphere mars = new Sphere();
        setPosition(mars, 5);
        mars.setRadius(3390);
        //material for the mars
        PhongMaterial marsMaterial = new PhongMaterial();
        marsMaterial.setDiffuseMap(new Image("marsTexture.jpg"));
        mars.setMaterial(marsMaterial);

        //create jupiter
        Sphere jupiter = new Sphere();
        setPosition(jupiter, 6);
        jupiter.setRadius(7149);
        //material for the jupiter
        PhongMaterial jupiterMaterial = new PhongMaterial();
        jupiterMaterial.setDiffuseMap(new Image("jupiterTexture.jpg"));
        jupiter.setMaterial(jupiterMaterial);

        //create saturn
        Sphere saturn = new Sphere();
        setPosition(saturn, 7);
        saturn.setRadius(6026);
        //material for the saturn
        PhongMaterial saturnMaterial = new PhongMaterial();
        saturnMaterial.setDiffuseMap(new Image("saturnTexture.jpg"));
        saturn.setMaterial(saturnMaterial);

        //create titan
        Sphere titan = new Sphere();
        setPosition(titan, 8);
        titan.setRadius(2570);
        //material for the titan
        PhongMaterial titanMaterial = new PhongMaterial();
        titanMaterial.setDiffuseMap(new Image("titanTexture.jpg"));
        titan.setMaterial(titanMaterial);

        //create neptune
        Sphere neptune = new Sphere();
        setPosition(neptune, 9);
        neptune.setRadius(2476);
        //material for the neptune
        PhongMaterial neptuneMaterial = new PhongMaterial();
        neptuneMaterial.setDiffuseMap(new Image("neptuneTexture.jpg"));
        neptune.setMaterial(neptuneMaterial);

        //create uranus
        Sphere uranus = new Sphere();
        setPosition(uranus, 10);
        uranus.setRadius(2555);
        //material for the uranus
        PhongMaterial uranusMaterial = new PhongMaterial();
        uranusMaterial.setDiffuseMap(new Image("uranusTexture.jpg"));
        uranus.setMaterial(uranusMaterial);

        group.getChildren().addAll(sun,sunLabel, mercury, venus, earth, moon, mars, jupiter, saturn, titan, neptune, uranus);

        return group;
    }


    public static void main(String... args) {
        launch(args);
    }

    private static double[][] positions = {{0, 0, 0}, {7.83e6, 4.49e7, 2.87e6}, {-2.82e7, 1.04e8, 3.01e6},
            {-1.48e8, -2.78e7, 3.37e4}, {-1.48e8, -2.75e7, 7.02e4}, {-1.59e8, 1.89e8, 7.87e6},
            {6.93e8, 2.59e8, -1.66e7}, {1.25e9, -7.60e8, -3.67e7}, {1.25e9, 7.61e8, -3.63e7},
            {4.45e9, -3.98e8, -9.45e7}, {1.96e9, 2.19e9, -1.72e7}};
    private static double scale = 2000;
    private static int planetSize = 6371;

    private String getposition(Sphere sphere) {
        return ("X: " + sphere.getTranslateX() + ", Y: " + sphere.getTranslateY() + ", Z: " + sphere.getTranslateZ());
    }

    private void setPosition(Sphere sphere, int index) {
        sphere.setTranslateX(positions[index][0] / scale);
        sphere.setTranslateY(positions[index][1] / scale);
        sphere.setTranslateZ(positions[index][2] / scale);
    }
    private Group buildAxes() {
        //green - Z
        //blue -Y
        //red - X

        Box xAxis = new Box(1200000, 10, 10);
        Box yAxis = new Box(10, 1200000, 10);
        Box zAxis = new Box(10, 10, 1200000);

        xAxis.setMaterial(new PhongMaterial(Color.RED));
        yAxis.setMaterial(new PhongMaterial(Color.GREEN));
        zAxis.setMaterial(new PhongMaterial(Color.BLUE));

        Group axisGroup = new Group();
        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        return axisGroup;
    }
}