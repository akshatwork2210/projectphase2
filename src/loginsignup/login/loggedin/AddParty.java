package loginsignup.login.loggedin;

import mainpack.MyClass;
import testpackage.UtilityMethods;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
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
    private JTextField openingBalanceTextField;

    public AddParty() {

    }

    private void reset() {
        customerNameField.setText("");
        openingBalanceTextField.setText("");

    }

    public void init() {
//        ((AbstractDocument) openingBalanceTextField.getDocument()).setDocumentFilter(UtilityMethods.getDocFilter());
        setContentPane(panel1);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                MyClass.mainScreen.setVisible(true);
                dispose();

            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = customerNameField.getText().isEmpty() ? "error" : customerNameField.getText();
                if (name.contentEquals("error")) return;
                String query = "insert into customers(customer_name,openingaccount,balance) values(?,?,0)";
                double openingaccount=0;
                try {
                    openingaccount = Double.parseDouble(openingBalanceTextField.getText()==null || openingBalanceTextField.getText().trim().isEmpty()?"0": openingBalanceTextField.getText());
                } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addParty,"please enter valid balance");
                return;
                }
                try {
                    PreparedStatement preparedStatement = MyClass.C.prepareStatement(query);
                    preparedStatement.setString(1, name);
                    preparedStatement.setDouble(2, openingaccount);
                    preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(addParty, "succesfull operation");
                    reset();
                } catch (SQLIntegrityConstraintViolationException ex) {
//                    throw new RuntimeException(ex);
                    JOptionPane.showMessageDialog(addParty, "Part Name already exists");
                    ex.printStackTrace();

                    return;
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        pack();

    }
}
