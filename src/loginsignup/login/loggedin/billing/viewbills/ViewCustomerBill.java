package loginsignup.login.loggedin.billing.viewbills;

import mainpack.MyClass;
import testpackage.UtilityMethods;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.print.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

import static testpackage.UtilityMethods.*;

public class ViewCustomerBill extends JFrame {
    private int billID;

    private static void printCustomerBill(DefaultTableModel model, int billID, Date date, String customerName) {
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
                Font headerFont = new Font("Arial", Font.BOLD, 12);
                Font normalFont = new Font("Arial", Font.PLAIN, 12);
                g.setFont(normalFont);
                FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);

                // Fixed width for "SNo"
                colWidths[0] = 30;
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
                g.setFont(new Font("Arial", Font.BOLD, 18));
                g.drawString("Gurukripa Jewellers", x + 10, y + 20);
                y += 70;
                g.setFont(new Font("Arial", Font.PLAIN, 12));
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

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    private JButton backButton;
    private JTable billTable;
    private JPanel panel;
    private JLabel idLabel;
    private JButton previousButton;
    private JButton nextButton;
    private JComboBox<String> customerNameComboBox;
    private JTextField billIDTextField;
    private JLabel dateLabel;
    private JButton printButton;
    private JLabel customerNameLabel;
    private JLabel totalLabel;

