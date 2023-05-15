package com.example.planets;

import com.example.planets.BackEnd.Models.*;
import com.example.planets.BackEnd.NumericalMethods.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;


class NumericalExperiments {

    /*
    ToDo
    + finish setting up the engine for easy use and implementation
    + write documentation
    + make a test folder and add folders inside with separated test cases for everything in here (pain)
    + make lightweight versions of the classes that are extented by heavier ones we use normally
    + fix AB2 to use same logic as F/_RK2

    make time to be saved per model only, not static in celestial body

    change time to run instead of in amount of days, to be in hours

    change the way we chose initial conidtions to one that matches earths coordinate system in angles
     */


    public static void main(String[] args) {
        // engineTest()

        comparingToEachOther();
    }

    public static void engineTest() {
        /*
        +write a method to add points to the planning so the engine can be tested with these
         */

    }

    // change so that each solver can have its own dt and display it as well
    public static void comparingToEachOther() {
        //experiment setup hyper parameters
        double time = 30;
        boolean isDay = true;
        int checkInterval = 1; //every how many days do you want it to print the values
        final int MARS = 4;

        // benchmark model
        Model3D benchmark = new Gravity0( 0, Math.PI / 2.0, new RK2() );
        double benchmarkPrecision = 0.01/2;


        //  testing models
        ArrayList<Model3D> models = new ArrayList<Model3D>();
        ArrayList<Double> steps = new ArrayList<Double>();

        // add a model with the solver and next the step size its used with it
        //models.add( new Gravity0( 0, Math.PI / 2.0, new Euler() ) );
        //steps.add( 0.1 );

        //models.add( new Gravity0( 0, Math.PI / 2.0, new FastRK2() ) );
        //steps.add( 0.1 );

        models.add( new Gravity0( 0, Math.PI / 2.0, new RK4() ) );
        steps.add( 0.5 );


        //test details
        System.out.println("target planet: MARS");
        System.out.println("benchmark precision: " + benchmarkPrecision);
        System.out.println("benchmark model: " + benchmark.getSolverName() + "\n");

        double[][] errors = new double[models.size()][3];
        double[] chrono = new double[models.size()+1]; //last index is the benchmark

        //using mars
        for (int i = 0; i < time; i++) {
            //benchmark precision
            chrono[ chrono.length-1 ] = System.currentTimeMillis();
            benchmark.updatePos(1, benchmarkPrecision, isDay);
            chrono[ chrono.length-1 ] = System.currentTimeMillis() - chrono[ chrono.length-1 ];

            // update all models positions
            for (int j=0; j<models.size(); j++) {
                //start count of how much each model took here
                chrono[j] = System.currentTimeMillis();
                models.get(j).updatePos(1, steps.get(j), isDay);
                //end count of how long each model here
                chrono[j] = System.currentTimeMillis() - chrono[j];
            }


            //calculate errors
            for (int j = 0; j < models.size(); j++) {
                errors[j][0] = benchmark.getBody(MARS).getPos()[0] - models.get(j).getBody(MARS).getPos()[0];
                errors[j][1] = benchmark.getBody(MARS).getPos()[1] - models.get(j).getBody(MARS).getPos()[1];
                errors[j][2] = benchmark.getBody(MARS).getPos()[2] - models.get(j).getBody(MARS).getPos()[2];
            }


            //prints
            if ((i + 1) % checkInterval == 0) {
                System.out.println("Day: " + (i + 1) + "\n");

                for (int j = 0; j < models.size(); j++) {
                    System.out.println( models.get(j).getSolverName() );
                    System.out.println("Execution time: " + chrono[j] + "ms");
                    System.out.println("step size: " + steps.get(j) + "s");
                    System.out.println("Error= X: " + errors[j][0] + "; Y: " + errors[j][1] + "; Z: " + errors[j][2] + "\n");

                }

                //print benchmark data here
                System.out.println("Benchmark time: " + chrono[ chrono.length-1 ] + "ms");
                System.out.println("in sim time: " + Math.round( benchmark.getTime() ) + "s");
                System.out.println("\n");
            }


        }


        }



        public static void excelTest() throws IOException {
            Gravity0 grav = new Gravity0(new Euler());


            FileInputStream file = new FileInputStream(new File(
                    "C:\\Users\\User\\Documents\\Div\\Toon09-Crew17_spaceMission\\src\\main\\java\\com\\example\\planets\\innit_Pos.xlsx"));
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            int rows = sheet.getLastRowNum() + 1;
            int columns = sheet.getRow(0).getLastCellNum();
            String[][] data = new String[rows][columns];
            int i = 0;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                int j = 0;

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    data[i][j] = cell.toString();
                    System.out.print(data[i][j] + " ");
                    j++;
                }

                i++;
            }

            workbook.close();

            Double[][] data2 = new Double[rows][columns];
            for(int k = 0; k < rows; k++) {
                for (int l = 0; l < columns; l++) {
                    data2[k][l] = Double.parseDouble(data[k][l]);
                }
            }

        }

    public static double[][] LineGetData(int lineSelected, String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = null;
            int currentLineNumber = 1;
            while ((line = reader.readLine()) != null) {
                if (currentLineNumber == lineSelected) {
                    double X = Double.parseDouble(line.substring(51,73));
                    double Y = Double.parseDouble(line.substring(75,97));
                    double Z = Double.parseDouble(line.substring(99,121));
                    double VX = Double.parseDouble(line.substring(123,145));
                    double VY = Double.parseDouble(line.substring(147,169));
                    double VZ = Double.parseDouble(line.substring(171,193));
                    double [][] positionAndVelocity = {{X,Y,Z},{VX,VY,VZ}};
                    return positionAndVelocity;
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
