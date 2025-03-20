package loginsignup.login.loggedin.ordermanagement.generateorder;

import mainpack.MyClass;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class OrderGenerateForm extends JFrame {
    private HashMap<Integer, String> customerNameMap;

    private JPanel panel;
    DefaultTableModel backupModel;
    private JButton backButton;
    TableModelListener modelListener;
    private JComboBox customerNameComboBox;
    private JComboBox panaTypeComboBox;
    private JTable orderSlip;
    private JButton submitButton;
    private JButton resetFormButton;
    private JButton undoResetButton;
    ArrayList<Integer[][]> ar;

    public HashMap<Integer, String> getCustomerNameMap() {
        return customerNameMap;
    }

    public OrderGenerateForm() {
        setContentPane(panel);

        pack();
        undoResetButton.setEnabled(false);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.orderScreen.setVisible(true);
            }
        });
        orderSlip.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }
        });

        resetFormButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                backupModel = (DefaultTableModel) orderSlip.getModel();
                init();
                undoResetButton.setEnabled(true);
                resetFormButton.setEnabled(false);
            }
        });
        undoResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model = backupModel;
                orderSlip.setModel(backupModel);
                backupModel = null;
                undoResetButton.setEnabled(false);
                resetFormButton.setEnabled(true);
            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
                    stmt.setString(1, panaTypeComboBox.getSelectedItem().toString());
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
                        int itemNumber = i + 1;  // Item number starts from 1 for each new slip
                        if (model.getValueAt(i, 1).toString().contentEquals("")) break;
                        String designId = (String) model.getValueAt(i, 0);
                        String itemName = (String) model.getValueAt(i, 1);

                        int quantity = !model.getValueAt(i, 2).toString().contentEquals("") ? Integer.parseInt(model.getValueAt(i, 2).toString()) : 0;
                        double platingGrams = Double.parseDouble(model.getValueAt(i, 3).toString());
                        double rawMaterialCost = Double.parseDouble(model.getValueAt(i, 4).toString());
                        String otherDetails;
                        try {
                            otherDetails = model.getValueAt(i, 5).toString();
                        } catch (java.lang.NullPointerException ex) {
                            otherDetails = "";
                        }
                        String customerName = customerNameComboBox.getSelectedItem().toString();
                        String panaType = panaTypeComboBox.getSelectedItem().toString();
                        int sno=i+1;
                        // Insert Query
                        String query = "INSERT INTO order_slips (slip_type,customer_name,slip_id, item_number, design_id, item_name, quantity, plating_grams, raw_material_price, other_details,sno) " + "VALUES (" + "\"" + panaType + "\"," + "\"" + customerName + "\"," + slipId + ", " + itemNumber + ", '" + designId + "', '" + itemName + "', " + quantity + ", " + platingGrams + ", " + rawMaterialCost + ", \"" + otherDetails + "\","+sno+");";

                        stmt.executeUpdate(query);

                    }
                    if (created) System.out.println("New  slip created: Slip ID = " + slipId);
                    else
                        JOptionPane.showMessageDialog(MyClass.orderGenerateForm, "Empty form  error", "error", JOptionPane.ERROR_MESSAGE);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                init();
            }
        });
    }

    int prevRow = 0;
    DefaultTableModel model;

    public void init() {
        ar = new ArrayList<>();
        // this method will be initializing functinality of this window
        String[] columnNames = {"design id", "Item Name", "Quantity", "Plating", "Raw Material Cost", "Other Details"};//jtable content
        orderSlip.getTableHeader().setReorderingAllowed(false);
        // Create a DefaultTableModel with columns and no rows initially
        model = new DefaultTableModel(columnNames, 1);
        modelListener = new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    if (model.getRowCount() == 3) undoResetButton.setEnabled(false);
                    resetFormButton.setEnabled(true);
                    int lastRow = model.getRowCount() - 1;
                    boolean isRowFilled = false;

                    // Check if any column in the flast row is filled
                    for (int i = 0; i < model.getColumnCount(); i++) {
                        Object value = model.getValueAt(lastRow, i);
                        if (value != null && !value.toString().trim().isEmpty()) {
                            isRowFilled = true;
                            break;
                        }
                    }

                    if (isRowFilled) {
                        model.addRow(new Object[]{"", null, "", null, ""});
                    }


                    int row = orderSlip.getSelectedRow(), column = orderSlip.getSelectedColumn();
                    String cellContent;
                    try {
                        cellContent = orderSlip.getModel().getValueAt(row, column).toString();
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        disableName();
                        return;
                    }
                    DefaultTableModel newModel = null;


                    if (column == 0) {
                        if (!cellContent.contentEquals("")) {
                            model.removeTableModelListener(modelListener);
//                            JOptionPane.showMessageDialog(MyClass.orderGenerateForm, "success in building algorithm");
                            int designIDindex = orderSlip.getColumnModel().getColumnIndex("design id");
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
                                    model.setValueAt("", row, 1);
                                    model.setValueAt("", row, 0);
                                    prevRow = 0;
                                }


                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }

                        }
                    }

                    model.addTableModelListener(modelListener);

                    disableName();

                }

            }
        };
        model.addTableModelListener(modelListener);
        customerNameMap = new HashMap<>();

        ArrayList<String> customerNames = new ArrayList<>();
        try {

            Statement stmt = MyClass.C.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT customer_name,customer_id FROM customers");
            // Clear previous entries in combo boxes before populating
            customerNameComboBox.removeAllItems();
            customerNames.add("Select Customer");
            customerNameMap.clear();
            // Fetching the data and adding to lists
            while (rs.next()) {
                String customerName = rs.getString("customer_name");
                customerNameMap.put(rs.getInt("customer_id"), rs.getString("customer_name"));
                customerNames.add(customerName);
            }

            // Adding data to combo boxes


            for (String customerName : customerNames) {
                customerNameComboBox.addItem(customerName);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching customer data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        Vector<String> panaTypes = new Vector<>();
        try {
            Statement statement = MyClass.C.createStatement();
            ResultSet rs = statement.executeQuery("select type_name from ordertype;");
            while (rs.next()) panaTypes.add(rs.getString("type_name"));
        } catch (SQLException e) {

        }

        DefaultComboBoxModel<String> panaTypeModel = new DefaultComboBoxModel<>(panaTypes);
        panaTypeComboBox.setModel(panaTypeModel);
        orderSlip.setModel(model);

    }

    private DefaultTableModel disableName() {
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

        DefaultTableModel m = new DefaultTableModel(tableData, v) {
            @Override
            public boolean isCellEditable(int row, int column) {
                for (int[] cell : listOfDisableCells) {
                    if (cell[0] == row && cell[1] == column) {

                        return false; // Disable this cell

                    } else return true;
                }
                return true; // Other cells remain editable
            }
        };

        m.addTableModelListener(modelListener);
        model = m;
        orderSlip.setModel(model);
        return m;
    }
}
