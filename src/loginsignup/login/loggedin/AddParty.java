package loginsignup.login.loggedin;

import mainpack.MyClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import static mainpack.MyClass.addParty;

public class AddParty extends JFrame {
    private JPanel panel1;
    private JButton backButton;
    private JTextField customerNameField;
    private JButton addButton;

    public AddParty() {
setContentPane(panel1);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.mainScreen.setVisible(true);


            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = customerNameField.getText().isEmpty() ? "error" : customerNameField.getText();
                if (name.contentEquals("error")) return;
                String query = "insert into customers(customer_name) values(?)";

                try {
                    PreparedStatement preparedStatement = MyClass.C.prepareStatement(query);
                    preparedStatement.setString(1, name);
                    preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(addParty,"succesfull operation");
                    reset();
                } catch (SQLIntegrityConstraintViolationException ex) {
//                    throw new RuntimeException(ex);
                    JOptionPane.showMessageDialog(addParty,"Part Name already exists");
                    ex.printStackTrace();

                    return;
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
    }

    private void reset() {
    customerNameField.setText("");
    }

    public void init() {
        pack();

    }
}
