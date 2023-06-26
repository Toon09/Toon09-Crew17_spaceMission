package com.example.planets.GUI;

import com.example.planets.BackEnd.Models.Gravity0;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class GUIsupport {

    public static void printPositions(Gravity0 model, double scale) {
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

    private static void setTextProperties(Text field, int changeY, double ScreenWIDTH, double ScreenHEIGHT) {
        field.setFont(Font.font("Arial", 16));
        field.setFill(Color.WHITE);
        field.setLayoutX((double) (ScreenWIDTH - 100) / 2);
        field.setLayoutY((double) (ScreenHEIGHT + changeY) / 2);
    }

    private static void setTextProperties(Text field, int changeY, Scene scene) {
        field.setFill(Color.WHITE);
        field.setFont(Font.font("Arial", 16));
        field.setTextAlignment(TextAlignment.RIGHT);
        field.setTranslateX(scene.getWidth() - 730);
        field.setTranslateY(scene.getHeight() + changeY);
    }
}
