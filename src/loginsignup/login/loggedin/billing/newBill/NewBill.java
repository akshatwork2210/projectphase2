package loginsignup.login.loggedin.billing.newBill;

import mainpack.MyClass;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import org.jdesktop.swingx.prompt.PromptSupport;
import testpackage.TestClass;

public class NewBill extends JFrame {
    int itemID;
    private double goldrate;
    private int temp = 0;
    private String customerName;

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void createBillToOrderSlipAssosciation(int sNo, int itemID) {
        snoToItemIdMap.put(sNo, itemID);
        System.out.println(sNo + "-> " + itemID);
    }

    public boolean updateThroughSlip = false;
    private JComboBox customerComboBox;
    private JPanel panel;
    private int sno = 0;
    private int curBillID;
    private JLabel idLabel;
    private JTable billTable;
    private JButton backButton;
    private JComboBox slipDetailComboBox;
    private JComboBox dateComboBox;
    private JButton submitButton;
    private HashMap<Integer, Integer> snoToItemIdMap;
    private JButton resetButton;
    private JButton undoButton;
    private JTextField slipNumberField;
    private JTextField goldRateTextField;
    private JScrollPane scrollPane;
    private Connection transacTemp;
    public boolean notThroughOrderSlip = true;

    public JTable getBillTable() {
        return billTable;
    }

    public int getCurBillID() {
        return curBillID;
    }

    // Safely get string values from JTable model
    private String getStringValue(DefaultTableModel model, int row, int col) {
        Object value = model.getValueAt(row, col);
        return (value == null) ? "" : value.toString();
    }

    // Safely get BigDecimal values from JTable model
    private BigDecimal getBigDecimalValue(DefaultTableModel model, int row, int col) {
        Object value = model.getValueAt(row, col);
        if (value == null || value.toString().trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(value.toString());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    public NewBill() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(panel);

        pack();
        ((AbstractDocument) goldRateTextField.getDocument()).setDocumentFilter(new DecimalDocumentFilter());

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

                }
                setGoldRate(goldrate);
                reCalculateValuesAndAppend();
            }
        });

        snoToItemIdMap = new HashMap<>();
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.billingScreen.setVisible(true);
                MyClass.searchResultWindow.dispose();
            }
        });

        // below comment is for submit button invalid code incase needed will use it otherwise remove it

