package com.example.planets;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.NumericalMethods.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class Merged extends Application {
    // static private Gravity0 model = new Gravity0(0, Math.PI / 2.0, new Euler());
    static final private Gravity0 model = new Gravity0(0.0, 0.0, new RK4());
    private static int scale = 25;
    private static final int smallScale = 25;
    private static final int bigScale = 2000;    private static int counter = 0;
    private static boolean lookAtEarth = false;
    private static boolean lookAtTitan = false;
    private static boolean lookAtSun = false;
    private static boolean lookAtEverything = false;
    private static boolean lookAtSpaceship = false;
    private final static Box[] path = new Box[10000];
    private final static double time = 0.1;
    private static double dt = 1.5;
    private static double lastAcc = 0;
    private final static double phaseTime = 10000;
    private final static double slowPhaseTime = 100000;
    private static boolean toTitan = true;
    private final static double targetDistance = 500000;
    private static final double fuelConsumptionRate = 1451.5;
    private static double totalConsumption = 488000;
    private final double maxForce = 3 * Math.pow(10, 7);
    private final static Text positionText = new Text("Spacecraft position at : ");
    private final static Text distanceText = new Text("Distance between spacecraft and titan : ");
    private final static Text timeText = new Text("Time so far : ");
    private final static Text reachedTitanText = new Text("Reached titan in : not yet (days)");
    private final static Text reachedTitan2Text = new Text("Closest distance reached by the spacecraft : (km)");
    private static boolean checkForReachedTitan = false;

    final int ScreenWIDTH = 1920;
    final int ScreenHEIGHT = 1080;

    @Override
    public void start(Stage stage) throws Exception {

        inicilizePath();

        stage.setMaximized(true);
        //create a new group
        Group root = new Group();


        Group world = createEnvironment();
        SubScene worldScene = new SubScene(world, ScreenWIDTH, ScreenHEIGHT, true, SceneAntialiasing.BALANCED);

        Scene scene = new Scene(root, ScreenWIDTH, ScreenHEIGHT, true);
        world.getChildren().addAll(path);
        //background
        worldScene.setFill(Color.BLACK);
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        Camera camera = new PerspectiveCamera();
        camera.setFarClip(4000);
        camera.setNearClip(1);

        // text for spacecraft position
        positionText.setFill(Color.WHITE);
        positionText.setFont(Font.font("Arial", 16));
        positionText.setTextAlignment(TextAlignment.RIGHT);
        positionText.setTranslateX(scene.getWidth() - 730);
        positionText.setTranslateY(scene.getHeight() -130);

        // text for distance between spacecraft and titan
        distanceText.setFill(Color.WHITE);
        distanceText.setFont(Font.font("Arial", 16));
        distanceText.setTextAlignment(TextAlignment.RIGHT);
        distanceText.setTranslateX(scene.getWidth() - 730);
        distanceText.setTranslateY(scene.getHeight() - 110);

        // text for time
        timeText.setFill(Color.WHITE);
        timeText.setFont(Font.font("Arial", 16));
        timeText.setTextAlignment(TextAlignment.RIGHT);
        timeText.setTranslateX(scene.getWidth() - 730);
        timeText.setTranslateY(scene.getHeight() - 90);

        // text for time reached titan
        reachedTitanText.setFill(Color.WHITE);
        reachedTitanText.setFont(Font.font("Arial", 16));
        reachedTitanText.setTextAlignment(TextAlignment.RIGHT);
        reachedTitanText.setTranslateX(scene.getWidth() - 730);
        reachedTitanText.setTranslateY(scene.getHeight() - 70);

        // text for closest distance
        reachedTitan2Text.setFill(Color.WHITE);
        reachedTitan2Text.setFont(Font.font("Arial", 16));
        reachedTitan2Text.setTextAlignment(TextAlignment.RIGHT);
        reachedTitan2Text.setTranslateX(scene.getWidth() - 730);
        reachedTitan2Text.setTranslateY(scene.getHeight() - 50);


        //labels
        Label textLabel = new Label("fuel used:");
        textLabel.setTextFill(Color.WHITE);

        Label fuelLabel = new Label("0.0");
        fuelLabel.setTextFill(Color.WHITE);
        fuelLabel.setLayoutY(20);
        //bar
        ProgressBar progressBar = new ProgressBar(0.5);
        progressBar.setLayoutY(40);
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "0.01", "0.1", "0.5", "1.0", "1.5"
                );
        //ComboBox
        ComboBox dtBox = new ComboBox(options);
        dtBox.setLayoutY(60);
        root.getChildren().addAll(worldScene, textLabel, fuelLabel, dtBox, positionText, distanceText, timeText, reachedTitanText, reachedTitan2Text);


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
                }
                case Q -> System.exit(0);
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
        });

        world.setTranslateZ(world.getTranslateZ() + 100000);
        worldRotX.setAngle(worldRotX.getAngle());
        System.out.println(camera.getRotationAxis());
        world.setRotationAxis(new Point3D(model.getBody(3).getPos()[0], model.getBody(3).getPos()[1], model.getBody(3).getPos()[2]));
        // -------------------------------------------------------------------------------------------------------------
        // CHOOSE INITIAL LANDING DATA GUI

        Group initialData = new Group();
        Scene dataSelector = new Scene(initialData, ScreenWIDTH, ScreenHEIGHT);
        dataSelector.setFill(Color.BLACK);

