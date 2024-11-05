package com.api.solver;

import com.api.solver.xlutil.ExcelUtil;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.FilerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExcelReaderTests {



    @Test
    void testExcelReader() throws IOException {
        ExcelUtil excelUtil = new ExcelUtil();

        String fileName = "/excel/chemicals.xlsx";


        ExcelUtil excelUtil1 = new ExcelUtil(fileName, 0);

       Map<Integer, List<String>> data = excelUtil1.getData();
        System.out.println(data.get(0));
    }
}
