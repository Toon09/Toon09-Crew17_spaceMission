package com.example.planets;

import com.example.planets.BackEnd.CelestialBody;
import com.example.planets.BackEnd.Models.*;
import com.example.planets.BackEnd.NumericalMethods.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


class NumericalExperiments {

    /*
     * a simple experiment set up to check how things work so far, before improvements
     */
    public static void main(String[] args) throws IOException {
        //experiment setup hyper parameters
        double time = 5;
        boolean isDay = true;
        int checkInterval = 1; //every how many days do you want it to print the values
        final int MARS = 4;

        //  testing models
        ArrayList<Model3D> models = new ArrayList<Model3D>();
        //models.add( new Gravity0( new Eulers() ) );

        //new Gravity0(0, Math.PI / 2.0, new double[]{11, 11, 0}, new RK2());

        models.add( new Gravity0(0, Math.PI / 2.0, new double[]{11, 11, 0}, new RK2()) );
        models.add( new Gravity0(new AB2()) );


        // benchmark model
        Model3D benchmark = new Gravity0(new RK2());

        double testDt = 0.1;
        double benchmarkPrecision = 0.1;

        //test details
        System.out.println("target planet: MARS");
        System.out.println("test precision: " + testDt);
        System.out.println("benchmark precision: " + benchmarkPrecision);
        System.out.println("benchmark model: " + benchmark.getSolverName() + "\n");

        double[][] errors = new double[models.size()][3];

        //using mars
        for (int i = 0; i < time; i++) {
            //benchmark precision
            benchmark.updatePos(1, benchmarkPrecision, isDay);

            // update all models positions
            for (int j = 0; j < models.size(); j++) {
                models.get(j).updatePos(1, testDt, isDay);
            }


            //calculae errors
            for (int j = 0; j < models.size(); j++) {
                errors[j][0] = benchmark.getBody(MARS).getPos()[0] - models.get(j).getBody(MARS).getPos()[0];
                errors[j][1] = benchmark.getBody(MARS).getPos()[1] - models.get(j).getBody(MARS).getPos()[1];
                errors[j][2] = benchmark.getBody(MARS).getPos()[2] - models.get(j).getBody(MARS).getPos()[2];
            }


            //prints
            if ((i + 1) % checkInterval == 0) {
                System.out.println("Day: " + (i + 1) + "\n");

                for (int j = 0; j < models.size(); j++) {
                    System.out.println(models.get(j).getSolverName() + "\nError= X: " + errors[j][0] + "; Y: " + errors[j][1] + "; Z: " + errors[j][2] + "\n");
                }

                System.out.println("\n\n");
            }


        }


        }
        public static void excelTest() throws IOException {
            Gravity0 grav = new Gravity0();


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
