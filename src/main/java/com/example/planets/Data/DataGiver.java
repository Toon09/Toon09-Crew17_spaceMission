package com.example.planets.Data;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import com.example.planets.BackEnd.CelestialEntities.CelestialBody;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
public class DataGiver {
    //
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//





        public DataGiver() {
        }

        public static void main(String[] args) throws IOException {
            String[][] data = Data("C:\\Users\\User\\Documents\\Div\\Toon09-Crew17_spaceMission\\src\\main\\java\\com\\example\\planets\\innit_Pos.xlsx");


        }

        public static String[][] Data(String xs) throws IOException {
            FileInputStream file = new FileInputStream(new File(xs));
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            int rows = sheet.getLastRowNum() + 1;
            int columns = sheet.getRow(0).getLastCellNum();
            String[][] data = new String[rows][columns];

            for(int i = 0; rowIterator.hasNext(); ++i) {
                Row row = (Row)rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                for(int j = 0; cellIterator.hasNext(); ++j) {
                    Cell cell = (Cell)cellIterator.next();
                    data[i][j] = cell.toString();
                }
            }

            System.out.println(data[0][0]);
            workbook.close();
            file.close();
            return data;
        }
    public  static CelestialBody[] GetP(String xs) throws IOException {
        String[][] data = Data(xs);

            CelestialBody[] P = new CelestialBody[data.length];
            for (int i = 0; i < data.length; i++) {
                P[i] = new CelestialBody(
                        data[i+1][0],
                        data[i+1][1],
                        data[i+1][2],
                        data[i+1][3],
                        data[i+1][4],
                        data[i+1][5],
                        data[i+1][6],
                        data[i+1][7]);


            }
            return P;
    }}



