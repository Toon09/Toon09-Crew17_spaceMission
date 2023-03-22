package com.example.planets;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.shape.Sphere;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.effect.Glow;
import javafx.util.Duration;

public class GUI extends Application {

    public static int SIZEFACTOR = 100;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group world = createEnvironment();

        Scene scene = new Scene(world, 1920, 1080, true);
        scene.setFill(Color.BLACK);

        // trying out stuff
        //StackPane root = new StackPane();
        //Image img = new Image("background.jpg");
        //BackgroundImage bImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        //Background bGround = new Background(bImg);
        //root.setBackground(bGround);

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



        worldRotX.setAngle(worldRotX.getAngle());
        primaryStage.show();
    }

    public Group createEnvironment() {
        Group group = new Group();

        //create the sun
        Sphere sun = new Sphere();
        setPosition(sun, 0);
        sun.setRadius(planetSize);
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
        sunLabel.setTranslateY(planetSize+sun.getRadius());
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

        rotation(mercury);

        //create venus
        Sphere venus = new Sphere();
        setPosition(venus, 2);
        venus.setRadius(planetSize);
        //material for the venus
        PhongMaterial venusMaterial = new PhongMaterial();
        venusMaterial.setDiffuseMap(new Image("venusTexture.jpg"));
        venus.setMaterial(venusMaterial);

        // rotation for venus

        rotation(venus);

        //create earth
        Sphere earth = new Sphere();
        earth.setRadius(planetSize);
        setPosition(earth, 3);
        //material for the earth
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image("earthTexture.jpg"));
        earth.setMaterial(earthMaterial);

        // rotation for earth

        rotation(earth);

        //create moon
        Sphere moon = new Sphere();
        setPosition(moon, 4);
        moon.setRadius(1500);
        //material for the moon
        PhongMaterial moonMaterial = new PhongMaterial();
        moonMaterial.setDiffuseMap(new Image("moonTexture.jpg"));
        moon.setMaterial(moonMaterial);

        // rotation for moon

        rotation(moon);

        //create mars
        Sphere mars = new Sphere();
        setPosition(mars, 5);
        mars.setRadius(planetSize);
        //material for the mars
        PhongMaterial marsMaterial = new PhongMaterial();
        marsMaterial.setDiffuseMap(new Image("marsTexture.png"));
        mars.setMaterial(marsMaterial);

        // rotation for mars

        rotation(mars);

        //create jupiter
        Sphere jupiter = new Sphere();
        setPosition(jupiter, 6);
        jupiter.setRadius(planetSize);
        //material for the jupiter
        PhongMaterial jupiterMaterial = new PhongMaterial();
        jupiterMaterial.setDiffuseMap(new Image("jupiterTexture.jpg"));
        jupiter.setMaterial(jupiterMaterial);

        // rotation for jupiter

        rotation(jupiter) ;

        //create saturn
        Sphere saturn = new Sphere();
        setPosition(saturn, 7);
        saturn.setRadius(planetSize);
        //material for the saturn
        PhongMaterial saturnMaterial = new PhongMaterial();
        saturnMaterial.setDiffuseMap(new Image("saturnTexture.jpg"));
        saturn.setMaterial(saturnMaterial);

        // rotation for saturn

        rotation(saturn) ;

        //create titan
        Sphere titan = new Sphere();
        setPosition(titan, 8);
        titan.setRadius(planetSize);
        //material for the titan
        PhongMaterial titanMaterial = new PhongMaterial();
        titanMaterial.setDiffuseMap(new Image("titanTexture.jpg"));
        titan.setMaterial(titanMaterial);

        // rotation for titan

        rotation(titan) ;

        //create neptune
        Sphere neptune = new Sphere();
        setPosition(neptune, 9);
        neptune.setRadius(planetSize);
        //material for the neptune
        PhongMaterial neptuneMaterial = new PhongMaterial();
        neptuneMaterial.setDiffuseMap(new Image("neptuneTexture.jpg"));
        neptune.setMaterial(neptuneMaterial);

        // rotation for neptune

        rotation(neptune) ;

        //create uranus
        Sphere uranus = new Sphere();
        setPosition(uranus, 10);
        uranus.setRadius(planetSize);
        //material for the uranus
        PhongMaterial uranusMaterial = new PhongMaterial();
        uranusMaterial.setDiffuseMap(new Image("uranusTexture.jpg"));
        uranus.setMaterial(uranusMaterial);

        // rotation for uranus

        rotation(uranus) ;

        // create the rocket

        Box rocketBase = new Box(1000, 500, 1000);
        setPosition(rocketBase, 3);
        rocketBase.setTranslateX(rocketBase.getTranslateX()+earth.getRadius()+100);
        PhongMaterial rocketBaseMaterial = new PhongMaterial() ;
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


        group.getChildren().addAll(sun, mercury, venus, earth, moon, mars, jupiter, saturn, titan, neptune, uranus,rocketBase,rocketPath);

        return group;
    }


    public static void main(String... args) {
        launch(args);
    }

    public static void rotation(Sphere sphere)
    {
        RotateTransition rotate = new RotateTransition(Duration.seconds(10), sphere);
        rotate.setByAngle(360);
        rotate.setAxis(Rotate.Y_AXIS);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setCycleCount(Animation.INDEFINITE);
        rotate.play() ;
    }

    private static double[][] positions = {{0, 0, 0}, {7.83e6, 4.49e7, 2.87e6}, {-2.82e7, 1.04e8, 3.01e6},
            {-1.48e8, -2.78e7, 3.37e4}, {-1.48e8, -2.75e7, 7.02e4}, {-1.59e8, 1.89e8, 7.87e6},
            {6.93e8, 2.59e8, -1.66e7}, {1.25e9, -7.60e8, -3.67e7}, {1.25e9, 7.61e8, -3.63e7},
            {4.45e9, -3.98e8, -9.45e7}, {1.96e9, 2.19e9, -1.72e7}};
    private static double scale = 3000;
    private static int planetSize = 6371/2;

    private String getposition(Sphere sphere) {
        return ("X: " + sphere.getTranslateX() + ", Y: " + sphere.getTranslateY() + ", Z: " + sphere.getTranslateZ());
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

}