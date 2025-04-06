package testpackage;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

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

    public static void csvOut(TableModel tableModel) {
        String fileName = "output.csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            int columnCount = tableModel.getColumnCount();
            int rowCount = tableModel.getRowCount();

            // Writing headers
            for (int col = 0; col < columnCount; col++) {
                writer.append(tableModel.getColumnName(col));
                if (col < columnCount - 1) {
                    writer.append(",");
                }
            }
            writer.append("\n");

            // Writing rows
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < columnCount; col++) {
                    Object value = tableModel.getValueAt(row, col);
                    writer.append(value != null ? value.toString() : "");
                    if (col < columnCount - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
            writer.flush();
            System.out.println("CSV file generated successfully: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void csvToTableModel(DefaultTableModel tableModel, String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isHeader = tableModel.getColumnCount() == 0; // Only set headers if model is empty
            TableModelListener[] tl = tableModel.getTableModelListeners();
            for (TableModelListener listener : tl) {
                tableModel.removeTableModelListener(listener);
            }
            tableModel.setRowCount(0);
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (i == 0) {
                    i++;
                    continue;
                }
                String[] values = line.split(",", -1); // Split by comma, preserve empty values

                if (isHeader) {
                    for (String columnName : values) {
                        System.out.println("header");
                        tableModel.addColumn(columnName.trim());
                    }
                    isHeader = false;
                } else {
                    // Add row data

                    Vector<String> row = new Vector<>();
                    for (String value : values) {
                        row.add(value.trim().isEmpty() ? "" : value.trim());
                    }

                    tableModel.addRow(row);

                }
            }
            for (TableModelListener listener : tl) {
                tableModel.addTableModelListener(listener);
            }
            for (Vector row : tableModel.getDataVector()) {
                for (Object data : row) {
                    System.out.print(data + "  ");
                }
                System.out.println();
            }
            System.out.println("success");
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

    public static LocalDateTime parseDate(String dateStr) {
        if(dateStr.isEmpty())return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        LocalDate inputDate = LocalDate.parse(dateStr, formatter);
        LocalDate today = LocalDate.now();

        if (inputDate.equals(today)) {
            return LocalDateTime.now();
        } else {
            return LocalDateTime.of(inputDate, LocalTime.of(13, 0)); // 1 PM
        }
    }

    public static void generateAndAddDates(JComboBox<String> comboBox) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");

        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusYears(1);

        // Clear any existing items
        if (comboBox != null) {
            comboBox.removeAllItems();

            // Add dates from today to 1 year ago
            for (LocalDate date = today; !date.isBefore(oneYearAgo); date = date.minusDays(1)) {
                comboBox.addItem(date.format(formatter));
            }
        }else Thread.dumpStack();
    }
}
