package loginsignup.login.loggedin.billing.purcahseBill;

import mainpack.MyClass;
import testpackage.UtilityMethods;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;

public class PurchaseBill extends JFrame {
    private JPanel panel;
    private int billid;

    public int getBillid() {
        return billid;
    }

    public void setBillid(int billid) {
        this.billid = billid;
    }

    private JButton backButton;
    private JComboBox<String> customerComboBox;
    private JComboBox<String> dateComboBox;
    private JLabel billidLabel;
    private JTable billTable;
    private JButton submitButton;
    Vector<String> headers;
    DefaultTableModel model;
    int quantityIndex;
    int snoIndex;
    int totalIndex;
    int rawCostIndex;
    int designIDIndex;
    int itemNameIndex;

    Connection con = null;


    private boolean returnIsCellEditable(int row, int col, int[][] extraIndices) {
        if (col == headers.indexOf("Sno")) return false;
        if (extraIndices != null) {
            for (int[] index : extraIndices) {
                if (index[0] == row && index[1] == col) return false;
                if (index[0] == -1 && index[1] == col) return false;

            }
        }
        return true;
    }

    public PurchaseBill() {

    }

    private boolean designIDexists(String designID) {
        String query = "SELECT 1 FROM inventory WHERE DesignID = ? LIMIT 1";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, designID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true if at least one row is found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // or throw a custom exception if you prefer
        }
    }

    void updateInventory() {


        try {
            String customerName = customerComboBox.getSelectedItem() == null ? "" : customerComboBox.getSelectedItem().toString();
            String billQuery = "INSERT INTO billdetails (BillID, SNo, DesignID, ItemName, Quantity, RawCost, TotalFinalCost, " +
                    "OrderType, LabourCost, TotalBaseCosting, GoldRate, GoldPlatingWeight, TotalGoldCost, customer_name) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            String updateQuery = "UPDATE inventory SET TotalQuantity = TotalQuantity + ? WHERE DesignID = ?";
            String insertQuery = "INSERT INTO inventory(DesignID, TotalQuantity, itemname, price) VALUES (?, ?, ?, ?)";

            PreparedStatement updateStmt = con.prepareStatement(updateQuery);
            PreparedStatement insertStmt = con.prepareStatement(insertQuery);
            PreparedStatement billStmt = con.prepareStatement(billQuery);
            System.out.println("called");
            for (int i = 0; i < model.getRowCount() - 1; i++) {
                System.out.println("in lool");
                Object snoObj = model.getValueAt(i, snoIndex);
                Object designIDObj = model.getValueAt(i, designIDIndex);
                Object itemNameObj = model.getValueAt(i, itemNameIndex);
                Object quantityObj = model.getValueAt(i, quantityIndex);
                Object rawCostObj = model.getValueAt(i, rawCostIndex);
                Object totalCostObj = model.getValueAt(i, totalIndex);

                int sno = snoObj == null ? 0 : Integer.parseInt(snoObj.toString());
                String designID = designIDObj == null ? "" : designIDObj.toString();
                String itemName = itemNameObj == null ? "" : itemNameObj.toString();
                int quantity = quantityObj == null ? 0 : Integer.parseInt(quantityObj.toString());
                BigDecimal rawCost = rawCostObj == null ? BigDecimal.ZERO : new BigDecimal(rawCostObj.toString());
                BigDecimal totalCost = totalCostObj == null ? BigDecimal.ZERO : new BigDecimal(totalCostObj.toString());

                billStmt.setInt(1, getBillid());
                billStmt.setInt(2, sno);
                billStmt.setString(3, designID);
                billStmt.setString(4, itemName);
                billStmt.setInt(5, quantity);
                billStmt.setBigDecimal(6, rawCost);
                billStmt.setBigDecimal(7, totalCost);

                // Fill remaining required fields with defaults
                billStmt.setString(8, "purchase"); // example OrderType
                billStmt.setBigDecimal(9, BigDecimal.ZERO); // LabourCost
                billStmt.setBigDecimal(10, BigDecimal.ZERO); // TotalBaseCosting
                billStmt.setBigDecimal(11, BigDecimal.ZERO); // GoldRate
                billStmt.setBigDecimal(12, BigDecimal.ZERO); // GoldPlatingWeight
                billStmt.setBigDecimal(13, BigDecimal.ZERO); // TotalGoldCost
                billStmt.setString(14, customerName); // customer name

                billStmt.addBatch();

                designID = model.getValueAt(i, designIDIndex) == null ? "" : model.getValueAt(i, designIDIndex).toString();
                quantity = getIntegerValue(model.getValueAt(i, quantityIndex));
                itemName = model.getValueAt(i, itemNameIndex) == null ? "" : model.getValueAt(i, itemNameIndex).toString();
                int price = model.getValueAt(i, rawCostIndex) == null ? 0 : getIntegerValue(model.getValueAt(i, rawCostIndex));
                System.out.println(designIDexists(designID));
                if (designIDexists(designID)) {
                    updateStmt.setInt(1, quantity);
                    updateStmt.setString(2, designID);
                    updateStmt.addBatch();
                } else {
                    insertStmt.setString(1, designID);
                    insertStmt.setInt(2, quantity);
                    insertStmt.setString(3, itemName);
                    insertStmt.setInt(4, price);
                    insertStmt.addBatch();
                }
                System.out.println("workiing");
            }
            billStmt.executeBatch();
            updateStmt.executeBatch();
            insertStmt.executeBatch();
            con.commit();
            JOptionPane.showMessageDialog(MyClass.purchaseBill, "Inventory successfully updated.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(MyClass.purchaseBill, "Error: " + ex.getMessage());
            throw new RuntimeException();
        }
    }

    public void init() {
        setContentPane(panel);
        pack();
        headers = new Vector<>();
        headers.add("Sno");
        headers.add("designID");
        headers.add("item name");
        headers.add("Quantity");
        headers.add("Raw cost");
        headers.add("total");
        designIDIndex = headers.indexOf("designID");
        quantityIndex = headers.indexOf("Quantity");
        rawCostIndex = headers.indexOf("Raw cost");
        totalIndex = headers.indexOf("total");
        snoIndex = headers.indexOf("Sno");
        itemNameIndex = headers.indexOf("item name");
        model = new DefaultTableModel(headers, 1) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return returnIsCellEditable(row, column, null);
            }
        };
        model.setValueAt(1, 0, snoIndex);
        billTable.setModel(model);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(billid);
                try {
                    if (customerComboBox.getSelectedItem() != null && !customerComboBox.getSelectedItem().toString().trim().isEmpty()) {
                        updateInventory();
//                        makeBill(getBillid(), (customerComboBox.getSelectedItem() == null || customerComboBox.getSelectedIndex() == 0)
//                              ? ""
//                                : customerComboBox.getSelectedItem().toString(), billTable);
                    } else {
                        JOptionPane.showMessageDialog(MyClass.purchaseBill, "select a customer");
                        return;
                    }
                    con.commit();
                    con.close();
                    dispose();
                    MyClass.purchaseBill=new PurchaseBill();

                    MyClass.purchaseBill.init();
                    MyClass.purchaseBill.setVisible(true);

                } catch (Exception ex) {
                    try {
                        con.rollback();
                    } catch (SQLException exc) {
                        throw new RuntimeException(exc);
                    }
                }
            }
        });

        model.addTableModelListener(e -> {

            int col = e.getColumn();
            int row = e.getFirstRow();
            if (col == -1 || row == -1) {
                System.out.println("unexpected return from modellisterer");
                Thread.dumpStack();
                return;
            }
            TableModelListener[] listeners = UtilityMethods.removeModelListener(model);
            int quantity = 0;
            double rawCost = 0;
            //ccase of unintended calls of the model
            TableColumnModel columnModel = billTable.getColumnModel();

            if (col == quantityIndex) {
                try {
                    quantity = getIntegerValue(model.getValueAt(row, quantityIndex));
                    if (quantity == 0) model.setValueAt("", row, quantityIndex);

                } catch (NumberFormatException exception) {
                    model.setValueAt("", row, quantityIndex);
                }
                updateTotal(row);

            }
            if (col == rawCostIndex) {
                try {
                    rawCost = getDoubleValue(model.getValueAt(row, rawCostIndex));
                    if (rawCost == 0) model.setValueAt("", row, rawCostIndex);
                } catch (NumberFormatException ex) {
                    model.setValueAt("", row, rawCostIndex);
                }
                updateTotal(row);
            }
            if(col== designIDIndex){

                fetchAndAppend( model.getValueAt(row,designIDIndex)==null?"noidblank":model.getValueAt(row,designIDIndex).toString() ,row);
            }
            doRowManipulation(row);
            UtilityMethods.addModelListeners(listeners, model);
        });