    public ViewCustomerBill() {
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        backButton.addActionListener(e -> {
            setVisible(false);
            MyClass.billingScreen.setVisible(true);
        });
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printQueue.offer(() -> printCustomerBill((DefaultTableModel) billTable.getModel(), getBillID(), getDate(), getCustomerName()));

            }
        });
    }

    private String getCustomerName() {
        return customerName;
    }

    private String customerName;

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;

    public void init(String mode) {
        setListOfCustomer();// sets the list of customers in jcombobox
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        if (mode.contentEquals("customer")) {
            String[] model = new String[]{"S.No", "item", "Gold (g)", "Plus G", "TGC", "Total"};
            DefaultTableModel tableModel = new DefaultTableModel(model, 1) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            billTable.setModel(tableModel);
            loadBillData(billTable, getMinBillID());

            customerNameComboBox.addActionListener(e -> {
                int billid = getMinBillID();
                loadBillData(billTable, billid);
            });

            nextButton.addActionListener(e -> {
                int currentBillID = getBillID();  // Function to get the current BillID
                String query;
                System.out.println("current bill id is "+currentBillID);
                if (customerNameComboBox.getSelectedIndex() == 0) {
                    // No customer filter
                    query = "SELECT BillID FROM billdetails WHERE BillID > " + currentBillID + " ORDER BY BillID ASC LIMIT 1";
                } else {
                    // Get selected customer
                    String selectedCustomer = customerNameComboBox.getSelectedItem() == null ? "" : customerNameComboBox.getSelectedItem().toString();
                    query = "SELECT BillID FROM billdetails WHERE BillID > " + currentBillID + " AND customer_name = '" + selectedCustomer + "' ORDER BY BillID ASC LIMIT 1";
                }
                try {
                    Statement stmt = MyClass.C.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) loadBillData(billTable, rs.getInt(1));

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            });
            previousButton.addActionListener(e -> {
                int currentBillID = getBillID();  // Function to get the current BillID
                String query;

                if (customerNameComboBox.getSelectedIndex() == 0) {
                    // No customer filter
                    query = "SELECT BillID FROM billdetails WHERE BillID < " + currentBillID + " ORDER BY BillID DESC LIMIT 1";
                } else {
                    // Get selected customer
                    String selectedCustomer = customerNameComboBox.getSelectedItem() == null ? "" : customerNameComboBox.getSelectedItem().toString();
                    query = "SELECT BillID FROM billdetails WHERE BillID < " + currentBillID + " AND customer_name = '" + selectedCustomer + "' ORDER BY BillID DESC LIMIT 1";
                }

                try {
                    Statement stmt = MyClass.C.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        loadBillData(billTable, rs.getInt(1));  // Load previous bill data
                    }

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
            billIDTextField.addActionListener(e -> {
                try {
                    if (billIDTextField.getText().trim().isEmpty()) return;
                    int inputBillID = Integer.parseInt(billIDTextField.getText().trim()); // Get and parse BillID
                    String query = "SELECT BillID FROM billdetails WHERE BillID = " + inputBillID;

                    Statement stmt = MyClass.C.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        loadBillData(billTable, inputBillID);

                        billIDTextField.setText("");

                        // Load bill data if found
                    } else {
                        billIDTextField.setText("");
                        JOptionPane.showMessageDialog(null, "Bill ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid Bill ID!", "Error", JOptionPane.ERROR_MESSAGE);
                    billIDTextField.setText("");

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });


        }
    }

    private int getMinBillID() {
        String query, customerName;

        if (customerNameComboBox.getSelectedIndex() != 0) {
            query = "select min(billid) from billdetails where customer_name =?;";
        } else query = "select min(billid) from billdetails;";
        try {
            PreparedStatement stmt = MyClass.C.prepareStatement(query);
            if (customerNameComboBox.getSelectedIndex() != 0) {
                customerName = customerNameComboBox.getSelectedItem() == null ? "" : customerNameComboBox.getSelectedItem().toString();
                stmt.setString(1, customerName);
            }
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -2;
    }

    public void setListOfCustomer() {
        String query = "select customer_name from customers;";
        customerNameComboBox.removeAllItems();
        Statement stmt;
        try {
            stmt = MyClass.C.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            customerNameComboBox.addItem("Select Customer");
            while (rs.next()) customerNameComboBox.addItem(rs.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadBillData(JTable table, int billID) {

        String sql1 = "SELECT SNo,Quantity, ItemName, TotalBaseCosting, GoldPlatingWeight, TotalGoldCost, TotalFinalCost , customer_name " + "FROM billdetails WHERE BillID = ?";
        String sql2 = "select billid,amount , date from transactions where billid=?";

        try (PreparedStatement pstmt = MyClass.C.prepareStatement(sql1)) {
            pstmt.setInt(1, billID);
            ResultSet rs = pstmt.executeQuery();

            // Define table columns
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            // Populate model with data from ResultSet
            String customername = "";
            double grandtotal = 0;
            while (rs.next()) {

                Vector<Object> row = new Vector<>();
                while (row.size() < model.getColumnCount()) {
                    row.add(null);
                }
                TableColumnModel columnModel = table.getColumnModel();

                row.set(columnModel.getColumnIndex("S.No"), rs.getString("SNo"));
                row.set(columnModel.getColumnIndex("item"), rs.getString("ItemName") + "      " + rs.getString("Quantity"));
                row.set(columnModel.getColumnIndex("Gold (g)"), rs.getString("GoldPlatingWeight"));
                row.set(columnModel.getColumnIndex("Plus G"), rs.getString("TotalBaseCosting"));
                row.set(columnModel.getColumnIndex("TGC"), rs.getString("TotalGoldCost"));
                row.set(columnModel.getColumnIndex("Total"), rs.getString("TotalFinalCost"));


                grandtotal += rs.getDouble("TotalFinalCost");
                double value = grandtotal;
                BigDecimal rounded = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
                grandtotal = rounded.doubleValue();
                model.addRow(row);
                customername = rs.getString("customer_name");
            }
            model.setRowCount(model.getRowCount() + 1);
            model.setValueAt("Grand Total", model.getRowCount() - 1, model.getColumnCount() - 2);
            model.setValueAt(grandtotal, model.getRowCount() - 1, model.getColumnCount() - 1);


            totalLabel.setText(grandtotal + "");
            customerNameLabel.setText(customername);
            setCustomerName(customername);
            idLabel.setText("billID: " + billID);
            setBillID(billID);
            table.setModel(model);
            double[] balance;

            {
                balance=UtilityMethods.balance(billID);
                System.out.println(balance[0]+"\t"+balance[1 ]);
                model.setRowCount(model.getRowCount() + 1);
                model.setValueAt("prev:",model.getRowCount()-1,model.getColumnCount()-2);
                model.setValueAt(balance[0],model.getRowCount()-1,model.getColumnCount()-1);

            }//showing previous balance code
            {
                sql1 = "select *from bills where billid=?;";
                PreparedStatement pstmt2 = MyClass.C.prepareStatement(sql1);
                pstmt2.setInt(1, billID);
                rs = pstmt2.executeQuery();
                if (rs.next()) setDateTime(rs.getTimestamp("date"));
            }// showing the date
            {
                PreparedStatement preparedStatement = MyClass.C.prepareStatement(sql2);
                preparedStatement.setInt(1, billID);
                ResultSet rs2 = preparedStatement.executeQuery();
                double totalRecieved = 0;
                while (rs2.next()) {

                    model.setRowCount(model.getRowCount() + 1);
                    java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(rs2.getString("date"));
                    String dateformatted = new SimpleDateFormat("dd-MM-yy").format(date);
                    model.setValueAt(rs2.getString("billid") + "/" + dateformatted, model.getRowCount() - 1, model.getColumnCount() - 2);
                    model.setValueAt(rs2.getString("amount"), model.getRowCount() - 1, model.getColumnCount() - 1);
                    totalRecieved += rs2.getDouble("amount");
                }
                model.setRowCount(model.getRowCount() + 1);
                model.setValueAt("totalRecieved", model.getRowCount() - 1, model.getColumnCount() - 2);
                model.setValueAt(totalRecieved, model.getRowCount() - 1, model.getColumnCount() - 1);
            }// showing the transactions linked
            {
                model.setRowCount(model.getRowCount()+1);
                model.setValueAt("baki",model.getRowCount()-1,model.getColumnCount()-2);
                model.setValueAt(balance[1],model.getRowCount()-1,model.getColumnCount()-1);
            }
            System.out.println("Table updated successfully!");
        } catch (SQLException e) {
            throw new RuntimeException();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    private void setDateTime(Timestamp date) {
        LocalDateTime datetime = date.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy hh:mm a");
        dateLabel.setText(datetime.format(formatter));
        setDate(Date.valueOf(datetime.toLocalDate()));
    }

}
