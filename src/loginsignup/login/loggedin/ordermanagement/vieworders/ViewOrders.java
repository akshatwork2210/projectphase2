package loginsignup.login.loggedin.ordermanagement.vieworders;

import mainpack.MyClass;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ViewOrders extends JFrame{
    public ViewOrders(){
        setContentPane(panel);
        pack();
        Vector<String> v=new Vector<>();
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.orderScreen.setVisible(true);
            }
        });
        init();
        customerComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id=0;
                try {
                    ResultSet rs = MyClass.S.executeQuery(
                            "SELECT MIN(slip_id) FROM order_slips " +
                                    "WHERE customer_name = \"" + customerComboBox.getSelectedItem().toString() + "\" " +
                                    "AND slip_type = \"" + panaTypeComboBox.getSelectedItem().toString() + "\";"
                    );

                if(rs.next())id=rs.getInt(1); else{
                    rs=MyClass.S.executeQuery("SELECT MIN(slip_id) FROM order_slips");
                }
                billIDLabel.setText("Bill id: "+id);
                setCurrentBill(id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        panaTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id=0;
                try {
                    ResultSet rs = MyClass.S.executeQuery(
                            "SELECT MIN(slip_id) FROM order_slips " +
                                    "WHERE customer_name = \"" + customerComboBox.getSelectedItem().toString() + "\" " +
                                    "AND slip_type = \"" + panaTypeComboBox.getSelectedItem().toString() + "\";"
                    );

                    if(rs.next())id=rs.getInt(1);
                    billIDLabel.setText("Bill id: "+id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void setCurrentBill(int id) {
    }

    public void init(){
        String[] columnNames = {"design id", "Item Name", "Quantity", "Plating", "Raw Material Cost", "Other Details"};//jtable content
panaTypeComboBox.removeAllItems();
customerComboBox.removeAllItems();
customerComboBox.addItem("Select Customer");
        try {
            ResultSet rs= MyClass.S.executeQuery("select type_name from ordertype;");
            while (rs.next()) {
                panaTypeComboBox.addItem(rs.getString("type_name"));
            }
            rs=MyClass.S.executeQuery("select customer_name from customers;");
            while (rs.next()) {
                customerComboBox.addItem(rs.getString("customer_name"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        DefaultTableModel model=new DefaultTableModel(columnNames,0);
        String[] emptyRow = {"", "", "", "", "", ""};
        model.addRow(emptyRow);
        orderSlip.setModel(model);
        billIDLabel.setText("Bill ID: ");

    }
    private JButton backButton;
    private JTable orderSlip;
    private JButton nextButton;
    private JButton prevButton;
    private JComboBox customerComboBox;
    private JComboBox panaTypeComboBox;
    private JPanel panel;
    private JLabel billIDLabel;
}
