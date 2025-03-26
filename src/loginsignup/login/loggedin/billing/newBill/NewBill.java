package loginsignup.login.loggedin.billing.newBill;

import mainpack.MyClass;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.jdesktop.swingx.prompt.PromptSupport;

public class NewBill extends JFrame {
    int itemID;

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void createBillToOrderSlipAssosciation(int sNo, int itemID) {
        System.out.println("before inserting sno is"+sNo);

        snoToItemIdMap.put(sNo, itemID);
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
                    String query = "select slip_id from order_slips where slip_id=" + slipNumberField.getText() + ";";
                    ResultSet rs = stmt.executeQuery(query);
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(MyClass.newBill, "orderslip not found");
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

                for (int i = 0; i < rowCount; i++) {
                    String serialNo = getStringValue(model, i, 0);
                    String itemName = getStringValue(model, i, 1);
                    String quantity = getStringValue(model, i, 2);
                    String designID = getStringValue(model, i, 3);
                    String labour = getStringValue(model, i, 4);
                    String raw = getStringValue(model, i, 5);
                    String dullChillai = getStringValue(model, i, 6);
                    String mcm = getStringValue(model, i, 7);
                    String rh = getStringValue(model, i, 8);
                    String nag = getStringValue(model, i, 9);
                    String other = getStringValue(model, i, 10);
                    String otherDetails = getStringValue(model, i, 11);
                    String gPlus = getStringValue(model, i, 12);
                    String goldIngGrams = getStringValue(model, i, 13);
                    String goldCost = getStringValue(model, i, 14);
                    String total = getStringValue(model, i, 15);

                    // Convert necessary values to BigDecimal safely
                    BigDecimal labourCost = convertToBigDecimal(labour);
                    BigDecimal dullChillaiCost = convertToBigDecimal(dullChillai);
                    BigDecimal meenaCost = convertToBigDecimal(mcm);
                    BigDecimal rhodiumCost = convertToBigDecimal(rh);
                    BigDecimal nagSettingCost = convertToBigDecimal(nag);
                    BigDecimal otherBaseCosts = convertToBigDecimal(other);
                    BigDecimal totalBaseCosting = convertToBigDecimal(gPlus);
                    BigDecimal goldRate = convertToBigDecimal(goldIngGrams);
                    BigDecimal goldPlatingWeight = convertToBigDecimal(goldCost);
                    BigDecimal totalGoldCost = convertToBigDecimal(total);

                    // Print fetched values (for debugging)
                }

            }
        });
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

        listOfNonEditableCells = new Vector<>();
        Vector<String> dateList = new Vector<>();
        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusYears(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        billDetails = new Vector<>();
        billDetails.add("SNo");

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
                return column != billDetails.indexOf("SNo");
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
                for (int key:snoToItemIdMap.keySet())
                {
                    System.out.println(key+" : "+snoToItemIdMap.get(key));
                }
                TableModelListener[] listeners = removeModelListener(tableModel);

                int row = e.getFirstRow();
                int col = e.getColumn();

                int snoValue = (tableModel.getValueAt(row, billDetails.indexOf("SNo")) != null && !tableModel.getValueAt(row,billDetails.indexOf("SNo")).toString().isEmpty()) ? Integer.parseInt(tableModel.getValueAt(row, billDetails.indexOf("SNo")).toString().contentEquals("") ? "0" : tableModel.getValueAt(row, billDetails.indexOf("SNo")).toString()) : -1;
                if (snoValue == -1) {
                    sno++;
                    tableModel.setValueAt(sno, row, billDetails.indexOf("SNo"));
                    if (updateThroughSlip) {
                        System.out.println(sno+" is the value of sno before passing");
                        createBillToOrderSlipAssosciation(sno, itemID);
                        updateThroughSlip=false;
                    }
                }
                snoValue = tableModel.getValueAt(row, billDetails.indexOf("SNo")) == null ? -1 : Integer.parseInt(tableModel.getValueAt(row, billDetails.indexOf("SNo")).toString().contentEquals("")?"0":tableModel.getValueAt(row, billDetails.indexOf("SNo")).toString());



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

                    String query = "SELECT * FROM order_slips WHERE slip_id = " +
                            "(SELECT slip_id FROM order_slips WHERE item_id = ?)";

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

                if (row != tableModel.getRowCount() - 1) {
                    if (curValue.toString().isEmpty()) {
                        checkAndRemoveRow(row, tableModel, billDetails.indexOf("SNo"));
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

                                // If DesignID exists, update ItemName column
                                listOfNonEditableCells.add(new Integer[]{row, billDetails.indexOf("ItemName")});

                                String itemName = rs.getString("itemname");
                                String price = rs.getString("price");
                                tableModel.setValueAt(itemName, row, billDetails.indexOf("ItemName"));
                                tableModel.setValueAt(price, row, billDetails.indexOf("Raw"));
                            } else {
                                int i = 0;
                                for (Integer[] cell : listOfNonEditableCells) {
                                    if (cell[0] == row && cell[1] == billDetails.indexOf("ItemName")) {
                                        listOfNonEditableCells.remove(i);
                                        break;
                                    }
                                    i++;
                                }
                                tableModel.setValueAt("", row, designIdIndex);
                                addModelListeners(listeners, tableModel);
                                tableModel.setValueAt("", row, billDetails.indexOf("ItemName"));
                                tableModel.setValueAt("", row, billDetails.indexOf("Raw"));
                                removeModelListener(tableModel);
                            }

                            DefaultTableModel newModel = redoModel();

                            billTable.setModel(newModel);
                            tableModel = newModel;
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // If designID field is empty, clear ItemName as well
                        tableModel.setValueAt("", row, designIdIndex);
                        addModelListeners(listeners, tableModel);
                        tableModel.setValueAt("", row, billDetails.indexOf("ItemName"));
                        tableModel.setValueAt("", row, billDetails.indexOf("Raw"));
                        removeModelListener(tableModel);
                        int i = 0;
                        for (Integer[] cell : listOfNonEditableCells) {
                            if (cell[0] == row && cell[1] == billDetails.indexOf("ItemName")) {
                                listOfNonEditableCells.remove(i);

                                break;
                            }
                            i++;
                        }

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
                    for(Integer a:snoToItemIdMap.keySet()){
                        System.out.println("key set values:"+a);
                    }
                    if (snoToItemIdMap.containsKey(snoValue)) {
                        try {
                            System.out.println("damn it yrr");
                            Statement stmt = getTransacTemp().createStatement();
                            String query = "select * from order_slips where item_id=" + snoToItemIdMap.get(snoValue);
                            int item_id = snoToItemIdMap.get(snoValue);
                            int netQuantity = 0;
                            int keyy=0;
                            for (int key : snoToItemIdMap.keySet()) {
                                if (snoToItemIdMap.get(key) == item_id) {
                                    System.out.println("");
                                    netQuantity = netQuantity + ((tableModel.getValueAt(key - 1, quantityIndex) == null) ? 0 : Integer.parseInt(tableModel.getValueAt(key - 1, quantityIndex).toString()));

                                }
                            }
                            System.out.println("item id:"+item_id+" has total "+netQuantity);

                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                }
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

    private void checkAndRemoveRow(int row, DefaultTableModel tableModel, int snoIndex) {
        // Ensure the row index is valid
        TableModelListener[] listeners = removeModelListener(tableModel);
        if (row < 0 || row >= tableModel.getRowCount()) {
            return; // Invalid row index, exit method
        }

        boolean isEmpty = true;

        // Check if all columns in the given row are empty
        for (int col = 0; col < tableModel.getColumnCount(); col++) {
            if (col == snoIndex) continue;
            Object value = tableModel.getValueAt(row, col);
            if (value != null && !value.toString().trim().isEmpty()) {
                isEmpty = false;
                break;
            }
        }

        if (isEmpty) {
            int sno = tableModel.getValueAt(row, billDetails.indexOf("SNo")) == null ? -1 : Integer.parseInt(tableModel.getValueAt(row, billDetails.indexOf("SNo")).toString());
            tableModel.removeRow(row);
            snoToItemIdMap.remove(sno);
        }
        int sno = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            sno++; // Increment SNo
            tableModel.setValueAt(sno, i, snoIndex); // Set new value in SNo column
        }
        this.sno = sno;
        addModelListeners(listeners, tableModel);

    }

    private void addModelListeners(TableModelListener[] listeners, DefaultTableModel model) {
        for (TableModelListener listener : listeners) {
            model.addTableModelListener(listener);
        }
    }

    private TableModelListener[] removeModelListener(DefaultTableModel tableModel) {

        TableModelListener[] listeners = tableModel.getTableModelListeners();
        for (TableModelListener listener : listeners) {
            tableModel.removeTableModelListener(listener);
        }
        return listeners;
    }

    Vector<Integer[]> listOfNonEditableCells;

    DefaultTableModel redoModel() {

        for (Integer[] i : listOfNonEditableCells) {
        }
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
