package loginsignup.login.loggedin.billing;

import loginsignup.login.loggedin.billing.newBill.NewBill;
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
        return viewCustomerBillsButton;
    }

    private JButton viewBillButton;
    private JButton newPurchaseButton;

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
                newBill=new NewBill();
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
        newPurchaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                purchaseBill.init();
                purchaseBill.setVisible(true);
                setVisible(false);
            }
        });
    }

    public JButton getPurchaseBillButton() {
    return newPurchaseButton;
    }
}
