package com.example.planets;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import com.example.planets.BackEnd.Models.Gravity0;
import com.example.planets.BackEnd.NumericalMethods.*;
import com.example.planets.BackEnd.Trajectory.Cost.MinDistAndFuel;
import com.example.planets.BackEnd.Trajectory.FeedBack;
import com.example.planets.GUI.SolarSystem;
import javafx.application.*;
import javafx.collections.*;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.*;

@SuppressWarnings("rawtypes")
public class Merged extends Application {

    static private final Gravity0 model = new Gravity0(0.0, 0.0, new RK4(), "titan", 1, 364, new MinDistAndFuel());
    private static int scale = 25;
    private static final int smallScale = 25;
    private static final int bigScale = 2000;
    private static int counter = 0;
    private static boolean lookAtEarth = false;
    private static boolean lookAtTitan = false;
    private static boolean lookAtSun = false;
    private static boolean lookAtEverything = false;
    private static boolean lookAtSpaceship = false;
    private final static Box[] path = new Box[10000];
    private final static double time = 0.1;
    private static double dt = 2;
    private static double lastAcc = 0;
    private final static double phaseTime = 10000;
    private final static double slowPhaseTime = 100000;
    private static boolean toTitan = true;
    private final static double targetDistance = 500000;
    private static final double fuelConsumptionRate = 1451.5;
    private static double totalConsumption = 488000;
    private final double maxForce = 3 * Math.pow(10, 7);

    private static boolean checkForReachedTitan = false;
    final int ScreenWIDTH = 1920;
    final int ScreenHEIGHT = 1080;


