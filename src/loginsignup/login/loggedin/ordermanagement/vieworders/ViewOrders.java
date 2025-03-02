package loginsignup.login.loggedin.ordermanagement.vieworders;

import mainpack.MyClass;
import org.jdesktop.swingx.prompt.PromptSupport;
import testpackage.TestClass;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class ViewOrders extends JFrame {
    public ViewOrders() {
        setContentPane(panel);
        pack();
        Vector<String> v = new Vector<>();
        customerComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                id = 1;
                try {
                    Statement stmt = MyClass.C.createStatement();
                    String query;
                    query = "SELECT MIN(slip_id) FROM order_slips ";
                    if (!(customerComboBox.getSelectedIndex() == 0)) {
                        query += "WHERE customer_name = \"" + customerComboBox.getSelectedItem().toString() + "\" ";
                        if (!(panaTypeComboBox.getSelectedIndex() == 0)) {
                            query += "AND slip_type = \"" + panaTypeComboBox.getSelectedItem().toString() + "\";";
                        } else {
                            query += ";";
                        }
                    } else if (!(panaTypeComboBox.getSelectedIndex() == 0)) {
                        query += "WHERE slip_type =\"" + panaTypeComboBox.getSelectedItem().toString() + "\";";

                    } else query += ";";


                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) id = rs.getInt(1);
                    else {
                        id = 1;
                    }
                    System.out.println("id is " + id);
                    setCurrentBill(id);
                    billIDLabel.setText("Bill id: " + id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (NullPointerException ex) {
                }

            }
        });
        panaTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                id = 1;
                try {
                    Statement stmt = MyClass.C.createStatement();
                    String query;
//                    query= + "WHERE customer_name = \"" + customerComboBox.getSelectedItem().toString() + "\" " + "AND slip_type = \"" + panaTypeComboBox.getSelectedItem().toString() + "\";";
                    query = "SELECT MIN(slip_id) FROM order_slips ";
                    if (!(customerComboBox.getSelectedIndex() == 0)) {
                        query += "WHERE customer_name = \"" + customerComboBox.getSelectedItem().toString() + "\" ";
                        if (!(panaTypeComboBox.getSelectedIndex() == 0)) {
                            query += "AND slip_type = \"" + panaTypeComboBox.getSelectedItem().toString() + "\";";
                        } else {
                            query += ";";
                        }
                    } else if (!(panaTypeComboBox.getSelectedIndex() == 0)) {
                        query += "WHERE slip_type =\"" + panaTypeComboBox.getSelectedItem().toString() + "\";";

                    } else query += ";";


                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next()) id = rs.getInt(1);
                    else {
                        id = 1;
                    }
                    System.out.println("id is " + id);
                    setCurrentBill(id);
                    billIDLabel.setText("Bill id: " + id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.orderScreen.setVisible(true);
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = getCurrentBillID();
                try {
                    Statement stmt = MyClass.C.createStatement();
                    String query = "SELECT * FROM order_slips WHERE slip_id > " + id;

                    // Check if a customer is selected
                    if (customerComboBox.getSelectedIndex() != 0) {
                        query += " AND customer_name = '" + customerComboBox.getSelectedItem().toString() + "'";
                    }

                    // Check if a slip type is selected
                    if (panaTypeComboBox.getSelectedIndex() != 0) {
                        query += " AND slip_type = '" + panaTypeComboBox.getSelectedItem().toString() + "'";
                    }

                    query += " ORDER BY slip_id ASC LIMIT 1;"; // Ensuring we get the next slip_id

                    System.out.println("Generated Query: " + query);

                    ResultSet resultSet = stmt.executeQuery(query);

                    if (resultSet.next()) {
                        setCurrentBill(Integer.parseInt(resultSet.getString("slip_id")));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }


            }
        });
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                int id = getCurrentBillID();
//                try {
//                    Statement stmt = MyClass.C.createStatement();
//                    ResultSet resultSet;
//String query
//                    if (!(customerComboBox.getSelectedIndex() == 0)) {
//                        if(!(panaTypeComboBox.getSelectedIndex()==0)){
//
//                        }
//                    } else {
//                        resultSet = stmt.executeQuery("SELECT * FROM order_slips WHERE slip_id < " + id + " ORDER BY slip_id DESC LIMIT 1;");
//                    }
//                    resultSet = stmt.executeQuery("SELECT * FROM order_slips WHERE slip_id < " + id + " AND customer_name = '" + customerComboBox.getSelectedItem().toString() + "'");
//
//                    System.out.println("prev id finding, current id is " + id);
//
//                    if (resultSet.next()) {
//                        setCurrentBill(Integer.parseInt(resultSet.getString("slip_id")));
//                    }
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                }
                int id = getCurrentBillID();
                try {
                    Statement stmt = MyClass.C.createStatement();
                    String query = "SELECT * FROM order_slips WHERE slip_id < " + id; // Get previous slips

                    // Check if a customer is selected
                    if (customerComboBox.getSelectedIndex() != 0) {
                        query += " AND customer_name = '" + customerComboBox.getSelectedItem().toString() + "'";
                    }

                    // Check if a slip type is selected
                    if (panaTypeComboBox.getSelectedIndex() != 0) {
                        query += " AND slip_type = '" + panaTypeComboBox.getSelectedItem().toString() + "'";
                    }

                    query += " ORDER BY slip_id DESC LIMIT 1;"; // Ensuring we get the previous slip_id

                    System.out.println("Generated Query: " + query);

                    ResultSet resultSet = stmt.executeQuery(query);

                    if (resultSet.next()) {
                        setCurrentBill(Integer.parseInt(resultSet.getString("slip_id"))); // Set previous slip_id
                    } else {
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                init();
                setCurrentBill(Integer.parseInt(searchField.getText()));
                searchField.setText("");
                panaTypeComboBox.setSelectedItem(panaType);
                customerComboBox.setSelectedItem(customerName);
            }
        });
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyClass.printPanel(panel);
                TestClass.writeTableToExcel(orderSlipTable, "myfile.xlxx");
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int deleteAction = JOptionPane.showConfirmDialog(MyClass.viewOrders, "Are you sure you want to delete order slip " + id, "confirm delete action", JOptionPane.OK_CANCEL_OPTION);
                if (deleteAction == JOptionPane.CANCEL_OPTION) {
                    return;
                }
                if (deleteAction == JOptionPane.OK_OPTION) {
                    try {
                        String query;
                        query = "select min(slip_id) from order_slips ";
                        if (!(customerComboBox.getSelectedIndex() == 0)) {
                            query += "where customer_name=\"" + customerName + "\" ";
                            if (!(panaTypeComboBox.getSelectedIndex() == 0)) {
                                query += " and slip_type= \"" + panaType + "\";";
                            } else {
                                query += ";";
                            }

                        } else if (!(panaTypeComboBox.getSelectedIndex() == 0)) {
                            query += "where slip_type=\"" + panaType + "\";";
                        } else query += ";";


                        ResultSet rs = MyClass.C.createStatement().executeQuery(query);
                        int idd=MyClass.viewOrders.id;

                        if (rs.next()) {
                            if (rs.getString(1).contentEquals("" + idd)) {
                                System.out.println(rs.getString(1) + "       " + idd);
                                nextButton.doClick();
                            } else {
                                prevButton.doClick();
                                System.out.println(rs.getString(1)+"       "+idd);

                            }
                        }
                        MyClass.C.createStatement().executeUpdate("delete from order_slips where slip_id=" + idd);
                        System.out.println("id ->"+idd+"deleted");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    int id;

    public int getCurrentBillID() {
        return id;
    }

    private void setCurrentBill(int id) {
        try {
            // Query to fetch data based on slip_id
            String query = "SELECT design_id, item_name, quantity, plating_grams, raw_material_price, other_details, customer_name, slip_id,slip_type FROM order_slips WHERE slip_id = " + id;
            Statement stmt = MyClass.C.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"Design ID", "Item Name", "Quantity", "Plating", "Raw Material Cost", "Other Details"});

            int slipId = 1;
            double totalPlating = 0;
            while (rs.next()) {
                customerName = rs.getString("customer_name");
                slipId = rs.getInt("slip_id");
                panaType = rs.getString("slip_type");
                totalPlating += Double.parseDouble(rs.getString("plating_grams"));
                totalPlatingField.setText("total plating: " + totalPlating);
                model.addRow(new Object[]{rs.getString("design_id"), rs.getString("item_name"), rs.getInt("quantity"), rs.getBigDecimal("plating_grams"), rs.getBigDecimal("raw_material_price"), rs.getString("other_details")});
            }
            orderSlipTable.setModel(model);
            nameLabel.setText(customerName + "-> " + panaType);
            billIDLabel.setText(String.valueOf(slipId));
            this.id = slipId;
             query = "SELECT DATE_FORMAT(created_at, '%d-%m-%y') AS formatted_date FROM order_slips WHERE slip_id = "+id+";";
            Statement stmt2 = MyClass.C.createStatement();
            ResultSet rs2 = stmt2.executeQuery(query);
            if(rs2.next())dateLabel.setText("date: "+rs2.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching bill details: " + e.getMessage());
        }
    }

    String customerName = "", panaType = "";

    public void init() {
        String[] columnNames = {"design id", "Item Name", "Quantity", "Plating", "Raw Material Cost", "Other Details"};//jtable content
        // panaTypeComboBox.removeAllItems();
        customerComboBox.removeAllItems();
        PromptSupport.setPrompt("Go to id", searchField);
        customerComboBox.addItem("Select Customer");
        try {
            Statement stmt1 = MyClass.C.createStatement();
            ResultSet rs1 = stmt1.executeQuery("select type_name from ordertype;");
            panaTypeComboBox.addItem("All Slips");
            while (rs1.next()) {
                panaTypeComboBox.addItem(rs1.getString("type_name"));
            }
            Statement stmt2 = MyClass.C.createStatement();
            ResultSet rs2 = stmt2.executeQuery("select customer_name from customers;");
            while (rs2.next()) {
                customerComboBox.addItem(rs2.getString("customer_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        String[] emptyRow = {"", "", "", "", "", ""};
        model.addRow(emptyRow);
        orderSlipTable.setModel(model);
        billIDLabel.setText("Bill ID: ");
        setCurrentBill(id);
        totalPlatingField.setEditable(false);
        pack();
    }

    private JButton backButton;
    private JTable orderSlipTable;
    private JButton nextButton;
    private JButton prevButton;
    private JComboBox customerComboBox;
    private JComboBox panaTypeComboBox;
    private JPanel panel;
    private JLabel billIDLabel;
    private JLabel nameLabel;
    private JTextField searchField;
    private JTextField totalPlatingField;
    private JButton printButton;
    private JButton deleteButton;
    private JLabel dateLabel;
}