//        if (distance < targetDistance) {
//              stage.setScene(dataSelector);
//        }

        TextField altitudeSelector = new TextField(); // Y coordinate
        TextField longitudeSelector = new TextField(); // X coordinate
        TextField xVelocitySelector = new TextField();

        altitudeSelector.setLayoutX((ScreenWIDTH-100)/2);
        altitudeSelector.setLayoutY((ScreenHEIGHT+200)/2);
        longitudeSelector.setLayoutX((ScreenWIDTH-100)/2);
        longitudeSelector.setLayoutY((ScreenHEIGHT)/2);
        xVelocitySelector.setLayoutX((ScreenWIDTH-100)/2);
        xVelocitySelector.setLayoutY((ScreenHEIGHT-200)/2);

        Button awewa = new Button("SELECTOR");
        awewa.setLayoutY((ScreenHEIGHT+200)/2);
        awewa.setOnAction(e -> stage.setScene(dataSelector));
        root.getChildren().add(awewa);

        Button submit = new Button("SUBMIT");
        submit.setLayoutX((ScreenWIDTH-100)/2);
        submit.setLayoutY((ScreenHEIGHT-400)/2);
        submit.setOnAction(e -> {
            try {
                double initAltitude = Double.parseDouble(altitudeSelector.getText());
                double initLongitude = Double.parseDouble(longitudeSelector.getText());
                double initxVelocity = Double.parseDouble(xVelocitySelector.getText());
            }
            catch (NumberFormatException exception) {
                System.out.println("ONLY NUMBERS BITCH");
            }
        });

        initialData.getChildren().addAll(altitudeSelector, longitudeSelector, xVelocitySelector, submit);

        // I WOKE UP IN A BUGATTI
        // -------------------------------------------------------------------------------------------------------------



        // -------------------------------------------------------------------------------------------------------------
        // LANDING ON TITAN GUI



        Group landing = new Group();
        Scene landingScene = new Scene(landing, ScreenWIDTH, ScreenHEIGHT);
        landingScene.setFill(Color.BLACK);

        Button button = new Button("\uD83C\uDF11 LANDING \uD83C\uDF11");
        button.setLayoutY(ScreenHEIGHT/2);
        button.setOnAction(e -> stage.setScene(landingScene));
        root.getChildren().add(button);

        Sphere titan = new Sphere(800);
        titan.translateXProperty().set((ScreenWIDTH)/2);
        titan.translateYProperty().set((ScreenHEIGHT+1600)/2);
        PhongMaterial titanMaterial = new PhongMaterial();
        titanMaterial.setDiffuseMap(new Image("titanTexture.jpg"));
        titan.setMaterial(titanMaterial);

        Cylinder spaceship = new Cylinder(25, 100);
        spaceship.translateXProperty().set((ScreenWIDTH)/2);
        spaceship.translateYProperty().set((ScreenHEIGHT-800)/2);
        PhongMaterial spaceshipMaterial = new PhongMaterial();
        spaceshipMaterial.setDiffuseMap(new Image("metalTexture2.jpg"));
        spaceship.setMaterial(spaceshipMaterial);

        Rotate rotate = new Rotate();
        rotate.setAngle(90);
        spaceship.getTransforms().addAll(rotate);

        Cylinder landingModule = new Cylinder(10, 50);
        landingModule.translateXProperty().set((ScreenWIDTH)/2);
        landingModule.translateYProperty().set((ScreenHEIGHT-300)/2);
        landingModule.setMaterial(spaceshipMaterial);
        landingModule.getTransforms().addAll(rotate);

        Camera landingCamera = new PerspectiveCamera(); // set at (0;0;0)
        landingScene.setCamera(landingCamera);
        landingScene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case X -> {
                    landingCamera.setTranslateX(0);
                    landingCamera.setTranslateY(-landingModule.getTranslateY());
                    landingCamera.setTranslateZ(-400);
                }
                case Z -> {
                    landingCamera.setTranslateX(0);
                    landingCamera.setTranslateY(-spaceship.getTranslateY());
                    landingCamera.setTranslateZ(-400);
                }
                case R -> {
                    landingCamera.setTranslateX(0);
                    landingCamera.setTranslateY(0);
                    landingCamera.setTranslateZ(0);
                }
                case B -> stage.setScene(scene);
                case W -> landingCamera.setTranslateZ(landingCamera.getTranslateZ() + 100);
                case S -> landingCamera.setTranslateZ(landingCamera.getTranslateZ() - 100);

            }
        });

        landing.getChildren().addAll(titan, spaceship, landingModule, landingCamera);

        // THAT'S HOW WE SEPARATE CODE
        // -------------------------------------------------------------------------------------------------------------

        stage.show();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.updatePos(time, dt, true);
                for (int i = 0; i < 12; i++) {
                    setPosition(world.getChildren().get(i), model.getBody(i));
                }

                double accX = model.getShip().getAcc()[0] * 30 * 60 * 60 * 60;
                double accY = model.getShip().getAcc()[1] * 30 * 60 * 60 * 60;
                double accZ = model.getShip().getAcc()[2] * 30 * 60 * 60 * 60;


                double currentAccMagnitude = Math.sqrt(accX * accX + accY * accY + accZ * accZ);

                double fuelConsumed = (currentAccMagnitude * 50000/ maxForce) * fuelConsumptionRate;

                totalConsumption += fuelConsumed;

                Platform.runLater(() -> {
                    //fuelLabel.setText(Double.toString(model.getShip().getUsedFuel()));
                    fuelLabel.setText(Double.toString(totalConsumption));
                    positionText.setText("Spacecraft position at : " + Arrays.toString(model.getShip().getPos()));
                    distanceText.setText("Distance between spacecraft and titan in km : " + model.getShip().getDistance(model.getBody(8)));
                    //timeText.setText("Time taken so far : " + model.getTime()/(60*24*60));
                    timeText.setText("Time taken so far : " + timeInDays(model.getTime()));

                    if(checkForReachedTitan)
                    {
                        reachedTitanText.setText("Reached titan in " + timeInDays(model.getTime()) + " days");
                        reachedTitan2Text.setText("Closest distance reached by the spacecraft : " + distance + " km");
                        checkForReachedTitan = false ;
                    }

                });
                if (dtBox.getValue() != null) {
                    dt = Double.valueOf((String) dtBox.getValue());
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
                    camera.setTranslateY(-130000);
                    camera.setTranslateZ(-(484573 * 2));
                }
                if (lookAtSpaceship) {
                    camera.setTranslateX(model.getBody(11).getPos()[0] / scale + 1000);
                    camera.setTranslateY(model.getBody(11).getPos()[1] / scale + 2000);
                    camera.setTranslateZ(model.getBody(11).getPos()[2] / scale - 24000);
                }
                goTitan();    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

            }
        }, 5, 40);
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
        int planetSize = 6371 ;
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

        Box rocketBase = new Box(2000, 1000, 2000);
        setPosition(rocketBase, 3);
        rocketBase.setTranslateX(rocketBase.getTranslateX() + earth.getRadius() + 100);
        PhongMaterial rocketBaseMaterial = new PhongMaterial();
        rocketBaseMaterial.setDiffuseMap(new Image(("crew17.jpeg")));
        rocketBase.setMaterial(rocketBaseMaterial);

        // create the path of the rocket
        Cylinder rocketPath = new Cylinder(200, 2000);
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

    private static double distance = 0;

    private static void goTitan() {
        CelestialBody targetPlanet = model.getBody(3);
        if (toTitan) {
            targetPlanet = model.getBody(8);
        }
        double x = targetPlanet.getPos()[0] - model.getShip().getPos()[0];
        double y = targetPlanet.getPos()[1] - model.getShip().getPos()[1];
        double z = targetPlanet.getPos()[2] - model.getShip().getPos()[2];
        double[] newAcc = new double[3];

        distance = model.getShip().getDistance(targetPlanet);
        if ((distance > 500000000 && model.getTime() > lastAcc + slowPhaseTime) || distance == 0) {
            newAcc[0] = model.getShip().getVel()[0] + x / 900000;
            newAcc[1] = model.getShip().getVel()[1] + y / 900000;
            newAcc[2] = model.getShip().getVel()[2] + z / 900000;
            lastAcc = model.getTime();
        } else if (model.getTime() > lastAcc + phaseTime && distance < 90000000) {
            newAcc[0] = model.getShip().getVel()[0] + x / 40000;
            newAcc[1] = model.getShip().getVel()[1] + y / 40000;
            newAcc[2] = model.getShip().getVel()[2] + z / 40000;
            lastAcc = model.getTime();

        } else if (model.getTime() > lastAcc + phaseTime && distance < 500000000) {
            newAcc[0] = model.getShip().getVel()[0] + x / 550000;
            newAcc[1] = model.getShip().getVel()[1] + y / 550000;
            newAcc[2] = model.getShip().getVel()[2] + z / 550000;
            lastAcc = model.getTime();
        }
        model.getShip().setVel(newAcc);

        if (distance < targetDistance && distance != 0) {
            toTitan = false;
            checkForReachedTitan = true ;
            System.out.println("FINISHED, reached a distance of " + distance);
        }

    }

    private int timeInDays(double time) {
        return (int) (((time / 60) / 60) / 24);
    }

}