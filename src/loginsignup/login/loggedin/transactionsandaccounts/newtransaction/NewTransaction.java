package loginsignup.login.loggedin.transactionsandaccounts.newtransaction;

import mainpack.MyClass;
import testpackage.TestClass;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewTransaction extends JFrame {
    public NewTransaction() {
        setContentPane(panel);

        pack();
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(inRadioButton);
        buttonGroup.add(outRadioButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                MyClass.transactions.setVisible(true);


            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                double amount;
                String remark=remarkTextField.getText();
                try {
                    amount = Double.parseDouble(amountTextField.getText());
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(MyClass.newTransaction,"invalid amount entered");
                    throw new RuntimeException();
                }

                String customerName = partyNameComboBox.getSelectedItem() == null ? "" : partyNameComboBox.getSelectedItem().toString();
                if(customerName.isEmpty() || partyNameComboBox.getSelectedIndex()==0) {
                    JOptionPane.showMessageDialog(MyClass.newTransaction, "select valid party name");
                    return;
                }
                String query = "INSERT INTO transactions (customer_name, amount, date,remark) VALUES (?, ?, ?,?)";
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
                Date parsed = null;
                try {
                    parsed = sdf.parse(dateComboBox.getSelectedItem() == null ? "" : dateComboBox.getSelectedItem().toString());
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                if(outRadioButton.isSelected())amount=amount*(-1);
                java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
                try {

                    PreparedStatement statement = MyClass.C.prepareStatement(query);
                    statement.setString(1, customerName);
                    statement.setDouble(2, amount);
                    statement.setDate(3, sqlDate);
                    statement.setString(4, remark);
                    statement.executeUpdate();

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
    }

    private void appendListOfCustomers(JComboBox comboBox) {

        String Query = "Select customer_name from  customers;";
        if (comboBox.getItemCount() != 0) comboBox.removeAllItems();
        comboBox.addItem("Select Customer");

        try {
            Statement stmt = MyClass.C.prepareStatement(Query);
            ResultSet rs = stmt.executeQuery(Query);
            while (rs.next()) {
                comboBox.addItem(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void init() {
        appendListOfCustomers(partyNameComboBox);
        TestClass.generateAndAddDates(dateComboBox,false);
        inRadioButton.setSelected(true);
        pack();
    }

    private JButton backButton;
    private JComboBox dateComboBox;
    private JPanel panel;
    private JComboBox partyNameComboBox;
    private JTextField amountTextField;
    private JRadioButton inRadioButton;
    private JRadioButton outRadioButton;
    private JButton submitButton;
    private JTextField remarkTextField;


}