// Map the Delete key
        InputMap inputMap = billTable.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = billTable.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "deleteRow");

        actionMap.put("deleteRow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getRowCount() == 1) return;
                model.removeRow(billTable.getSelectedRow());
                reAssignSno();
            }
        });

        backButton.addActionListener(e -> {
            setVisible(false);
            dispose();
            MyClass.purchaseBill=new PurchaseBill();
            MyClass.billingScreen.setVisible(true);
        });

        UtilityMethods.generateAndAddNames(customerComboBox);
        UtilityMethods.generateAndAddDates(dateComboBox, false);

        try {
            String query;
            query = "insert into bills() values()";
            con = DriverManager.getConnection(MyClass.login.getUrl(), MyClass.login.getLoginID(), MyClass.login.getPassword());
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet billid = stmt.getGeneratedKeys();

            if (billid.next()) setBillid(billid.getInt(1));

            billidLabel.setText("bill id:" + getBillid());
        } catch (Exception e) {
            try {
                con.rollback();
                throw new RuntimeException();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }


    }

    private void  fetchAndAppend(String designID, int row) {
        String name, rawCost;
        String query="select * from inventory where designid=?";
        try {
            PreparedStatement preparedStatement=con.prepareStatement(query);
            preparedStatement.setString(1,designID);
            ResultSet rs=preparedStatement.executeQuery();
            if(rs.next()){
//                model.setValueAt("",row,designIDIndex);
                model.setValueAt(rs.getString("itemname"),row,itemNameIndex);
                model.setValueAt(rs.getString("price"),row,rawCostIndex);

            }else {


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void makeBill(int billID, String customerName, JTable yourTable) {
        String query = "INSERT INTO billdetails (BillID, SNo, DesignID, ItemName, Quantity, RawCost, TotalFinalCost, " +
                "OrderType, LabourCost, TotalBaseCosting, GoldRate, GoldPlatingWeight, TotalGoldCost, customer_name) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            DefaultTableModel model = (DefaultTableModel) yourTable.getModel();

            for (int i = 0; i < model.getRowCount(); i++) {

            }

            JOptionPane.showMessageDialog(null, "Bill created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while creating bill: " + e.getMessage());
        }
    }


    private void doRowManipulation(int row) {
        if (row == model.getRowCount() - 1) {
            if (!isRowEmpty(row)) {
                model.setRowCount(model.getRowCount() + 1);
                model.setValueAt(getIntegerValue(model.getValueAt(row, snoIndex)) + 1, row + 1, snoIndex);
            }

        } else {
            if (isRowEmpty(row)) {
                model.removeRow(row);
                reAssignSno();
            }
        }

    }

    private void reAssignSno() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, snoIndex);
        }
    }

    private boolean isRowEmpty(int row) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            if (model.getValueAt(row, i) != null && !model.getValueAt(row, i).toString().trim().isEmpty()) {
                if (i == snoIndex) continue;
                return false;
            }
        }
        return true;
    }


    private void updateTotal(int row) {
        int quantity = getIntegerValue(model.getValueAt(row, quantityIndex));
        double rawCost = getDoubleValue(model.getValueAt(row, rawCostIndex));
        double total = quantity * rawCost;

        model.setValueAt(total == 0 ? "" : total, row, totalIndex);
        model.fireTableRowsUpdated(row, row);
        billTable.repaint();
    }

    private int getIntegerValue(Object value) {
        return (value != null && !value.toString().isEmpty()) ? Integer.parseInt(value.toString()) : 0;
    }

    private double getDoubleValue(Object value) {
        return (value != null && !value.toString().isEmpty()) ? Double.parseDouble(value.toString()) : 0.0;
    }


}
