package loginsignup.login.loggedin.inventorymanagement.addinventory;

import mainpack.MyClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddInventory extends JFrame
{
    public AddInventory(){
        setContentPane(panel);

        submitQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!designID.getText().contentEquals("") && !openingStock.getText().contentEquals("") && !itemName.getText().contentEquals("")) {
                    try {
                        // Establish database connection
                        String query = "INSERT INTO Inventory (DesignID, TotalQuantity, SupplierName) VALUES (?, ?, ?)";
                        PreparedStatement stmt = MyClass.C.prepareStatement(query);
                        stmt.setString(1, designID.getText());
                        stmt.setInt(2, Integer.parseInt(openingStock.getText()));
                        stmt.setString(3, itemName.getText());

                        // Execute the update
                        int rowsAffected = stmt.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(MyClass.addInventory, "Data added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                           itemName.setText("");
                           openingStock.setText("");
                           designID.setText("");


                        } else {
                            JOptionPane.showMessageDialog(MyClass.addInventory, "Failed to add data.", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                        // Close the connection

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(MyClass.addInventory, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(MyClass.addInventory, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            MyClass.inventoryScreen.refresh();
            }
        });
    pack();
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.inventoryScreen.setVisible(true);
            }
        });
    }
    public void init(){}
    private JButton submitQueryButton;
    private JButton backButton;
    private JTextField itemName;
    private JTextField designID;
    private JTextField openingStock;
    private JPanel panel;
}
