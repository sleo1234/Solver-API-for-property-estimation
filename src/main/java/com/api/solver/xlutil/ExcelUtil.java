package com.api.solver.xlutil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ExcelUtil {
    private String fileName;


    private int sheetIndex;



    public Workbook getAbsolutePath() throws IOException{
        File xlfile = new File(getClass().getResource(fileName).getFile());
        FileInputStream file = new FileInputStream(xlfile.getAbsolutePath());
        Workbook workbook = new XSSFWorkbook(file);
        return workbook;
    }

    public Map<Integer, List<String>>  getData() throws IOException {
        Workbook workbook=getAbsolutePath();
        Sheet sheet = workbook.getSheetAt(sheetIndex);

        Map<Integer, List<String>> data = new HashMap<>();
        int i = 0;
        for (Row row : sheet) {
            data.put(i, new ArrayList<String>());
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING:
                        data.get(Integer.valueOf(i)).add(cell.getRichStringCellValue().getString());
                        break;
                    case NUMERIC:
                        break;
                    case BOOLEAN:
                        break;
                    case FORMULA:
                        break;
                    default: data.get(Integer.valueOf(i)).add(" ");
                }
            }
            i++;
        }



        return data;
    }



    public void readByCol(int colIndex) throws IOException {

        Workbook workbook = getAbsolutePath();

        Sheet sheet = workbook.getSheetAt(sheetIndex);
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                int columnIndex = cell.getColumnIndex();
                if (columnIndex == colIndex) {


                switch (cell.getCellType()) {
                    case STRING:
                        System.out.print(cell.getStringCellValue() + "\t\t\t");
                        break;
                    case NUMERIC:
                        System.out.print(cell.getNumericCellValue() + "\t\t\t");
                        break;
                    case BOOLEAN:
                        System.out.print(cell.getBooleanCellValue() + "\t\t\t");
                        break;
                    case FORMULA:
                        System.out.print(cell.getCellFormula() + "\t\t\t");
                        break;
                    default:
                }
            }
            }
            System.out.println("");
        }



    }




    public void getData(int n) throws IOException {

        Workbook workbook = getAbsolutePath();

        Sheet sheet = workbook.getSheetAt(sheetIndex);
        Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                for (int i=0; i<n; i++) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();


                    if (columnIndex == i) {
                    switch (cell.getCellType()) {
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t\t\t");
                            break;
                        case NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t\t\t");
                            break;
                        case BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t\t\t");
                            break;
                        case FORMULA:
                            System.out.print(cell.getCellFormula() + "\t\t\t");
                            break;
                        default:
                    }

                }
                }
                System.out.println("");
            }


        }


      return ;
    }


    public <T> List<T> dfByCol(int colIndex) throws IOException {
        List<T> data = new ArrayList<>();
        Workbook workbook = getAbsolutePath();

        Sheet sheet = workbook.getSheetAt(sheetIndex);
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                int columnIndex = cell.getColumnIndex();
                if (columnIndex == colIndex) {


                    switch (cell.getCellType()) {
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t\t\t");
                            data.add((T) cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t\t\t");
                            data.add( (T) Double.valueOf(cell.getNumericCellValue()));
                            break;
                        case BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t\t\t");
                            data.add((T) Boolean.valueOf(cell.getBooleanCellValue()));
                            break;
                        case FORMULA:
                            System.out.print(cell.getCellFormula() + "\t\t\t");
                            data.add((T) cell.getCellFormula());
                            break;
                        default:
                    }
                }
            }
            System.out.println("");
        }


     return data;
    }





}
