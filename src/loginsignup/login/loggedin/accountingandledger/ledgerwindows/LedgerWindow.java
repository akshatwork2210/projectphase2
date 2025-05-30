package loginsignup.login.loggedin.accountingandledger.ledgerwindows;

import mainpack.MyClass;
import testpackage.UtilityMethods;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectStreamException;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Stack;

public class LedgerWindow extends JFrame {

    public static  final int CUSTOMER_MODE=65*1;
    public static final int ITEM_MODE=65*2;
    private static final int PURCHASE_MODE = 65 * 3;
    private JButton backButton;
    private JTable ledgerTable;
    private JPanel panel;
    private JLabel nameLabel;

    public LedgerWindow() {

    }

    public void init(String customerName,int mode) {
        setContentPane(panel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyClass.aalScreen.setVisible(true);
                dispose();

            }
        });
        String[] columnNames=null;
        if(mode==ITEM_MODE)
             columnNames= new String[]{"id", "date", "debit", "credit"};
        else if (mode==CUSTOMER_MODE) {
            columnNames= new String[]{"id", "date", "debit", "credit","balance"};

        }
        else if(mode== PURCHASE_MODE){
            columnNames= new String[]{"id","date","Party Name", "debit", "credit"};
        }
        DefaultTableModel defaultTableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
       ledgerTable.setModel(defaultTableModel);
        if(mode==CUSTOMER_MODE){
            fetchCustomerLedger(customerName);
        }
//        fetchLedgerToModel(ledgerTable);


    }

    private void fetchCustomerLedger(String customerName) {
        nameLabel.setText(customerName);
        System.out.println("starting to fetch customer ledger");
//        String query = "(SELECT b.date AS date, b.BillID AS bill_id, NULL AS transaction_id, IFNULL(SUM(bd.TotalFinalCost), 0) AS bill_amount, NULL AS transaction_amount, NULL AS remark FROM bills b LEFT JOIN billdetails bd ON b.BillID = bd.BillID WHERE b.customer_name = ? GROUP BY b.BillID, b.date) UNION ALL (SELECT t.date AS date, t.billid AS bill_id, t.transaction_id, NULL AS bill_amount, t.amount AS transaction_amount, t.remark FROM transactions t WHERE t.customer_name = ?) ORDER BY date";
        DefaultTableModel model=(DefaultTableModel) ledgerTable.getModel();
        String ledgerQuery = "(SELECT b.date AS date, b.BillID AS bill_id, NULL AS transaction_id, IFNULL(SUM(bd.TotalFinalCost), 0) AS bill_amount, NULL AS transaction_amount, NULL AS remark FROM bills b LEFT JOIN billdetails bd ON b.BillID = bd.BillID WHERE b.customer_name = ? GROUP BY b.BillID, b.date) UNION ALL (SELECT t.date AS date, NULL AS bill_id, t.transaction_id, NULL AS bill_amount, t.amount AS transaction_amount, t.remark FROM transactions t WHERE t.customer_name = ?) ORDER BY date";

        String openingBalaceQuery="select openingAccount from customers where customer_name = ?";

        try {
            PreparedStatement openingAccountStatement=MyClass.C.prepareStatement(openingBalaceQuery);
            openingAccountStatement.setString(1,customerName);
            ResultSet openingAccountResultSet=openingAccountStatement.executeQuery();
            model.setRowCount(0);
            double balance=0;
            if (openingAccountResultSet.next())
                balance+=openingAccountResultSet.getDouble(1);
            model.addRow(new String[]{"opening balance","","","",balance+""});
            System.out.println("first row added in customer ledger for opening balance");
            PreparedStatement ledgerStatement=MyClass.C.prepareStatement(ledgerQuery);
            ledgerStatement.setString(1,customerName);
            ledgerStatement.setString(2,customerName);
            ResultSet rs=ledgerStatement.executeQuery();
            while (rs.next()) {
                String id;
                Date date = rs.getDate("date");
                Double debit = null;
                Double credit = null;

                int billID = rs.getInt("bill_id");
                if (!rs.wasNull()) {
                    id = "BILL#" + billID;
                    debit = rs.getDouble("bill_amount");
                } else {

                    int transactionID = rs.getInt("transaction_id");
                    id = "TXN#" + transactionID;
                    double amount = rs.getDouble("transaction_amount");
                    String remark = rs.getString("remark");
                    if (amount >= 0) {
                        credit = amount;
                    } else {
                        debit = -amount;
                    }
                }
                String parsedDate= LocalDate.parse(date.toString(),DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DateTimeFormatter.ofPattern("dd-MM-yy"));
                balance=balance+(debit!=null?debit:0)-(credit!=null?credit:0);
                Object[] objects=new Object[]{id,parsedDate, debit, credit, UtilityMethods.round(balance,2)};
                model.addRow(objects);
                for(Object object:objects){
                    System.out.print(object+"\t");
                }
                System.out.println();
            }
            model.fireTableDataChanged();
        ledgerTable.repaint();
            System.out.println("\n\nreturning from fetchcustomer ledger function bye \n\n");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

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
