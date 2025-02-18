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
                Statement s;
                try {
                    Connection c= MyClass.getConnection("localhost","sample",user.getText(),pass.getText());
                    MyClass.C=c;
                  s=c.createStatement();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(MyClass.login,"error");
                return;
                }
                MyClass.mainScreen.setVisible(true);
                setVisible(false);

            }
        });
    }
}
