package loginsignup.login.loggedin.inventorymanagement.addinventory;

import mainpack.MyClass;
import testpackage.UtilityMethods;

import javax.lang.model.util.ElementScanner6;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static testpackage.DBStructure.*;
import static testpackage.ERROR_CODES.*;

public class AddInventory extends JFrame {
    public AddInventory() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);

        submitQueryButton.addActionListener(e -> {
            if (
                    !designID.getText().trim().isEmpty() &&
                            !openingStockField.getText().trim().isEmpty() &&
                            !itemNameField.getText().trim().isEmpty() &&
                            !buyPriceField.getText().trim().isEmpty() &&
                            !sellPriceField.getText().trim().isEmpty()) {
                int returnCode = addData();
                if ((returnCode == DUPLICATE_SQL_ENTRY)) {
                    int answer = JOptionPane.showConfirmDialog(panel, "design id duplicate, press yes to add to the quantity and change item names and prices");
                    if (answer == JOptionPane.YES_OPTION)
                        returnCode = updateData(); else return;
                    if (returnCode == SUCCESS) JOptionPane.showMessageDialog(panel, "succesfully updated data");
                    else {
                        JOptionPane.showMessageDialog(panel, "sql error occured");
                        return;
                    }
                } else if (returnCode != SUCCESS) {
                    JOptionPane.showMessageDialog(panel, "an error has occured, exiting the system program");
                    System.exit(FAIL_CODE);
                }
            } else {
                JOptionPane.showMessageDialog(MyClass.addInventory, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            MyClass.inventoryScreen.refresh();
        });
        pack();
        backButton.addActionListener(e -> {
            AddInventory.this.setVisible(false);
            MyClass.inventoryScreen.setVisible(true);
        });
    }

    private int addData() {
        String inventoryAddQuery = "INSERT INTO " + INVENTORY_TABLE + " (" + INVENTORY_DESIGN_ID + ", " + INVENTORY_TOTAL_QUANTITY + ", " + INVENTORY_SUPPLIER_NAME + "," + INVENTORY_BUY_PRICE + ", " + INVENTORY_SELL_PRICE + ", " + INVENTORY_ITEM_NAME + ") VALUES (?, ?, ?,? ,?,? )";
        try (Connection con = MyClass.createConnection();
             PreparedStatement stmt = con.prepareStatement(inventoryAddQuery)) {
            stmt.setString(1, designID.getText());
            stmt.setInt(2, Integer.parseInt(openingStockField.getText()));
            stmt.setString(3, "hi   ");
            try {
                stmt.setDouble(4, Double.parseDouble(buyPriceField.getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "invalid buy price entered");
                return NUMBER_FORMAT_ERROR;
            }
            try {
                stmt.setDouble(5, Double.parseDouble(sellPriceField.getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "invalid sell price entered");
                return NUMBER_FORMAT_ERROR;
            }
            stmt.setString(6, itemNameField.getText());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(MyClass.addInventory, "Data added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clear();
                return SUCCESS;
            } else {
                JOptionPane.showMessageDialog(MyClass.addInventory, "Failed to add data.", "Error", JOptionPane.ERROR_MESSAGE);
                return SQL_ERROR;
            }
        } catch (java.sql.SQLIntegrityConstraintViolationException ex) {
            if (ex.getErrorCode() == 1062) {
                return 1062;
            } else {
                ex.printStackTrace();
                return SQL_ERROR;
            }
        } catch (SQLException e) {

            e.printStackTrace();
            return SQL_ERROR;

        }
    }

    private int updateData() {
        String inventoryUpdateQuery = "UPDATE " + INVENTORY_TABLE + " SET "
                + INVENTORY_TOTAL_QUANTITY + " = " + INVENTORY_TOTAL_QUANTITY + " + ?, "
                + INVENTORY_ITEM_NAME + " = ?, "
                + INVENTORY_BUY_PRICE + " = ?, "
                + INVENTORY_SELL_PRICE + " = ? ,"
                + INVENTORY_SUPPLIER_NAME + " =? "
                + "WHERE " + INVENTORY_DESIGN_ID + " = ?";

        try (Connection con = MyClass.createConnection(); PreparedStatement inventoryUpdateStmt = con.prepareStatement(inventoryUpdateQuery)) {
            inventoryUpdateStmt.setDouble(1, Double.parseDouble(openingStockField.getText())); // quantity increment
            inventoryUpdateStmt.setString(2, itemNameField.getText()); // assuming you have this field
            inventoryUpdateStmt.setDouble(3, Double.parseDouble(buyPriceField.getText())); // buy price
            inventoryUpdateStmt.setDouble(4, Double.parseDouble(sellPriceField.getText())); // sell price
            inventoryUpdateStmt.setString(5, supplierComboBox.getSelectedItem().toString()); // WHERE condition
            inventoryUpdateStmt.setString(6, designID.getText()); // WHERE condition
            inventoryUpdateStmt.executeUpdate();
            return SUCCESS;
        } catch (SQLException e) {
            e.printStackTrace();
            return SQL_ERROR;
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
