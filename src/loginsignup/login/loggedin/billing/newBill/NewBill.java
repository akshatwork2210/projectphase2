package loginsignup.login.loggedin.billing.newBill;

import mainpack.MyClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NewBill extends JFrame {
    private JComboBox customerComboBox;
    private JPanel panel;
    private JLabel idLabel;
    private JTable table1;
    private JButton backButton;

    public NewBill(){
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

    public void initSystemlogin(){
        customerComboBox.removeAllItems();
        customerComboBox.addItem("Select Customer");

        try {
            Statement stmt;
            stmt= MyClass.C.createStatement();
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
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
