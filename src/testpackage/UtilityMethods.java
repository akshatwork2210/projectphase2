package testpackage;

import mainpack.MyClass;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.print.*;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UtilityMethods {
    public static final int VERTI_SPLIT = 0;
    public static final int HORIZONTAL_SPLIT = 1;
    //Thread printingThread;
    public static Thread printingThread;
    public final static int ORDER_SLIP = 0;
    public static final int CUSTOMER_BILL = 1;

    public static final BlockingQueue<Runnable> printQueue = new LinkedBlockingQueue<>();

    //    public static int[] balance(int billId) {
//        int[] balances = new int[2]; // [0] = previous balance, [1] = next balance
//
//        try {
//            Connection con =MyClass.C;
//            // Step 1: Get bill date and customer
//            PreparedStatement ps1 = con.prepareStatement("SELECT date, customer_name FROM bills WHERE BillID = ?");
//            ps1.setInt(1, billId);
//            ResultSet rs1 = ps1.executeQuery();
//
//            if (!rs1.next()) return balances; // Bill not found
//
//            java.sql.Timestamp billDate = rs1.getTimestamp("date");
//            String customer = rs1.getString("customer_name");
//
//            // Step 2: Calculate total payments and bills before this bill
//            PreparedStatement ps2 = con.prepareStatement("SELECT IFNULL(SUM(amount), 0) AS total FROM transactions WHERE customer_name = ? AND date < ?");
//            ps2.setString(1, customer);
//            ps2.setTimestamp(2, billDate);
//            ResultSet rs2 = ps2.executeQuery();
//            rs2.next();
//            double paymentsBefore = rs2.getDouble("total");
//
//            PreparedStatement ps3 = con.prepareStatement("SELECT IFNULL(SUM(amount), 0) AS total FROM bills WHERE customer_name = ? AND date < ?");
//            ps3.setString(1, customer);
//            ps3.setTimestamp(2, billDate);
//            ResultSet rs3 = ps3.executeQuery();
//            rs3.next();
//            double billsBefore = rs3.getDouble("total");
//
//            // Step 3: Calculate total payments and bills up to and including this bill
//            PreparedStatement ps4 = con.prepareStatement("SELECT IFNULL(SUM(amount), 0) AS total FROM transactions WHERE customer_name = ? AND date <= ?");
//            ps4.setString(1, customer);
//            ps4.setTimestamp(2, billDate);
//            ResultSet rs4 = ps4.executeQuery();
//            rs4.next();
//            double paymentsUpto = rs4.getDouble("total");
//
//            PreparedStatement ps5 = con.prepareStatement("SELECT IFNULL(SUM(amount), 0) AS total FROM bills WHERE customer_name = ? AND date <= ?");
//            ps5.setString(1, customer);
//            ps5.setTimestamp(2, billDate);
//            ResultSet rs5 = ps5.executeQuery();
//            rs5.next();
//            double billsUpto = rs5.getDouble("total");
//
//            // Step 4: Compute balances
//            balances[0] = (int) Math.round(paymentsBefore - billsBefore); // Previous Balance
//            balances[1] = (int) Math.round(paymentsUpto - billsUpto);     // Next Balance
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return balances;
//    }
    public static double[] balance(int billID) {
        double lastbillTotal = 0;

        String billQuery = "select billid from bills where customer_name=(select customer_name from bills where billid=?) and date <= (select date from bills where billid=?) and billid<?";
//        String transactionQuery="Select *from transactions where date<=()"
        String transactionQuery = "SELECT sum(amount) FROM transactions " +
                "WHERE customer_name = (SELECT customer_name FROM bills WHERE billid = ?) " +
                "AND (date <=(SELECT date FROM bills WHERE billid = ?) and (billid<?))";
        double grandTotalBill = 0;//contains total till previous bills
        try {
            PreparedStatement statement = MyClass.C.prepareStatement(billQuery);
            ArrayList<Integer> billIDList = new ArrayList<>();
            statement.setInt(1, billID);
            statement.setInt(2, billID);
            statement.setInt(3, billID);
            ResultSet rs = statement.executeQuery();
            StringBuilder placeholders = new StringBuilder();
            while (rs.next()) {
                billIDList.add(rs.getInt(1));
                placeholders.append(rs.getInt(1)).append(",");
            }
            placeholders.append(billID);

            billQuery = "SELECT " +
                    "SUM((" +
                    "(IFNULL(LabourCost, 0) + " +
                    "IFNULL(DullChillaiCost, 0) + " +
                    "IFNULL(MeenaColorMeenaCost, 0) + " +
                    "IFNULL(RhodiumCost, 0) + " +
                    "IFNULL(NagSettingCost, 0) + " +
                    "IFNULL(OtherBaseCosts, 0) + " +
                    "IFNULL(RawCost, 0)) * IFNULL(quantity, 0)" +
                    ") + (IFNULL(GoldRate, 0) * IFNULL(GoldPlatingWeight, 0))) AS total, " +
                    "SUM(TotalFinalCost),billid " +
                    "FROM billdetails " +
                    "WHERE billid in ( " + placeholders + ")" +
                    "GROUP BY billid;";
            System.out.println("placeholders are" + placeholders);
            statement.close();
            if (!placeholders.isEmpty()) {
                statement = MyClass.C.prepareStatement(billQuery);
//            statement.setInt(1, billID);
                rs.close();
                rs = statement.executeQuery();
                lastbillTotal = 0;
                while (rs.next()) {
                    if (rs.getInt(3) == billID) lastbillTotal += rs.getDouble(1);
                    grandTotalBill += rs.getDouble(1);
                    System.out.println(rs.getDouble(1) + "   from sql" + rs.getDouble(2));
                }
            }
            statement.close();
            statement = MyClass.C.prepareStatement(transactionQuery);
            statement.setInt(1, billID);
            statement.setInt(2, billID);
            statement.setInt(3, billID);
            rs.close();
            rs = statement.executeQuery();
            double transactionSum = 0;
            if (rs.next()) {
                transactionSum = rs.getDouble(1);
                System.out.println("transaction sum is " + rs.getInt(1));
            }
            System.out.println("\n\n\ntotal is " + grandTotalBill);
            double[] result = new double[2];
            result[0] = grandTotalBill - lastbillTotal - transactionSum;
            result[1] = grandTotalBill - transactionSum;

            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

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
        if (dateStr.isEmpty()) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        LocalDate inputDate = LocalDate.parse(dateStr, formatter);
        LocalDate today = LocalDate.now();

        if (inputDate.equals(today)) {
            return LocalDateTime.now();
        } else {
            return LocalDateTime.of(inputDate, LocalTime.of(13, 0)); // 1 PM
        }
    }

    public static void generateAndAddDates(JComboBox<String> comboBox, boolean headerrow) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");

        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusYears(1);

        // Clear any existing items
        if (comboBox != null) {
            comboBox.removeAllItems();

            // Add dates from today to 1 year ago
            if (headerrow) comboBox.addItem("Select date");
            for (LocalDate date = today; !date.isBefore(oneYearAgo); date = date.minusDays(1)) {
                comboBox.addItem(date.format(formatter));
            }
        } else Thread.dumpStack();
    }

    public static void printStartUp() {
        PrinterJob.getPrinterJob(); // triggers internal loading
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.getAllFonts(); // forces font rasterizer to load
    }

    public static void generateAndAddNames(JComboBox<String> comboBox) {
        if (comboBox == null) return;
        comboBox.removeAllItems();
        comboBox.addItem("Select Customer");
        Statement stmt1 = null;
        ResultSet rs = null;
        try {
            stmt1 = MyClass.C.createStatement();
            rs = stmt1.executeQuery("SELECT customer_name FROM customers");
            while (rs.next()) comboBox.addItem(rs.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt1 != null)
                    stmt1.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static DecimalDocumentFilter getDocFilter() {
        return new DecimalDocumentFilter();
    }

    public static TableModelListener[] removeModelListener(DefaultTableModel tableModel) {

        TableModelListener[] listeners = tableModel.getTableModelListeners();
        for (TableModelListener listener : listeners) {


            tableModel.removeTableModelListener(listener);
        }
        return listeners;
    }

    public static void addModelListeners(TableModelListener[] listeners, DefaultTableModel model) {
        for (TableModelListener listener : listeners) {
            model.addTableModelListener(listener);
        }
    }

    private static void printModel(DefaultTableModel model, String title) {
        PrinterJob job = PrinterJob.getPrinterJob();
//
        Printable printable = new Printable() {
            @Override
            public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) return NO_SUCH_PAGE;

                Graphics2D g2d = (Graphics2D) g;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                int x = 0;
                int y = 0;
                int rowHeight = 20;
                int padding = 5;

                double printableWidth = pageFormat.getImageableWidth();
                int colCount = model.getColumnCount();
                int[] colWidths = new int[colCount];

                // Font setup
                java.awt.Font headerFont = new java.awt.Font("Arial", java.awt.Font.BOLD, 12);
                java.awt.Font normalFont = new java.awt.Font("Arial", java.awt.Font.PLAIN, 12);
                g.setFont(normalFont);
                FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);

                // Fixed width for "SNo"
                colWidths[0] = 50;
                int usedWidth = colWidths[0];

                // Estimate width for other columns
                for (int col = 1; col < colCount; col++) {
                    int maxWidth = (int) headerFont.getStringBounds(model.getColumnName(col), frc).getWidth();
                    for (int row = 0; row < model.getRowCount(); row++) {
                        Object val = model.getValueAt(row, col);
                        if (val != null) {
                            int width = (int) normalFont.getStringBounds(val.toString(), frc).getWidth();
                            if (width > maxWidth) maxWidth = width;
                        }
                    }
                    colWidths[col] = maxWidth + padding * 2;
                    usedWidth += colWidths[col];
                }

                // Scale down if total width > printable width
                if (usedWidth > printableWidth) {
                    double scale = printableWidth / usedWidth;
                    for (int i = 0; i < colCount; i++) {
                        colWidths[i] = (int) (colWidths[i] * scale);
                    }
                }

                // Draw title
                g.setColor(Color.BLUE);
                g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
                g.drawString(title, x + 10, y + 20);

                y += 30;
                g.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(2));
                g.drawLine(x, y, x + (int) printableWidth, y);
                y += 10;

                // Column headers
                g.setFont(headerFont);
                int colX = x;
                for (int col = 0; col < colCount; col++) {
                    g.drawRect(colX, y, colWidths[col], rowHeight);
                    g.drawString(model.getColumnName(col), colX + padding, y + 15);
                    colX += colWidths[col];
                }
                y += rowHeight;

                // Table rows
                g.setFont(normalFont);
                for (int row = 0; row < model.getRowCount(); row++) {
                    colX = x;
                    for (int col = 0; col < colCount; col++) {
                        g.drawRect(colX, y, colWidths[col], rowHeight);
                        Object val = model.getValueAt(row, col);
                        if (val != null) {
                            g.drawString(val.toString(), colX + padding, y + 15);
                        }
                        colX += colWidths[col];
                    }
                    y += rowHeight;

                    if (y + rowHeight > pageFormat.getImageableHeight()) {
                        return NO_SUCH_PAGE; // Page break not handled yet
                    }
                }

                return PAGE_EXISTS;
            }
        };

        // Define ISO A4 paper
        PageFormat format = job.defaultPage();
        Paper paper = new Paper();
        paper.setSize(595.0, 842.0); // A4 in points
        paper.setImageableArea(40, 40, 515, 762); // Margins: 40pt

        format.setPaper(paper);
        job.setPrintable(printable, format);

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }


    public static void printPanel(JPanel panel) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Print Panel");

        job.setPrintable((g, pf, pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pf.getImageableX(), pf.getImageableY());

            // Panel size
            double panelWidth = panel.getWidth();
            double panelHeight = panel.getHeight();

            // Printable area size (A4 imageable area)
            double printWidth = pf.getImageableWidth();
            double printHeight = pf.getImageableHeight();

            // Calculate scale to fit the panel within printable area
            double scaleX = printWidth / panelWidth;
            double scaleY = printHeight / panelHeight;
            double scale = Math.min(scaleX, scaleY); // Maintain aspect ratio

            g2d.scale(scale, scale);

            // Paint the panel into the scaled graphics context
            panel.printAll(g2d);

            return Printable.PAGE_EXISTS;
        });

        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }

    public static TextAreaRenderer getTextAreaRenderer() {
        return new TextAreaRenderer();
    }

    public static TextAreaEditor getTextAreaEditor() {
        return new TextAreaEditor();
    }

    public static void printWithDefaultSettings(DefaultTableModel model, int billID, Date date, String customerName, int type) {
        PrinterJob job = PrinterJob.getPrinterJob();
//
        Printable printable = new Printable() {
            @Override
            public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) return NO_SUCH_PAGE;

                Graphics2D g2d = (Graphics2D) g;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                int x = 0;
                int y = 0;
                int rowHeight = 20;
                int padding = 5;

                double printableWidth = pageFormat.getImageableWidth();
                int colCount = model.getColumnCount();
                int[] colWidths = new int[colCount];

                // Font setup
                java.awt.Font headerFont = new java.awt.Font("Arial", java.awt.Font.BOLD, 12);
                java.awt.Font normalFont = new java.awt.Font("Arial", java.awt.Font.PLAIN, 12);
                g.setFont(normalFont);
                FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);

                // Fixed width for "SNo"
                int init;
                if (type == CUSTOMER_BILL) {
                    colWidths[0] = 30;
                    init = 1;
                } else {
                    init = 0;
                }
                int usedWidth = colWidths[0];

                // Estimate width for other columns
                for (int col = init; col < colCount; col++) {
                    int maxWidth = (int) headerFont.getStringBounds(model.getColumnName(col), frc).getWidth();
                    for (int row = 0; row < model.getRowCount(); row++) {
                        Object val = model.getValueAt(row, col);
                        if (val != null) {
                            int width = (int) normalFont.getStringBounds(val.toString(), frc).getWidth();
                            if (width > maxWidth) maxWidth = width;
                        }
                    }
                    colWidths[col] = maxWidth + padding * 2;
                    usedWidth += colWidths[col];
                }

                // Scale down if total width > printable width
                if (usedWidth > printableWidth) {
                    double scale = printableWidth / usedWidth;
                    for (int i = 0; i < colCount; i++) {
                        colWidths[i] = (int) (colWidths[i] * scale);
                    }
                }

                // Draw title
                g.setColor(Color.BLUE);
                g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
                g.drawString("Gurukripa Jewellers", x + 10, y + 20);
                y += 70;
                g.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
                g.setColor(Color.BLACK);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                int billtruncatedid = billID % 100 == 0 ? 100 : billID % 100;
                g.drawString("" + billtruncatedid, x + 10, y);
                g.drawString(customerName, x + 300, y);
                g.drawString(sdf.format(date), x + 150, y);
                y += 20;

                g.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(2));
                g.drawLine(x, y, x + (int) printableWidth, y);
                y += 10;

                int colX = x;
                // Column headers
