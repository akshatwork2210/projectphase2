package testpackage;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestClass {
    public static void writeTableToExcel(JTable table, String filename) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Table Data");

        TableModel model = table.getModel();

        // Write column headers
        Row headerRow = sheet.createRow(0);
        for (int col = 0; col < model.getColumnCount(); col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(model.getColumnName(col));
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        // Write table data
        for (int row = 0; row < model.getRowCount(); row++) {
            Row excelRow = sheet.createRow(row + 1);
            for (int col = 0; col < model.getColumnCount(); col++) {
                Cell cell = excelRow.createCell(col);
                Object value = model.getValueAt(row, col);
                if (value != null) {
                    cell.setCellValue(value.toString());
                }
            }
        }

        // Auto-size columns for better readability
        for (int col = 0; col < model.getColumnCount(); col++) {
            sheet.autoSizeColumn(col);
        }

        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream(filename)) {
            workbook.write(fileOut);
            System.out.println("Excel file saved as: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        // Data to be written
        String[][] foodData = {
                {"Item No.", "Item Name", "Quantity", "Unit Price", "Total Price"},
                {"1", "Burger", "2", "5.99", "11.98"},
                {"2", "Fries", "1", "2.99", "2.99"},
                {"3", "Soda", "2", "1.49", "2.98"},
                {"4", "Pizza", "1", "12.99", "12.99"},
                {"5", "Ice Cream", "3", "3.49", "10.47"},
                {"", "Total", "", "", "41.41"}
        };

        // Create a new Excel workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Food Bill");

        // Write data to the sheet
        for (int i = 0; i < foodData.length; i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < foodData[i].length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(foodData[i][j]);
            }
        }

        // Save the Excel file
        try (FileOutputStream fileOut = new FileOutputStream("FoodBill.xlsx")) {
            workbook.write(fileOut);
            System.out.println("Excel file 'FoodBill.xlsx' created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
