package loginsignup.login.loggedin.billing.newBill;

import mainpack.MyClass;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DateFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ViewBill extends JFrame {
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
    private JComboBox customerNameComboBox;
    private JTextField billIDTextField;
    private JLabel dateLabel;

    public ViewBill() {
        setContentPane(panel);


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.billingScreen.setVisible(true);
            }
        });
    }

    public void init(String mode) {
        pack();
        setListOfCustomer();// sets the list of customers in jcombobox

        if (mode.contentEquals("customer")) {
            String[] model = new String[]{"S.No", "Item Name", "Plus G", "Gold (g)", "TGC", "Total"};
            DefaultTableModel tableModel = new DefaultTableModel(model, 1) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            try {
                Statement stmt = MyClass.C.createStatement();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            billTable.setModel(tableModel);
            TableColumnModel columnModel = billTable.getColumnModel();
            loadBillData(billTable, getMinBillID());

            customerNameComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int billid = getMinBillID();
                    loadBillData(billTable, billid);
                }
            });

            nextButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int currentBillID = getBillID();  // Function to get the current BillID
                    String query = "";

                    if (customerNameComboBox.getSelectedIndex() == 0) {
                        // No customer filter
                        query = "SELECT BillID FROM billdetails WHERE BillID > " + currentBillID + " ORDER BY BillID ASC LIMIT 1";
                    } else {
                        // Get selected customer
                        String selectedCustomer = customerNameComboBox.getSelectedItem().toString();
                        query = "SELECT BillID FROM billdetails WHERE BillID > " + currentBillID +
                                " AND customer_name = '" + selectedCustomer + "' ORDER BY BillID ASC LIMIT 1";
                    }
                    try {
                        Statement stmt = MyClass.C.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if (rs.next()) loadBillData(billTable, rs.getInt(1));
                        else return;
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            });
            previousButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int currentBillID = getBillID();  // Function to get the current BillID
                    String query = "";

                    if (customerNameComboBox.getSelectedIndex() == 0) {
                        // No customer filter
                        query = "SELECT BillID FROM billdetails WHERE BillID < " + currentBillID + " ORDER BY BillID DESC LIMIT 1";
                    } else {
                        // Get selected customer
                        String selectedCustomer = customerNameComboBox.getSelectedItem().toString();
                        query = "SELECT BillID FROM billdetails WHERE BillID < " + currentBillID +
                                " AND customer_name = '" + selectedCustomer + "' ORDER BY BillID DESC LIMIT 1";
                    }

                    try {
                        Statement stmt = MyClass.C.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if (rs.next()) {
                            loadBillData(billTable, rs.getInt(1));  // Load previous bill data
                        } else {
                            return;
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            billIDTextField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int inputBillID = Integer.parseInt(billIDTextField.getText().trim()); // Get and parse BillID
                        String query = "SELECT BillID FROM billdetails WHERE BillID = " + inputBillID;

                        Statement stmt = MyClass.C.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        if (rs.next()) {
                            loadBillData(billTable, inputBillID);

                            billIDTextField.setText("");
                            ActionListener[] temp=customerNameComboBox.getActionListeners();

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
                customerName = customerNameComboBox.getSelectedItem().toString();
                stmt.setString(1, customerName);
            }
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -2;
    }

//    private int getMinBillID() {
//        String sql = "SELECT MIN(BillID) FROM billdetails";
//        try (PreparedStatement pstmt = MyClass.C.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
//            if (rs.next()) {
//                return rs.getInt(1); // Fetch the minimum BillID
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return -1; // Return -1 if query fails
//    }

    public void setListOfCustomer() {
        String query = "select customer_name from customers;";
        customerNameComboBox.removeAllItems();
        Statement stmt = null;
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

        String sql = "SELECT SNo, ItemName, TotalBaseCosting, GoldPlatingWeight, TotalGoldCost, TotalFinalCost , customer_name " + "FROM billdetails WHERE BillID = ?";


        try (PreparedStatement pstmt = MyClass.C.prepareStatement(sql)) {
            pstmt.setInt(1, billID);
            ResultSet rs = pstmt.executeQuery();

            // Define table columns
            String[] columns = {"S.No","customer_name", "Item Name", "Plus G", "Gold (g)", "TGC", "Total"};
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            String customer_name;
            // Populate model with data from ResultSet
            while (rs.next()) {
                customer_name=rs.getString("customer_name");

                Object[] row = {rs.getInt("SNo"), rs.getString("ItemName"), rs.getDouble("TotalBaseCosting"),  // plusG
                        rs.getDouble("GoldPlatingWeight"), // gold (g)
                        rs.getDouble("TotalGoldCost"),     // tgc
                        rs.getDouble("TotalFinalCost")     // total
                };
                model.addRow(row);
            }

            idLabel.setText("billID: " + billID);
            setBillID(billID);
            table.setModel(model);
            sql="select *from bills where billid=?;";
            PreparedStatement pstmt2=MyClass.C.prepareStatement(sql);
            pstmt2.setInt(1,billID);
            rs=pstmt2.executeQuery();
            if(rs.next())setDateTime(rs.getTimestamp("date"));
            System.out.println("Table updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void setDateTime(Timestamp date) {
        LocalDateTime datetime=date.toLocalDateTime();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yy hh:mm a");
        dateLabel.setText(datetime.format(formatter));
    }

}