//                g.setFont(headerFont);
//                for (int col = 0; col < colCount; col++) {
//                    g.drawRect(colX, y, colWidths[col], rowHeight);
//                    g.drawString(model.getColumnName(col), colX + padding, y + 15);
//                    colX += colWidths[col];
//                }
//                y += rowHeight;

                // Table rows
                g.setFont(normalFont);
                for (int row = 0; row < model.getRowCount(); row++) {
                    colX = x;
                    for (int col = 0; col < colCount; col++) {
                        g.drawRect(colX, y, colWidths[col], rowHeight);
                        Object val = model.getValueAt(row, col);
                        if (val != null) {
                            g.drawString(val.toString(), colX + padding, y + 15);
                        }
                        colX += colWidths[col];
                    }
                    y += rowHeight;

                    if (y + rowHeight > pageFormat.getImageableHeight()) {
                        return NO_SUCH_PAGE; // Page break not handled yet
                    }
                }

                return PAGE_EXISTS;
            }
        };

        // Define ISO A4 paper
        PageFormat format = job.defaultPage();
        Paper paper = new Paper();
        paper.setSize(595.0, 842.0); // A4 in points
//        paper.setImageableArea(40, 40, 515, 762); // Margins: 40pt
        paper.setImageableArea(20, 40, 555, 762);

        format.setPaper(paper);
        job.setPrintable(printable, format);

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }

    public static void splitFrame(JFrame topLeft, JFrame bottomRight, int splitType) {
        // Screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Position and size the frames
        if (splitType == HORIZONTAL_SPLIT) {
            topLeft.setBounds(0, 0, screenWidth, screenHeight / 2);
            bottomRight.setBounds(0, screenHeight / 2, screenWidth, screenHeight / 2);
            return;
        }
        if (splitType == VERTI_SPLIT) {
            topLeft.setBounds(0, 0, screenWidth / 2, screenHeight);
            bottomRight.setBounds(screenWidth / 2, 0, screenWidth / 2, screenHeight);
        }

    }

    public static String parseDateString(Date date) {
        return date.toLocalDate().format( DateTimeFormatter.ofPattern("dd-MM-yy"));
    }

    public static LocalDateTime getDate(Object selectedItem) {
        if (selectedItem == null || selectedItem.toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Date value is missing or invalid.");
        }

        String dateString = selectedItem.toString().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss");

        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateString + " 00:00:00", formatter);

            if (dateTime.toLocalDate().equals(LocalDate.now())) {
                return LocalDateTime.now(); // current system date & time
            } else {
                return dateTime; // parsed date with 00:00:00 time
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Date format should be dd-MM-yy", e);
        }
    }
}

class DecimalDocumentFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        String newText = getUpdatedText(fb, offset, 0, string);
        if (isValidInput(newText)) {
            super.insertString(fb, offset, string, attr);
        } else {
            Toolkit.getDefaultToolkit().beep(); // Invalid input feedback
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        String newText = getUpdatedText(fb, offset, length, text);
        if (isValidInput(newText)) {
            super.replace(fb, offset, length, text, attrs);
        } else {
            Toolkit.getDefaultToolkit().beep(); // Invalid input feedback
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length); // Allow deletion
    }

    private String getUpdatedText(FilterBypass fb, int offset, int length, String text) throws BadLocationException {
        String existingText = fb.getDocument().getText(0, fb.getDocument().getLength());
        StringBuilder newText = new StringBuilder(existingText);
        newText.replace(offset, offset + length, text);
        return newText.toString();
    }

    private boolean isValidInput(String text) {
        // Regex to allow only numeric values with optional decimal (max 3 decimal places)
        return text.matches("\\d*(\\.\\d{0,3})?");
    }
}

//
//class TextAreaRenderer extends JTextArea implements TableCellRenderer {
//    public TextAreaRenderer() {
//        setLineWrap(true);
//        setWrapStyleWord(true);
//        setOpaque(true);
//    }
//
//    @Override
//    public Component getTableCellRendererComponent(JTable table, Object value,
//                                                   boolean isSelected, boolean hasFocus,
//                                                   int row, int column) {
//        setText(value == null ? "" : value.toString());
//
//        if (isSelected) {
//            setBackground(table.getSelectionBackground());
//            setForeground(table.getSelectionForeground());
//        } else {
//            setBackground(table.getBackground());
//            setForeground(table.getForeground());
//        }
//        return this;
//    }
//}
class TextAreaRenderer extends JTextArea implements TableCellRenderer {
    public TextAreaRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        setText(value == null ? "" : value.toString());

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        // Dynamically adjust row height based on renderer content
        adjustRowHeight(table, row);

