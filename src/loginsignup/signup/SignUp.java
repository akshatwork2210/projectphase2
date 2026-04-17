package loginsignup.signup;

import mainpack.MyClass;
import testpackage.DBStructure;
import testpackage.UtilityMethods;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class SignUp extends JFrame {
    private JTextField userTextField;
    private JTextField authorityTextField;
    private JTextField passwordTextField;
    private JButton SUBMITButton;
    private JPanel panel;
    private JTextField rootPasswordTextField;

    public static void main(String args[]) {
        new SignUp().setVisible(true);
    }

    SignUp() {
        init();

    }

    void init() {
        setContentPane(panel);
        JFrame temp = this;
        SUBMITButton.addActionListener(e -> {
            String username = userTextField.getText();
            String password = passwordTextField.getText();
            String authority = authorityTextField.getText();

            String q1 = "mysql -u root -p" + rootPasswordTextField.getText() +
                    " -e \"CREATE DATABASE " + username + ";\"";

            String q2 = "mysql -u root -p" + rootPasswordTextField.getText() +
                    " -e \"CREATE USER '" + username + "'@'%' IDENTIFIED BY '" + password + "';\"";

            String q3 = "mysql -u root -p" + rootPasswordTextField.getText() +
                    " -e \"GRANT ALL PRIVILEGES ON " + username + ".* TO '" + username + "'@'%';\"";
            String q4 = "mysql -u root -p" + rootPasswordTextField.getText() + " " + username + " < " + "D:/gurukripa/src/resources/structure.sql";
            Process p1 = UtilityMethods.pBuilder(q1);
            Process p2 = UtilityMethods.pBuilder(q2);
            Process p3 = UtilityMethods.pBuilder(q3);
            Process p4 = UtilityMethods.pBuilder(q4);
            try {
                System.out.println("database structure imported with code " + p1.waitFor() + " " + p2.waitFor() + " " + p3.waitFor() + " " + p4.waitFor());
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            String query = "insert into " + DBStructure.USER_ACCOUNT_TABLE + "( " + DBStructure.user_name + ", " + DBStructure.password + "    , " + DBStructure.authority + ") values(?,?,?);";
            try (Connection con = MyClass.createConnection(""); PreparedStatement insertStatement = con.prepareStatement(query)) {
                insertStatement.setString(1, username);
                insertStatement.setString(2, password);
                insertStatement.setString(3, authority);
                insertStatement.executeUpdate();

            } catch (SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(temp, "USER NAME ALREADY EXISTS,, PLEASE CHANGE THE USERNAME");
                userTextField.setText("");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        pack();
    }
}

