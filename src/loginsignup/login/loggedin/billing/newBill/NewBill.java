package loginsignup.login.loggedin.billing.newBill;

import mainpack.MyClass;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.nimbus.State;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Vector;

import org.jdesktop.swingx.prompt.PromptSupport;

public class NewBill extends JFrame {
    private JComboBox customerComboBox;
    private JPanel panel;
    private int curBillID;

    private JLabel idLabel;
    private JTable billTable;
    private JButton backButton;
    private JComboBox slipDetailComboBox;
    private JComboBox dateComboBox;
    private JButton submitButton;
    private JButton resetButton;
    private JButton undoButton;
    private JTextField slipNumberField;
    private Connection transacTemp;

    public int getCurBillID() {
        return curBillID;
    }

    public NewBill() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(panel);
        pack();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.billingScreen.setVisible(true);
                MyClass.searchResultWindow.dispose();
            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int billID = getCurBillID();

                    // Insert into bills table
                    String insertBillQuery = "INSERT INTO bills (BillID) VALUES (?)";
                    try (PreparedStatement billStmt = MyClass.C.prepareStatement(insertBillQuery)) {
                        billStmt.setInt(1, billID);
                        billStmt.executeUpdate();
                    }

                    // Insert into billdetails table
                    String insertDetailsQuery = "INSERT INTO billdetails (BillID, SNo, ItemName, DesignID, OrderType, LabourCost, " + "DullChillaiCost, MeenaColorMeenaCost, RhodiumCost, NagSettingCost, OtherBaseCosts, OtherBaseCostNotes, " + "TotalBaseCosting, GoldRate, GoldPlatingWeight, TotalGoldCost, TotalFinalCost, OrderSlipNumber) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    try (PreparedStatement detailsStmt = MyClass.C.prepareStatement(insertDetailsQuery)) {
                        DefaultTableModel model = (DefaultTableModel) billTable.getModel();
                        String panaType = "";
                        String orderSlipNumber = "";

