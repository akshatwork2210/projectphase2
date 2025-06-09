package loginsignup;

import loginsignup.login.LOGIN;
import mainpack.MyClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LOGIN_SIGNUP extends JFrame{
    private JButton LOGINButton;
    private JPanel panel;
    private JButton SIGNUPButton;
    private JButton QUITButton;

    public LOGIN_SIGNUP() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setContentPane(panel);
        setTitle(MyClass.TITLE+": login/signup");
        LOGIN login= MyClass.login;

        QUITButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        LOGINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login.setVisible(true);
                setVisible(false);
            }
        });
pack();
        SIGNUPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
