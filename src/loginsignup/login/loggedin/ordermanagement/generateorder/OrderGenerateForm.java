package loginsignup.login.loggedin.ordermanagement.generateorder;

import mainpack.MyClass;
import testpackage.DBStructure;
import testpackage.CODES;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static testpackage.DBStructure.*;
import static testpackage.UtilityMethods.*;

public class OrderGenerateForm extends JFrame {

    HashMap<Integer, String> snoToDetailsMap;
    private JPanel panel;
    private JButton backButton;
    TableModelListener modelListener;
    private JComboBox<String> customerNameComboBox;
    private JComboBox<String> orderSlipTypeComboBox;
    private JTable orderSlipTable;
    private JButton submitButton;
    private JButton resetFormButton;
    private JButton undoResetButton;
    private JComboBox<String> dateComboBox;
    private JLabel slipIDLabel;
    private JButton inventorySelectButton;
    //THE BELOW CODE IS FOR COLUMN NAMES CONSTANTS
    public static final int DESIGN_ID_INDEX = 0;
    public static final int ITEM_NAME_INDEX = 1;
    public static final int QUANTITY_INDEX = 2;
    public static final int PLATING_INDEX = 3;
    public static final int RAW_MATERIAL_COST_INDEX = 4;
    public static final int OTHER_DETAILS_INDEX = 5;

    Connection orderSlipConnectionObject;

    public Connection getOrderSlipConnectionObject() {
        return orderSlipConnectionObject;
    }

    Vector<Integer[]> listOfDisabledCells;
    private Vector<Integer> listOfDisabledcolumn;


    public OrderGenerateForm() {

    }

    int oldQuantity;
    String oldDesignID="";

