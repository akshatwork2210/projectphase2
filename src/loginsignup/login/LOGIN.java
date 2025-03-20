package loginsignup.login;

import mainpack.MyClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class LOGIN extends JFrame {
    private JTextField user;
    private JTextField pass;
    private JButton LOGINButton;
    private JButton QUITButton;
    private JButton BACKButton;
    private JPanel panel;
    private String loginID;
    private String password;
    private String host;
    private String database;
    private String port;

    public String getPort() {
        return port;
    }

    public String getUrl() {
        return url;
    }

    public String getDatabase() {
        return database;
    }

    private String url;
    public String getPassword() {
        return password;
    }

    public String getLoginID() {
        return loginID;
    }

    public LOGIN() {
        setContentPane(panel);

        QUITButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    pack();
        BACKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.login_signup.setVisible(true);

            }
        });
        LOGINButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                     host="localhost";
                     database="sample";
                    String port="3306";
                    url = "jdbc:mysql://" + host + ":"+port+"/" + database;
                    Connection c= MyClass.getConnection(url,user.getText(),pass.getText());
                    MyClass.C=c;
                    loginID=user.getText();
                    password=pass.getText();
                  Statement stmt;
                    stmt=c.createStatement();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(MyClass.login,"error");
                return;
                }
                MyClass.mainScreen.setVisible(true);
                setVisible(false);

            }
        });
    }

    public JButton getLOGINButton() {
        return LOGINButton;
    }
}
