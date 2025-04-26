package loginsignup.login.loggedin.billing.newBill;

import org.jdesktop.swingx.prompt.PromptSupport;
import testpackage.UtilityMethods;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;

import static mainpack.MyClass.*;

public class NewBill extends JFrame {
    int itemID;
    private LocalDateTime dateTime;
    private double goldrate;
    private String customerName;

    public JButton getSubmitButton() {
        return submitButton;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void createBillToOrderSlipAssosciation(int sNo, int itemID) {
        snoToItemIdMap.put(sNo, itemID);
    }

    public boolean updateThroughSlip = false;
    private JComboBox<String> customerComboBox;
    private JPanel panel;
    private int sno = 0;
    private JLabel idLabel;
    private int curBillID;
    private JTable billTable;
    private JButton backButton;
    private JComboBox<String> slipDetailComboBox;
    private JButton submitButton;
    private final HashMap<Integer, Integer> snoToItemIdMap;
    private JButton resetButton;
    private JButton undoButton;
    private JTextField slipNumberField;
    private JTextField goldRateTextField;
    private JScrollPane scrollPane;
    private JComboBox<String> dateComboBox;
    private JLabel grandTotalLabel;
    private JButton resetButton1;
    private Connection transacTemp;
    public boolean notThroughOrderSlip = true;

    public JTable getBillTable() {
        return billTable;
    }

    public NewBill() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(panel);

        pack();
        ((AbstractDocument) goldRateTextField.getDocument()).setDocumentFilter(UtilityMethods.getDocFilter());

        goldRateTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Not needed for plain text fields.
            }

