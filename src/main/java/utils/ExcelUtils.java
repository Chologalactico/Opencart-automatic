package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {

    public static List<String[]> leerExcel(String filePath, String sheetName) {
        List<String[]> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            for (Row row : sheet) {
                int cols = row.getLastCellNum();
                String[] rowData = new String[cols];
                for (int i = 0; i < cols; i++) {
                    Cell cell = row.getCell(i);
                    rowData[i] = cell != null ? cell.toString() : "";
                }
                data.add(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void escribirExcel(String filePath, String sheetName, List<String[]> data) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i);
                String[] rowData = data.get(i);
                for (int j = 0; j < rowData.length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(rowData[j]);
                }
            }
            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
