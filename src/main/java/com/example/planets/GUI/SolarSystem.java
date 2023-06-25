package com.example.planets.GUI;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
public class SolarSystem {
    private static final int scale = 25;

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
        setPosition(rocketBase);
        rocketBase.setTranslateX(rocketBase.getTranslateX() + earth.getRadius() + 100);
        PhongMaterial rocketBaseMaterial = new PhongMaterial();
        rocketBaseMaterial.setDiffuseMap(new Image(("crew17.jpeg")));
        rocketBase.setMaterial(rocketBaseMaterial);

        // create the path of the rocket
        Cylinder rocketPath = new Cylinder(200, 2000);
        setPosition(rocketPath);
        rocketPath.setRotate(90);
        rocketPath.setTranslateX(rocketPath.getTranslateX() + earth.getRadius() + 100);
        PhongMaterial rocketPathMaterial = new PhongMaterial();
        rocketPathMaterial.setDiffuseColor(Color.GOLD);
        rocketPath.setMaterial(rocketPathMaterial);
        group.getChildren().addAll(sun, mercury, venus, earth, moon, mars, jupiter, saturn, titan, neptune, uranus, rocketBase, rocketPath);

        return group;
    }
    private void setPosition(Sphere sphere, int index) {
        sphere.setTranslateX(positions[index][0] / scale);
        sphere.setTranslateY(positions[index][1] / scale);
        sphere.setTranslateZ(positions[index][2] / scale);
    }

    private void setPosition(Box box) {
        box.setTranslateX(positions[3][0] / scale);
        box.setTranslateY(positions[3][1] / scale);
        box.setTranslateZ(positions[3][2] / scale);
    }

    private void setPosition(Cylinder cylinder) {
        cylinder.setTranslateX(positions[3][0] / scale);
        cylinder.setTranslateY(positions[3][1] / scale);
        cylinder.setTranslateZ(positions[3][2] / scale);
    }


    private static final double[][] positions = {{0, 0, 0}, {7.83e6, 4.49e7, 2.87e6}, {-2.82e7, 1.04e8, 3.01e6},
            {-1.48e8, -2.78e7, 3.37e4}, {-1.48e8, -2.75e7, 7.02e4}, {-1.59e8, 1.89e8, 7.87e6},
            {6.93e8, 2.59e8, -1.66e7}, {1.25e9, -7.60e8, -3.67e7}, {1.25e9, 7.61e8, -3.63e7},
            {4.45e9, -3.98e8, -9.45e7}, {1.96e9, 2.19e9, -1.72e7}};


}
