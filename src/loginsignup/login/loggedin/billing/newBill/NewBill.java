package loginsignup.login.loggedin.billing.newBill;

import mainpack.MyClass;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Vector;

public class NewBill extends JFrame {
    private JComboBox customerComboBox;
    private JPanel panel;
    private JLabel idLabel;
    private JTable billTable;
    private JButton backButton;
    private JComboBox comboBox1;
    private JComboBox dateComboBox;

    public NewBill() {

        setContentPane(panel);
        pack();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.billingScreen.setVisible(true);
            }
        });
    }

    DefaultTableModel tableModel;
    Vector<String> billDetails = new Vector<>();

    public void initSystemlogin() {
        billDetails = new Vector<>();
        listOfNonEditableCells = new ArrayList<>();
        Vector<String> dateList = new Vector<>();
        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusYears(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        billDetails.add("SNo");
        billDetails.add("ItemName");
        billDetails.add("DesignID");
        billDetails.add("Labour");
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
                        if (tableModel.getValueAt(row, i) != null &&
                                !tableModel.getValueAt(row, i).toString().trim().isEmpty()) {
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
                    if(col==designIdIndex){
                        tableModel.removeTableModelListener(this);
                        String designID = tableModel.getValueAt(row, designIdIndex).toString().trim();

                        if (!designID.isEmpty()) {
                            String query = "SELECT itemname FROM inventory WHERE DesignID = ?";

                            try (PreparedStatement stmt = MyClass.C.prepareStatement(query)) {

                                stmt.setString(1, designID);
                                ResultSet rs = stmt.executeQuery();

                                if (rs.next()) {
                                    // If DesignID exists, update ItemName column
                                    String itemName = rs.getString("itemname");
                                    tableModel.setValueAt(itemName, row, billDetails.indexOf("ItemName"));
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
        billTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("control D"), "copyAbove");

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
            Statement stmt;
            stmt = MyClass.C.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT customer_name FROM customers");
            while (rs.next()) {
                customerComboBox.addItem(rs.getString("customer_name"));
            }
            String query = "SELECT MAX(BillID) FROM bills;";

            rs = stmt.executeQuery(query);

            int newBillID = 1; // Default BillID if no bills exist

            // If the query returns a result, get the max BillID and increment it
            if (rs.next()) {
                int lastBillID = rs.getInt(1);
                if (rs.wasNull()) {
                    newBillID = 1; // If no bills, set BillID to 1
                } else {
                    newBillID = lastBillID + 1; // Increment the last BillID
                }
            }

            idLabel.setText("Bill ID: " + newBillID);
            pack();
            setExtendedState(JFrame.MAXIMIZED_BOTH);

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


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

        for (TableModelListener tlm:((DefaultTableModel) billTable.getModel()).getTableModelListeners()){
            m.addTableModelListener( tlm);
        }
        return m;
    }
}
