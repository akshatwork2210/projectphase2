package loginsignup.login.loggedin.billing;

import mainpack.MyClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BillingScreen extends JFrame {
    private JPanel panel;
    private JButton newBillButton;
    private JButton backButton;

    public BillingScreen(){
        setContentPane(panel);
        pack();

        newBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyClass.newBill.setVisible(true);
                MyClass.newBill.initSystemlogin();
                setVisible(false);

            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.mainScreen.setVisible(true);
            }
        });
    }
}
