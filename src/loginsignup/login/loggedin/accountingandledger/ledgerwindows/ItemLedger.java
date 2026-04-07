package loginsignup.login.loggedin.accountingandledger.ledgerwindows;

import mainpack.MyClass;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ItemLedger extends JFrame {
    private JButton backButton;
    private JTable ledgerTable;

    public ItemLedger() {

    }

    public void init(String s) {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyClass.aalScreen.setVisible(true);
                dispose();

            }
        });
        String[] columnNames = {"id", "date", "debit", "credit"};
        DefaultTableModel defaultTableModel = new DefaultTableModel(columnNames, 0);
        ledgerTable.setModel(defaultTableModel);


    }

}
