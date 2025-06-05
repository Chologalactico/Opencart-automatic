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
            boolean esPrimeraFila = true;

            for (Row row : sheet) {
                if (esPrimeraFila) {
                    esPrimeraFila = false;
                    continue;
                }

                int cols = row.getLastCellNum();
                String[] rowData = new String[cols];
                boolean filaValida = false;

                for (int i = 0; i < cols; i++) {
                    Cell cell = row.getCell(i);
                    String valor = limpiarValorCelda(cell);
                    rowData[i] = valor;

                    // Verificamos si la primera celda (nombre del producto) no está vacía
                    if (i == 0 && !valor.isEmpty()) {
                        filaValida = true;
                    }
                }

                if (filaValida) {
                    data.add(rowData);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    // Limpieza de .0 y nulos
    private static String limpiarValorCelda(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return "";
        }
        String valor = cell.toString().trim();
        if (valor.matches("^-?\\d+\\.0$")) {
            valor = valor.substring(0, valor.length() - 2);
        }
        return valor;
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
