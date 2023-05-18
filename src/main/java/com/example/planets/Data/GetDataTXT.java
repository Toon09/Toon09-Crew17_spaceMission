package com.example.planets.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GetDataTXT {

    public double[][] getData(int lineSelected, String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = null;
            int currentLineNumber = 0; // first line after all the summary is done and the data starts
            while ((line = reader.readLine()) != null) {
                if (currentLineNumber == lineSelected + 26) {
                    double X = Double.parseDouble(line.substring(51,73));
                    double Y = Double.parseDouble(line.substring(75,97));
                    double Z = Double.parseDouble(line.substring(99,121));
                    double VX = Double.parseDouble(line.substring(123,145));
                    double VY = Double.parseDouble(line.substring(147,169));
                    double VZ = Double.parseDouble(line.substring(171,193));
                    return new double[][]{{X,Y,Z},{VX,VY,VZ}};
                }
                currentLineNumber++;
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return null;
    }

}