        return this;
    }

    // Adjust the row height based on content
//    private void adjustRowHeight(JTable table, int row) {
//        TableCellRenderer renderer = table.getCellRenderer(row, 1); // Column 1
//        Component comp = renderer.getTableCellRendererComponent(table, table.getValueAt(row, 1), false, false, row, 1);
//        int preferredHeight = comp.getPreferredSize().height + table.getRowMargin();
//        table.setRowHeight(row, preferredHeight);
//    }
    private void adjustRowHeight(JTable table, int row) {
        int defaultHeight = 40; // or table.getRowHeight() if you want dynamic default

        TableCellRenderer renderer = table.getCellRenderer(row, 1); // Column 1
        Component comp = renderer.getTableCellRendererComponent(table, table.getValueAt(row, 1), false, false, row, 1);

        int preferredHeight = comp.getPreferredSize().height + table.getRowMargin();

        // Set to max of defaultHeight or preferredHeight
        int finalHeight = Math.max(defaultHeight, preferredHeight);

        table.setRowHeight(row, finalHeight);
    }
}

class TextAreaEditor extends AbstractCellEditor implements TableCellEditor {

    private final JScrollPane scrollPane;
    private final JTextArea textArea;

    public TextAreaEditor() {
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane = new JScrollPane(textArea);
        InputMap inputMap = textArea.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = textArea.getActionMap();

// Bind SHIFT+ENTER to inserting a newline
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.SHIFT_DOWN_MASK), "insert-break");
        actionMap.put("insert-break", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.append("\n");
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        textArea.setText(value == null ? "" : value.toString());
        return scrollPane;
    }

    @Override
    public Object getCellEditorValue() {
        return textArea.getText();
    }
}