package loginsignup.login.loggedin.inventorymanagement;

import mainpack.MyClass;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InventoryScreen extends  JFrame {
    private JPanel panel;
    private JButton backButton;
    private JTable inventoryTable;
    private JButton addInventoryButton;

    public InventoryScreen() {
        setContentPane(panel);


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.mainScreen.setVisible(true);
            }
        });
        addInventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.addInventory.setVisible(true);
                MyClass.addInventory.init();
            }
        });
    }
    public void refresh(){
        init();
    }
    public void init(){
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DefaultTableModel m = new DefaultTableModel(
                new Object[][]{}, // Empty initial data
                new String[]{"Design ID", "Total Quantity", "Supplier Name"} // Column names
        ){
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent all cells from being editable
            }
        };
        inventoryTable.setModel(m);
       try {
            String query = "SELECT `DesignID`, `TotalQuantity`, `SupplierName` FROM inventory";
           Statement stmt = MyClass.C.createStatement();
            ResultSet resultSet=stmt.executeQuery(query);

            while (resultSet.next()) {
                String designId = resultSet.getString("DesignID");
                int totalQuantity = resultSet.getInt("TotalQuantity");
                String supplierName = resultSet.getString("SupplierName");

                m.addRow(new Object[]{designId, totalQuantity, supplierName});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        inventoryTable.setModel(m);
    pack();
    }
}