    @Override
    public void start(Stage stage) throws Exception {

        inicilizePath();
        stage.setMaximized(true);
        stage.setTitle("Titanic Space Odyssey by crew17");
        stage.getIcons().add(new Image("amogus.png"));

        //create a new group
        Group root = new Group();

        SolarSystem system = new SolarSystem();
        Group world = system.createEnvironment();
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

        Text positionText = new Text("Spacecraft position at : ");
        Text distanceText = new Text("Distance between spacecraft and titan : ");
        Text timeText = new Text("Time so far : ");
        Text reachedTitanText = new Text("Reached titan in : not yet (days)");
        Text reachedTitan2Text = new Text("Closest distance reached by the spacecraft : (km)");

        setTextProperties(positionText, -130, scene);

        setTextProperties(distanceText, -110, scene);

        setTextProperties(timeText, -90, scene);

        setTextProperties(reachedTitanText, -70, scene);

        setTextProperties(reachedTitan2Text, -50, scene);



        //labels
        Label textLabel = new Label("fuel used:");
        textLabel.setTextFill(Color.WHITE);

        Label fuelLabel = new Label("0.0");
        fuelLabel.setTextFill(Color.WHITE);
        fuelLabel.setLayoutY(20);

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
                case DIGIT0 -> printPositions();
                case Q -> System.exit(0);
                case DIGIT1 -> {
                    if (!lookAtEarth) {
                        world.getChildren().get(7).setVisible(true);
                        scale = smallScale;
                        setLookBooleans("EARTH");
                    }
                }
                case DIGIT2 -> {
                    world.getChildren().get(7).setVisible(true);
                    scale = smallScale;
                    setLookBooleans("SUN");
                }
                case DIGIT3 -> {
                    world.getChildren().get(7).setVisible(true);
                    scale = smallScale;
                    setLookBooleans("TITAN");
                }
                case DIGIT4 -> {
                    world.getChildren().get(7).setVisible(false);
                    scale = bigScale;
                    setLookBooleans("EVERYTHING");
                }
                case DIGIT5 -> setLookBooleans("SHIP");
            }
        });

        world.setTranslateZ(world.getTranslateZ() + 100000);
        worldRotX.setAngle(worldRotX.getAngle());
        System.out.println(camera.getRotationAxis());
        world.setRotationAxis(new Point3D(model.getBody(3).getPos()[0], model.getBody(3).getPos()[1], model.getBody(3).getPos()[2]));

        // -------------------------------------------------------------------------------------------------------------
        // LANDING ON TITAN GUI

        Group landing = new Group();
        Scene landingScene = new Scene(landing, ScreenWIDTH, ScreenHEIGHT);
        landingScene.setFill(Color.BLACK);

        Cylinder titan = new Cylinder(800, 2920);
        titan.translateXProperty().set((double) (ScreenWIDTH) / 2);
        titan.translateYProperty().set((double) (ScreenHEIGHT + 1600) / 2);
        PhongMaterial titanMaterial = new PhongMaterial();
        titanMaterial.setDiffuseMap(new Image("titanTexture.jpg"));
        titan.setMaterial(titanMaterial);

        Rotate rotate = new Rotate();
        rotate.setAngle(90);
        titan.getTransforms().addAll(rotate);

        Cylinder landingModule = new Cylinder(10, 50);
        landingModule.translateXProperty().set((double) (ScreenWIDTH) / 2);
        landingModule.translateYProperty().set((double) (ScreenHEIGHT - 600) / 2);
        PhongMaterial spaceshipMaterial = new PhongMaterial();
        spaceshipMaterial.setDiffuseMap(new Image("spaceshipTexture.jpg"));
        landingModule.setMaterial(spaceshipMaterial);
        landingModule.getTransforms().addAll(rotate);

        Camera landingCamera = new PerspectiveCamera();
        landingScene.setCamera(landingCamera);
        landingScene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
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

        landing.getChildren().addAll(titan, landingModule, landingCamera);

        // -------------------------------------------------------------------------------------------------------------
        // CHOOSE INITIAL LANDING DATA GUI

        Group initialData = new Group();
        Scene dataSelector = new Scene(initialData, ScreenWIDTH, ScreenHEIGHT);
        dataSelector.setFill(Color.BLACK);

        TextField altitudeSelector = new TextField();
        setSelectorProperties(altitudeSelector, -100,-200);


        Text altitude = new Text("Choose initial altitude (X)");
        setTextProperties(altitude, -220);


        TextField longitudeSelector = new TextField();
        setSelectorProperties(longitudeSelector,-100,0);

        Text longitude = new Text("Choose initial longitude (Y)");
        setTextProperties(longitude, -20);

        TextField yVelocitySelector = new TextField();
        setSelectorProperties(yVelocitySelector, -100, 200);

        Text yVelocity = new Text("Choose initial ship velocity (V)");
        setTextProperties(yVelocity, 180);
        yVelocity.setFont(Font.font("Arial", 16));


        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Unexpected input");
        alert.setContentText("Only doubles are allowed!");

        FeedBack[] controller = new FeedBack[1];
        final double[] timeLanding = new double[1];

        Button defaultData = new Button("DEFAULT");
        defaultData.setLayoutX((double) (ScreenWIDTH + 60) / 2);
        defaultData.setLayoutY((double) (ScreenHEIGHT + 400) / 2);
        defaultData.setOnAction(e -> {
            controller[0] = new FeedBack();
            timeLanding[0] = model.getTime();
            stage.setScene(landingScene);
        });

        Button submit = new Button("SUBMIT");
        submit.setLayoutX((double) (ScreenWIDTH - 100) / 2);
        submit.setLayoutY((double) (ScreenHEIGHT + 400) / 2);
        submit.setOnAction(e -> {
            try {
                double initAltitude = Double.parseDouble(altitudeSelector.getText());
                double initLongitude = Double.parseDouble(longitudeSelector.getText());
                double initialVelocity = Double.parseDouble(yVelocitySelector.getText());
                double[] initialPosition = {initAltitude, initLongitude};
                controller[0] = new FeedBack(initialPosition, initialVelocity);
                timeLanding[0] = model.getTime();
                stage.setScene(landingScene);

            } catch (NumberFormatException exception) {
                alert.showAndWait();
                altitudeSelector.clear();
                longitudeSelector.clear();
                yVelocitySelector.clear();
            }
        });
        dataSelector.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.B) {
                stage.setScene(scene);
            }
        });
        initialData.getChildren().addAll(altitudeSelector, longitudeSelector, yVelocitySelector, submit,
                altitude, longitude, yVelocity, defaultData);

        // -------------------------------------------------------------------------------------------------------------

        Alert landingAlert = new Alert(Alert.AlertType.INFORMATION);
        landingAlert.setTitle("Spacecraft landed !");
        landingAlert.setHeaderText("Landing Information");

        Text textForAlert = new Text();
        textForAlert.setWrappingWidth(500);

        stage.show();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (stage.getScene().equals(landingScene)) {
                    controller[0].update();
                    double[] pos = controller[0].getLandingModule().getPos();

                    landingModule.translateXProperty().set(960);
                    landingModule.setTranslateY(250 + 2.77 * (300 - pos[1]));

                    System.out.println(Arrays.toString(controller[0].getLandingModule().getPos()));

                } else {
                    model.updatePos(time, dt, true);
                    for (int i = 0; i < 12; i++) {
                        setPosition(world.getChildren().get(i), model.getBody(i));
                    }


                    double accX = model.getShip().getAcc()[0] * 30 * 60 * 60 * 60;
                    double accY = model.getShip().getAcc()[1] * 30 * 60 * 60 * 60;
                    double accZ = model.getShip().getAcc()[2] * 30 * 60 * 60 * 60;


                    double currentAccMagnitude = Math.sqrt(accX * accX + accY * accY + accZ * accZ);

                    double fuelConsumed = (currentAccMagnitude * 50000 / maxForce) * fuelConsumptionRate;

                    totalConsumption += fuelConsumed;

                    Platform.runLater(() -> {

                        fuelLabel.setText(Double.toString(totalConsumption));
                        positionText.setText("Spacecraft position at : " + Arrays.toString(model.getShip().getPos()));
                        distanceText.setText("Distance between spacecraft and titan in km : " + model.getShip().getDistance(model.getBody(8)));
                        timeText.setText("Time taken so far : " + timeInDays(model.getTime()));

                        if (checkForReachedTitan) {
                            reachedTitanText.setText("Reached titan in " + timeInDays(model.getTime()) + " days");
                            reachedTitan2Text.setText("Closest distance reached by the spacecraft : " + distance + " km");
                            checkForReachedTitan = false;
                        }

                        if (distance < targetDistance) stage.setScene(dataSelector);

                        if (stage.getScene().equals(landingScene))
                            if (controller[0].isFinished()) {
                                textForAlert.setText("Landed successfully at x : " + controller[0].getLandingModule().getPos()[0] + " , y  : " + controller[0].getLandingModule().getPos()[1] + "\nWith velocity : " + controller[0].getLandingModule().getVel()[1] + " ! ");
                                textForAlert.setFont(Font.font("Verdana", 18));
                                landingAlert.getDialogPane().setContent(textForAlert);
                                //landingAlert.setContentText("Landed successfully at : " + controller[0].getLandingModule().getPos() + " with velocity : " + controller[0].getLandingModule().getVel() + " ! ");
                                landingAlert.setOnCloseRequest(e -> stage.setScene(scene));
                                landingAlert.showAndWait();

                            }

                    });
                    if (dtBox.getValue() != null) {
                        dt = Double.parseDouble((String) dtBox.getValue());
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
                    goTitan();


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
            checkForReachedTitan = true;
            System.out.println("FINISHED, reached a distance of " + distance);
        }

    }

    private void setTextProperties(Text field, int changeY) {
        field.setFont(Font.font("Arial", 16));
        field.setFill(Color.WHITE);
        field.setLayoutX((double) (ScreenWIDTH - 100) / 2);
        field.setLayoutY((double) (ScreenHEIGHT + changeY) / 2);
    }

    private void setTextProperties(Text field, int changeY, Scene scene) {
        field.setFill(Color.WHITE);
        field.setFont(Font.font("Arial", 16));
        field.setTextAlignment(TextAlignment.RIGHT);
        field.setTranslateX(scene.getWidth() - 730);
        field.setTranslateY(scene.getHeight() + changeY);
    }

    private void setLookBooleans(String name){
        lookAtEverything = false;
        lookAtTitan = false;
        lookAtSun = false;
        lookAtSpaceship = false;
        lookAtEarth = false;
        switch (name.toUpperCase()) {
            case "SUN" -> lookAtSun = true;
            case "EARTH" -> lookAtEarth = true;
            case "SHIP" -> lookAtSpaceship = true;
            case "TITAN" -> lookAtTitan = true;
            case "EVERYTHING" -> lookAtEverything = true;
        }
    }
    private void printPositions(){
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
    }

    private void setSelectorProperties(TextField field, int changeX, int changeY){
        field.setLayoutX((double) (ScreenWIDTH - changeX) / 2);
        field.setLayoutY((double) (ScreenHEIGHT - changeY) / 2);
    }

    private int timeInDays(double time) {
        return (int) (((time / 60) / 60) / 24);
    }

}