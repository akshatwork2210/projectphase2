package loginsignup.login.loggedin.transactionsandaccounts.newtransaction;

import mainpack.MyClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewTransaction extends JFrame {
    public NewTransaction(){
        setContentPane(panel);

        pack();
        ButtonGroup buttonGroup=new ButtonGroup();
        buttonGroup.add(inRadioButton);
        buttonGroup.add(outRadioButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.transactions.setVisible(true);


            }
        });
    }
    public void init(){

    }

    private JButton backButton;
    private JComboBox dateComboBox;
    private JPanel panel;
    private JComboBox partyNameComboBox;
    private JTextField textField1;
    private JRadioButton inRadioButton;
    private JRadioButton outRadioButton;


}
