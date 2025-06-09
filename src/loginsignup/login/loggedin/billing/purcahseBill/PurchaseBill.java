package loginsignup.login.loggedin.billing.purcahseBill;

import mainpack.MyClass;
import testpackage.UtilityMethods;
import static testpackage.DBStructure.*;
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
    private static final int ROLLBACKDONE = 18 * 1;
    private static final int SUCCESS_DONE = 18 * 2;
    private static final int ERROR_DONE = 18 * 3;
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

    private Connection con = null;

    public Connection getCon() {
        return con;
    }

    private boolean returnIsCellEditable(int row, int col, int[][] extraIndices) {
        if (col == snoIndex) return false;
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
        try (PreparedStatement stmt = getCon().prepareStatement(query)) {
            stmt.setString(1, designID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true if at least one row is found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // or throw a custom exception if you prefer
        }
    }

    int insertData() {


        try   {
            String customerName = customerComboBox.getSelectedItem() == null ? "" : customerComboBox.getSelectedItem().toString();
            String billDetailQuery = "INSERT INTO "+BILLDETAILS_TABLE+" ("+BILLDETAILS_BILL_ID+", "+BILLDETAILS_SNO+", "+BILLDETAILS_DESIGN_ID+", "+BILLDETAILS_ITEM_NAME+", "+BILLDETAILS_QUANTITY+", "+BILLDETAILS_RAW_COST+", "+BILLDETAILS_TOTAL_FINAL_COST+", " + ""+BILLDETAILS_ORDER_TYPE+", "+BILLDETAILS_LABOUR_COST+", "+BILLDETAILS_TOTAL_BASE_COSTING+", "+BILLDETAILS_GOLD_RATE+", "+BILLDETAILS_GOLD_PLATING_WEIGHT+", "+BILLDETAILS_TOTAL_GOLD_COST+") " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String billsTableQuery = "update "+BILLS_TABLE+" set "+BILLS_DATE+" = ?,"+BILLS_CUSTOMER_NAME+"=? where "+BILLS_BILL_ID+"=?";
            String updateInventoryQuery = "UPDATE "+INVENTORY_TABLE+" SET "+INVENTORY_TOTAL_QUANTITY+" = "+INVENTORY_TOTAL_QUANTITY+" + ? WHERE "+INVENTORY_DESIGN_ID+" = ?";
            String insertInventoryQuery = "INSERT INTO "+INVENTORY_TABLE+"("+INVENTORY_DESIGN_ID+", "+INVENTORY_TOTAL_QUANTITY+", "+INVENTORY_ITEM_NAME+", getBuyPrice) VALUES (?, ?, ?, ?)";
            String updateOtherQuery = "update inventory set getBuyPrice = ? , itemname=? where DesignID = ?";
            String customerTableQuery = "update "+CUSTOMERS_TABLE+" set "+CUSTOMERS_BALANCE+" = "+CUSTOMERS_BALANCE+" - ? where "+CUSTOMERS_CUSTOMER_NAME+" = ?";


            PreparedStatement billsTableStatement = getCon().prepareStatement(billsTableQuery);
            PreparedStatement customersTableStatement = getCon().prepareStatement(customerTableQuery);
            PreparedStatement updateStmt = getCon().prepareStatement(updateInventoryQuery);
            PreparedStatement insertStmt = getCon().prepareStatement(insertInventoryQuery);
            PreparedStatement billStmt = getCon().prepareStatement(billDetailQuery);
            PreparedStatement updateRawPriceStatement = getCon().prepareStatement(updateOtherQuery);

            System.out.println("called");
            billsTableStatement.setTimestamp(1, Timestamp.valueOf(UtilityMethods.getDate(dateComboBox.getSelectedItem())));
            billsTableStatement.setString(2, customerName);
            billsTableStatement.setInt(3, billid);
            BigDecimal grandTotal = BigDecimal.ZERO;
            for (int i = 0; i < model.getRowCount() - 1; i++) {
                System.out.println("in loop");
                Object snoObj = model.getValueAt(i, snoIndex);
                Object designIDObj = model.getValueAt(i, designIDIndex);
                Object itemNameObj = model.getValueAt(i, itemNameIndex);
                Object quantityObj = model.getValueAt(i, quantityIndex);
                Object rawCostObj = model.getValueAt(i, rawCostIndex);
                Object totalCostObj = model.getValueAt(i, totalIndex);

                int sno = snoObj == null ? 0 : Integer.parseInt(snoObj.toString());
                String designID = designIDObj == null ? "" : designIDObj.toString();
                String itemName = itemNameObj == null ? "" : itemNameObj.toString();
                int quantity = quantityObj == null || quantityObj.toString().isEmpty() ? 0 : Integer.parseInt(quantityObj.toString());
                BigDecimal rawCost = rawCostObj == null ? BigDecimal.ZERO : new BigDecimal(rawCostObj.toString());
                BigDecimal totalCost = totalCostObj == null || totalCostObj.toString().isEmpty() ? BigDecimal.ZERO : new BigDecimal(totalCostObj.toString());
                System.out.println("quantity is " + quantity);
                if (quantity == 0) {
                    try {
                        getCon().rollback();
                        MyClass.purchaseBill = new PurchaseBill();
                        MyClass.purchaseBill.init(model, getStringValue(customerComboBox.getSelectedItem()), getStringValue(dateComboBox.getSelectedItem()));
                        MyClass.purchaseBill.setVisible(true);
                        dispose();
                        return ROLLBACKDONE;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        return ERROR_DONE;
                    }


                }
                billStmt.setInt(1, getBillid());
                billStmt.setInt(2, sno);
                billStmt.setString(3, designID);
                billStmt.setString(4, itemName);
                billStmt.setInt(5, -quantity);
                billStmt.setBigDecimal(6, rawCost);
                billStmt.setBigDecimal(7, totalCost.negate());
                grandTotal = grandTotal.add(totalCost);
                // Fill remaining required fields with defaults
                billStmt.setString(8, "purchase"); // example OrderType
                billStmt.setBigDecimal(9, BigDecimal.ZERO); // LabourCost
                billStmt.setBigDecimal(10, BigDecimal.ZERO); // TotalBaseCosting
                billStmt.setBigDecimal(11, BigDecimal.ZERO); // GoldRate
                billStmt.setBigDecimal(12, BigDecimal.ZERO); // GoldPlatingWeight
                billStmt.setBigDecimal(13, BigDecimal.ZERO); // TotalGoldCost
                billStmt.addBatch();
                designID = model.getValueAt(i, designIDIndex) == null ? "" : model.getValueAt(i, designIDIndex).toString();
                quantity = getIntegerValue(model.getValueAt(i, quantityIndex));
                itemName = model.getValueAt(i, itemNameIndex) == null ? "" : model.getValueAt(i, itemNameIndex).toString();
                int price = model.getValueAt(i, rawCostIndex) == null ? 0 : getIntegerValue(model.getValueAt(i, rawCostIndex));
                System.out.println(designIDexists(designID));
                if (designIDexists(designID)) {
                    if (designID.contentEquals("")) continue;
                    updateStmt.setInt(1, quantity);
                    updateStmt.setString(2, designID);
                    updateRawPriceStatement.setDouble(1, price);
                    updateRawPriceStatement.setString(2, itemName);
                    updateRawPriceStatement.setString(3, designID);
                    updateRawPriceStatement.addBatch();
                    updateStmt.addBatch();

                } else {
                    if (designID.contentEquals("")) continue;
                    insertStmt.setString(1, designID);
                    insertStmt.setInt(2, quantity);
                    insertStmt.setString(3, itemName);
                    insertStmt.setInt(4, price);
                    insertStmt.addBatch();
                }

                System.out.println("workiing");

            }
            System.out.println(grandTotal.doubleValue());
            customersTableStatement.setBigDecimal(1, grandTotal);
            customersTableStatement.setString(2, customerName);
            System.out.println(grandTotal.doubleValue() + " is the grand total of this bill");
            customersTableStatement.executeUpdate();
            billsTableStatement.executeUpdate();
            billStmt.executeBatch();
            updateStmt.executeBatch();
            updateRawPriceStatement.executeBatch();
            insertStmt.executeBatch();
            getCon().commit();


            JOptionPane.showMessageDialog(MyClass.purchaseBill, "Inventory successfully updated.");
            return SUCCESS_DONE;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(MyClass.purchaseBill, "Error: " + ex.getMessage());
            try {
                getCon().rollback();
                dispose();

            } catch (SQLException e) {
                MyClass.purchaseBill = new PurchaseBill();
                MyClass.purchaseBill.init();
                MyClass.purchaseBill.setVisible(true);
                throw new RuntimeException(e);
            }
            return ERROR_DONE;
        }
    }

    private void init(DefaultTableModel model, String customerName, String date) {
        init();
        billTable.setModel(model);
        this.model = model;
        System.out.println(customerName);
        customerComboBox.setSelectedItem(customerName);
        dateComboBox.setSelectedItem(date);
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
                    int returnCode;
                    if (customerComboBox.getSelectedItem() != null && customerComboBox.getSelectedIndex() != 0) {
                        if ((returnCode = insertData()) == SUCCESS_DONE) {
                            getCon().commit();
                            getCon().close();
                            dispose();
                                MyClass.purchaseBill = new PurchaseBill();

                            MyClass.purchaseBill.init();
                            MyClass.purchaseBill.setVisible(true);
                        } else if (returnCode == ROLLBACKDONE) {
                            JOptionPane.showMessageDialog(MyClass.purchaseBill, "rollback sucess");
                        } else {
                            JOptionPane.showMessageDialog(MyClass.purchaseBill, "something is off");
                        }
//                        makeBill(getBillid(), (customerComboBox.getSelectedItem() == null || customerComboBox.getSelectedIndex() == 0)
//                              ? ""
//                                : customerComboBox.getSelectedItem().toString(), billTable);
//                                : customerComboBox.getSelectedItem().toString(), billTable);
                    } else {
                        JOptionPane.showMessageDialog(MyClass.purchaseBill, "select a customer");

                    }


                } catch (Exception ex) {
                    try {
                        getCon().rollback();
                        ex.printStackTrace();
                        throw new RuntimeException(ex);
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
            double quantity = 0;
            double rawCost = 0;
            //ccase of unintended calls of the model
            TableColumnModel columnModel = billTable.getColumnModel();

            if (col == quantityIndex) {
                try {
                    quantity = getDoubleValue(model.getValueAt(row, quantityIndex));
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
                if (!getStringValue(model.getValueAt(row, designIDIndex)).isEmpty() && designIDexists(getStringValue(model.getValueAt(row, designIDIndex)))) {
                    JOptionPane.showMessageDialog(MyClass.purchaseBill, "raw getBuyPrice will be update in inventory, re enter design id to avoid this");
                }
                updateTotal(row);
            }
            if (col == designIDIndex) {

                fetchAndAppend(model.getValueAt(row, designIDIndex) == null ? "noidblank" : model.getValueAt(row, designIDIndex).toString(), row);
                billTable.repaint();
            }
            if (col == itemNameIndex) {
                if (designIDexists(getStringValue(model.getValueAt(row, designIDIndex))))
                    JOptionPane.showMessageDialog(MyClass.purchaseBill, "आइटम का नाम इन्वन्टोरी मे बदल जाएगा, कृपया नाम न बदलने की स्थिति मे दुबारा से designid डालें ");
                String stringValueUperCase = getStringValue(model.getValueAt(row, itemNameIndex)).toUpperCase();
                String valueToReplace=MyClass.codeToItemName.get(stringValueUperCase);
                if(valueToReplace!=null){
                    model.setValueAt(valueToReplace,row,itemNameIndex);
                }
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
            MyClass.purchaseBill = new PurchaseBill();
            MyClass.billingScreen.setVisible(true);
        });

        UtilityMethods.generateAndAddNames(customerComboBox);
        UtilityMethods.generateAndAddDates(dateComboBox, false);

        try {
            String query;
            query = "insert into "+BILLS_TABLE+"() values()";
            con = DriverManager.getConnection(MyClass.login.getUrl(), MyClass.login.getLoginID(), MyClass.login.getPassword());
            getCon().setAutoCommit(false);
            Statement stmt = getCon().createStatement();
            stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet billid = stmt.getGeneratedKeys();

            if (billid.next()) setBillid(billid.getInt(1));

            billidLabel.setText("bill id:" + getBillid());
        } catch (Exception e) {
            try {
                getCon().rollback();
                throw new RuntimeException();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }


    }

    private String getStringValue(Object valueAt) {
        if (valueAt != null) return valueAt.toString();
        return "";
    }

    private void fetchAndAppend(String designID, int row) {
        String name, rawCost;
        String query = "select * from inventory where designid=?";
        try {
            PreparedStatement preparedStatement = getCon().prepareStatement(query);
            preparedStatement.setString(1, designID);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
//                model.setValueAt("",row,designIDIndex);
                model.setValueAt(rs.getString("itemname"), row, itemNameIndex);
                model.setValueAt(rs.getString("getBuyPrice"), row, rawCostIndex);

            } else {


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void makeBill(int billID, String customerName, JTable yourTable) {
        String query = "INSERT INTO "+BILLDETAILS_TABLE+" ("+BILLDETAILS_BILL_ID+", "+BILLDETAILS_SNO+", "+BILLDETAILS_DESIGN_ID+", "+BILLDETAILS_ITEM_NAME+", "+BILLDETAILS_QUANTITY+", "+BILLDETAILS_RAW_COST+", "+BILLDETAILS_TOTAL_FINAL_COST+", " + ""+BILLDETAILS_ORDER_TYPE+", "+BILLDETAILS_LABOUR_COST+", "+BILLDETAILS_TOTAL_BASE_COSTING+", "+BILLDETAILS_GOLD_RATE+", "+BILLDETAILS_GOLD_PLATING_WEIGHT+", "+BILLDETAILS_TOTAL_GOLD_COST+", customer_name) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = getCon().prepareStatement(query)) {
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
