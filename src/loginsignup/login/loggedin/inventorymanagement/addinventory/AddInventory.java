package loginsignup.login.loggedin.inventorymanagement.addinventory;

import mainpack.MyClass;
import testpackage.UtilityMethods;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static testpackage.DBStructure.*;

public class AddInventory extends JFrame {
    public AddInventory() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);

        submitQueryButton.addActionListener(e -> {
            if (
                    !designID.getText().contentEquals("") &&
                            !openingStockField.getText().contentEquals("") &&
                            !itemNameField.getText().contentEquals("") &&
                            !buyPriceField.getText().trim().isEmpty() &&
                            !sellPriceField.getText().trim().isEmpty()
            ) {
                String inventoryAddQuery = "INSERT INTO " + INVENTORY_TABLE + " (" + INVENTORY_DESIGN_ID + ", " + INVENTORY_TOTAL_QUANTITY + ", " + INVENTORY_SUPPLIER_NAME + "," + INVENTORY_BUY_PRICE + ", " + INVENTORY_SELL_PRICE + ", " + INVENTORY_ITEM_NAME + ") VALUES (?, ?, ?,? ,?,? )";
                int returnCode=addData();
            } else {
                JOptionPane.showMessageDialog(MyClass.addInventory, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            MyClass.inventoryScreen.refresh();
        });
        pack();
        backButton.addActionListener(_ -> {
            setVisible(false);
            MyClass.inventoryScreen.setVisible(true);
        });
    }

    private int addData() {
        try (Connection con = MyClass.createConnection();
             PreparedStatement stmt = con.prepareStatement(inventoryAddQuery)) {
            stmt.setString(1, designID.getText());
            stmt.setInt(2, Integer.parseInt(openingStockField.getText()));
            stmt.setString(3, supplierComboBox.getSelectedItem().toString());
            try {
                stmt.setDouble(4, Double.parseDouble(buyPriceField.getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "invalid buy price entered");
                return;
            }
            try {
                stmt.setDouble(5, Double.parseDouble(sellPriceField.getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "invalid sell price entered");
                return;
            }
            stmt.setString(6, itemNameField.getText());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return 0;
                JOptionPane.showMessageDialog(MyClass.addInventory, "Data added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clear();
            } else {
                return 1;
                JOptionPane.showMessageDialog(MyClass.addInventory, "Failed to add data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return 0;
    }

    private int updateData(Connection con) {
        String inventoryUpdateQuery = "UPDATE " + INVENTORY_TABLE + " SET "
                + INVENTORY_TOTAL_QUANTITY + " = " + INVENTORY_TOTAL_QUANTITY + " + ?, "
                + INVENTORY_ITEM_NAME + " = ?, "
                + INVENTORY_BUY_PRICE + " = ?, "
                + INVENTORY_SELL_PRICE + " = ? "
                + "WHERE " + INVENTORY_DESIGN_ID + " = ?";

        try (PreparedStatement inventoryUpdateStmt = con.prepareStatement(inventoryUpdateQuery)) {

            inventoryUpdateStmt.setDouble(1, Double.parseDouble(openingStockField.getText())); // quantity increment
            inventoryUpdateStmt.setString(2, itemNameField.getText()); // assuming you have this field
            inventoryUpdateStmt.setDouble(3, Double.parseDouble(buyPriceField.getText())); // buy price
            inventoryUpdateStmt.setDouble(4, Double.parseDouble(sellPriceField.getText())); // sell price
            inventoryUpdateStmt.setString(5, designID.getText()); // WHERE condition

            inventoryUpdateStmt.executeUpdate();
            return 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    private void clear() {
        sellPriceField.setText("");
        buyPriceField.setText("");
        itemNameField.setText("");
        supplierComboBox.setSelectedIndex(0);
        designID.setText("");
        openingStockField.setText("");
    }

    public void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UtilityMethods.generateAndAddNames(supplierComboBox);

    }

    private JButton submitQueryButton;
    private JButton backButton;
    private JTextField itemNameField;
    private JTextField designID;
    private JTextField openingStockField;
    private JPanel panel;
    private JComboBox supplierComboBox;
    private JTextField buyPriceField;
    private JTextField sellPriceField;
}
