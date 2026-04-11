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

    public static void  main(String args[]){
        new SignUp().setVisible(true);
    }
    SignUp() {
        init();

    }

    void init() {
        setContentPane(panel);
        JFrame temp=this;
        SUBMITButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username= userTextField.getText();

                String password= passwordTextField.getText();
                String authority= authorityTextField.getText();
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
            }
        });
        pack();
    }
}