                        for (int i = 0; i < model.getRowCount() - 1; i++) {
                            detailsStmt.setInt(1, billID); // BillID
                            detailsStmt.setInt(2, i + 1); // SNo
                            detailsStmt.setString(3, model.getValueAt(i, 1).toString()); // ItemName
                            detailsStmt.setString(4, model.getValueAt(i, 2).toString()); // DesignID
                            detailsStmt.setString(5, panaType); // OrderType
                            detailsStmt.setBigDecimal(6, new BigDecimal(model.getValueAt(i, 3).toString())); // LabourCost
                            detailsStmt.setBigDecimal(7, new BigDecimal(model.getValueAt(i, 5).toString())); // DullChillaiCost
                            detailsStmt.setBigDecimal(8, new BigDecimal(model.getValueAt(i, 6).toString())); // MeenaColorMeenaCost
                            detailsStmt.setBigDecimal(9, new BigDecimal(model.getValueAt(i, 7).toString())); // RhodiumCost
                            detailsStmt.setBigDecimal(10, new BigDecimal(model.getValueAt(i, 8).toString())); // NagSettingCost
                            detailsStmt.setBigDecimal(11, new BigDecimal(model.getValueAt(i, 9).toString())); // OtherBaseCosts
                            detailsStmt.setString(12, model.getValueAt(i, 10).toString()); // OtherBaseCostNotes
                            detailsStmt.setBigDecimal(13, new BigDecimal(model.getValueAt(i, 11).toString())); // TotalBaseCosting (+G)
                            detailsStmt.setBigDecimal(14, new BigDecimal(model.getValueAt(i, 13).toString())); // GoldRate
                            detailsStmt.setBigDecimal(15, new BigDecimal(model.getValueAt(i, 12).toString())); // GoldPlatingWeight
                            detailsStmt.setBigDecimal(16, new BigDecimal(model.getValueAt(i, 14).toString())); // TotalGoldCost
                            detailsStmt.setBigDecimal(17, new BigDecimal(model.getValueAt(i, 15).toString())); // TotalFinalCost
                            detailsStmt.setString(18, orderSlipNumber); // OrderSlipNumber (optional)

                            detailsStmt.addBatch();
                        }
                        detailsStmt.executeBatch();
                        getTransacTemp().commit();
                        getTransacTemp().close();
                    }

                    System.out.println("Data inserted successfully.");

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
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
                    return;
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
                    return;
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
    }

    public int getNextBillID() {
        String query = "SELECT MAX(BillID) FROM bills";
        try (Statement stmt = MyClass.C.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
e.printStackTrace();        }
        return 1; // Default if no records exist
    }

    public Connection getTransacTemp() {
        return transacTemp;
    }

    DefaultTableModel tableModel;
    Vector<String> billDetails = new Vector<>();

    public void initSystemlogin() {
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

        listOfNonEditableCells = new ArrayList<>();
        Vector<String> dateList = new Vector<>();
        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusYears(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        billDetails = new Vector<>();
        billDetails.add("SNo");
        billDetails.add("ItemName");
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
        tableModel = new DefaultTableModel(billDetails, 1);
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
                int row = e.getFirstRow();
                int col = e.getColumn();

                // Ignore updates where column index is -1 (not a data update)
                if (col == -1) return;

                // Check if the edited row is the last row
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

                if (col == designIdIndex) {

                    System.out.println("model changed");
                    if (tableModel.getValueAt(row, 2).toString().contentEquals("")) {
                        listOfNonEditableCells.removeIf(cell -> cell[0] == Integer.parseInt(String.valueOf(row)) && cell[1] == 1);
                    } else {
                        listOfNonEditableCells.add(new Integer[]{row, 1});

                    }
                    DefaultTableModel newModel = redoModel();
                    billTable.setModel(newModel);
                    tableModel = newModel;
                    if (col == designIdIndex) {
                        tableModel.removeTableModelListener(this);
                        String designID = tableModel.getValueAt(row, designIdIndex).toString().trim();

                        if (!designID.isEmpty()) {
                            String query = "SELECT itemname,price FROM inventory WHERE DesignID = ?";

                            try (PreparedStatement stmt = MyClass.C.prepareStatement(query)) {

                                stmt.setString(1, designID);
                                ResultSet rs = stmt.executeQuery();

                                if (rs.next()) {
                                    // If DesignID exists, update ItemName column
                                    String itemName = rs.getString("itemname");
                                    String price = rs.getString("price");
                                    tableModel.setValueAt(itemName, row, billDetails.indexOf("ItemName"));
                                    tableModel.setValueAt(price, row, billDetails.indexOf("Raw"));
                                } else {
                                    // If DesignID does not exist, clear ItemName column
                                    tableModel.setValueAt("", row, billDetails.indexOf("ItemName"));
                                    tableModel.setValueAt("", row, designIdIndex);

                                }

                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            // If designID field is empty, clear ItemName as well
                            tableModel.setValueAt("", row, billDetails.indexOf("ItemName"));
                            tableModel.setValueAt("", row, designIdIndex);


                        }
                        tableModel.addTableModelListener(this);

                    }
                }
            }
        });
        // Step 1: Get InputMap of JTable for WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        billTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("control D"), "copyAbove");

// Step 2: Bind the KeyStroke to an Action
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
e.printStackTrace();        }


    }

    ArrayList<Integer[]> listOfNonEditableCells;

    DefaultTableModel redoModel() {


        Vector<Vector<String>> v = new Vector<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Vector<String> data = new Vector<>();

            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                Object cellValue = tableModel.getValueAt(i, j);
                data.add(cellValue != null ? cellValue.toString() : ""); // Avoid NullPointerException
            }
            int count = 0;
            for (String name : data) {
                System.out.println("vaulue is " + name + " count is " + count);
                count++;
            }
            v.add(data);
        }
        for (Object[] items : listOfNonEditableCells) {
            System.out.println(items[0] + " " + items[1]);

        }

        DefaultTableModel m = new DefaultTableModel(v, billDetails) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Avoid IndexOutOfBoundsException
                if (listOfNonEditableCells.isEmpty()) {
                    return true;  // If no locked cells, allow editing
                }

                // Loop through all non-editable cells
                for (Integer[] cell : listOfNonEditableCells) {
                    if (cell[0].intValue() == row && cell[1].intValue() == column) {
                        System.out.println("Making cell non-editable at row: " + row + ", column: " + column);
                        return false;
                    }
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
