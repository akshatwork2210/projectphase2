package loginsignup.login.loggedin.ordermanagement.vieworders;

import mainpack.MyClass;
import org.jdesktop.swingx.prompt.PromptSupport;
import testpackage.TestClass;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class ViewOrders extends JFrame {
    private int prevCustomerSelected = 0;
    private String prevPanaSelected;


    public ViewOrders() {
        setContentPane(panel);
        pack();
        customerComboBox.addActionListener(e -> {
            id = 1;
            try {
                Statement stmt = MyClass.C.createStatement();
                String query;
                query = "SELECT MIN(slip_id) FROM order_slips ";
                if (!(customerComboBox.getSelectedIndex() == 0)) {
                    query += "WHERE customer_name = \"" + Objects.requireNonNull(customerComboBox.getSelectedItem()) + "\" ";
                    if (!(panaTypeComboBox.getSelectedIndex() == 0)) {
                        query += "AND slip_type = \"" + Objects.requireNonNull(panaTypeComboBox.getSelectedItem()) + "\";";
                    } else {
                        query += ";";
                    }
                } else if (!(panaTypeComboBox.getSelectedIndex() == 0)) {
                    query += "WHERE slip_type =\"" + Objects.requireNonNull(panaTypeComboBox.getSelectedItem()) + "\";";

                } else query += ";";


                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    if (null != rs.getObject(1)) {
                        System.out.println(id + " from if block");
                        id = rs.getInt(1);
                    } else {
                        setCurrentBill(-1);
                        return;
                    }
                }

                System.out.println(id + " is the new id");
                setCurrentBill(id);
                billIDLabel.setText("Bill id: " + id);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (NullPointerException ignored) {
            }
            prevCustomerSelected = customerComboBox.getSelectedIndex();
        });
        panaTypeComboBox.addActionListener(e -> {
            id = 1;
            try {
                Statement stmt = MyClass.C.createStatement();
                String query;
//                    query= + "WHERE customer_name = \"" + customerComboBox.getSelectedItem().toString() + "\" " + "AND slip_type = \"" + panaTypeComboBox.getSelectedItem().toString() + "\";";
                query = "SELECT MIN(slip_id) FROM order_slips ";
                if (!(customerComboBox.getSelectedIndex() == 0)) {
                    query += "WHERE customer_name = \"" + Objects.requireNonNull(customerComboBox.getSelectedItem()).toString() + "\" ";
                    if (!(panaTypeComboBox.getSelectedIndex() == 0)) {
                        query += "AND slip_type = \"" + Objects.requireNonNull(panaTypeComboBox.getSelectedItem()).toString() + "\";";
                    } else {
                        query += ";";
                    }
                } else if (!(panaTypeComboBox.getSelectedIndex() == 0)) {
                    query += "WHERE slip_type =\"" + Objects.requireNonNull(panaTypeComboBox.getSelectedItem()).toString() + "\";";

                } else query += ";";


                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    if (rs.getObject(1) != null)
                        id = rs.getInt(1);
                    else {
                        setCurrentBill(-1);
                        return;
                    }
                }
                System.out.println("id is " + id);
                setCurrentBill(id);
                billIDLabel.setText("Bill id: " + id);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        backButton.addActionListener(e -> {
            panaTypeComboBox.getActionListeners();
            ActionListener[] customers = customerComboBox.getActionListeners();
            setVisible(false);
            MyClass.orderScreen.setVisible(true);
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

                int id;
                try {
                    id = searchField.getText().contentEquals("") ? -1 : Integer.parseInt(searchField.getText().trim());

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MyClass.viewOrders, "invalid number entered", "error", JOptionPane.ERROR_MESSAGE);
                    searchField.setText("");
                    return;
                }
                searchField.setText("");
                if (!IDExists(id)) {
                    JOptionPane.showMessageDialog(MyClass.viewOrders, "id not found from field", "error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                setCurrentBill(id);
            }

            private boolean IDExists(int id) {
                try {
                    Statement stmt = MyClass.C.createStatement();
                    ResultSet rs = stmt.executeQuery("select slip_id from order_slips where slip_id=" + id + ";");
                    if (rs.next())
                        return true;
                    else return false;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
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
                        String query = generateQueryForDelete();


                        ResultSet rs = MyClass.C.createStatement().executeQuery(query);
                        int idd = MyClass.viewOrders.id;

                        if (rs.next()) {
                            if (rs.getString(1).contentEquals("" + idd)) {
                                System.out.println(rs.getString(1) + "       " + idd);
                                nextButton.doClick();
                            } else {
                                prevButton.doClick();
                                System.out.println(rs.getString(1) + "       " + idd);

                            }
                        }
                        MyClass.C.createStatement().executeUpdate("delete from order_slips_main where slip_id=" + idd);
                        System.out.println("id ->" + idd + "deleted");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

            private String generateQueryForDelete() {
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
                return query;
            }
        });
    }

    int id;

    public int getCurrentBillID() {
        return id;
    }

    private void setCurrentBill(int id) {
        if (id == -1) {
            DefaultTableModel model = (DefaultTableModel) orderSlipTable.getModel();
            dateLabel.setText("");
            billIDLabel.setText("");
            nameLabel.setText("");
            this.id=id;
            model.setRowCount(0);
            return;
        }

        ActionListener[] panas = panaTypeComboBox.getActionListeners();
        ActionListener[] customers = customerComboBox.getActionListeners();

        try {
            for (ActionListener pana : panas) {
                panaTypeComboBox.removeActionListener(pana);
            }
            for (ActionListener customer : customers) {
                customerComboBox.removeActionListener(customer);
            }
            // Query to fetch data based on slip_id
            String query = "SELECT design_id, item_name, quantity, plating_grams, raw_material_price, other_details, customer_name, slip_id,slip_type FROM order_slips WHERE slip_id = " + id;
            Statement stmt = MyClass.C.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            model.setColumnIdentifiers(new String[]{"Design ID", "Item Name", "Quantity", "Plating", "Raw Material Cost", "Other Details"});

            int slipId = 1;
            BigDecimal totalPlating = BigDecimal.ZERO;
            int count = 0;
            while (rs.next()) {
                count++;
                customerName = rs.getString("customer_name");
                slipId = rs.getInt("slip_id");
                panaType = rs.getString("slip_type");
                totalPlating = totalPlating.add(BigDecimal.valueOf(Double.parseDouble(rs.getString("plating_grams"))));
                totalPlatingField.setText("total plating: " + totalPlating.setScale(3, RoundingMode.HALF_UP));
                model.addRow(new Object[]{rs.getString("design_id"), rs.getString("item_name"), rs.getInt("quantity"), rs.getBigDecimal("plating_grams"), rs.getBigDecimal("raw_material_price"), rs.getString("other_details")});
            }
            if (count == 0) {
                JOptionPane.showMessageDialog(MyClass.viewOrders, "slip not found error", "error", JOptionPane.ERROR_MESSAGE);
                setCurrentBill(-1);
                throw new RuntimeException();
            }
            orderSlipTable.setModel(model);
            nameLabel.setText(customerName + "-> " + panaType);
            billIDLabel.setText(String.valueOf(slipId));
            this.id = slipId;
            query = "SELECT DATE_FORMAT(created_at, '%d-%m-%y') AS formatted_date FROM order_slips WHERE slip_id = " + id + ";";
            Statement stmt2 = MyClass.C.createStatement();
            ResultSet rs2 = stmt2.executeQuery(query);
            if (rs2.next()) dateLabel.setText("date: " + rs2.getString(1));

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching bill details: " + e.getMessage());
        }
        for (ActionListener pana : panas) {
            panaTypeComboBox.addActionListener(pana);
        }
        for (ActionListener customer : customers) {
            customerComboBox.addActionListener(customer);
        }

    }

    String customerName = "", panaType = "";

    public void init() {
        String[] columnNames = {"design id", "Item Name", "Quantity", "Plating", "Raw Material Cost", "Other Details"};//jtable content
        ActionListener[] panas = panaTypeComboBox.getActionListeners();
        ActionListener[] customers = customerComboBox.getActionListeners();
        for (ActionListener pana : panas) panaTypeComboBox.removeActionListener(pana);
        for (ActionListener customer : customers) customerComboBox.removeActionListener(customer);
        panaTypeComboBox.removeAllItems();
        customerComboBox.removeAllItems();
        PromptSupport.setPrompt("Go to id", searchField);
        customerComboBox.addItem("Select Customer");
        panaTypeComboBox.addItem("All Slips");
        try {
            Statement stmt1 = MyClass.C.createStatement();
            ResultSet rs1 = stmt1.executeQuery("select type_name from ordertype;");

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
        for (ActionListener pana : panas) panaTypeComboBox.addActionListener(pana);
        for (ActionListener customer : customers) customerComboBox.addActionListener(customer);


        DefaultTableModel model = new DefaultTableModel(columnNames, 1);
        orderSlipTable.setModel(model);
        billIDLabel.setText("Bill ID: ");
        String query = "SELECT MIN(slip_id) FROM order_slips ";
        if (!(customerComboBox.getSelectedIndex() == 0)) {
            query += "WHERE customer_name = \"" + customerComboBox.getSelectedItem().toString() + "\" ";
            if (!(panaTypeComboBox.getSelectedIndex() == 0)) {
                query += "AND slip_type = \"" + Objects.requireNonNull(panaTypeComboBox.getSelectedItem()) + "\";";
            } else {
                query += ";";
            }
        } else if (!(panaTypeComboBox.getSelectedIndex() == 0)) {
            query += "WHERE slip_type =\"" + Objects.requireNonNull(panaTypeComboBox.getSelectedItem()) + "\";";
        } else query += ";";
        try {
            Statement stmt = MyClass.C.createStatement();
            System.out.println(id + " this is id");
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                if (rs.getObject(1) != null)
                    id = rs.getInt(1);
                else setCurrentBill(-1);
            } else {
                throw new RuntimeException();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

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
