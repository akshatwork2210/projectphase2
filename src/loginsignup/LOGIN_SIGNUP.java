package loginsignup;

import mainpack.Myclass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LOGIN_SIGNUP extends JFrame{
    private JButton LOGINButton;
    private JPanel panel;
    private JButton SIGNUPButton;
    private JButton QUITButton;

    public LOGIN_SIGNUP() {
       setContentPane(panel);

        LOGIN login=Myclass.login;

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
    }
}