            private void textChanged() {
                double goldrate = 0;
                try {
                    goldrate = goldRateTextField.getText().isEmpty() ? 0 : Double.parseDouble(goldRateTextField.getText());
                } catch (NumberFormatException e) {
                    Thread.dumpStack();
                    throw new RuntimeException();
                }
                setGoldRate(goldrate);
                reCalculateValuesAndAppend();
            }
        });

        snoToItemIdMap = new HashMap<>();
        backButton.addActionListener(e -> {
            setVisible(false);
            billingScreen.setVisible(true);
            searchResultWindow.dispose();
        });

        NewBill temp = this;
        slipNumberField.addActionListener(e -> {
            try {
                slipNumberField.getText();
            } catch (NumberFormatException ex) {
                Thread.dumpStack();
                slipNumberField.setText("");
                JOptionPane.showMessageDialog(newBill, "an error occured, invalid input");

                throw new RuntimeException();

            }

            try {

                Statement stmt = C.createStatement();
//                    String query = "select slip_id from order_slips where (slip_id=" + slipNumberField.getText() +" and customer_name='"+customerName+"'"+ ";";
                String query = "SELECT slip_id FROM order_slips WHERE slip_id=" + slipNumberField.getText() + " AND customer_name='" + customerName + "';";

                ResultSet rs = stmt.executeQuery(query);
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(newBill, "orderslip not found");
                    slipNumberField.setText("");

                    return;
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(newBill, "sql exception occured");
                Thread.dumpStack();
                throw new RuntimeException();

            }

            if (searchResultWindow.isVisible()) {
                searchResultWindow.dispose();
            }
            searchResultWindow = new SearchResultWindow(Integer.parseInt(slipNumberField.getText()));
            searchResultWindow.setVisible(true);
            submitButton.setEnabled(false);
            backButton.setEnabled(false);
            positionFrames(temp, searchResultWindow);

        });
        submitButton.addActionListener(e -> {
            if (insertData())
                linkBillToTransactions();
        });


        goldRateTextField.addActionListener(e -> {
        });
        resetButton.addActionListener(e -> UtilityMethods.csvOut(tableModel));
        customerComboBox.addActionListener(e -> customerName = customerComboBox.getSelectedItem() == null ? "" : customerComboBox.getSelectedItem().toString());
        undoButton.addActionListener(e -> {
            UtilityMethods.csvToTableModel(tableModel, "C:\\Users\\Aparw\\ShreeGurukripaJewellers\\output.csv");
            billTable.repaint();
        });
        dateComboBox.addActionListener(e -> setCurrentDate(UtilityMethods.parseDate(dateComboBox.getSelectedItem() == null ? "" : dateComboBox.getSelectedItem().toString())));
        resetButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                newBill = new NewBill();
                newBill.init();
                newBill.setVisible(true);
            }
        });
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    private void linkBillToTransactions() {
        int billID = getCurBillID();
        String customerName = getCustomerName();

        String query = "UPDATE transactions SET billid = ? WHERE customer_name = ? AND billid IS NULL";

        try (
                PreparedStatement pstmt = C.prepareStatement(query)) {

            pstmt.setInt(1, billID);
            pstmt.setString(2, customerName);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            Thread.dumpStack();
            JOptionPane.showMessageDialog(null, "Failed to update transactions!", "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException();
        }


    }

    private String getStringValue(DefaultTableModel model, int row, int columnIndex, String defaultValue) {
        Object value = model.getValueAt(row, columnIndex);
        return (value != null && !value.toString().trim().isEmpty()) ? value.toString() : defaultValue;
    }

    private boolean insertData() {

        DefaultTableModel model = (DefaultTableModel) billTable.getModel();
        int rowCount = model.getRowCount();

        // Database connection setup
        String url = "jdbc:mysql://localhost:3306/" + login.getDatabase();
        String user = login.getLoginID();
        String password = login.getPassword();
        int billID = -1;
        Connection conn = getTransacTemp();
        PreparedStatement stmt1 = null;

        String customerName = customerComboBox.getSelectedItem() == null ? "" : customerComboBox.getSelectedItem().toString();

        if (customerComboBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(newBill, "select customer please");
            return false;
        }

        try {
//            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false); // Start transaction

            String sql1 = "INSERT INTO bills (date) VALUES (?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setTimestamp(1, Timestamp.valueOf(getDateTime()));
                int affectedRows = pstmt.executeUpdate();
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) setCurBillID((rs.getInt(1)));


            } catch (SQLException exception) {
                JOptionPane.showMessageDialog(newBill, "error");
                Thread.dumpStack();
                throw new RuntimeException();
            }
            sql1 = "INSERT INTO billdetails (BillID, SNo, ItemName, DesignID, OrderType, RawCost, LabourCost, DullChillaiCost, " + "MeenaColorMeenaCost, RhodiumCost, NagSettingCost, OtherBaseCosts, TotalBaseCosting, GoldRate, " + "GoldPlatingWeight, TotalGoldCost, TotalFinalCost, OrderSlipNumber,OtherBaseCostNotes,quantity,customer_name) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
            stmt1 = conn.prepareStatement(sql1);

            for (int i = 0; i < rowCount - 1; i++) {
                int serialNo = Integer.parseInt(getStringValue(model, i, billDetails.indexOf("SNo"), "0"));
                String itemName = getStringValue(model, i, billDetails.indexOf("ItemName"), "Unknown");

                if (itemName.isEmpty()) {
                    JOptionPane.showMessageDialog(newBill, "please do not leave item name empty");
                    throw new RuntimeException();
                }


                String designID = getStringValue(model, i, billDetails.indexOf("DesignID"), "NoID");
                String orderType = slipDetailComboBox.getSelectedItem() == null ? "" : slipDetailComboBox.getSelectedItem().toString();


                if (snoToItemIdMap.containsKey(serialNo)) {
                    int itemId = snoToItemIdMap.get(serialNo);

                    // Fetch OrderType from orderslip table using itemId
                    String query = "SELECT slip_type FROM order_slips WHERE item_id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setInt(1, itemId);
                    ResultSet rs = pstmt.executeQuery();

                    orderType = slipDetailComboBox.getSelectedItem().toString();
                    if (rs.next()) {
                        orderType = rs.getString("slip_type"); // Override with DB value
                    }
                    rs.close();
                    pstmt.close();
                }
                int quantity = Integer.parseInt(getStringValue(model, i, billDetails.indexOf("Quantity"), "0"));
                if (quantity == 0) {
                    JOptionPane.showMessageDialog(newBill, "please do not leave quantity field empty");
                    return false;
                }
                double raw = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("Raw"), "0"));
                double labour = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("L"), "0"));
                double dullChillai = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("dc"), "0"));
                double mcm = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("M/CM"), "0"));
                double rh = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("Rh"), "0"));
                double nag = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("Nag"), "0"));
                double other = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("Other"), "0"));
                double totalBaseCost = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("+G"), "0"));
                double goldRate = newBill.goldrate;
                double goldPlatingWeight = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("Gold(ing g)"), "0"));
                double totalGoldCost = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("gold cost"), "0"));
                double totalFinalCost = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("total"), "0"));
                String otherDetails = getStringValue(model, i, billDetails.indexOf("OtherDetails"), "");
                int orderSlipNumber = snoToItemIdMap.getOrDefault(serialNo, 0);

                stmt1.setInt(1, getCurBillID()); // Replace with actual BillID
                stmt1.setInt(2, serialNo);
                stmt1.setString(3, itemName);
                stmt1.setString(4, designID);
                stmt1.setString(5, orderType);
                stmt1.setDouble(6, raw);
                stmt1.setDouble(7, labour);
                stmt1.setDouble(8, dullChillai);
                stmt1.setDouble(9, mcm);
                stmt1.setDouble(10, rh);
                stmt1.setDouble(11, nag);
                stmt1.setDouble(12, other);
                stmt1.setDouble(13, totalBaseCost);
                stmt1.setDouble(14, goldRate);
                stmt1.setDouble(15, goldPlatingWeight);
                stmt1.setDouble(16, totalGoldCost);
                stmt1.setDouble(17, totalFinalCost);
                stmt1.setInt(18, orderSlipNumber);
                stmt1.setString(19, otherDetails);
                stmt1.setInt(20, quantity);
                stmt1.setString(21, customerName);
                stmt1.addBatch();

            }


            stmt1.executeBatch();

            conn.commit(); // Commit transaction


            JOptionPane.showMessageDialog(null, "Bill details saved successfully! bill id is " + getCurBillID());
            setCustomerName(customerName);
            billTable.repaint();
            return true;
        } catch (Exception ex) {
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback on error
                }

            } catch (SQLException rollbackEx) {
                Thread.dumpStack();
                throw new RuntimeException();
            }
            Thread.dumpStack();
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving bill details!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;

        } finally {
            try {
                if (stmt1 != null) stmt1.close();
//                if (conn != null) conn.close();
            } catch (SQLException closeEx) {
                Thread.dumpStack();
            }
        }
    }

    private void setCurBillID(int billID) {
        curBillID = billID;
    }

    public int getCurBillID() {
        return curBillID;
    }

    private void setCurrentDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return;
        }
        dateTime = localDateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    private void reCalculateValuesAndAppend() {
        List<Integer> mathColumns = Arrays.asList(billDetails.indexOf("L"), billDetails.indexOf("Raw"), billDetails.indexOf("dc"), billDetails.indexOf("M/CM"), billDetails.indexOf("Rh"), billDetails.indexOf("Nag"), billDetails.indexOf("Other"), billDetails.indexOf("+G"), billDetails.indexOf("Gold(ing g)"), billDetails.indexOf("gold cost"), billDetails.indexOf("total"));
        double plusG = 0;
//        TableModelListener[] listeners = removeModelListener(tableModel);
        int i = 0;
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            for (int col : mathColumns) {

                Object value = tableModel.getValueAt(row, col);
                double numericValue = 0.0;

                if (value != null && !value.toString().trim().isEmpty()) {
                    try {
                        numericValue = Double.parseDouble(value.toString());
                    } catch (NumberFormatException e) {
                        Thread.dumpStack();
                    }

                }

                if (i < 7) {
                    plusG += numericValue;
                }
                double goldrate = this.goldrate;
                double goldcost;
                if (i == 8) {
                    goldcost = goldrate * numericValue;
                    tableModel.setValueAt(goldcost == 0 ? "" : goldcost, row, billDetails.indexOf("gold cost"));

                    tableModel.setValueAt((goldcost + plusG) == 0 ? "" : (goldcost + plusG), row, billDetails.indexOf("total"));
                }

                i++;

            }
            int quantity = tableModel.getValueAt(row, billDetails.indexOf("Quantity")) != null && !tableModel.getValueAt(row, billDetails.indexOf("Quantity")).toString().isEmpty() ? Integer.parseInt((tableModel.getValueAt(row, billDetails.indexOf("Quantity")).toString())) : 0;
            plusG = plusG * quantity;
            tableModel.setValueAt(plusG == 0 ? "" : plusG, row, billDetails.indexOf("+G"));

            plusG = 0;
            i = 0;
        }

        for (int row = 0; row < tableModel.getRowCount() - 1; row++)
            checkAndRemoveRow(row, tableModel, billDetails.indexOf("SNo"), false);
        billTable.repaint();
        scrollPane.repaint();
