package com.api.solver.xlutil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ExcelUtil {
    private String fileName;

    private int sheetIndex;


    public Map<Integer, List<String>>  getData() throws IOException {
        File xlfile = new File(getClass().getResource(fileName).getFile());
        FileInputStream file = new FileInputStream(xlfile.getAbsolutePath());
        Workbook workbook = new XSSFWorkbook(file);
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
}
