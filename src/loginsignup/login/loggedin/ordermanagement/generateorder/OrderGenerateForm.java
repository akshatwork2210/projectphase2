package loginsignup.login.loggedin.ordermanagement.generateorder;

import mainpack.MyClass;
import testpackage.UtilityMethods;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static testpackage.UtilityMethods.*;

public class OrderGenerateForm extends JFrame {

    HashMap<Integer, String> snoToDetailsMap;
    private JPanel panel;
    DefaultTableModel backupModel;
    private JButton backButton;
    TableModelListener modelListener;
    private JComboBox<String> customerNameComboBox;
    private JComboBox<String> orderSlipTypeComboBox;
    private JTable orderSlip;
    private JButton submitButton;
    private JButton resetFormButton;
    private JButton undoResetButton;
    private JComboBox<String> dateComboBox;
    private JLabel slipIDLabel;
    //THE BELOW CODE IS FOR COLUMN NAMES CONSTANTS
    public static final int DESIGN_ID_INDEX = 0;
    public static final int ITEM_NAME_INDEX = 1;
    public static final int QUANTITY_INDEX = 2;
    public static final int PLATING_INDEX = 3;
    public static final int RAW_MATERIAL_COST_INDEX = 4;
    public static final int OTHER_DETAILS_INDEX = 5;

    Connection orderSlipConnetionObject;

    ArrayList<Integer[][]> ar;
    Vector<Integer[]> listOfDisabledCells;


