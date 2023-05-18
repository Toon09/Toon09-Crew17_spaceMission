package com.example.planets;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.NumericalMethods.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

//ToDo
//add a spaceship model
//fix the spaceship camera
//maybe add a camera on the whole solar system - changed the look at everything so its further away, I think that's enough
//add used fuel meter
//fix the path ( problems with changing the scale )

//ToDo Actual
// rotate textures of the planets to face the actual direction they are supposed to
// thingie to change step size in real time
// have screen to chose solver & innit coniditions (have an option for our chosen coords)
// have an actual sprite for the rocket lol
// put names of planets on top of them????
// add some sort of background
// fix problem where size of trajectory points of ship doesn't fit the perspective given

// done from ToDo Actual
// when you close the window the program stops too - DONE


public class Merged extends Application {
    static Gravity0 model = new Gravity0(0, Math.PI / 2.0, new RK4());
    private static int scale = 25;
    private static final int smallScale = 25;
    private static final int bigScale = 3000;
    private static int counter = 0;
    private static boolean lookAtEarth = false;
    private static boolean lookAtTitan = false;
    private static boolean lookAtSun = false;
    private static boolean lookAtEverything = false;
    private static boolean lookAtSpaceship = false;
    private static Box[] path = new Box[10000];

    @Override
    public void start(Stage stage) throws Exception {
        inicilizePath();
        //create a new group
        Group root = new Group();

        Group world = createEnvironment();
        SubScene worldScene = new SubScene(world, 1920, 1080, true, SceneAntialiasing.BALANCED);

        Scene scene = new Scene(root, 1920, 1080, true);
        world.getChildren().addAll(path);
        //background
        worldScene.setFill(Color.BLACK);
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        Camera camera = new PerspectiveCamera();
        camera.setFarClip(4000);
        camera.setNearClip(1);

        //label and progressbar to show the fuel
        Label title = new Label("Fuel remaining:");
        title.setTextFill(Color.WHITE);
        Label progressLabel = new Label("100000000");
        progressLabel.setTranslateY(20);
        progressLabel.setTextFill(Color.WHITE);
        ProgressBar fuelBar = new ProgressBar(0.5);
        fuelBar.setPrefSize(200,20);
        fuelBar.setLayoutY(40);
        root.getChildren().addAll(worldScene, title, fuelBar, progressLabel);
        //initial camera setting
        worldScene.setCamera(camera);
        Rotate worldRotX = new Rotate(0, Rotate.X_AXIS);
        Rotate worldRotY = new Rotate(0, Rotate.Y_AXIS);
        camera.getTransforms().addAll(worldRotY, worldRotX);
        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case DIGIT0 -> {
                    System.out.println("------------------------------------------");
                    System.out.println("titan at: ");
                    for (int i = 0; i < 3; i++) {
                        System.out.println(model.getBody(8).getPos()[i] / scale);
                    }
                    System.out.println("------------------------------------------");
                    System.out.println("spaceship at: ");
                    for (int i = 0; i < 3; i++) {
                        System.out.println(model.getBody(11).getPos()[i] / scale);
                    }
                    System.out.println("------------------------------------------");
                    System.out.println("camera at:");
                    System.out.println("x: " + camera.getTranslateX() + ", y: " + camera.getTranslateY() + ", z: " + camera.getTranslateZ());
                    System.out.println("------------------------------------------");
                    System.out.println("Time is: ");
                    System.out.println(model.getTime());
                }
                case P -> System.exit(0);
                case DIGIT1 -> {
                    if (!lookAtEarth) {
                        world.getChildren().get(7).setVisible(true);
                        scale = smallScale;
                        lookAtEarth = true;
                        lookAtTitan = false;
                        lookAtSun = false;
                        lookAtEverything = false;
                        lookAtSpaceship = false;
                    }
                }
                case DIGIT2 -> {
                    world.getChildren().get(7).setVisible(true);
                    scale = smallScale;
                    lookAtSun = true;
                    lookAtTitan = false;
                    lookAtEarth = false;
                    lookAtEverything = false;
                    lookAtSpaceship = false;
                }
                case DIGIT3 -> {
                    world.getChildren().get(7).setVisible(true);
                    scale = smallScale;
                    lookAtTitan = true;
                    lookAtSun = false;
                    lookAtEarth = false;
                    lookAtEverything = false;
                    lookAtSpaceship = false;
                }
                case DIGIT4 -> {
                    world.getChildren().get(7).setVisible(false);
                    scale = bigScale;
                    lookAtEverything = true;
                    lookAtSun = false;
                    lookAtEarth = false;
                    lookAtTitan = false;
                    lookAtSpaceship = false;
                }
                case DIGIT5 -> {
                    lookAtSpaceship = true;
                    scale = smallScale;
                    lookAtEverything = false;
                    lookAtSun = false;
                    lookAtEarth = false;
                    lookAtTitan = false;
                }
            }

