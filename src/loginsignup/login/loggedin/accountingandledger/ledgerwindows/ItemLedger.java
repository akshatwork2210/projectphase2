package loginsignup.login.loggedin.accountingandledger.ledgerwindows;

import mainpack.MyClass;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
//        fetchLedgerToModel(ledgerTable);


    }

    private void fetchLedgerToModel(JTable ledgerTable,String designID) {
        String billQuery = "select sum(bd.quantity) , bd.ordertype,b.date,b.billid from billdetails bd join bills b on b.billid=bd.billid where bd.designid=? group by bd.billid ";
        try {
            PreparedStatement stmt=MyClass.C.prepareStatement(billQuery);
            stmt.setString(1,designID);
//            ResultSet rs=stmt.;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String transactionQuery = "select amount,date,transaction_id from transactions where customer_name=?";

    }
}
