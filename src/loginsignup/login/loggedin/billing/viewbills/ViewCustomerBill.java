package loginsignup.login.loggedin.billing.viewbills;

import mainpack.MyClass;
import testpackage.UtilityMethods;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
            dispose();
            MyClass.billingScreen.setVisible(true);
        });
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printQueue.offer(() -> printWithDefaultSettings((DefaultTableModel) billTable.getModel(), getBillID(), getDate(), getCustomerName(), UtilityMethods.CUSTOMER_BILL));

            }
        });
    }

    private String getCustomerName() {
        return customerName;
    }

    private String customerName;

    public void setCustomerName(String customerName) {
        customerNameLabel.setText(customerName);
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
                if (customerNameComboBox.getSelectedIndex() == 0) {
                    // No customer filter
                    query = "SELECT BillID FROM bills WHERE BillID > " + currentBillID + " ORDER BY BillID ASC LIMIT 1";
                } else {
                    // Get selected customer
                    String selectedCustomer = customerNameComboBox.getSelectedItem() == null ? "" : customerNameComboBox.getSelectedItem().toString();
                    query = "SELECT BillID FROM bills WHERE BillID > " + currentBillID + " AND customer_name = '" + selectedCustomer + "' ORDER BY BillID ASC LIMIT 1";
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
                    query = "SELECT BillID FROM bills WHERE BillID < " + getBillID() + " ORDER BY BillID DESC LIMIT 1";
                } else {
                    // Get selected customer
                    String selectedCustomer = customerNameComboBox.getSelectedItem() == null ? "" : customerNameComboBox.getSelectedItem().toString();
                    query = "SELECT BillID FROM bills WHERE BillID < " + getBillID() + " AND customer_name = '" + selectedCustomer + "' ORDER BY BillID DESC LIMIT 1";
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
                    String query = "SELECT BillID FROM bills WHERE BillID = " + inputBillID;

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
            query = "select min(billid) from bills where customer_name =?;";
        } else query = "select min(billid) from bills;";
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

        String sql1 = "SELECT SNo,Quantity, ItemName, TotalBaseCosting, GoldPlatingWeight, TotalGoldCost, TotalFinalCost  " + "FROM billdetails WHERE BillID = ?";
        String sql2 = "select billid,amount , date from transactions where billid=?";

        try (PreparedStatement pstmt = MyClass.C.prepareStatement(sql1)) {
            pstmt.setInt(1, billID);
            ResultSet rs = pstmt.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            double grandtotal = 0;
            double[] balance;
            balance = UtilityMethods.balance(billID);
            {
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
                }
                model.setRowCount(model.getRowCount() + 1);
                model.setValueAt("Grand Total", model.getRowCount() - 1, model.getColumnCount() - 2);
                model.setValueAt(UtilityMethods.round(grandtotal,2), model.getRowCount() - 1, model.getColumnCount() - 1);

            totalLabel.setText(grandtotal + "");
            idLabel.setText("billID: " + billID);
                table.setModel(model);}//updating the table model with bill details
            {
                model.setRowCount(model.getRowCount() + 1);
                model.setValueAt("prev:", model.getRowCount() - 1, model.getColumnCount() - 2);
                model.setValueAt(UtilityMethods.round(balance[0],2),model.getRowCount()-1,model.getColumnCount()-1);
                model.setRowCount(model.getRowCount() + 1);
                model.setValueAt("total", model.getRowCount() - 1, model.getColumnCount() - 2);
                model.setValueAt(UtilityMethods.round(balance[1],2),model.getRowCount()-1,model.getColumnCount()-1);

            }//showing the balances

            {
                sql1 = "select *from bills where billid=?;";
                PreparedStatement pstmt2 = MyClass.C.prepareStatement(sql1);
                pstmt2.setInt(1, billID);
                rs = pstmt2.executeQuery();
                if (rs.next()) {
                    setDateTime(rs.getTimestamp("date"));
                    setCustomerName(rs.getString("customer_name"));
                }
            }// showing the date,customername
            double totalRecieved = 0;
            {
                PreparedStatement preparedStatement = MyClass.C.prepareStatement(sql2);
                preparedStatement.setInt(1, billID);
                ResultSet rs2 = preparedStatement.executeQuery();

                while (rs2.next()) {
                    model.setRowCount(model.getRowCount() + 1);
                    java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(rs2.getString("date"));
                    String dateformatted = new SimpleDateFormat("dd-MM-yy").format(date);
                    model.setValueAt(rs2.getString("billid") + "/" + dateformatted, model.getRowCount() - 1, model.getColumnCount() - 2);
                    model.setValueAt(UtilityMethods.round(rs2.getDouble("amount"),2), model.getRowCount() - 1, model.getColumnCount() - 1);
                    totalRecieved += rs2.getDouble("amount");

                }
                model.setRowCount(model.getRowCount() + 1);
                model.setValueAt("totalRecieved", model.getRowCount() - 1, model.getColumnCount() - 2);
                model.setValueAt(UtilityMethods.round(totalRecieved,2), model.getRowCount() - 1, model.getColumnCount() - 1);
            }// showing the transactions linked

            {
                model.setRowCount(model.getRowCount() + 1);
                model.setValueAt("baki", model.getRowCount() - 1, model.getColumnCount() - 2);
                model.setValueAt(UtilityMethods.round((balance[1]-totalRecieved  ),2), model.getRowCount() - 1, model.getColumnCount() - 1);
            }//showing the balances
            setBillID(billID);

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
