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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UtilityMethods {
    //Thread printingThread;
    public static Thread printingThread;
    public static final BlockingQueue<Runnable> printQueue = new LinkedBlockingQueue<>();

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
    private static void printModel(DefaultTableModel model,String title) {
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
    public static TextAreaEditor getTextAreaEditor(){
        return new TextAreaEditor();
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