//        submitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    int billID = getCurBillID();
//
//                    // Insert into bills table
//                    String insertBillQuery = "INSERT INTO bills (BillID) VALUES (?)";
//                    try (PreparedStatement billStmt = MyClass.C.prepareStatement(insertBillQuery)) {
//                        billStmt.setInt(1, billID);
//                        billStmt.executeUpdate();
//                    }
//
//                    // Insert into billdetails table
//                    String insertDetailsQuery = "INSERT INTO billdetails (BillID, SNo, ItemName, DesignID, OrderType, LabourCost, " + "DullChillaiCost, MeenaColorMeenaCost, RhodiumCost, NagSettingCost, OtherBaseCosts, OtherBaseCostNotes, " + "TotalBaseCosting, GoldRate, GoldPlatingWeight, TotalGoldCost, TotalFinalCost, OrderSlipNumber) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//
//                    try (PreparedStatement detailsStmt = MyClass.C.prepareStatement(insertDetailsQuery)) {
//                        DefaultTableModel model = (DefaultTableModel) billTable.getModel();
//                        String panaType = "";
//                        String orderSlipNumber = "";
//                        TableColumnModel columnModel=billTable.getColumnModel();
//
//                        for (int i = 0; i < model.getRowCount() - 1; i++) {
//                            int rowIndex = i; // Assuming 'i' is the row index
//
//
//                            String itemName = getStringValue(model, rowIndex, 1);
//                            String designID = getStringValue(model, rowIndex, 3);
//                            String otherDetails = getStringValue(model, rowIndex, 11);
//
//// Convert necessary values to BigDecimal safely
//                            BigDecimal labourCost = getBigDecimalValue(model, rowIndex, 3);
//                            BigDecimal dullChillaiCost = getBigDecimalValue(model, rowIndex, 5);
//                            BigDecimal meenaCost = getBigDecimalValue(model, rowIndex, 6);
//                            BigDecimal rhodiumCost = getBigDecimalValue(model, rowIndex, 7);
//                            BigDecimal nagSettingCost = getBigDecimalValue(model, rowIndex, 8);
//                            BigDecimal otherBaseCosts = getBigDecimalValue(model, rowIndex, 9);
//                            BigDecimal totalBaseCosting = getBigDecimalValue(model, rowIndex, 11);
//                            BigDecimal goldRate = getBigDecimalValue(model, rowIndex, 13);
//                            BigDecimal goldPlatingWeight = getBigDecimalValue(model, rowIndex, 12);
//                            BigDecimal totalGoldCost = getBigDecimalValue(model, rowIndex, 14);
//                            BigDecimal totalFinalCost = getBigDecimalValue(model, rowIndex, 15);
//
//// Insert into database
//                            detailsStmt.setInt(1, billID);
//                            detailsStmt.setInt(2, rowIndex + 1);
//                            detailsStmt.setString(3, itemName);
//                            detailsStmt.setString(4, designID);
//                            detailsStmt.setString(5, panaType);
//                            detailsStmt.setBigDecimal(6, labourCost);
//                            detailsStmt.setBigDecimal(7, dullChillaiCost);
//                            detailsStmt.setBigDecimal(8, meenaCost);
//                            detailsStmt.setBigDecimal(9, rhodiumCost);
//                            detailsStmt.setBigDecimal(10, nagSettingCost);
//                            detailsStmt.setBigDecimal(11, otherBaseCosts);
//                            detailsStmt.setString(12, otherDetails);
//                            detailsStmt.setBigDecimal(13, totalBaseCosting);
//                            detailsStmt.setBigDecimal(14, goldRate);
//                            detailsStmt.setBigDecimal(15, goldPlatingWeight);
//                            detailsStmt.setBigDecimal(16, totalGoldCost);
//                            detailsStmt.setBigDecimal(17, totalFinalCost);
//                            detailsStmt.setString(18, orderSlipNumber); // Optional
//
//                            detailsStmt.addBatch();
//                        }
//                        detailsStmt.executeBatch();
//                        getTransacTemp().close();
//                    }
//
//                    System.out.println("Data inserted successfully.");
//
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                    try {
//                        getTransacTemp().commit();
//                        JOptionPane.showMessageDialog(null,"commited");
//                    } catch (SQLException exc) {
//                        throw new RuntimeException(exc);
//                    }
//                }
//            }
//        });
        NewBill temp = this;
        slipNumberField.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                try {
                    int x = Integer.parseInt(slipNumberField.getText());
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    slipNumberField.setText("");
                    JOptionPane.showMessageDialog(MyClass.newBill, "an error occured, invalid input");

                    throw new RuntimeException();

                }

                try {

                    Statement stmt = MyClass.C.createStatement();
//                    String query = "select slip_id from order_slips where (slip_id=" + slipNumberField.getText() +" and customer_name='"+customerName+"'"+ ";";
                    String query = "SELECT slip_id FROM order_slips WHERE slip_id=" + slipNumberField.getText() + " AND customer_name='" + customerName + "';";

                    ResultSet rs = stmt.executeQuery(query);
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(MyClass.newBill, "orderslip not found");
                        slipNumberField.setText("");

                        return;
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(MyClass.newBill, "sql exception occured");
                    ex.printStackTrace();
                    throw new RuntimeException();

                }

                if (MyClass.searchResultWindow.isVisible()) {
                    MyClass.searchResultWindow.dispose();
                }
                MyClass.searchResultWindow = new SearchResultWindow(Integer.parseInt(slipNumberField.getText()));
                MyClass.searchResultWindow.setVisible(true);
                backButton.setEnabled(false);
                MyClass.positionFrames(temp, MyClass.searchResultWindow);

            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) billTable.getModel();
                int rowCount = model.getRowCount();

                // Database connection setup
                String url = "jdbc:mysql://localhost:3306/" + MyClass.login.getDatabase();
                String user = MyClass.login.getLoginID();
                String password = MyClass.login.getPassword();
                int billID = -1;
                Connection conn = null;
                PreparedStatement stmt = null;

                try {
                    conn = DriverManager.getConnection(url, user, password);
                    conn.setAutoCommit(false); // Start transaction
                    String sql = "INSERT INTO bills () VALUES ()";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                        int affectedRows = pstmt.executeUpdate();
                        if (affectedRows > 0) {
                            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                                if (rs.next()) {
                                    billID = rs.getInt(1);
                                }
                            }
                        }
                    } catch (SQLException exception) {
                        JOptionPane.showMessageDialog(MyClass.newBill, "error");
                        exception.printStackTrace();
                    }
                    sql = "INSERT INTO billdetails (BillID, SNo, ItemName, DesignID, OrderType, LabourCost, DullChillaiCost, " + "MeenaColorMeenaCost, RhodiumCost, NagSettingCost, OtherBaseCosts, TotalBaseCosting, GoldRate, " + "GoldPlatingWeight, TotalGoldCost, TotalFinalCost, OrderSlipNumber,OtherBaseCostNotes) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

                    stmt = conn.prepareStatement(sql);

                    for (int i = 0; i < rowCount - 1; i++) {
                        int serialNo = Integer.parseInt(getStringValue(model, i, billDetails.indexOf("SNo"), "0"));
                        String itemName = getStringValue(model, i, billDetails.indexOf("ItemName"), "Unknown");

                        String designID = getStringValue(model, i, billDetails.indexOf("DesignID"), "NoID");
                        String orderType = "Repairing";
                        System.out.println(serialNo + "\t" + itemName + "\t" + designID + "\t" + orderType);


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
                        }// Default, update as needed
                        double raw = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("Raw"), " "));
                        double labour = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("Labour"), "0"));
                        double dullChillai = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("DullChillai"), "0"));
                        double mcm = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("M/CM"), "0"));
                        double rh = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("Rh"), "0"));
                        double nag = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("Nag"), "0"));
                        double other = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("Other"), "0"));
                        double totalBaseCost = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("total"), "0"));
                        double goldRate = MyClass.newBill.goldrate;
                        double goldPlatingWeight = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("Gold(ing g)"), "0"));
                        double totalGoldCost = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("gold cost"), "0"));
                        double totalFinalCost = Double.parseDouble(getStringValue(model, i, billDetails.indexOf("total"), "0"));
                        String otherDetails = getStringValue(model, i, billDetails.indexOf("OtherDetails"), "");
                        int orderSlipNumber = snoToItemIdMap.getOrDefault(serialNo, 0);

                        stmt.setInt(1, billID); // Replace with actual BillID
                        stmt.setInt(2, serialNo);
                        stmt.setString(3, itemName);
                        stmt.setString(4, designID);
                        stmt.setString(5, orderType);
                        stmt.setDouble(6, labour);
                        stmt.setDouble(7, dullChillai);
                        stmt.setDouble(8, mcm);
                        stmt.setDouble(9, rh);
                        stmt.setDouble(10, nag);
                        stmt.setDouble(11, other);
                        stmt.setDouble(12, totalBaseCost);
                        stmt.setDouble(13, goldRate);
                        stmt.setDouble(14, goldPlatingWeight);
                        stmt.setDouble(15, totalGoldCost);
                        stmt.setDouble(16, totalFinalCost);
                        stmt.setInt(17, orderSlipNumber);
                        stmt.setString(18, otherDetails);
                        stmt.addBatch();
                    }

                    stmt.executeBatch();
                    conn.commit(); // Commit transaction

                    for (int key : snoToItemIdMap.keySet()) {
                        System.out.println(key + "-> " + snoToItemIdMap.get(key));
                    }
                    JOptionPane.showMessageDialog(null, "Bill details saved successfully!");
                    billTable.repaint();
                } catch (Exception ex) {
                    try {
                        if (conn != null) {
                            conn.rollback(); // Rollback on error
                        }
                    } catch (SQLException rollbackEx) {
                        rollbackEx.printStackTrace();
                    }
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error saving bill details!", "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    try {
                        if (stmt != null) stmt.close();
                        if (conn != null) conn.close();
                    } catch (SQLException closeEx) {
                        closeEx.printStackTrace();
                    }
                }
            }

            private String getStringValue(DefaultTableModel model, int row, int columnIndex, String defaultValue) {
                Object value = model.getValueAt(row, columnIndex);
                return (value != null && !value.toString().trim().isEmpty()) ? value.toString() : defaultValue;
            }
        });


        goldRateTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TestClass.csvOut(tableModel);
            }
        });
        customerComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customerName = customerComboBox.getSelectedItem().toString();
            }
        });
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TestClass.csvToTableModel(tableModel, "C:\\Users\\Aparw\\ShreeGurukripaJewellers\\output.csv");
                billTable.repaint();
            }
        });
    }

    private void reCalculateValuesAndAppend() {
        List<Integer> mathColumns = Arrays.asList(billDetails.indexOf("Labour"), billDetails.indexOf("Raw"), billDetails.indexOf("DullChillai"), billDetails.indexOf("M/CM"), billDetails.indexOf("Rh"), billDetails.indexOf("Nag"), billDetails.indexOf("Other"), billDetails.indexOf("+G"), billDetails.indexOf("Gold(ing g)"), billDetails.indexOf("gold cost"), billDetails.indexOf("total"));
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
                        e.printStackTrace();
                    }

                }

                if (i < 7) {
                    plusG += numericValue;
                }
                double goldrate = this.goldrate;
                double goldcost = 0;
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

    public int getNextBillID() {
        String query = "SELECT MAX(BillID) FROM bills";
        try (Statement stmt = MyClass.C.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException();

        }
        return 1; // Default if no records exist
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

    private BigDecimal convertToBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO; // Return 0 if the string is empty
        }

        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number format: " + value, "Input Error", JOptionPane.ERROR_MESSAGE);
            throw e; // Stop execution since an invalid value was entered
        }
    }

    public void init() {
        billTable.getInputMap(JTable.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DELETE"), "deleteRow");
        billTable.getActionMap().put("deleteRow", new AbstractAction() {
            @Override

            public void actionPerformed(ActionEvent e) {
                Action oldAction = billTable.getActionMap().get("deleteRow");
                billTable.getActionMap().remove("deleteRow");
                int selectedRow = billTable.getSelectedRow(); // Get selected row
                TableModelListener[] listeners = removeModelListener((tableModel));
                if (selectedRow != -1) {
                    // Ensure a row is selected
                    if (billTable.getRowCount() != 1)
                        checkAndRemoveRow(selectedRow, tableModel, billDetails.indexOf("SNo"), true);// Remove the row
                    billTable.repaint();
                }
                addModelListeners(listeners, tableModel);
                billTable.getActionMap().put("deleteRow", oldAction);
            }
        });

        curBillID = getNextBillID();
        try {
            if (transacTemp != null) if (!transacTemp.isClosed()) transacTemp.close();
            transacTemp = MyClass.getConnection(MyClass.login.getUrl(), MyClass.login.getLoginID(), MyClass.login.getPassword());
            transacTemp.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PromptSupport.setPrompt("Slip Number", slipNumberField);
        PromptSupport.setForeground(Color.GRAY, slipNumberField);
        PromptSupport.setPrompt("gold rate", goldRateTextField);
        PromptSupport.setForeground(Color.GRAY, goldRateTextField);
        listOfNonEditableCells = new Vector<>();
        Vector<String> dateList = new Vector<>();
        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusYears(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        billDetails = new Vector<>();
        billDetails.add("SNo");
        billDetails.add("OrderSlip/quantity");

        billDetails.add("ItemName");
        billDetails.add("Quantity");
        billDetails.add("DesignID");
        billDetails.add("Labour");
        billDetails.add("Raw");
        billDetails.add("DullChillai");
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
                else return true;
            }

        };
        Vector<String> emptyRow = new Vector<>();

        for (int count = 0; count < 14; count++) emptyRow.add("");
//        data.add(emptyRow);
        billTable.setModel(tableModel);
        while (!today.isBefore(oneYearAgo)) {
            dateList.add(today.format(formatter));
            today = today.minusDays(1);
        }
        DefaultComboBoxModel m = new DefaultComboBoxModel<>(dateList);
        dateComboBox.setModel(m);
        customerComboBox.removeAllItems();
        customerComboBox.addItem("Select Customer");
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Get the row index where the data is stored

// Extract values from the table, handling null or empty values
// Utility function to safely parse values
                TableModelListener[] listeners = removeModelListener(tableModel);
                System.out.println("triggered");
                int row = e.getFirstRow();
                int col = e.getColumn();
                System.out.println(sno + " raw value of sno");
                int snoValue = (tableModel.getValueAt(row, billDetails.indexOf("SNo")) != null && !tableModel.getValueAt(row, billDetails.indexOf("SNo")).toString().isEmpty()) ? Integer.parseInt(tableModel.getValueAt(row, billDetails.indexOf("SNo")).toString().contentEquals("") ? "0" : tableModel.getValueAt(row, billDetails.indexOf("SNo")).toString()) : -1;
                if (snoValue == -1) {
                    System.out.println(sno + " is the value of sno before inc");
                    sno++;
                    System.out.println("Sno modified! Current Value: " + sno + " | Stack Trace:");

                    System.out.println(sno + " is the value of sno after inc");
                    tableModel.setValueAt(sno, row, billDetails.indexOf("SNo"));
                    snoValue = sno;

//                    tableModel.getValueAt(row, billDetails.indexOf("SNo")) == null ? -1 : Integer.parseInt(tableModel.getValueAt(row, billDetails.indexOf("SNo")).toString().contentEquals("") ? "0" : tableModel.getValueAt(row, billDetails.indexOf("SNo")).toString());
                }


                if (col == -1) return;

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
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(MyClass.newBill, "invalid value entered");

                }
                Object curValue = tableModel.getValueAt(row, col) == null ? "" : tableModel.getValueAt(row, col);
                int curSlipID = -1;
                if (snoToItemIdMap.containsKey(snoValue)) {
                    int itemId = snoToItemIdMap.get(snoValue);

                    String query = "SELECT * FROM order_slips WHERE slip_id = " + "(SELECT slip_id FROM order_slips WHERE item_id = ?)";

                    try (PreparedStatement stmt = MyClass.C.prepareStatement(query)) {
                        stmt.setInt(1, itemId); // Use PreparedStatement to prevent SQL injection
                        ResultSet rs = stmt.executeQuery();

                        while (rs.next()) {
                            curSlipID = rs.getInt("slip_id");


                            // Process result
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }


                if (col == designIdIndex) {
                    String designID = tableModel.getValueAt(row, designIdIndex) == null ? "" : tableModel.getValueAt(row, designIdIndex).toString().trim();

                    if (!designID.isEmpty()) {
                        String query = "SELECT itemname,price FROM inventory WHERE DesignID = ?";

                        try (PreparedStatement stmt = MyClass.C.prepareStatement(query)) {

                            stmt.setString(1, designID);
                            ResultSet rs = stmt.executeQuery();

                            if (rs.next()) {

                                listOfNonEditableCells.add(new Integer[]{row, billDetails.indexOf("ItemName")});
                                System.out.println("added to list non editabe " + row + ", " + billDetails.indexOf("ItemName"));
                                String itemName = rs.getString("itemname");
                                String price = rs.getString("price");
                                tableModel.setValueAt(itemName, row, billDetails.indexOf("ItemName"));
                                tableModel.setValueAt(price, row, billDetails.indexOf("Raw"));
                            } else {
                                listOfNonEditableCells.removeIf(cell -> cell[0] == row && cell[1] == billDetails.indexOf("ItemName"));
                                System.out.println("removed from the list " + row + billDetails.indexOf("ItemName"));

                                tableModel.setValueAt("", row, designIdIndex);
                                tableModel.setValueAt("", row, billDetails.indexOf("ItemName"));
                                tableModel.setValueAt("", row, billDetails.indexOf("Raw"));
                            }

                            DefaultTableModel newModel = redoModel();

                            billTable.setModel(newModel);
                            System.out.println("new model set");
                            tableModel = newModel;
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // If designID field is empty, clear ItemName as well
                        tableModel.setValueAt("", row, designIdIndex);
                        tableModel.setValueAt("", row, billDetails.indexOf("ItemName"));
                        tableModel.setValueAt("", row, billDetails.indexOf("Raw"));
                        int i = 0;
                        listOfNonEditableCells.removeIf(cell -> cell[0] == row && cell[1] == billDetails.indexOf("ItemName"));

                    }

                }

                if ((col == quantityIndex)) {

                    Object designID = tableModel.getValueAt(row, designIdIndex);
                    if (notThroughOrderSlip && designID != null && !designID.toString().contentEquals("")) {
                        try {
                            String query = "select totalquantity from inventory where designid='" + designID + "';";
                            Statement stmt = MyClass.C.createStatement();
                            ResultSet resultSet = stmt.executeQuery(query);
                            if (resultSet.next()) if (resultSet.getInt("totalquantity") < quantity)
                                JOptionPane.showMessageDialog(MyClass.newBill, "warning stock not remaining");
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }


                    }
                    for (Integer a : snoToItemIdMap.keySet()) {
                    }
                    if (snoToItemIdMap.containsKey(snoValue)) {
                        try {
                            Statement stmt = MyClass.C.createStatement();
                            String query = "select * from order_slips where item_id=" + snoToItemIdMap.get(snoValue);
                            int item_id = snoToItemIdMap.get(snoValue);
                            int netQuantity = 0;
                            int keyy = 0;
                            for (int key : snoToItemIdMap.keySet()) {
                                if (snoToItemIdMap.get(key) == item_id) {
                                    netQuantity = netQuantity + ((tableModel.getValueAt(key - 1, quantityIndex) != null && !tableModel.getValueAt(key - 1, quantityIndex).toString().isEmpty()) ? Integer.parseInt(tableModel.getValueAt(key - 1, quantityIndex).toString()) : 0);
                                }
                                ResultSet rs = stmt.executeQuery(query);
                                if (rs.next()) {
                                    int quantityTemp = rs.getInt("quantity") - rs.getInt("billed_quantity");
                                    if (quantityTemp < netQuantity) {
                                        JOptionPane.showMessageDialog(MyClass.newBill, "invalid quanity has been entered for an item connected to a order slip");
                                        tableModel.setValueAt("", row, quantityIndex);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(MyClass.newBill, "error has occured");
                                    System.exit(-1);
                                    throw new RuntimeException();
                                }
                            }

                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                }

                double labour = getDoubleValue(tableModel.getValueAt(row, billDetails.indexOf("Labour")));
                tableModel.setValueAt(labour == 0 ? "" : labour, row, billDetails.indexOf("Labour"));
                double raw = getDoubleValue(tableModel.getValueAt(row, billDetails.indexOf("Raw")));
                tableModel.setValueAt(raw == 0 ? "" : raw, row, billDetails.indexOf("Raw"));
                double dullChillai = getDoubleValue(tableModel.getValueAt(row, billDetails.indexOf("DullChillai")));
                tableModel.setValueAt(dullChillai == 0 ? "" : dullChillai, row, billDetails.indexOf("DullChillai"));
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
//                if (col == billDetails.indexOf("Labour") || col == billDetails.indexOf("Raw") || col == billDetails.indexOf("DullChillai") || col == billDetails.indexOf("M/CM") || col == billDetails.indexOf("Rh") || col == billDetails.indexOf("Nag") || col == billDetails.indexOf("Other") || col == billDetails.indexOf("+G") || col == billDetails.indexOf("Gold(ing g)") || col == billDetails.indexOf("gold cost") || col == billDetails.indexOf("total"))

                plusG = labour + raw + dullChillai + mCm + rh + nag + other;
                goldCost = goldIngG * goldrate;
                plusG *= quantity;
                total = plusG + goldCost;
                tableModel.setValueAt(total == 0 ? "" : total, row, billDetails.indexOf("total"));
                tableModel.setValueAt(plusG == 0 ? "" : plusG, row, billDetails.indexOf("+G"));
                tableModel.setValueAt(goldCost == 0 ? "" : goldCost, row, billDetails.indexOf("gold cost"));


                if (updateThroughSlip) {
                    System.out.println(sno + " is the current sno value");
                    createBillToOrderSlipAssosciation(sno, itemID);
                    updateThroughSlip = false;
                }
                if (row != tableModel.getRowCount() - 1) {
                    checkAndRemoveRow(row, tableModel, billDetails.indexOf("SNo"), false);
                }
                billTable.repaint();
                addModelListeners(listeners, tableModel);
            }


        });

        billTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("control D"), "copyAbove");

        billTable.getActionMap().put("copyAbove", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = billTable.getSelectedRow();
                int selectedColumn = billTable.getSelectedColumn();
                DefaultTableModel model = (DefaultTableModel) billTable.getModel();

                if (selectedRow > 0 && selectedColumn != -1) {  // Ensure it's not the first row and a column is selected
                    Object aboveValue = model.getValueAt(selectedRow - 1, selectedColumn); // Get value from above row
                    model.setValueAt(aboveValue, selectedRow, selectedColumn); // Copy to current row
                }
            }
        });


        try {
            Statement stmt1;
            stmt1 = MyClass.C.createStatement();
            ResultSet rs1 = stmt1.executeQuery("SELECT customer_name FROM customers");
            while (rs1.next()) {
                customerComboBox.addItem(rs1.getString("customer_name"));
            }
            String query = "SELECT MAX(BillID) FROM bills;";
            Statement stmt2 = MyClass.C.createStatement();

            rs1 = stmt1.executeQuery(query);

            int newBillID = 1; // Default BillID if no bills exist

            // If the query returns a result, get the max BillID and increment it
            if (rs1.next()) {
                int lastBillID = rs1.getInt(1);
                if (rs1.wasNull()) {
                    newBillID = 1; // If no bills, set BillID to 1
                } else {
                    newBillID = lastBillID + 1; // Increment the last BillID
                }
            }
            ResultSet rs2 = stmt2.executeQuery("select type_name from ordertype;");
            while (rs2.next()) {
                slipDetailComboBox.addItem(rs2.getString(1));
            }

            idLabel.setText("Bill ID: " + newBillID);
            pack();
            setExtendedState(JFrame.MAXIMIZED_BOTH);

            rs1.close();
        } catch (SQLException e) {
            throw new RuntimeException();
        }


    }

    private void checkAndRemoveRow(int row, DefaultTableModel tableModel, int snoIndex, boolean forceRemove) {
        // Ensure the row index is valid

        if (row < 0 || row >= tableModel.getRowCount()) {
            return; // Invalid row index, exit method
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
            int sno = tableModel.getValueAt(row, billDetails.indexOf("SNo")) == null ? -1 : Integer.parseInt(tableModel.getValueAt(row, billDetails.indexOf("SNo")).toString());
            relinkAssociations(row + 1);
            tableModel.removeRow(row);
            if (!(tableModel.getValueAt(row, billDetails.indexOf("DesignID")) != null && !tableModel.getValueAt(row, billDetails.indexOf("DesignID")).toString().isEmpty())) {
                listOfNonEditableCells.removeIf(cell -> (cell[0] == row));
            }
        }
        int sno = 0;
        for (int i = 0; i < tableModel.getRowCount() - 1; i++) {
            sno++; // Increment SNo
            tableModel.setValueAt(sno, i, snoIndex); // Set new value in SNo column
        }
        tableModel.setValueAt("", billTable.getRowCount() - 1, billDetails.indexOf("SNo"));
        this.sno = sno;

    }

    private void relinkAssociations(int removeSNO) {
        HashMap<Integer, Integer> tempMap = new HashMap<>();
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

    public void addModelListeners(TableModelListener[] listeners, DefaultTableModel model) {
        for (TableModelListener listener : listeners) {
            model.addTableModelListener(listener);
        }
    }

    public TableModelListener[] removeModelListener(DefaultTableModel tableModel) {

        TableModelListener[] listeners = tableModel.getTableModelListeners();
        for (TableModelListener listener : listeners) {


            tableModel.removeTableModelListener(listener);
        }
        return listeners;
    }

    Vector<Integer[]> listOfNonEditableCells;

    DefaultTableModel redoModel() {
        for (Integer[] cell : listOfNonEditableCells)
            System.out.println("list contains: " + cell[0] + ", " + cell[1]);

        Vector<Vector<String>> v = new Vector<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Vector<String> data = new Vector<>();

            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                Object cellValue = tableModel.getValueAt(i, j);
                data.add(cellValue != null ? cellValue.toString() : ""); // Avoid NullPointerException
            }
            int count = 0;
            for (String name : data) {
                count++;
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
                if (listOfNonEditableCells.isEmpty()) {
                    return true;  // If no locked cells, allow editing
                }

                return true; // Otherwise, allow editing
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