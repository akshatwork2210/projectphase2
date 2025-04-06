package loginsignup.login.loggedin.billing;

import mainpack.MyClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BillingScreen extends JFrame {
    private JPanel panel;
    private JButton newBillButton;
    private JButton backButton;
    private JButton viewCustomerBillsButton;
    private JButton viewBillButton;

    public JButton getNewBillButton() {
        return newBillButton;
    }

    public BillingScreen() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(panel);
        pack();

        newBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyClass.newBill.setVisible(true);
                MyClass.newBill.init();
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
        viewCustomerBillsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.viewBill.setVisible(true);
                MyClass.viewBill.init("customer");
            }
        });
    }
}
