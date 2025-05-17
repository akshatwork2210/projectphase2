package loginsignup.login.loggedin.transactionsandaccounts.newtransaction;

import jdk.jshell.execution.Util;
import mainpack.MyClass;
import testpackage.UtilityMethods;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class NewTransaction extends JFrame {
    private LocalDate date;
    Connection transacCon = null;
    public void setDate(Object date) {
        if (date == null) {
            this.date = null;
            return;
        } else if (date.toString().isEmpty()) {
            this.date = null;
            return;
        }
        this.date = LocalDate.parse(date.toString(), DateTimeFormatter.ofPattern("dd-MM-yy"));
    }

    public LocalDate getDate() {
        return date;
    }

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
                addedTransactions.dispose();
                MyClass.transactions.setVisible(true);


            }
        });
        submitButton.addActionListener(
                e -> {


                    double amount;
                    String remark = remarkTextField.getText();
                    try {
                        amount = Double.parseDouble(amountTextField.getText());
                    } catch (NumberFormatException exception) {
                        JOptionPane.showMessageDialog(MyClass.newTransaction, "invalid amount entered");
                        throw new RuntimeException();
                    }

                    String customerName = partyNameComboBox.getSelectedItem() == null ? "" : partyNameComboBox.getSelectedItem().toString();
                    if (customerName.isEmpty() || partyNameComboBox.getSelectedIndex() == 0) {
                        JOptionPane.showMessageDialog(MyClass.newTransaction, "select valid party name");
                        return;
                    }

                    String transactionQuery = "INSERT INTO transactions (customer_name, amount, date,remark) VALUES (?, ?, ?,?)";
                    String customerTableQuery = "update customers set balance=balance-? where customer_name=?";
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
                    Date parsed = null;
                    try {
                        parsed = sdf.parse(dateComboBox.getSelectedItem() == null ? "" : dateComboBox.getSelectedItem().toString());
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (outRadioButton.isSelected()) amount = amount * (-1);
                    java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());


                    try {
                        transacCon = DriverManager.getConnection(MyClass.login.getUrl(), MyClass.login.getLoginID(), MyClass.login.getPassword());

                        transacCon.setAutoCommit(false);
                        PreparedStatement statement = transacCon.prepareStatement(transactionQuery);
                        statement.setString(1, customerName);
                        statement.setDouble(2, amount);
                        statement.setDate(3, sqlDate);
                        statement.setString(4, remark);
                        statement.executeUpdate();
                        statement.close();
                        statement = transacCon.prepareStatement(customerTableQuery);

                        statement.setDouble(1,amount);
                        statement.setString(2,customerName);
                        statement.executeUpdate();

                        transacCon.commit();
                        addedTransactions.fetchData(getDate());
                        transacCon.close();
                    } catch (SQLException ex) {
                        if (transacCon != null) {
                            try {
                                ex.printStackTrace();
                                transacCon.rollback();
                                transacCon.close();
                            } catch (SQLException exc) {
                                throw new RuntimeException(exc);
                            }
                            throw new RuntimeException(ex);
                        }
                    }

                });
        dateComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDate(dateComboBox.getSelectedItem());
                if (addedTransactions != null && getDate() != null)
                    addedTransactions.fetchData(getDate());


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

    AddedTransactions addedTransactions;

    public void init() {
        appendListOfCustomers(partyNameComboBox);
        UtilityMethods.generateAndAddDates(dateComboBox, false);

        inRadioButton.setSelected(true);
        addedTransactions = new AddedTransactions();
        addedTransactions.setVisible(true);


        addedTransactions.fetchData(getDate());
        UtilityMethods.splitFrame(MyClass.newTransaction, addedTransactions, UtilityMethods.VERTI_SPLIT);

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
