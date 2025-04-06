package loginsignup.login.loggedin.transactionsandaccounts;

import mainpack.MyClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Transactions extends JFrame {
    private JButton backButton;
    private JButton newTransactionButton;
    private JButton button3;
    private JButton button4;
    private JPanel panel;


    public Transactions() {
        setContentPane(panel);
        pack();
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.mainScreen.setVisible(true);
            }
        });
        newTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyClass.newTransaction.init();
                MyClass.newTransaction.setVisible(true);
                setVisible(false);

            }
        });
    }

    public void init() {
    }
}
