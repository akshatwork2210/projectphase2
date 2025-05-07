package loginsignup.login.loggedin.ordermanagement.generateorder;

import mainpack.MyClass;
import testpackage.UtilityMethods;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.TableView;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

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

            try {
                // Generate a new slip_id for the entire batch
                String insertMainQuery = "INSERT INTO order_slips_main (slip_type) VALUES (?)";
                PreparedStatement stmt = MyClass.C.prepareStatement(insertMainQuery, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, orderSlipTypeComboBox.getSelectedItem() == null ? "" : orderSlipTypeComboBox.getSelectedItem().toString());
                stmt.executeUpdate();

                // Get the generated slip_id
                ResultSet rs = stmt.getGeneratedKeys();

                int slipId = -1;
                if (rs.next()) {
                    slipId = rs.getInt(1);  // Retrieve the auto-incremented slip_id
                }
                rs.close();
                boolean created = false;
                // Loop through each row and insert into order_slips
                for (int i = 0; i < rowCount - 1; i++) {
                    created = true;
                    if (model.getValueAt(i, 1).toString().contentEquals("")) break;
                    String designId = (String) model.getValueAt(i, 0);
                    String itemName = (String) model.getValueAt(i, 1);

                    int quantity = !model.getValueAt(i, 2).toString().contentEquals("") ? Integer.parseInt(model.getValueAt(i, 2).toString()) : 0;
                    double platingGrams = Double.parseDouble(model.getValueAt(i, 3).toString());
                    double rawMaterialCost = Double.parseDouble(model.getValueAt(i, 4).toString());
                    String otherDetails;
                    try {
                        otherDetails = model.getValueAt(i, 5).toString();
                    } catch (NullPointerException ex) {
                        otherDetails = "";
                    }
                    String customerName = customerNameComboBox.getSelectedItem() == null ? "" : customerNameComboBox.getSelectedItem().toString();
                    String orderSlipType = orderSlipTypeComboBox.getSelectedItem().toString();
                    int sno = i + 1;
                    // Insert Query
                    String query = "INSERT INTO order_slips (slip_type, customer_name, slip_id, design_id, item_name, quantity, plating_grams, raw_material_price, other_details, sno) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    PreparedStatement preparedStatement = MyClass.C.prepareStatement(query);
                    preparedStatement.setString(1, orderSlipType);
                    preparedStatement.setString(2, customerName);
                    preparedStatement.setInt(3, slipId);
                    preparedStatement.setString(4, designId);
                    preparedStatement.setString(5, itemName);
                    preparedStatement.setInt(6, quantity);
                    preparedStatement.setDouble(7, platingGrams);
                    preparedStatement.setDouble(8, rawMaterialCost);
                    preparedStatement.setString(9, otherDetails);
                    preparedStatement.setInt(10, sno);

                    preparedStatement.executeUpdate();
                    preparedStatement.close();


                }
                if (created) System.out.println("New  slip created: Slip ID = " + slipId);
                else
                    JOptionPane.showMessageDialog(MyClass.orderGenerateForm, "Empty form  error", "error", JOptionPane.ERROR_MESSAGE);

            } catch (SQLException ex) {
//                    ex.printStackTrace();
                Thread.dumpStack();
            }
            init();
        });
    }

    int prevRow = 0;
    DefaultTableModel model;

    public void init() {

        listOfDisabledCells = new Vector<>();
        ar = new ArrayList<>();

        generateAndAddDates(dateComboBox, false);
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
                int designIDindex = orderSlip.getColumnModel().getColumnIndex("design id");
                String cellContent;
                try {
                    cellContent = model.getValueAt(row, column) == null ? "" : model.getValueAt(row, column).toString();
                } catch (ArrayIndexOutOfBoundsException ex) {
                    disableName();
                    return;
                }
                if (column == designIDindex) {
                    if (!cellContent.contentEquals("")) {
                        try {
                            Statement stmt = MyClass.C.createStatement();
                            String designid = orderSlip.getModel().getValueAt(row, designIDindex) == null ? "" : orderSlip.getModel().getValueAt(row, designIDindex).toString();
                            ResultSet resultSet = stmt.executeQuery("Select * from inventory where DesignID= '" + designid + "';");
                            if (resultSet.next()) {
                                if (resultSet.getString(1).contentEquals(cellContent)) {
                                    model.setValueAt(resultSet.getString("itemname"), row, 1);
                                    model.setValueAt(resultSet.getString("price"), row, 4);
                                    model.setValueAt(resultSet.getString(1), row, 0);
                                }

                            } else {
                                TableColumnModel columnModel = orderSlip.getColumnModel();
                                model.setValueAt("", row, columnModel.getColumnIndex("design id"));
                                model.setValueAt("", row, columnModel.getColumnIndex("Item Name"));
                                model.setValueAt("", row, columnModel.getColumnIndex("Raw Material Cost"));

                                //add code to make raw amount also emppty

                                prevRow = 0;
                            }


                        } catch (SQLException ex) {
                            Thread.dumpStack();
                        }

                    }
                }
                int lastRow = model.getRowCount() - 1;
                if (row == lastRow && !cellContent.isEmpty()) {
                    model.setRowCount(model.getRowCount() + 1);
                }
                if (model.getRowCount() != 1) {
                    if (isRowEmpty(row)) {
                        model.removeRow(row);
//                       reMapKeys(row + 1);
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

        ArrayList<String> customerNames = new ArrayList<>();
        try {
            Statement stmt = MyClass.C.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT customer_name,customer_id FROM customers");
            customerNameComboBox.removeAllItems();
            customerNames.add("Select Customer");
            while (rs.next()) {
                String customerName = rs.getString("customer_name");
                customerNames.add(customerName);
            }


            for (String customerName : customerNames) {
                customerNameComboBox.addItem(customerName);
            }

        } catch (SQLException ex) {
            Thread.dumpStack();
            JOptionPane.showMessageDialog(null, "Error fetching customer data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        Vector<String> orderSlipType = new Vector<>();
        try {
            Statement statement = MyClass.C.createStatement();
            ResultSet rs = statement.executeQuery("select type_name from ordertype;");
            while (rs.next()) orderSlipType.add(rs.getString("type_name"));
        } catch (SQLException e) {
            Thread.dumpStack();
        }

        DefaultComboBoxModel<String> panaTypeModel = new DefaultComboBoxModel<>(orderSlipType);
        orderSlipTypeComboBox.setModel(panaTypeModel);
        orderSlip.setModel(model);

    }

    private void reMapKeys(int sno) {
        HashMap<Integer, String> tempMap = new HashMap<>();

        Iterator<Integer> iterator = snoToDetailsMap==null?null:snoToDetailsMap.keySet().iterator();
        while (iterator!=null && iterator.hasNext()) {
            int key = iterator.next();
            if (key < sno) {
                tempMap.put(key, snoToDetailsMap.get(key));
                continue;
            }
            if (key == sno) continue;
            if (key > sno) tempMap.put(key - 1, snoToDetailsMap.get(key));

        }
        snoToDetailsMap.clear();
        snoToDetailsMap.putAll(tempMap);
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