    public void init() {
//        backupModel = null;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        orderSlipTable.addPropertyChangeListener(evt -> {
            int row = orderSlipTable.getSelectedRow();
            DefaultTableModel defaultTableModel = (DefaultTableModel) orderSlipTable.getModel();
            if (row == -1) return;
            String oldQuantityString = Objects.toString(defaultTableModel.getValueAt(row, QUANTITY_INDEX), "0");
            oldQuantityString = oldQuantityString.trim().isEmpty() ? "0" : oldQuantityString;
            oldQuantity = Integer.parseInt(oldQuantityString);
            oldDesignID = Objects.toString(model.getValueAt(row, DESIGN_ID_INDEX), "");
        });

        setContentPane(panel);


        backButton.addActionListener(e -> {
            dispose();
            MyClass.inventorySelect.dispose();
            MyClass.orderScreen.setVisible(true);
        });
        orderSlipTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }
        });

        resetFormButton.addActionListener(e -> {
            int answer = JOptionPane.showConfirmDialog(OrderGenerateForm.this, "are you sure you want to reset the form, data will be lost?");
            if (answer != JOptionPane.YES_OPTION) return;
            dispose();
            MyClass.inventorySelect.dispose();
            MyClass.orderGenerateForm = new OrderGenerateForm();
            MyClass.orderGenerateForm.init();
            MyClass.orderGenerateForm.setVisible(true);
        });
        submitButton.addActionListener(e -> {
            if (customerNameComboBox.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(MyClass.orderGenerateForm, "please select a customer name", "incomplete information", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (pushSlipData() == CODES.SUCCESS_CODE && updateInventory() == CODES.SUCCESS_CODE) {
                try {
                    orderSlipConnectionObject.commit();
                    dispose();
                    MyClass.orderGenerateForm = new OrderGenerateForm();
                    MyClass.orderGenerateForm.init();
                    MyClass.orderGenerateForm.setVisible(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });
        inventorySelectButton.addActionListener(e -> {
            if (MyClass.inventorySelect.isVisible()) return;
            MyClass.inventorySelect = new InventorySelect();
            MyClass.inventorySelect.init(OrderGenerateForm.this);
            MyClass.inventorySelect.setVisible(true);
            splitFrame(OrderGenerateForm.this, MyClass.inventorySelect, VERTI_SPLIT);
        });
        MyClass.inventorySelect = new InventorySelect();
        MyClass.inventorySelect.init(this);
        splitFrame(this, MyClass.inventorySelect, VERTI_SPLIT);
        MyClass.inventorySelect.setVisible(true);
        //refresh method code below


        columnNames = new Vector<>(Arrays.asList(new String[]{"design id", "Item Name", "Quantity", "Plating", "Raw Material Cost", "Other Details"}));
        listOfDisabledcolumn = new Vector<>();
        listOfDisabledCells = new Vector<>();
        generateAndAddDates(dateComboBox, false);
        generateAndAddNames(customerNameComboBox);
        orderSlipTable.getTableHeader().setReorderingAllowed(false);
        // Create a DefaultTableModel with columns and no rows initially
        model = new DefaultTableModel(columnNames, 1);
        modelListener = e -> {

            if (e.getType() == TableModelEvent.UPDATE) {

                TableModelListener[] listeners = removeModelListener(model);
                int row = e.getFirstRow();
                int column = e.getColumn();
                Integer quantity = getIntegerValue(Objects.toString(model.getValueAt(row, QUANTITY_INDEX), String.valueOf(0)));
                String cellContent;
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
                    if (!itemName.contentEquals(String.valueOf(DBStructure.NOT_FOUND))) {
                        model.setValueAt(itemName, row, ITEM_NAME_INDEX);
                        model.setValueAt(DBStructure.getSellPrice(designID), row, RAW_MATERIAL_COST_INDEX);
                        listOfDisabledCells.add(new Integer[]{row, ITEM_NAME_INDEX});
                    } else {
                        model.setValueAt("", row, DESIGN_ID_INDEX);
                        model.setValueAt("", row, ITEM_NAME_INDEX);
                        model.setValueAt("", row, RAW_MATERIAL_COST_INDEX);
                        model.setValueAt("", row, QUANTITY_INDEX);
                        quantity = 0;
                        listOfDisabledCells.removeIf(cell -> (cell[0] == row && cell[1] == ITEM_NAME_INDEX));
                    }
                }
                if (column == QUANTITY_INDEX) {
                    if (quantity == null) quantity = 0;
                    if (quantity == 0) {
                        model.setValueAt("", row, QUANTITY_INDEX);
                    }
                }

                if (column == RAW_MATERIAL_COST_INDEX) {
                    Double rawPrice = getDoubleValue(Objects.toString(model.getValueAt(row, RAW_MATERIAL_COST_INDEX), "0"));
                    model.setValueAt(rawPrice == null || rawPrice == 0 ? "" : rawPrice, row, RAW_MATERIAL_COST_INDEX);
                }
                if (column == PLATING_INDEX) {
                    Double plating = getDoubleValue(Objects.toString(model.getValueAt(row, PLATING_INDEX), "0"));
                    model.setValueAt(plating == null || plating == 0 ? "" : plating, row, PLATING_INDEX);
                }
                if (row == model.getRowCount() - 1 && !cellContent.isEmpty()) {
                    model.setRowCount(model.getRowCount() + 1);
                }
                if ((row != model.getRowCount() - 1) && isRowEmpty(row)) {
                    model.removeRow(row);
                    refreshListOfDisabledCells();
                }
                reBuildModel();
                String designID = Objects.toString(model.getValueAt(row, DESIGN_ID_INDEX), "").toUpperCase();
                System.out.println(oldDesignID.contains(designID));
                if (oldDesignID.contentEquals(designID)) {
                    System.out.println("design id is not changed");
                    quantity = (quantity - oldQuantity);
                    MyClass.inventorySelect.updateTable(quantity, designID);
                } else {
                    System.out.println("design id is changed");
                    MyClass.inventorySelect.updateTable((-oldQuantity),oldDesignID);
                    MyClass.inventorySelect.updateTable((quantity),designID);
                }
                addModelListeners(listeners, model);

            }
        };
        model.addTableModelListener(modelListener);
        orderSlipTable.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK), "openPopup");
        orderSlipTable.getActionMap().put("openPopup", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        Vector<String> orderSlipType = new Vector<>();
        try (Connection con = MyClass.createConnection(); Statement statement = con.createStatement()) {
            try (ResultSet rs = statement.executeQuery("select type_name from ordertype;")) {
                while (rs.next()) orderSlipType.add(rs.getString("type_name"));
            }
        } catch (SQLException e) {
            Thread.dumpStack();
        }
        String insertMainQuery = "INSERT INTO order_slips_main (slip_type) VALUES (?)";
        orderSlipConnectionObject = null;
        try {
            orderSlipConnectionObject = DriverManager.getConnection(MyClass.login.getUrl(), MyClass.login.getLoginID(), MyClass.login.getPassword());
            orderSlipConnectionObject.setAutoCommit(false);

            PreparedStatement stmt = orderSlipConnectionObject.prepareStatement(insertMainQuery, Statement.RETURN_GENERATED_KEYS);
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
        orderSlipTable.setModel(model);
//        pack();


    }

    private int updateInventory() {


        return Integer.parseInt(null);
    }

    //    public void
    public int pushSlipData() {
        DefaultTableModel model = (DefaultTableModel) orderSlipTable.getModel();
        int rowCount = model.getRowCount();
        String updateOrderSlipsMainTableQuery = "update " + ORDER_SLIPS_MAIN_TABLE + " set " + ORDER_SLIPS_CREATED_AT + "=? where " + ORDER_SLIPS_MAIN_SLIP_ID + "=?";
        String slipDataInsertQuery = "INSERT INTO " + ORDER_SLIPS_TABLE + " (" + ORDER_SLIPS_SLIP_TYPE + ", " + ORDER_SLIPS_CUSTOMER_NAME + ", " + ORDER_SLIPS_SLIP_ID + ", " + ORDER_SLIPS_DESIGN_ID + ", " + ORDER_SLIPS_ITEM_NAME + ", " + ORDER_SLIPS_QUANTITY + ", " + ORDER_SLIPS_PLATING_GRAMS + ", "
                + ORDER_SLIPS_RAW_MATERIAL_PRICE + ", " + ORDER_SLIPS_OTHER_DETAILS + ", " + ORDER_SLIPS_SNO + "," + ORDER_SLIPS_CREATED_AT + ") " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement updateOrderSlipsMainTableStatement = orderSlipConnectionObject.prepareStatement(updateOrderSlipsMainTableQuery);
             PreparedStatement slipDataStatement = orderSlipConnectionObject.prepareStatement(slipDataInsertQuery)
        ) {
            java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(LocalDateTime.parse(Objects.toString(dateComboBox.getSelectedItem(), "") + " 00:00:00", DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss")));
            updateOrderSlipsMainTableStatement.setTimestamp(1, timestamp);
            updateOrderSlipsMainTableStatement.setInt(2, getSlipID());
            updateOrderSlipsMainTableStatement.executeUpdate();
            for (int i = 0; i < rowCount - 1; i++) {
                if (model.getValueAt(i, ITEM_NAME_INDEX) == null || model.getValueAt(i, ITEM_NAME_INDEX).toString().trim().contentEquals(""))
                    break;
                String designId = model.getValueAt(i, DESIGN_ID_INDEX) != null ? model.getValueAt(i, DESIGN_ID_INDEX).toString() : "";
                String itemName = (String) model.getValueAt(i, ITEM_NAME_INDEX);
                int quantity = getIntegerValue(Objects.toString(model.getValueAt(i, QUANTITY_INDEX), "0"));
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
            return CODES.SUCCESS_CODE;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(MyClass.orderGenerateForm, " error " + ex.getMessage());
            ex.printStackTrace();
            return CODES.SQL_ERROR;
        }

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

    private void refreshListOfDisabledCells() {
        listOfDisabledCells.clear();
        for (int i = 0; i < model.getRowCount(); i++) {
            String value = Objects.toString(model.getValueAt(i, DESIGN_ID_INDEX), "");
            if (!value.isEmpty()) {
                listOfDisabledCells.add(new Integer[]{i, ITEM_NAME_INDEX});
            }
        }
        for (Integer[] value : listOfDisabledCells) {
            System.out.println(value[0] + " " + value[1]);
        }
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
            public boolean isCellEditable(int row, int column) {
                for (Integer[] cell : listOfDisabledCells) {
                    if (cell[0] == row && cell[1] == column) {
                        return false;
                    }
                }
                for (Integer col : listOfDisabledcolumn) {
                    if (col == column) return false;

                }
                return true;
            }
        };
        orderSlipTable.setModel(model);


    }
}
