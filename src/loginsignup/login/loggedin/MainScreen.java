package loginsignup.login.loggedin;

import mainpack.MyClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MainScreen extends JFrame{
    public MainScreen(){
        this.setContentPane(panel);
        pack();

        billingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyClass.billingScreen.setVisible(true);
                setVisible(false);


            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                try {
                    MyClass.C.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                MyClass.login.setVisible(true);
            }
        });
        orderManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyClass.orderScreen.setVisible(true);
                setVisible(false);
            }
        });
        inventoryManagementButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.inventoryScreen.setVisible(true);
                MyClass.inventoryScreen.init();
            }
        });
    }
    private JButton billingButton;
    private JPanel panel;
    private JButton backButton;
    private JButton orderManagementButton;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton inventoryManagementButton;
}
