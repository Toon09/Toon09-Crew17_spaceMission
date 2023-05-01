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
import java.util.Iterator;

//

class NumericalExperiments {

    /*
     * a simple experiment set up to check how things work so far, before improvements
     */
    public static void main(String[] args) throws IOException {
        testing3D();
    }

    /*
     * hohmann for going from planet to planet
     */

    public  static void testHohmann(){
        Gravity0 grav = new Gravity0();


    }
    public static void testing3D() throws IOException {
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
        for (int k = 0; k < rows; k++) {
            for (int l = 0; l < columns; l++) {
                data2[k][l] = Double.parseDouble(data[k][l]);
            }
        }




    /*
     * write names of planets
     * write their radiuses as well
     */


}
        //System.out.println(Gravity0.positions.length);
        //CelestialBody[] bodies = new CelestialBody[ Gravity0.positions.length ];
        //for(int i=0; i<bodies.length; i++){
        //    bodies[i] = new CelestialBody(Gravity0.names[i], Gravity0.mass[i][0], Gravity0.positions[i], Gravity0.velocity[i]);
        //}

        //grav.addBody(bodies);



        // F_g = Gm1m2/dist^2 = 0

        //Vesc = sqrt( 2GM_titan/r_titan&rock )


        ////////////////////////////// parameters
        double dt = 1;
        long days = 10;
        //earth is index 3

        //with any dt days/execution time ratio remains mostly constant
        //time to execution time ratio is approx 1.6666 for dt=0.1
        



    public static void testing1D(){
        Model1D simple = new simple2(0, 8, 1);

        int size = 1000;
        double dx = 0.001;
        
        double[][] errors = new double[2][size];

        //test
        System.out.println("approx");
        for(int i=0; i<size; i++){
            errors[0][i] = Eulers._2DegStep1D(simple, dx);
            System.out.println(simple.getX() + ", " + errors[0][i]);
        }
        System.out.println("\n");


        //solution for test
        System.out.println("solution: y(x) = 3*e^t-2"); 
        //found with walfram alpha
        // https://www.wolframalpha.com/input?i=y%27+%3D+2+%2B+y%2C+y%280%29+%3D+1

        double x = 0;
        for(int i=0; i<size; i++){
            x+=dx;
            errors[1][i] = 7 * Math.exp(x) - 6;
            //System.out.println( x + ", " + errors[1][i] );
        }
        System.out.println("\n");


        //error finding
        x = 0;
        System.out.println("Errors");
        for(int i=0; i<size; i++){
            //System.out.println( x + ", " + (errors[0][i] - errors[1][i])/errors[1][i] );
            x+=dx;
        }
    }


    /*
     * write names of planets
     * write their radiuses as well
     */


}
