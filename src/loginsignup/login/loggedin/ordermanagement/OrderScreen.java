package loginsignup.login.loggedin.ordermanagement;

import mainpack.MyClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderScreen extends JFrame {

    private JPanel panel;
    private JButton generateANewOrderButton;
    private JButton backButton;
    private JButton viewOrdersButton;
    private JButton analyseLateOrderAndButton;
public JButton getViewOrdersButton(){
    return viewOrdersButton;
}
    public OrderScreen(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(panel);
        pack();
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.mainScreen.setVisible(true);
            }
        });
        generateANewOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.orderGenerateForm.setVisible(true);
                MyClass.orderGenerateForm.init();
            }
        });

        viewOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyClass.viewOrders.setVisible(true);
                MyClass.viewOrders.init();
                setVisible(false);
            }
        });
    }
public JButton getGenerateANewOrderButton(){
        return generateANewOrderButton;

}
}
