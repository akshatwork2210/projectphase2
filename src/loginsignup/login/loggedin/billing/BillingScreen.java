package loginsignup.login.loggedin.billing;

import mainpack.MyClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static mainpack.MyClass.*;

public class BillingScreen extends JFrame {
    private JPanel panel;
    private JButton newBillButton;
    private JButton backButton;
    private JButton viewCustomerBillsButton;

    public JButton getViewBillButton() {
        return viewBillButton;
    }

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
                newBill.setVisible(true);
                newBill.init();
                setVisible(false);

            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                mainScreen.setVisible(true);
            }
        });
        viewCustomerBillsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                viewCustomerBill.setVisible(true);
                viewCustomerBill.init("customer");
            }
        });
        viewBillButton.addActionListener(e -> {
            viewBackendBill.init();
            viewBackendBill.setVisible(true);
            setVisible(false);
        });
    }
}