    public OrderGenerateForm() {

        backupModel = null;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(panel);

        pack();

        backButton.addActionListener(e -> {
            setVisible(false);
            MyClass.orderScreen.setVisible(true);
        });
        orderSlip.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }
        });

        resetFormButton.addActionListener(e -> {
            backupModel = (DefaultTableModel) orderSlip.getModel();
            init();
        });
        undoResetButton.addActionListener(e -> {
            if (backupModel != null) {
                int columnCount = model.getColumnCount();
                Vector<String> columnName = new Vector<>();
                for (int i = 0; i < columnCount; i++) {
                    columnName.add(model.getColumnName(i));
                }
                DefaultTableModel temp = new DefaultTableModel(model.getDataVector(), columnName);
                TableModelListener[] listeners = model.getTableModelListeners();
                for (TableModelListener listener : listeners) {
                    temp.addTableModelListener(listener);
                }
                model = backupModel;
                orderSlip.setModel(model);
                backupModel = temp;
            }
        });
        submitButton.addActionListener(e -> {

            if (customerNameComboBox.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(MyClass.orderGenerateForm, "please select a customer name", "incomplete information", JOptionPane.ERROR_MESSAGE);
                return;
            }
            DefaultTableModel model = (DefaultTableModel) orderSlip.getModel();
            int rowCount = model.getRowCount();
            PreparedStatement slipDataStatement = null;
            PreparedStatement inventoryUpdateStatement = null;
            PreparedStatement updateOrderSlipsMainTableStatement = null;
            try {

                // Generate a new slip_id for the entire batch
                String inventoryUpdateQuery = "update inventory set totalquantity=totalquantity-? where designid=?";
                java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(LocalDateTime.parse(Objects.toString(dateComboBox.getSelectedItem(), "") + " 00:00:00", DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss")));
                String updateOrderSlipsMainTableQuery;
                updateOrderSlipsMainTableQuery = "update order_slips_main set created_at=? where slip_id=?";
                inventoryUpdateStatement = orderSlipConnetionObject.prepareStatement(inventoryUpdateQuery);
                updateOrderSlipsMainTableStatement = orderSlipConnetionObject.prepareStatement(updateOrderSlipsMainTableQuery);
                updateOrderSlipsMainTableStatement.setTimestamp(1, timestamp);
                updateOrderSlipsMainTableStatement.setInt(2, getSlipID());
                updateOrderSlipsMainTableStatement.executeUpdate();

                boolean created = false;
                String slipDataInsertQuery = "INSERT INTO order_slips (slip_type, customer_name, slip_id, design_id, item_name, quantity, plating_grams, raw_material_price, other_details, sno,created_at) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                slipDataStatement = orderSlipConnetionObject.prepareStatement(slipDataInsertQuery);
                // Loop through each row and insert into order_slips
                for (int i = 0; i < rowCount - 1; i++) {
                    created = true;
                    if (model.getValueAt(i, ITEM_NAME_INDEX) == null || model.getValueAt(i, ITEM_NAME_INDEX).toString().trim().contentEquals(""))
                        break;
                    String designId = model.getValueAt(i, DESIGN_ID_INDEX) != null ? model.getValueAt(i, DESIGN_ID_INDEX).toString() : "";
                    String itemName = (String) model.getValueAt(i, ITEM_NAME_INDEX);

                    int quantity = !model.getValueAt(i, QUANTITY_INDEX).toString().contentEquals("") ? Integer.parseInt(model.getValueAt(i, QUANTITY_INDEX).toString()) : 0;
                    double platingGrams = getDoubleValue(model.getValueAt(i, PLATING_INDEX)) == null ? 0 : getDoubleValue(model.getValueAt(i, PLATING_INDEX));
                    double rawMaterialCost = getDoubleValue(model.getValueAt(i, RAW_MATERIAL_COST_INDEX)) == null ? 0 : getDoubleValue(model.getValueAt(i, RAW_MATERIAL_COST_INDEX));
                    String otherDetails;
                    try {
                        otherDetails = model.getValueAt(i, OTHER_DETAILS_INDEX).toString();
                    } catch (NullPointerException ex) {
                        otherDetails = "";
                    }

                    String customerName = customerNameComboBox.getSelectedItem() == null ? "" : customerNameComboBox.getSelectedItem().toString();
                    String orderSlipType = orderSlipTypeComboBox.getSelectedItem().toString();
                    int sno = i + 1;
                    // Insert Query


                    slipDataStatement.setString(1, orderSlipType);
                    slipDataStatement.setString(2, customerName);
                    slipDataStatement.setInt(3, getSlipID());
                    if (!designId.isEmpty()) {
                        inventoryUpdateStatement.setInt(1, quantity);
                        inventoryUpdateStatement.setString(2, designId);
                        inventoryUpdateStatement.addBatch();
                    }
                    slipDataStatement.setString(4, designId);

                    slipDataStatement.setString(5, itemName);
                    slipDataStatement.setInt(6, quantity);
                    slipDataStatement.setDouble(7, platingGrams);
                    slipDataStatement.setDouble(8, rawMaterialCost);
                    slipDataStatement.setString(9, otherDetails);
                    slipDataStatement.setInt(10, sno);
                    slipDataStatement.setTimestamp(11, timestamp);
                    slipDataStatement.addBatch();
                }
                slipDataStatement.executeBatch();
                inventoryUpdateStatement.executeBatch();
                orderSlipConnetionObject.commit();

                if (created) System.out.println("New  slip created: Slip ID = " + getSlipID());
                else
                    JOptionPane.showMessageDialog(MyClass.orderGenerateForm, "Empty form  error", "error", JOptionPane.ERROR_MESSAGE);

            } catch (SQLException ex) {
//                    ex.printStackTrace();
                try {
                    orderSlipConnetionObject.rollback();
                    System.out.println("rollbacked");
                    ex.printStackTrace();
                    return;
                } catch (SQLException exc) {
                    exc.printStackTrace();
                    throw new RuntimeException(exc);
                }
            } finally {
                try {
                    if (inventoryUpdateStatement != null) inventoryUpdateStatement.close();
                    if (slipDataStatement != null) slipDataStatement.close();
                    if (orderSlipConnetionObject != null) orderSlipConnetionObject.close();
                    System.out.println("connection closed");
                } catch (SQLException ex) {
                    System.out.println("could not close connection");
                    throw new RuntimeException(ex);
                }
            }
            dispose();
            MyClass.orderGenerateForm=new OrderGenerateForm();
            MyClass.orderGenerateForm.init();
            MyClass.orderGenerateForm.setVisible(true);
        });
    }

    private Double getDoubleValue(Object string) {
        if (string == null) return null;

        try {
            return Double.parseDouble(string.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    int prevRow = 0;
    DefaultTableModel model;

    public void init() {

        listOfDisabledCells = new Vector<>();
        ar = new ArrayList<>();
        generateAndAddDates(dateComboBox, false);
        generateAndAddNames(customerNameComboBox);
        String[] columnNames = {"design id", "Item Name", "Quantity", "Plating", "Raw Material Cost", "Other Details"};//jtable content
        orderSlip.getTableHeader().setReorderingAllowed(false);
        // Create a DefaultTableModel with columns and no rows initially
        model = new DefaultTableModel(columnNames, 1);
        modelListener = e -> {

            if (e.getType() == TableModelEvent.UPDATE) {
                TableModelListener[] listeners = removeModelListener(model);
                int row = e.getFirstRow();
                int column = e.getColumn();
                model.removeTableModelListener(modelListener);
                String cellContent;
                try {
                    cellContent = model.getValueAt(row, column) == null ? "" : model.getValueAt(row, column).toString();
                } catch (ArrayIndexOutOfBoundsException ex) {
                    disableName();
                    return;
                }
                if (column == DESIGN_ID_INDEX) {
                    if (!cellContent.contentEquals("")) {
                        try {
                            Statement stmt = MyClass.C.createStatement();
                            String designid = orderSlip.getModel().getValueAt(row, DESIGN_ID_INDEX) == null ? "" : orderSlip.getModel().getValueAt(row, DESIGN_ID_INDEX).toString();
                            ResultSet resultSet = stmt.executeQuery("Select * from inventory where DesignID= '" + designid + "';");
                            if (resultSet.next()) {
                                if (resultSet.getString(1).contentEquals(cellContent)) {
                                    model.setValueAt(resultSet.getString("itemname"), row, ITEM_NAME_INDEX);
                                    model.setValueAt(resultSet.getDouble("price"), row, RAW_MATERIAL_COST_INDEX);
                                }

                            } else {
                                TableColumnModel columnModel = orderSlip.getColumnModel();
                                model.setValueAt("", row, DESIGN_ID_INDEX);
                                model.setValueAt("", row, ITEM_NAME_INDEX);
                                model.setValueAt("", row, RAW_MATERIAL_COST_INDEX);
                                //add code to make raw amount also emppty
                                prevRow = 0;
                            }


                        } catch (SQLException ex) {
                            Thread.dumpStack();
                        }

                    }
                }
                if (column == QUANTITY_INDEX) {
                    model.setValueAt(getIntegerValue(model.getValueAt(row, QUANTITY_INDEX)) == null ? "" : getIntegerValue(model.getValueAt(row, QUANTITY_INDEX)), row, QUANTITY_INDEX);
                }
                if (column == RAW_MATERIAL_COST_INDEX) {
                    model.setValueAt(getDoubleValue(model.getValueAt(row, RAW_MATERIAL_COST_INDEX) == null ? "" : getDoubleValue(model.getValueAt(row, RAW_MATERIAL_COST_INDEX))), row, RAW_MATERIAL_COST_INDEX);
                }
                if (column == PLATING_INDEX) {
                    model.setValueAt(getDoubleValue(model.getValueAt(row, PLATING_INDEX) == null ? "" : getDoubleValue(model.getValueAt(row, PLATING_INDEX))), row, PLATING_INDEX);

                }
                int lastRow = model.getRowCount() - 1;
                if (row == lastRow && !cellContent.isEmpty()) {
                    model.setRowCount(model.getRowCount() + 1);
                }
                if (model.getRowCount() != 1) {
                    if (isRowEmpty(row)) {
                        model.removeRow(row);
                        disableName();
                    }
                }


                disableName();
                addModelListeners(listeners, model);

            }

        };
        model.addTableModelListener(modelListener);
        orderSlip.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK), "openPopup");
        orderSlip.getActionMap().put("openPopup", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        Vector<String> orderSlipType = new Vector<>();
        try {
            Statement statement = MyClass.C.createStatement();
            ResultSet rs = statement.executeQuery("select type_name from ordertype;");
            while (rs.next()) orderSlipType.add(rs.getString("type_name"));
        } catch (SQLException e) {
            Thread.dumpStack();
        }
        String insertMainQuery = "INSERT INTO order_slips_main (slip_type) VALUES (?)";
        orderSlipConnetionObject = null;
        try {
            orderSlipConnetionObject = DriverManager.getConnection(MyClass.login.getUrl(), MyClass.login.getLoginID(), MyClass.login.getPassword());
            orderSlipConnetionObject.setAutoCommit(false);

            PreparedStatement stmt = orderSlipConnetionObject.prepareStatement(insertMainQuery, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, orderSlipTypeComboBox.getSelectedItem() == null ? "" : orderSlipTypeComboBox.getSelectedItem().toString());
            stmt.executeUpdate();

            // Get the generated slip_id
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                setSlipID(rs.getInt(1));
            } else setSlipID(-1);

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DefaultComboBoxModel<String> panaTypeModel = new DefaultComboBoxModel<>(orderSlipType);
        orderSlipTypeComboBox.setModel(panaTypeModel);
        orderSlip.setModel(model);

    }

    private void setSlipID(int slipID) {
        this.slipID = slipID;
        slipIDLabel.setText(slipID + "");
    }

    private int slipID;

    public int getSlipID() {
        return slipID;
    }

    private Integer getIntegerValue(Object value) {
        if (value == null) return null;
        try {
            return Integer.valueOf(value.toString().trim());
        } catch (NumberFormatException ex) {
            System.err.println("Invalid integer input at line 311: " + value);
            return null;
        }
    }


    private boolean isRowEmpty(int row) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            String value = model.getValueAt(row, i) == null ? "" : model.getValueAt(row, i).toString();
            if (!value.isEmpty()) return false;
        }
        return true;
    }

    private void disableName() {
        Vector<int[]> listOfDisableCells = new Vector<>();
        Vector<Vector<Object>> tableData = new Vector<>();
        model = (DefaultTableModel) orderSlip.getModel();
        for (int countRow = 0; countRow < orderSlip.getRowCount(); countRow++) {
            String designID = orderSlip.getModel().getValueAt(countRow, 0) == null ? "" : orderSlip.getModel().getValueAt(countRow, 0).toString();
            if (!designID.contentEquals("")) {
                int[] arr = new int[2];
                arr[0] = countRow;
                arr[1] = 1;
                listOfDisableCells.add(arr);
                System.out.println(arr[0] + " " + arr[1]);
            }

        }
        System.out.println("end line\n\n\n\n\n");
        for (int ro = 0; ro < model.getRowCount(); ro++) {
            Vector<Object> rowData = new Vector<>();
            for (int col = 0; col < model.getColumnCount(); col++) {
                rowData.add(model.getValueAt(ro, col)); // Add cell data to row vector
            }
            tableData.add(rowData); // Add row vector to ArrayList
        }
        Vector<String> v = new Vector<>();
        for (int i = 0; i < orderSlip.getColumnCount(); i++) {
            v.add(orderSlip.getColumnName(i));
        }

        model = new DefaultTableModel(tableData, v) {
            @Override
            public boolean isCellEditable(int row, int column) {
                for (int[] cell : listOfDisableCells) {
                    return cell[0] != row || cell[1] != column; // Disable this cell
                }
                return true; // Other cells remain editable
            }
        };
        orderSlip.setModel(model);


//        TableColumn otherDetailsColumn = orderSlip.getColumn("Other Details");
//        otherDetailsColumn.setCellEditor(getTextAreaEditor());
//        otherDetailsColumn.setCellRenderer(getTextAreaRenderer());
    }
}
