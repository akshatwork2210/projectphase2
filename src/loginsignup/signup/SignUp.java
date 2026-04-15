package loginsignup.signup;

import mainpack.MyClass;
import testpackage.DBStructure;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public static void  main(String args[]){
        new SignUp().setVisible(true);
    }
    SignUp() {
        init();

    }

    void init() {
        setContentPane(panel);
        JFrame temp=this;
        SUBMITButton.addActionListener(e -> {
            String username= userTextField.getText();
            String password= passwordTextField.getText();
            String authority= authorityTextField.getText();

            String q1 = "mysql -u root -p" + rootPasswordTextField.getText() +
                    " -e \"CREATE DATABASE " + username + ";\"";

            String q2 = "mysql -u root -p" + rootPasswordTextField.getText() +
                    " -e \"CREATE USER '" + username + "'@'%' IDENTIFIED BY '" + password + "';\"";

            String q3 = "mysql -u root -p" + rootPasswordTextField.getText() +
                    " -e \"GRANT ALL PRIVILEGES ON " + username + ".* TO '" + username + "'@'%';\"";
            String q4 = "mysql -u "+username+  " -p"+rootPasswordTextField.getText()+" "+username+" < " + "D:/gurukripa/src/resources/structure.sql";
            testpackage.LibraryStudyDummy.pBuilder(q1+" && "+q2+" && "+q3+" && " +q4);
            String query = "insert into "+ DBStructure.USER_ACCOUNT_TABLE+"( "+DBStructure.user_name+", "+DBStructure.password+", "+DBStructure.authority+") values(?,?,?);";
            try(Connection con= MyClass.createConnection(""); PreparedStatement insertStatement=con.prepareStatement(query))
                {   con.setAutoCommit(false);
                    insertStatement.setString(1,username);
                    insertStatement.setString(2,password);
                    insertStatement.setString(3,authority);
                    insertStatement.executeUpdate();
                } catch (SQLIntegrityConstraintViolationException ex) {
                    JOptionPane.showMessageDialog(temp,"USER NAME ALREADY EXISTS,, PLEASE CHANGE THE USERNAME");
                    userTextField.setText("");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        pack();
    }
}