//        addModelListeners(listeners, tableModel);

    }

    private void setGoldRate(double goldrate) {
        this.goldrate = goldrate;
    }

    private double getDoubleValue(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.toString().trim());
        } catch (NumberFormatException e) {

            return 0.0; // Return 0.0 if parsing fails
        }
    }

    public Connection getTransacTemp() {
        return transacTemp;
    }

    DefaultTableModel tableModel;
    Vector<String> billDetails = new Vector<>();

    public void init() {
        if (!customerComboBox.isEnabled()) customerComboBox.setEnabled(true);
        billTable.getInputMap(JTable.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DELETE"), "deleteRow");
        billTable.getActionMap().put("deleteRow", new AbstractAction() {
            @Override

            public void actionPerformed(ActionEvent e) {
                Action oldAction = billTable.getActionMap().get("deleteRow");
                billTable.getActionMap().remove("deleteRow");
                int selectedRow = billTable.getSelectedRow(); // Get selected row
                TableModelListener[] listeners = UtilityMethods.removeModelListener((tableModel));

                if (selectedRow != -1) {
                    // Ensure a row is selected
                    if (billTable.getRowCount() != 1)
                        checkAndRemoveRow(selectedRow, tableModel, billDetails.indexOf("SNo"), true);// Remove the row
                }

                UtilityMethods.addModelListeners(listeners, tableModel);
                billTable.getActionMap().put("deleteRow", oldAction);
                int selectedrow = billTable.getSelectedRow();
                tableModel.fireTableDataChanged();
                billTable.setRowSelectionInterval(selectedrow, selectedrow);
            }

        });

        try {
            if (transacTemp != null) if (!transacTemp.isClosed()) transacTemp.close();
            transacTemp = getConnection(login.getUrl(), login.getLoginID(), login.getPassword());
            transacTemp.setAutoCommit(false);
        } catch (SQLException e) {
            Thread.dumpStack();
            throw new RuntimeException();
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PromptSupport.setPrompt("Slip Number", slipNumberField);
        PromptSupport.setForeground(Color.GRAY, slipNumberField);
        PromptSupport.setPrompt("gold rate", goldRateTextField);
        PromptSupport.setForeground(Color.GRAY, goldRateTextField);
        listOfNonEditableCells = new Vector<>();
        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusYears(1);
        billDetails = new Vector<>();
        billDetails.add("SNo");
        billDetails.add("OrderSlip/quantity");
        billDetails.add("ItemName");
        billDetails.add("Quantity");
        billDetails.add("DesignID");
        billDetails.add("L");
        billDetails.add("Raw");
        billDetails.add("dc");
        billDetails.add("M/CM");
        billDetails.add("Rh");
        billDetails.add("Nag");
        billDetails.add("Other");
        billDetails.add("OtherDetails");
        billDetails.add("+G");
        billDetails.add("Gold(ing g)");
        billDetails.add("gold cost");
        billDetails.add("total");
        tableModel = new DefaultTableModel(billDetails, 1) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == billDetails.indexOf("SNo")) return false;
                if (column == billDetails.indexOf("OrderSlip/quantity")) return false;
                if (column == billDetails.indexOf("total")) return false;
                if (column == billDetails.indexOf("+G")) return false;
                if (column == billDetails.indexOf("gold cost")) return false;
                String value = tableModel.getValueAt(row, billDetails.indexOf("OrderSlip/quantity")) == null ? "" : tableModel.getValueAt(row, billDetails.indexOf("OrderSlip/quantity")).toString();
                return (column != billDetails.indexOf("Quantity") && column != billDetails.indexOf("DesignID")) || value.isEmpty();
            }

        };

        billTable.setModel(tableModel);
        while (!today.isBefore(oneYearAgo)) {
            today = today.minusDays(1);
        }
        tableModel.addTableModelListener(e -> {
            // Get the row index where the data is stored

            int row = e.getFirstRow();
            int col = e.getColumn();
            if (row == -1 || col == -1) {
                return;
            }
            if (customerComboBox.isEnabled()) {
                customerComboBox.setEnabled(false);
            }
            int snoValue = (tableModel.getValueAt(row, billDetails.indexOf("SNo")) != null && !tableModel.getValueAt(row, billDetails.indexOf("SNo")).toString().isEmpty()) ? Integer.parseInt(tableModel.getValueAt(row, billDetails.indexOf("SNo")).toString().contentEquals("") ? "0" : tableModel.getValueAt(row, billDetails.indexOf("SNo")).toString()) : -1;
            TableModelListener[] listeners = UtilityMethods.removeModelListener(tableModel);
            if (snoValue == -1) {
                sno++;

                tableModel.setValueAt(sno, row, billDetails.indexOf("SNo"));
                snoValue = sno;

//                    tableModel.getValueAt(row, billDetails.indexOf("SNo")) == null ? -1 : Integer.parseInt(tableModel.getValueAt(row, billDetails.indexOf("SNo")).toString().contentEquals("") ? "0" : tableModel.getValueAt(row, billDetails.indexOf("SNo")).toString());
            }


            if (row == tableModel.getRowCount() - 1) {
                // Check if any column in this row has data
                boolean rowHasData = false;
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    if (tableModel.getValueAt(row, i) != null && !tableModel.getValueAt(row, i).toString().trim().isEmpty()) {
                        rowHasData = true;
                        break;
                    }
                }


                // If the last row has data, add a new empty row
                if (rowHasData) {
                    tableModel.addRow(new Vector<>(billDetails.size()));
                }

            }
            int designIdIndex = billDetails.indexOf("DesignID");
            int quantityIndex = billDetails.indexOf("Quantity");

            int quantity = 0;
            try {
                quantity = (tableModel.getValueAt(row, quantityIndex) == null || tableModel.getValueAt(row, quantityIndex).toString().trim().isEmpty()) ? 0 : Integer.parseInt(tableModel.getValueAt(row, quantityIndex).toString().trim());
            } catch (NumberFormatException ex) {
                tableModel.setValueAt("", row, col);

            }

            String desingID = tableModel.getValueAt(row, designIdIndex) == null ? "" : tableModel.getValueAt(row, designIdIndex).toString();
            if (col == designIdIndex && !searchResultWindow.isSearchFlag()) {
                String designID = tableModel.getValueAt(row, designIdIndex) == null ? "" : tableModel.getValueAt(row, designIdIndex).toString().trim();

                if (!designID.isEmpty()) {
                    String query = "SELECT itemname,price FROM inventory WHERE DesignID = ?";

                    try (PreparedStatement stmt = C.prepareStatement(query)) {

                        stmt.setString(1, designID);
                        ResultSet rs = stmt.executeQuery();

                        if (rs.next()) {

                            listOfNonEditableCells.add(new Integer[]{row, billDetails.indexOf("ItemName")});
                            String itemName = rs.getString("itemname");
                            String price = rs.getString("price");
                            tableModel.setValueAt(itemName, row, billDetails.indexOf("ItemName"));
                            tableModel.setValueAt(price, row, billDetails.indexOf("Raw"));
                        } else {
                            listOfNonEditableCells.removeIf(cell -> cell[0] == row && cell[1] == billDetails.indexOf("ItemName"));

                            tableModel.setValueAt("", row, designIdIndex);
                            tableModel.setValueAt("", row, billDetails.indexOf("ItemName"));
                            tableModel.setValueAt("", row, billDetails.indexOf("Raw"));
                        }

                        DefaultTableModel newModel = redoModel();

                        billTable.setModel(newModel);
                        tableModel = newModel;
                    } catch (SQLException ex) {
                        Thread.dumpStack();
                        JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // If designID field is empty, clear ItemName as well
                    tableModel.setValueAt("", row, designIdIndex);
                    tableModel.setValueAt("", row, billDetails.indexOf("ItemName"));
                    tableModel.setValueAt("", row, billDetails.indexOf("Raw"));
                    listOfNonEditableCells.removeIf(cell -> cell[0] == row && cell[1] == billDetails.indexOf("ItemName"));

                }
            }

            if ((col == quantityIndex)) {

                Object designID = tableModel.getValueAt(row, designIdIndex);
                if (notThroughOrderSlip && designID != null && !designID.toString().contentEquals("")) {
                    try {
                        String query = "select totalquantity from inventory where designid='" + designID + "';";
                        Statement stmt = C.createStatement();
                        ResultSet resultSet = stmt.executeQuery(query);
                        if (resultSet.next()) if (resultSet.getInt("totalquantity") < quantity)
                            JOptionPane.showMessageDialog(newBill, "warning stock not remaining");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }


                }
                if (snoToItemIdMap.containsKey(snoValue)) {
                    try {
                        Statement stmt = C.createStatement();
                        String query = "select * from order_slips where item_id=" + snoToItemIdMap.get(snoValue);
                        int item_id = snoToItemIdMap.get(snoValue);
                        int netQuantity = 0;
                        for (int key : snoToItemIdMap.keySet()) {
                            if (snoToItemIdMap.get(key) == item_id) {
                                netQuantity = netQuantity + ((tableModel.getValueAt(key - 1, quantityIndex) != null && !tableModel.getValueAt(key - 1, quantityIndex).toString().isEmpty()) ? Integer.parseInt(tableModel.getValueAt(key - 1, quantityIndex).toString()) : 0);
                            }
                            ResultSet rs = stmt.executeQuery(query);
                            if (rs.next()) {
                                int quantityTemp = rs.getInt("quantity") - rs.getInt("billed_quantity");
                                if (quantityTemp < netQuantity) {
                                    JOptionPane.showMessageDialog(newBill, "invalid quanity has been entered for an item connected to a order slip");
                                    tableModel.setValueAt("", row, quantityIndex);
                                }
                            } else {
                                JOptionPane.showMessageDialog(newBill, "error has occured");
                                System.exit(-1);
                                throw new RuntimeException();
                            }
                        }

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }

            double labour = getDoubleValue(tableModel.getValueAt(row, billDetails.indexOf("L")));
            tableModel.setValueAt(labour == 0 ? "" : labour, row, billDetails.indexOf("L"));
            double raw = getDoubleValue(tableModel.getValueAt(row, billDetails.indexOf("Raw")));
            tableModel.setValueAt(raw == 0 ? "" : raw, row, billDetails.indexOf("Raw"));
            double dullChillai = getDoubleValue(tableModel.getValueAt(row, billDetails.indexOf("dc")));
            tableModel.setValueAt(dullChillai == 0 ? "" : dullChillai, row, billDetails.indexOf("dc"));
            double mCm = getDoubleValue(tableModel.getValueAt(row, billDetails.indexOf("M/CM")));
            tableModel.setValueAt(mCm == 0 ? "" : mCm, row, billDetails.indexOf("M/CM"));
            double rh = getDoubleValue(tableModel.getValueAt(row, billDetails.indexOf("Rh")));
            tableModel.setValueAt(rh == 0 ? "" : rh, row, billDetails.indexOf("Rh"));
            double nag = getDoubleValue(tableModel.getValueAt(row, billDetails.indexOf("Nag")));
            tableModel.setValueAt(nag == 0 ? "" : nag, row, billDetails.indexOf("Nag"));
            double other = getDoubleValue(tableModel.getValueAt(row, billDetails.indexOf("Other")));
            tableModel.setValueAt(other == 0 ? "" : other, row, billDetails.indexOf("Other"));
            double plusG;
            double goldIngG = getDoubleValue(tableModel.getValueAt(row, billDetails.indexOf("Gold(ing g)")));
            tableModel.setValueAt(goldIngG == 0 ? "" : goldIngG, row, billDetails.indexOf("Gold(ing g)"));
            double goldCost;
            double total;
//                if (col == billDetails.indexOf("L") || col == billDetails.indexOf("Raw") || col == billDetails.indexOf("dc") || col == billDetails.indexOf("M/CM") || col == billDetails.indexOf("Rh") || col == billDetails.indexOf("Nag") || col == billDetails.indexOf("Other") || col == billDetails.indexOf("+G") || col == billDetails.indexOf("Gold(ing g)") || col == billDetails.indexOf("gold cost") || col == billDetails.indexOf("total"))

            plusG = labour + raw + dullChillai + mCm + rh + nag + other;
            goldCost = goldIngG * goldrate;
            plusG *= quantity;
            total = plusG + goldCost;
            tableModel.setValueAt(total == 0 ? "" : total, row, billDetails.indexOf("total"));
            tableModel.setValueAt(plusG == 0 ? "" : plusG, row, billDetails.indexOf("+G"));
            tableModel.setValueAt(goldCost == 0 ? "" : goldCost, row, billDetails.indexOf("gold cost"));


            if (updateThroughSlip) {
                createBillToOrderSlipAssosciation(sno, itemID);
                updateThroughSlip = false;
            }
            checkAndRemoveRow(row, tableModel, billDetails.indexOf("SNo"), false);
            double grandTotal = 0;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                double value = tableModel.getValueAt(i, billDetails.indexOf("total")) == null ? 0 : tableModel.getValueAt(i, billDetails.indexOf("total")).toString().isEmpty() ? 0 : Double.parseDouble(tableModel.getValueAt(i, billDetails.indexOf("total")).toString());
                grandTotal += value;
            }
            grandTotalLabel.setText("Grand total:" + grandTotal);

            billTable.repaint();
            UtilityMethods.addModelListeners(listeners, tableModel);
            for (TableModelListener l : listeners) System.out.println(l);
            System.out.println("listeners added");
        });


        try {


            Statement stmt2 = C.createStatement();
            ResultSet rs2 = stmt2.executeQuery("select type_name from ordertype;");
            while (rs2.next()) {
                slipDetailComboBox.addItem(rs2.getString(1));
            }
            pack();
            setExtendedState(JFrame.MAXIMIZED_BOTH);

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        UtilityMethods.generateAndAddNames(customerComboBox);
        UtilityMethods.generateAndAddDates(dateComboBox, false);
    }

    int i = 0;

//    private void generateBillID() {
//        try {
//            Statement stmt1;
//            stmt1 = C.createStatement();
//            ResultSet rs1;
//            String query = "SELECT MAX(BillID) FROM bills;";
//            rs1 = stmt1.executeQuery(query);
//            int newBillID; // Default BillID if no bills exist
//            if (rs1.next()) {
//                int lastBillID = rs1.getInt(1);
//                if (rs1.wasNull()) {
//                    newBillID = 1; // If no bills, set BillID to 1
//                } else {
//                    newBillID = lastBillID + 1; // Increment the last BillID
//                }
//                setCurBillID(newBillID);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private void checkAndRemoveRow(int row, DefaultTableModel tableModel, int snoIndex, boolean forceRemove) {
        // Ensure the row index is valid
//        Thread.dumpStack();

        if (row < 0 || row >= tableModel.getRowCount() - 1) {
            return; // Invalid row index, exit method last row can not be deleted
        }
        boolean isEmpty = true;

        // Check if all columns in the given row are empty
        if (!forceRemove) for (int col = 0; col < tableModel.getColumnCount(); col++) {
            if (col == snoIndex) continue;
            Object value = tableModel.getValueAt(row, col);
            if (value != null && !value.toString().trim().isEmpty()) {
                isEmpty = false;
                break;
            }
        }

        if (isEmpty) {
//            try {

//            }
//            catch ()
            relinkAssociations(row + 1);
            tableModel.removeRow(row);
            reCheckEnableDisableInItemNameColumn();

            int sno = 0;
            for (int i = 0; i < tableModel.getRowCount() - 1; i++) {
                sno++; // Increment SNo
                tableModel.setValueAt(sno, i, snoIndex); // Set new value in SNo column
            }
            tableModel.setValueAt("", billTable.getRowCount() - 1, billDetails.indexOf("SNo"));
            this.sno = sno;
        }
    }

    private void reCheckEnableDisableInItemNameColumn() {
        listOfNonEditableCells.removeIf(cell -> cell[1] == billDetails.indexOf("ItemName"));
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String value = tableModel.getValueAt(i, billDetails.indexOf("DesignID")) == null ? "" : tableModel.getValueAt(i, billDetails.indexOf("DesignID")).toString();
            if (!value.isEmpty()) listOfNonEditableCells.add(new Integer[]{i, billDetails.indexOf("ItemName")});
        }
    }

    private void relinkAssociations(int removeSNO) //THIS FUNCTION IS ONLY CALLED WHEN DELETING A ITEM ROW FROM BILL TABLE WHICH
