package loginsignup.login.loggedin.ordermanagement;

import mainpack.MyClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderScreen extends JFrame {

    private JPanel panel;
    private JButton generateANewOrderButton;
    private JButton backButton;
    private JButton kachheKaJamaButton;
    private JButton kachheKaBaakiButton;
    private JButton repairingButton;
    private JButton analyseLateOrderAndButton;

    public OrderScreen(){
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

    }
public JButton getGenerateANewOrderButton(){
        return generateANewOrderButton;

}
}