            //stage.setOnCloseRequest(e -> Platform.exit());


        });

        world.setTranslateZ(world.getTranslateZ() + 100000);
        worldRotX.setAngle(worldRotX.getAngle());
        System.out.println(camera.getRotationAxis());
        world.setRotationAxis(new Point3D(model.getBody(3).getPos()[0], model.getBody(3).getPos()[1], model.getBody(3).getPos()[2]));

        stage.show();
        stage.setOnCloseRequest(e ->
        {
            System.out.println("Closing");
            Platform.exit() ;
            stage.close();
            System.exit(0) ;
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.updatePos(0.05, 1.0, true);
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
                if (lookAtEverything) {
                    camera.setTranslateX(183608);
                    camera.setTranslateY(-128907);
                    camera.setTranslateZ(-(484573*2));
                }
                if (lookAtSpaceship) {
                    camera.setTranslateX(model.getBody(11).getPos()[0] / scale + 1000);
                    camera.setTranslateY(model.getBody(11).getPos()[1] / scale + 2000);
                    camera.setTranslateZ(model.getBody(11).getPos()[2] / scale - 24000);
                }

            }
        }, 1, 1);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (counter < path.length) {
                    path[counter].setTranslateX(model.getBody(11).getPos()[0] / scale);
                    path[counter].setTranslateY(model.getBody(11).getPos()[1] / scale);
                    path[counter].setTranslateZ(model.getBody(11).getPos()[2] / scale);
                    counter++;
                }
                if(model.getTime() == 10){
                    //model.getShip().accelerate();
                }
            }
        },5,40);
    }

    public static void main(String... args) {
        launch(args);
    }

    public static void setPosition(Node sphere, CelestialBody body) {
        sphere.setTranslateX(body.getPos()[0] / scale);
        sphere.setTranslateY(body.getPos()[1] / scale);
        sphere.setTranslateZ(body.getPos()[2] / scale);
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

    private static final double[][] positions = {{0, 0, 0}, {7.83e6, 4.49e7, 2.87e6}, {-2.82e7, 1.04e8, 3.01e6},
            {-1.48e8, -2.78e7, 3.37e4}, {-1.48e8, -2.75e7, 7.02e4}, {-1.59e8, 1.89e8, 7.87e6},
            {6.93e8, 2.59e8, -1.66e7}, {1.25e9, -7.60e8, -3.67e7}, {1.25e9, 7.61e8, -3.63e7},
            {4.45e9, -3.98e8, -9.45e7}, {1.96e9, 2.19e9, -1.72e7}};

    public Group createEnvironment() {
        Group group = new Group();

        //create the sun
        Sphere sun = new Sphere();
        setPosition(sun, 0);
        int planetSize = 6371 / 2;
        sun.setRadius(planetSize);
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

        //create venus
        Sphere venus = new Sphere();
        setPosition(venus, 2);
        venus.setRadius(planetSize);
        //material for the venus
        PhongMaterial venusMaterial = new PhongMaterial();
        venusMaterial.setDiffuseMap(new Image("venusTexture.jpg"));
        venus.setMaterial(venusMaterial);

        //create earth
        Sphere earth = new Sphere();
        earth.setRadius(planetSize);
        setPosition(earth, 3);
        //material for the earth
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image("earthTexture.jpg"));
        earth.setMaterial(earthMaterial);

        //create moon
        Sphere moon = new Sphere();
        setPosition(moon, 4);
        moon.setRadius(500);
        //material for the moon
        PhongMaterial moonMaterial = new PhongMaterial();
        moonMaterial.setDiffuseMap(new Image("moonTexture.jpg"));
        moon.setMaterial(moonMaterial);

        //create mars
        Sphere mars = new Sphere();
        setPosition(mars, 5);
        mars.setRadius(planetSize);
        //material for the Mars
        PhongMaterial marsMaterial = new PhongMaterial();
        marsMaterial.setDiffuseMap(new Image("marsTexture.png"));
        mars.setMaterial(marsMaterial);

        //create jupiter
        Sphere jupiter = new Sphere();
        setPosition(jupiter, 6);
        jupiter.setRadius(planetSize);
        //material for the jupiter
        PhongMaterial jupiterMaterial = new PhongMaterial();
        jupiterMaterial.setDiffuseMap(new Image("jupiterTexture.jpg"));
        jupiter.setMaterial(jupiterMaterial);

        //create saturn
        Sphere saturn = new Sphere();
        setPosition(saturn, 7);
        saturn.setRadius(planetSize);
        //material for the saturn
        PhongMaterial saturnMaterial = new PhongMaterial();
        saturnMaterial.setDiffuseMap(new Image("saturnTexture.jpg"));
        saturn.setMaterial(saturnMaterial);


        //create titan
        Sphere titan = new Sphere();
        setPosition(titan, 8);
        titan.setRadius(planetSize);
        //material for the titan
        PhongMaterial titanMaterial = new PhongMaterial();
        titanMaterial.setDiffuseMap(new Image("titanTexture.jpg"));
        titan.setMaterial(titanMaterial);

        //create neptune
        Sphere neptune = new Sphere();
        setPosition(neptune, 9);
        neptune.setRadius(planetSize);
        //material for the neptune
        PhongMaterial neptuneMaterial = new PhongMaterial();
        neptuneMaterial.setDiffuseMap(new Image("neptuneTexture.jpg"));
        neptune.setMaterial(neptuneMaterial);

        //create uranus
        Sphere uranus = new Sphere();
        setPosition(uranus, 10);
        uranus.setRadius(planetSize);
        //material for the uranus
        PhongMaterial uranusMaterial = new PhongMaterial();
        uranusMaterial.setDiffuseMap(new Image("uranusTexture.jpg"));
        uranus.setMaterial(uranusMaterial);


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

    private static void inicilizePath() {
        for (int i = 0; i < path.length; i++) {
            path[i] = new Box(800, 800, 800);
        }
    }

}