//            CORRUSPONDS TO A ORDER SLIP

    {
        if (snoToItemIdMap.get(removeSNO) == null) return;
        HashMap<Integer, Integer> tempMap = new HashMap<>();
        int itemid = snoToItemIdMap.get(removeSNO);
        System.out.println(tableModel.getValueAt(removeSNO, billDetails.indexOf("Quantity")) == null ? "" : tableModel.getValueAt(removeSNO, billDetails.indexOf("Quantity")).toString());
        int quantity = Integer.parseInt(tableModel.getValueAt(removeSNO - 1, billDetails.indexOf("Quantity")).toString());
        Connection temp = getTransacTemp();

        try {
            String query = "update order_slips set billed_quantity =billed_quantity-? where item_id=?";

            PreparedStatement stmt = temp.prepareStatement(query);
            stmt.setInt(1, quantity);
            stmt.setInt(2, itemid);


            stmt.executeUpdate();
            if (searchResultWindow.isVisible())
                searchResultWindow.fetchData(searchResultWindow.getTableModel());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Iterator<Integer> i = snoToItemIdMap.keySet().iterator();
        while (i.hasNext()) {
            int key = i.next();
            if (key < removeSNO) tempMap.put(key, snoToItemIdMap.get(key));
            if (key > removeSNO) {
                tempMap.put(key - 1, snoToItemIdMap.get(key));
            }
            if (key == removeSNO) i.remove();
        }
        snoToItemIdMap.clear();
        snoToItemIdMap.putAll(tempMap);

    }

    Vector<Integer[]> listOfNonEditableCells;

    DefaultTableModel redoModel() {
        Vector<Vector<String>> v = new Vector<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Vector<String> data = new Vector<>();

            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                Object cellValue = tableModel.getValueAt(i, j);
                data.add(cellValue != null ? cellValue.toString() : ""); // Avoid NullPointerException
            }
            v.add(data);
        }


        DefaultTableModel m = new DefaultTableModel(v, billDetails) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Avoid IndexOutOfBoundsException

                // Loop through all non-editable cells
                for (Integer[] cell : listOfNonEditableCells) {
                    if (cell[0] == row && cell[1] == column) {
                        return false;
                    }
                }

                if (column == billDetails.indexOf("SNo")) return false;
                if (column == billDetails.indexOf("OrderSlip/quantity")) return false;
                if (column == billDetails.indexOf("total")) return false;
                if (column == billDetails.indexOf("+G")) return false;
                if (column == billDetails.indexOf("gold cost")) return false;
                String value = tableModel.getValueAt(row, billDetails.indexOf("OrderSlip/quantity")) == null ? "" : tableModel.getValueAt(row, billDetails.indexOf("OrderSlip/quantity")).toString();
                return (column != billDetails.indexOf("Quantity") && column != billDetails.indexOf("DesignID")) || value.isEmpty();
            }
        };

        for (TableModelListener tlm : ((DefaultTableModel) billTable.getModel()).getTableModelListeners()) {
            m.addTableModelListener(tlm);
        }
        return m;
    }

    public JButton getBackButton() {
        return backButton;
    }
}

