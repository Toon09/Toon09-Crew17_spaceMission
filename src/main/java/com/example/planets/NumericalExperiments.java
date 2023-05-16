package com.example.planets;

import com.example.planets.BackEnd.Models.*;
import com.example.planets.BackEnd.NumericalMethods.*;
import com.example.planets.Data.DataGetter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


class NumericalExperiments {

    /*
    ToDo
    + finish setting up the engine for easy use and implementation
    + write documentation
    + make a test folder and add folders inside with separated test cases for everything in here (pain)
    + make lightweight versions of the classes that are extended by heavier ones we use normally
    + fix AB2 to use same logic as F/_RK2
    + make class dataGetter & have it use different data getting classes depending on termination of String (txt, xlsx, etc)

    + https://youtu.be/l_iZk4n5QFU?t=569 [ hohmann transfer theory ]
    + https://youtu.be/l_iZk4n5QFU?t=786 [ arbitrary impulse transfer ]

    change time to run instead of in amount of days, to be in hours

    change the way we chose initial conditions to one that matches earths coordinate system in angles

    //for adaptive method
    // https://youtu.be/6bHdFef1S60
    // https://en.m.wikipedia.org/wiki/Adaptive_step_size

    Runge Kutta Fehlberg (famous adaptive rk)
    Cash - Karp rk fehlberg -> rk4&5

    // RK6: https://youtu.be/soEj7YHrKyE

    // https://youtube.com/playlist?list=PLYdroRCLMg5PhZqzEJJlyLo55-1Vdd4Bd [numerical methods]
    // https://youtube.com/playlist?list=PLOIRBaljOV8je0oxFAyj2o6YLXcBX1rTZ [rocket trajectory]
    // https://youtu.be/l_iZk4n5QFU [1h lecture on trajectory planning]

    change for loop to be inside the step func in NumSolver so the step ize change of of dormant prince doesnt destroy anything
    have the length of execution be a parameter

    -implement dormant prince with for loop inside & just change the dt you already have
     */

    // https://ssd.jpl.nasa.gov/horizons/app.html#/ [ experiment data ]
    public static void main(String[] args) {
        // engineTest()

        // comparingToEachOther();

        DataGetter data = new DataGetter();

        double[][] temp = data.getTxtExpData(0, "src/main/java/com/example/planets/Data/Mars_Data.txt");

        System.out.println( Arrays.deepToString(temp) );
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
        Model3D benchmark = new Gravity0( 0, Math.PI / 2.0, new RK4() );
        double benchmarkPrecision = 1;


        //  testing models
        ArrayList<Model3D> models = new ArrayList<Model3D>();
        ArrayList<Double> steps = new ArrayList<Double>();

        // add a model with the solver and next the step size its used with it
        //models.add( new Gravity0( 0, Math.PI / 2.0, new Euler() ) );
        //steps.add( 0.1 );

        models.add( new Gravity0( 0, Math.PI / 2.0, new AB2() ) );
        steps.add( 0.1 );


        //test details
        System.out.println("target planet: MARS");
        System.out.println("benchmark precision: " + benchmarkPrecision + "s");
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
                    System.out.println("Sim time: " + models.get(j).getTime() + "s");
                    System.out.println("step size: " + steps.get(j) + "s");
                    System.out.println("Error= X: " + errors[j][0] + "; Y: " + errors[j][1] + "; Z: " + errors[j][2] + "\n");

                }

                //print benchmark data here
                System.out.println("Benchmark time: " + chrono[ chrono.length-1 ] + "ms");
                System.out.println("in sim time: " + Math.round( benchmark.getTime() ) + "s");
                System.out.println("benchmark step size: " + benchmarkPrecision + "s");
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


}
