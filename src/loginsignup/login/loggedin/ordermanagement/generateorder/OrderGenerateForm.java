package loginsignup.login.loggedin.ordermanagement.generateorder;

import mainpack.MyClass;
import testpackage.DBStructure;
import testpackage.UtilityMethods;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
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
    private Vector<Integer> listOfDisabledcolumn;


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
            MyClass.orderGenerateForm = new OrderGenerateForm();
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
    Vector<String> columnNames;

    public void init() {
        columnNames = new Vector<>(Arrays.asList(new String[]{"design id", "Item Name", "Quantity", "Plating", "Raw Material Cost", "Other Details"}));
        listOfDisabledcolumn = new Vector<>();

        listOfDisabledCells = new Vector<>();
        ar = new ArrayList<>();
        generateAndAddDates(dateComboBox, false);
        generateAndAddNames(customerNameComboBox);
        orderSlip.getTableHeader().setReorderingAllowed(false);
        // Create a DefaultTableModel with columns and no rows initially
        model = new DefaultTableModel(columnNames, 1);
        modelListener = e -> {

            if (e.getType() == TableModelEvent.UPDATE) {
                TableModelListener[] listeners = UtilityMethods.removeModelListener(model);
                int row = e.getFirstRow();
                int column = e.getColumn();

                model.removeTableModelListener(modelListener);
                String cellContent = "";
                try {
                    cellContent = Objects.toString(model.getValueAt(row, column), "");
                } catch (ArrayIndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                    addModelListeners(listeners, model);
                    return;
                }

                if (column == DESIGN_ID_INDEX) {
                    String designID = cellContent;
                    String itemName = DBStructure.getInventoryItemName(designID);
                    if (itemName.contentEquals(String.valueOf(DBStructure.NOT_FOUND))) {
                        model.setValueAt("", row, DESIGN_ID_INDEX);
                        model.setValueAt("", row, ITEM_NAME_INDEX);
                        model.setValueAt("", row, RAW_MATERIAL_COST_INDEX);
                        listOfDisabledCells.removeIf(cell -> (cell[0] == row && cell[1] == ITEM_NAME_INDEX));
                    } else {
                        model.setValueAt(itemName, row, ITEM_NAME_INDEX);
                        model.setValueAt(DBStructure.getSellPrice(designID), row, RAW_MATERIAL_COST_INDEX);
                        listOfDisabledCells.add(new Integer[]{row, ITEM_NAME_INDEX});
                    }
                    reBuildModel();

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
                        refreshListOfDisabledCells();
                    }
                }


                reBuildModel();
                UtilityMethods.addModelListeners(listeners, model);

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

    private void refreshListOfDisabledCells() {
        listOfDisabledCells.clear();
        for (int i = 0; i < model.getRowCount(); i++) {
            String value = Objects.toString(model.getValueAt(i, DESIGN_ID_INDEX),"");
            if (!value.isEmpty()) {
                listOfDisabledCells.add(new Integer[]{i, ITEM_NAME_INDEX});
            }
        }
        for (Integer[] value : listOfDisabledCells) {
            System.out.println(value[0] + " " + value[1]);
        }
        System.out.println("finish");
        reBuildModel();
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
            System.out.println(ex.getMessage() + " from getInteger Value function");

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

    private void reBuildModel() {
        Vector<Vector<Object>> dataVector = new Vector<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            Vector<Object> data = new Vector<>();
            for (int j = 0; j < model.getColumnCount(); j++) {
                data.add(model.getValueAt(i, j));
            }
            dataVector.add(data);
        }

        model = new DefaultTableModel(dataVector, columnNames) {
            @Override
            public boolean isCellEditable(int row1, int column) {
                for (Integer[] cell : listOfDisabledCells) {
                    if (cell[0] == row1 && cell[1] == column) {
                        return false;
                    }
                }
                for (Integer col :  listOfDisabledcolumn) {
                    if (col == column) return false;
                }
                return true;
            }
        };
        orderSlip.setModel(model);


    }
}
